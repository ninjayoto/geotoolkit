//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.29 at 02:39:59 PM CEST 
//


package org.geotoolkit.metadata.dimap.spotscene;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for t_Location complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="t_Location">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}X"/>
 *         &lt;element ref="{}Y"/>
 *         &lt;element ref="{}Z"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "t_Location", propOrder = {

})
public class TLocation {

    @XmlElement(name = "X", required = true)
    protected TX x;
    @XmlElement(name = "Y", required = true)
    protected TY y;
    @XmlElement(name = "Z", required = true)
    protected TZ z;

    /**
     * Gets the value of the x property.
     * 
     * @return
     *     possible object is
     *     {@link TX }
     *     
     */
    public TX getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     * 
     * @param value
     *     allowed object is
     *     {@link TX }
     *     
     */
    public void setX(TX value) {
        this.x = value;
    }

    /**
     * Gets the value of the y property.
     * 
     * @return
     *     possible object is
     *     {@link TY }
     *     
     */
    public TY getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     * 
     * @param value
     *     allowed object is
     *     {@link TY }
     *     
     */
    public void setY(TY value) {
        this.y = value;
    }

    /**
     * Gets the value of the z property.
     * 
     * @return
     *     possible object is
     *     {@link TZ }
     *     
     */
    public TZ getZ() {
        return z;
    }

    /**
     * Sets the value of the z property.
     * 
     * @param value
     *     allowed object is
     *     {@link TZ }
     *     
     */
    public void setZ(TZ value) {
        this.z = value;
    }

}
