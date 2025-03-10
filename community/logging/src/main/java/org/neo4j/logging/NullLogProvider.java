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
package org.neo4j.logging;

/**
 * A {@link LogProvider} implementation that discards all messages
 */
public final class NullLogProvider implements LogProvider
{
    private static final NullLogProvider INSTANCE = new NullLogProvider();

    private NullLogProvider()
    {
    }

    /**
     * @return A singleton {@link NullLogProvider} instance
     */
    public static NullLogProvider getInstance()
    {
        return INSTANCE;
    }

    @Override
    public Log getLog( Class<?> loggingClass )
    {
        return NullLog.getInstance();
    }

    @Override
    public Log getLog( String name )
    {
        return NullLog.getInstance();
    }
}
