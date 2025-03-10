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
package org.neo4j.server.rest.repr;

import org.neo4j.server.rest.discovery.DiscoverableURIs;
import org.neo4j.server.rest.discovery.ServerVersionAndEdition;

public class DiscoveryRepresentation extends MappingRepresentation
{
    private static final String DISCOVERY_REPRESENTATION_TYPE = "discovery";
    private final DiscoverableURIs uris;
    private final ServerVersionAndEdition serverInfo;
    private final MappingRepresentation authConfigRepr;

    /**
     * @param uris URIs that we want to make publicly discoverable.
     * @param serverInfo server version and edition information
     * @param authConfigRepr authentication configuration of the server.
     */
    public DiscoveryRepresentation( DiscoverableURIs uris, ServerVersionAndEdition serverInfo, MappingRepresentation authConfigRepr )
    {
        super( DISCOVERY_REPRESENTATION_TYPE );
        this.uris = uris;
        this.serverInfo = serverInfo;
        this.authConfigRepr = authConfigRepr;
    }

    @Override
    protected void serialize( MappingSerializer serializer )
    {
        uris.forEach( serializer::putString );
        serverInfo.forEach( serializer::putString );
        authConfigRepr.serialize( serializer );
    }
}
