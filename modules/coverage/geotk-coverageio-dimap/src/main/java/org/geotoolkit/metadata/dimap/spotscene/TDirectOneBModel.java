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
 * <p>Java class for t_Direct_OneB_Model complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="t_Direct_OneB_Model">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}I"/>
 *         &lt;element ref="{}J"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "t_Direct_OneB_Model", propOrder = {

})
public class TDirectOneBModel {

    @XmlElement(name = "I", required = true)
    protected TI i;
    @XmlElement(name = "J", required = true)
    protected TJ j;

    /**
     * Gets the value of the i property.
     * 
     * @return
     *     possible object is
     *     {@link TI }
     *     
     */
    public TI getI() {
        return i;
    }

    /**
     * Sets the value of the i property.
     * 
     * @param value
     *     allowed object is
     *     {@link TI }
     *     
     */
    public void setI(TI value) {
        this.i = value;
    }

    /**
     * Gets the value of the j property.
     * 
     * @return
     *     possible object is
     *     {@link TJ }
     *     
     */
    public TJ getJ() {
        return j;
    }

    /**
     * Sets the value of the j property.
     * 
     * @param value
     *     allowed object is
     *     {@link TJ }
     *     
     */
    public void setJ(TJ value) {
        this.j = value;
    }

}
