/*
 * Copyright (c) "Neo4j"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.internal.batchimport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Iterator;

import org.neo4j.test.Race;
import org.neo4j.test.extension.Inject;
import org.neo4j.test.extension.RandomExtension;
import org.neo4j.test.RandomSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith( RandomExtension.class )
class DataStatisticsTest
{
    @Inject
    private RandomSupport random;

    @Test
    void shouldSumCounts() throws Throwable
    {
        // given
        DataStatistics stats = new DataStatistics( 1, 2, new DataStatistics.RelationshipTypeCount[0] );
        Race race = new Race();
        int types = 10;
        long[] expected = new long[types];
        int threads = Runtime.getRuntime().availableProcessors();
        for ( int i = 0; i < threads; i++ )
        {
            long[] local = new long[types];
            for ( int j = 0; j < types; j++ )
            {
                local[j] = random.nextInt( 1_000, 2_000 );
                expected[j] += local[j];
            }
            race.addContestant( () ->
            {
                try ( DataStatistics.Client client = stats.newClient() )
                {
                    for ( int typeId = 0; typeId < types; typeId++ )
                    {
                        while ( local[typeId]-- > 0 )
                        {
                            client.increment( typeId );
                        }
                    }
                }
            } );
        }

        // when
        race.go();

        // then
        stats.forEach( count -> assertEquals( expected[count.getTypeId()], count.getCount() ) );
    }

    @Test
    void shouldGrowArrayProperly()
    {
        // given
        DataStatistics stats = new DataStatistics( 1, 1, new DataStatistics.RelationshipTypeCount[0] );

        // when
        int typeId = 1_000;
        try ( DataStatistics.Client client = stats.newClient() )
        {
            client.increment( typeId );
        }

        // then
        DataStatistics.RelationshipTypeCount count = typeCount( stats.iterator(), typeId );
        assertEquals( 1, count.getCount() );
        assertEquals( typeId, count.getTypeId() );
    }

    private static DataStatistics.RelationshipTypeCount typeCount( Iterator<DataStatistics.RelationshipTypeCount> iterator, int typeId )
    {
        while ( iterator.hasNext() )
        {
            DataStatistics.RelationshipTypeCount count = iterator.next();
            if ( count.getTypeId() == typeId )
            {
                return count;
            }
        }
        throw new IllegalStateException( "Couldn't find " + typeId );
    }
}
