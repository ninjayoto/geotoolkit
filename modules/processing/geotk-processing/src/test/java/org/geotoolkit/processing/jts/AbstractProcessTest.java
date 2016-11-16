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

package org.geotoolkit.processing.jts;

import org.geotoolkit.process.ProcessDescriptor;
import org.geotoolkit.process.ProcessFinder;

import org.junit.Test;
import static org.junit.Assert.*;
import org.opengis.util.NoSuchIdentifierException;
/**
 * Abstract JUnit test for math process
 * @author Quentin Boileau
 * @module
 */
public abstract class AbstractProcessTest extends org.geotoolkit.test.TestBase {

    private static final String factory = "jts";
    private String process;


    protected AbstractProcessTest(final String process){
        this.process = process;
    }

    @Test
    public void findProcessTest() throws NoSuchIdentifierException{
        ProcessDescriptor desc = ProcessFinder.getProcessDescriptor(factory, process);
        assertNotNull(desc);
    }
}
