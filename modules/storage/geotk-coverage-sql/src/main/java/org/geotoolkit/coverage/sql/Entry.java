/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2018, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.coverage.sql;


/**
 * Base class of all entries to be created from table rows.
 *
 * @author Martin Desruisseaux (Geomatys)
 */
abstract class Entry {
    /**
     * For keeping trace of temporary hacks.
     */
    static final boolean HACK = true;

    /**
     * Creates a new entry.
     */
    Entry() {
    }
}
