/*
 *    GeotoolKit - An Open Source Java GIS Toolkit
 *    http://geotoolkit.org
 * 
 *    (C) 2009, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.resources.jaxb.feature.catalog;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.geotoolkit.feature.catalog.util.XmlUnlimitedInteger;

/**
 * JAXB adapter in order to map implementing class with the Types String. See
 * package documentation for more information about JAXB and String.
 *
 * @module
 * @since 3.03
 * @author Guilhem Legal
 */
public class UnlimitedIntegerAdapter extends XmlAdapter<UnlimitedIntegerAdapter, Integer> {

    private Integer multiplicity;

    /**
     * Empty constructor for JAXB only.
     */
    private UnlimitedIntegerAdapter() {
    }

    /**
     * Wraps an String value with a {@code SV_String} tags at marshalling-time.
     *
     * @param multiplicity The String value to marshall.
     */
    protected UnlimitedIntegerAdapter(final Integer multiplicity) {
        this.multiplicity = multiplicity;
    }

    /**
     * Returns the String value covered by a {@code SV_String} tags.
     *
     * @param value The value to marshall.
     * @return The adapter which covers the String value.
     */
    protected UnlimitedIntegerAdapter wrap(final Integer value) {
        return new UnlimitedIntegerAdapter(value);
    }

    /**
     * Returns the {@link StringImpl} generated from the metadata value.
     * This method is systematically called at marshalling-time by JAXB.
     */
    @XmlElement(name = "UnlimitedInteger", namespace = "http://www.isotc211.org/2005/gco")
    public XmlUnlimitedInteger getMultiplicity() {
        return new XmlUnlimitedInteger(multiplicity);
    }

    /**
     * Sets the value for the {@link StringImpl}. This method is systematically
     * called at unmarshalling-time by JAXB.
     */
    public void setMultiplicity(final Integer multiplicity) {
        this.multiplicity = multiplicity;
    }

    /**
     * Does the link between metadata red from an XML stream and the object which will
     * contains this value. JAXB calls automatically this method at unmarshalling-time.
     *
     * @param value The adapter for this metadata value.
     * @return A java object which represents the metadata value.
     */
    @Override
    public Integer unmarshal(final UnlimitedIntegerAdapter value) throws Exception {
        if (value == null) {
            return null;
        }
        return value.multiplicity;
    }

    /**
     * Does the link between java object and the way they will be marshalled into
     * an XML file or stream. JAXB calls automatically this method at marshalling-time.
     *
     * @param value The bound type value, here the String.
     * @return The adapter for this String.
     */
    @Override
    public UnlimitedIntegerAdapter marshal(final Integer value) throws Exception {
        return new UnlimitedIntegerAdapter(value);
    }




}
