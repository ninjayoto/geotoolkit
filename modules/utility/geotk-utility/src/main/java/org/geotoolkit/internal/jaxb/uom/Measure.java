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
package org.geotoolkit.internal.jaxb.uom;

import javax.measure.unit.Unit;
import javax.measure.unit.NonSI;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlAttribute;

import org.geotoolkit.measure.Units;


/**
 * A measurement value together with its unit of measure.
 * This is used for marshalling an element defined by ISO-19103.
 * <p>
 * This class duplicates {@link org.geotoolkit.measure.Measure}, but we had to do that way
 * because {@link org.geotoolkit.measure.Measure} extends {@link Number} and we are not
 * allowed to use the {@code @XmlValue} annotation on a class that extends an other class.
 *
 * @author Cédric Briançon (Geomatys)
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.06
 *
 * @see org.geotoolkit.measure.Measure
 *
 * @since 2.5
 * @module
 *
 * @todo We should annotate {@link org.geotoolkit.measure.Measure} directly
 *       if we can find some way to use {@code @XmlValue} with that class.
 */
public final class Measure {
    /**
     * The namespace of UOM identifier, to be used during XML marshalling.
     */
    private static final String NAMESPACE = "../uom/gmxUom.xsd";

    /**
     * The value of the measure.
     */
    @XmlValue
    public double value;

    /**
     * The unit of measure.
     */
    public Unit<?> unit;

    /**
     * Default empty constructor for JAXB. The value is initialized to NaN,
     * but JAXB will overwrite this value if a XML value is presents.
     */
    public Measure() {
        value = Double.NaN;
    }

    /**
     * Constructs a representation of the measure as defined in ISO-19103 standard,
     * with the UOM attribute like {@code '../uom/gmxUom.xsd#xpointer(//*[@gml:id='m'])'}.
     *
     * @param value The value of the measure.
     * @param unit  The unit of measure to use.
     */
    public Measure(final double value, final Unit<?> unit) {
        this.value = value;
        this.unit  = unit;
    }

    /**
     * Constructs a string representation of the units as defined in the ISO-19103 standard.
     * This method is invoked during XML marshalling. For example in the units are "metre",
     * then this method returns:
     *
     * {@preformat text
     *     ../uom/gmxUom.xsd#xpointer(//*[@gml:id='m'])
     * }
     *
     * @return The string representation of the unit of measure.
     */
    @XmlAttribute(required = true)
    public String getUOM() {
        final Unit<?> unit = this.unit;
        final String symbol;
        if (unit == null || unit.equals(Unit.ONE)) {
            symbol = "";
        } else if (unit.equals(NonSI.PIXEL)) {
            symbol = "pixel";
        } else {
            symbol = NAMESPACE + "#xpointer(//*[@gml:id='" + unit + "'])";
        }
        return symbol;
    }

    /**
     * Sets the unit of measure. This method is invoked by JAXB at unmarshalling
     * time, and can be invoked only once.
     *
     * @param uom The unit of measure as a string.
     */
    public void setUOM(String uom) {
        if (unit != null) {
            throw new IllegalStateException();
        }
        uom = uom.trim();
        int i = uom.lastIndexOf("@gml:id=");
        if (i >= 0) {
            i += 8; // 8 is the length of "@gml:id="
            for (final int length=uom.length(); i<length; i++) {
                final char c = uom.charAt(i);
                if (!Character.isWhitespace(c)) {
                    if (c == '\'') i++;
                    break;
                }
            }
            final int stop = uom.lastIndexOf('\'');
            uom = ((stop > i) ? uom.substring(i, stop) : uom.substring(i)).trim();
        }
        unit = Units.valueOf(uom);
    }
}
