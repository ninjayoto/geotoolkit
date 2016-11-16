/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2008 - 2009, Geomatys
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
package org.geotoolkit.se.xml.v110;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GraphicStrokeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GraphicStrokeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/se}Graphic"/>
 *         &lt;element ref="{http://www.opengis.net/se}InitialGap" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}Gap" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 * @module
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GraphicStrokeType", propOrder = {
    "graphic",
    "initialGap",
    "gap"
})
public class GraphicStrokeType {

    @XmlElement(name = "Graphic", required = true)
    protected GraphicType graphic;
    @XmlElement(name = "InitialGap")
    protected ParameterValueType initialGap;
    @XmlElement(name = "Gap")
    protected ParameterValueType gap;

    /**
     * Gets the value of the graphic property.
     * 
     * @return
     *     possible object is
     *     {@link GraphicType }
     *     
     */
    public GraphicType getGraphic() {
        return graphic;
    }

    /**
     * Sets the value of the graphic property.
     * 
     * @param value
     *     allowed object is
     *     {@link GraphicType }
     *     
     */
    public void setGraphic(final GraphicType value) {
        this.graphic = value;
    }

    /**
     * Gets the value of the initialGap property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterValueType }
     *     
     */
    public ParameterValueType getInitialGap() {
        return initialGap;
    }

    /**
     * Sets the value of the initialGap property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterValueType }
     *     
     */
    public void setInitialGap(final ParameterValueType value) {
        this.initialGap = value;
    }

    /**
     * Gets the value of the gap property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterValueType }
     *     
     */
    public ParameterValueType getGap() {
        return gap;
    }

    /**
     * Sets the value of the gap property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterValueType }
     *     
     */
    public void setGap(final ParameterValueType value) {
        this.gap = value;
    }

}
