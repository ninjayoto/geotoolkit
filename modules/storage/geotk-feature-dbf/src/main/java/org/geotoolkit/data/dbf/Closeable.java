/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2011, Geomatys
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
package org.geotoolkit.data.dbf;


/**
 * Extend java.io.Closeable with method isClosed.
 * This allows to log messages when objects are not properly disposed.
 * 
 * @author Johann Sorel (Geomatys)
 * @module
 */
public interface Closeable extends java.io.Closeable{
    
    /**
     * @return true if object has already been closed.
     */
    public boolean isClosed();
    
}
