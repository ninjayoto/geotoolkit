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
 * <p>Java class for t_Satellite_Attitudes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="t_Satellite_Attitudes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}Raw_Attitudes"/>
 *         &lt;element ref="{}Corrected_Attitudes" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "t_Satellite_Attitudes", propOrder = {

})
public class TSatelliteAttitudes {

    @XmlElement(name = "Raw_Attitudes", required = true)
    protected TRawAttitudes rawAttitudes;
    @XmlElement(name = "Corrected_Attitudes")
    protected TCorrectedAttitudes correctedAttitudes;

    /**
     * Gets the value of the rawAttitudes property.
     * 
     * @return
     *     possible object is
     *     {@link TRawAttitudes }
     *     
     */
    public TRawAttitudes getRawAttitudes() {
        return rawAttitudes;
    }

    /**
     * Sets the value of the rawAttitudes property.
     * 
     * @param value
     *     allowed object is
     *     {@link TRawAttitudes }
     *     
     */
    public void setRawAttitudes(TRawAttitudes value) {
        this.rawAttitudes = value;
    }

    /**
     * Gets the value of the correctedAttitudes property.
     * 
     * @return
     *     possible object is
     *     {@link TCorrectedAttitudes }
     *     
     */
    public TCorrectedAttitudes getCorrectedAttitudes() {
        return correctedAttitudes;
    }

    /**
     * Sets the value of the correctedAttitudes property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCorrectedAttitudes }
     *     
     */
    public void setCorrectedAttitudes(TCorrectedAttitudes value) {
        this.correctedAttitudes = value;
    }

}
