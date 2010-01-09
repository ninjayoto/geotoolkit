/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2008-2010, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009-2010, Geomatys
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
package org.geotoolkit.referencing.datum;

import java.util.Locale;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.opengis.referencing.datum.TemporalDatum;
import static org.geotoolkit.referencing.datum.DefaultTemporalDatum.*;

import org.junit.*;
import org.geotoolkit.referencing.ReferencingTestCase;


/**
 * Tests the {@link DefaultTemporalDatum} class.
 *
 * @author Martin Desruisseaux (IRD)
 * @version 3.00
 *
 * @since 2.2
 */
public class TemporalDatumTest extends ReferencingTestCase {
    /**
     * The object to use for formatting dates.
     */
    private DateFormat format;

    /**
     * Initialize the object to be used for testing purpose.
     */
    @Before
    public void setUp() {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Verifies the epoch values compared to the julian epoch.
     *
     * @see http://en.wikipedia.org/wiki/Julian_day
     */
    @Test
    public void testOrigins() {
        final double epoch = epoch(JULIAN);
        assertTrue(epoch < 0);

        assertEquals(2400000.5, epoch(MODIFIED_JULIAN) - epoch, 0);
        assertEquals("1858-11-17 00:00:00", epochString(MODIFIED_JULIAN));

        assertEquals(2440000.5, epoch(TRUNCATED_JULIAN) - epoch, 0);
        assertEquals("1968-05-24 00:00:00", epochString(TRUNCATED_JULIAN));

        assertEquals(2415020.0, epoch(DUBLIN_JULIAN) - epoch, 0);
        assertEquals("1899-12-31 12:00:00", epochString(DUBLIN_JULIAN));
    }

    /**
     * Returns the epoch of the given datum, in day units relative to Java epoch.
     */
    private static double epoch(final TemporalDatum datum) {
        return datum.getOrigin().getTime() / (24*60*60*1000.0);
    }

    /**
     * Returns the epoch of the given datum formatted as a string.
     */
    private String epochString(final TemporalDatum datum) {
        return format.format(datum.getOrigin());
    }
}
