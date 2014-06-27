/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2012, Geomatys
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
package org.geotoolkit.referencing;

import java.awt.geom.AffineTransform;
import java.util.List;
import org.geotoolkit.referencing.crs.DefaultCompoundCRS;
import org.geotoolkit.referencing.crs.DefaultGeographicCRS;
import org.geotoolkit.referencing.crs.DefaultTemporalCRS;
import org.geotoolkit.referencing.crs.DefaultVerticalCRS;
import org.apache.sis.internal.referencing.j2d.AffineTransform2D;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import static org.geotoolkit.test.Assert.*;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Referencing utilities tests.
 *
 * @author Johann Sorel (Geomatys)
 */
public class ReferencingUtilitiesTest {

    private static final double DELTA = 0.0000001;

    @Test
    public void testDecompose(){

        final CoordinateReferenceSystem crs = new DefaultCompoundCRS("group",
                DefaultGeographicCRS.WGS84,
                new DefaultCompoundCRS("group2",
                    DefaultVerticalCRS.GEOIDAL_HEIGHT,
                    DefaultTemporalCRS.JULIAN));

        final List<CoordinateReferenceSystem> parts = ReferencingUtilities.decompose(crs);
        assertEquals(3, parts.size());
        assertEquals(DefaultGeographicCRS.WGS84, parts.get(0));
        assertEquals(DefaultVerticalCRS.GEOIDAL_HEIGHT, parts.get(1));
        assertEquals(DefaultTemporalCRS.JULIAN, parts.get(2));
    }

    @Test
    public void testToTransform() throws TransformException{

        final AffineTransform2D base = new AffineTransform2D(new AffineTransform());

        final MathTransform trs = ReferencingUtilities.toTransform(base,
                new double[]{1,2,3},
                new double[]{-10,-5,0},
                new double[]{9,10,11});

        assertEquals(5, trs.getSourceDimensions());
        assertEquals(5, trs.getTargetDimensions());

        final double[] coords = new double[]{35,40, 1, 0, 2};
        trs.transform(coords, 0, coords, 0, 1);

        assertEquals(35d, coords[0], DELTA);
        assertEquals(40d, coords[1], DELTA);
        assertEquals(2d, coords[2], DELTA);
        assertEquals(-10d, coords[3], DELTA);
        assertEquals(11d, coords[4], DELTA);

    }

}
