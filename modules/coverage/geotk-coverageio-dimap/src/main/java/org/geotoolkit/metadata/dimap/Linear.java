//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.29 at 02:39:59 PM CEST 
//


package org.geotoolkit.metadata.dimap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import org.geotoolkit.metadata.dimap.spotscene.TELLIPSEAXIS;
import org.geotoolkit.metadata.dimap.spotscene.TERRORESTIMATION;
import org.geotoolkit.metadata.dimap.spotscene.TFIRSTWAVELENGTHVALUE;
import org.geotoolkit.metadata.dimap.spotscene.TMEANRECTIFICATIONELEVATION;
import org.geotoolkit.metadata.dimap.spotscene.TSAMPLINGSTEPX;
import org.geotoolkit.metadata.dimap.spotscene.TSAMPLINGSTEPY;
import org.geotoolkit.metadata.dimap.spotscene.TSATELLITEALTITUDE;
import org.geotoolkit.metadata.dimap.spotscene.TWAVELENGTHSTEP;


/**
 * <p>Java class for Linear complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Linear">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>double">
 *       &lt;attribute name="unit" type="{}Linear_Unit" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Linear", propOrder = {
    "value"
})
@XmlSeeAlso({
    TSATELLITEALTITUDE.class,
    TWAVELENGTHSTEP.class,
    TERRORESTIMATION.class,
    TSCENERECTIFICATIONELEV.class,
    TFIRSTWAVELENGTHVALUE.class,
    TELLIPSEAXIS.class,
    TMEANRECTIFICATIONELEVATION.class,
    TTIEPOINTCRSZ.class,
    TSAMPLINGSTEPX.class,
    TSAMPLINGSTEPY.class
})
public class Linear {

    @XmlValue
    protected double value;
    @XmlAttribute
    protected LinearUnit unit;

    /**
     * Gets the value of the value property.
     * 
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link LinearUnit }
     *     
     */
    public LinearUnit getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link LinearUnit }
     *     
     */
    public void setUnit(LinearUnit value) {
        this.unit = value;
    }

}
