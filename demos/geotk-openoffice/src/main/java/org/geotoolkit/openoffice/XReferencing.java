/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2005-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2010-2012, Geomatys
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
package org.geotoolkit.openoffice;

import com.sun.star.uno.XInterface;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.IllegalArgumentException;


/**
 * Services from the {@link org.geotoolkit.referencing} package to be exported to
 * <A HREF="http://www.openoffice.org">OpenOffice</A>.
 * <p>
 * This interface is derived from the {@code XReferencing.idl} file using the {@code javamaker}
 * tool provided in OpenOffice SDK, and disassembling the output using the {@code javap} tool
 * provided in Java SDK. This source file exists mostly for javadoc purpose and in order to keep
 * IDE happy. The {@code .class} file compiled from this source file <strong>MUST</strong> be
 * overwritten by the {@code .class} file generated by {@code javamaker}.
 *
 * @author Martin Desruisseaux (IRD, Geomatys)
 * @version 3.20
 *
 * @since 3.09 (derived from 2.2)
 * @module
 */
public interface XReferencing extends XInterface {
    /**
     * Converts text in degrees-minutes-seconds to an angle in decimal degrees.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  text     The text to be converted to an angle.
     * @param  pattern  The text that describes the format (example: "D°MM.m'").
     * @return The angle parsed as a number.
     * @throws IllegalArgumentException if {@code pattern} is illegal.
     */
    double getValueAngle(XPropertySet xOptions, String text, Object pattern)
            throws IllegalArgumentException;

    /**
     * Converts an angle to text according to a given format.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  value    The angle value (in decimal degrees) to be converted.
     * @param  pattern  The text that describes the format (example: "D°MM.m'").
     * @return The angle formatted as a string.
     * @throws IllegalArgumentException if {@code pattern} is illegal.
     */
    String getTextAngle(XPropertySet xOptions, double value, Object pattern)
            throws IllegalArgumentException;

    /**
     * Converts a longitude to text according to a given format.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  value The longitude value (in decimal degrees) to be converted.
     * @param  pattern The text that describes the format (example: "D°MM.m'").
     * @return The longitude formatted as a string.
     * @throws IllegalArgumentException if {@code pattern} is illegal.
     */
    String getTextLongitude(XPropertySet xOptions, double value, Object pattern)
            throws IllegalArgumentException;

    /**
     * Converts a latitude to text according to a given format.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  value The latitude value (in decimal degrees) to be converted.
     * @param  pattern The text that describes the format (example: "D°MM.m'").
     * @return The latitude formatted as a string.
     * @throws IllegalArgumentException if {@code pattern} is illegal.
     */
    String getTextLatitude(XPropertySet xOptions, double value, Object pattern)
            throws IllegalArgumentException;

    /**
     * Computes the orthodromic distance and azimuth between two coordinates.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  source The source positions.
     * @param  target The target positions.
     * @param  CRS Authority code of the coordinate reference system.
     * @return The orthodromic distance.
     */
    double[][] getOrthodromicDistance(XPropertySet xOptions, double[][] source, double[][] target, Object CRS);

    /**
     * Computes the coordinates after a displacement of the specified distance.
     *
     * @param  xOptions Provided by OpenOffice.
     * @param  source The source positions.
     * @param  displacement The distance and azimuth.
     * @param  CRS Authority code of the coordinate reference system.
     * @return The destination coordinates.
     */
    double[][] getOrthodromicForward(XPropertySet xOptions, double[][] source, double[][] displacement, Object CRS);
}