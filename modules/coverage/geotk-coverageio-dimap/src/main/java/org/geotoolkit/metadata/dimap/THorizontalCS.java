//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.29 at 02:39:59 PM CEST 
//


package org.geotoolkit.metadata.dimap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for t_Horizontal_CS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="t_Horizontal_CS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}HORIZONTAL_CS_TYPE"/>
 *         &lt;element ref="{}HORIZONTAL_CS_NAME"/>
 *         &lt;element ref="{}HORIZONTAL_CS_CODE"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "t_Horizontal_CS", propOrder = {

})
public class THorizontalCS {

    @XmlElement(name = "HORIZONTAL_CS_TYPE", required = true)
    protected THORIZONTALCSTYPE horizontalcstype;
    @XmlElement(name = "HORIZONTAL_CS_NAME", required = true)
    protected THORIZONTALCSNAME horizontalcsname;
    @XmlElement(name = "HORIZONTAL_CS_CODE", required = true)
    protected THORIZONTALCSCODE horizontalcscode;

    /**
     * Gets the value of the horizontalcstype property.
     * 
     * @return
     *     possible object is
     *     {@link THORIZONTALCSTYPE }
     *     
     */
    public THORIZONTALCSTYPE getHORIZONTALCSTYPE() {
        return horizontalcstype;
    }

    /**
     * Sets the value of the horizontalcstype property.
     * 
     * @param value
     *     allowed object is
     *     {@link THORIZONTALCSTYPE }
     *     
     */
    public void setHORIZONTALCSTYPE(THORIZONTALCSTYPE value) {
        this.horizontalcstype = value;
    }

    /**
     * Gets the value of the horizontalcsname property.
     * 
     * @return
     *     possible object is
     *     {@link THORIZONTALCSNAME }
     *     
     */
    public THORIZONTALCSNAME getHORIZONTALCSNAME() {
        return horizontalcsname;
    }

    /**
     * Sets the value of the horizontalcsname property.
     * 
     * @param value
     *     allowed object is
     *     {@link THORIZONTALCSNAME }
     *     
     */
    public void setHORIZONTALCSNAME(THORIZONTALCSNAME value) {
        this.horizontalcsname = value;
    }

    /**
     * Gets the value of the horizontalcscode property.
     * 
     * @return
     *     possible object is
     *     {@link THORIZONTALCSCODE }
     *     
     */
    public THORIZONTALCSCODE getHORIZONTALCSCODE() {
        return horizontalcscode;
    }

    /**
     * Sets the value of the horizontalcscode property.
     * 
     * @param value
     *     allowed object is
     *     {@link THORIZONTALCSCODE }
     *     
     */
    public void setHORIZONTALCSCODE(THORIZONTALCSCODE value) {
        this.horizontalcscode = value;
    }

}
