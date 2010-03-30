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
import org.geotoolkit.metadata.dimap.TBANDINDEX;


/**
 * <p>Java class for t_Band_Parameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="t_Band_Parameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}BAND_INDEX"/>
 *         &lt;element ref="{}Gain_Section" minOccurs="0"/>
 *         &lt;element ref="{}Dead_Detectors" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "t_Band_Parameters", propOrder = {

})
public class TBandParameters {

    @XmlElement(name = "BAND_INDEX", required = true)
    protected TBANDINDEX bandindex;
    @XmlElement(name = "Gain_Section")
    protected TGainSection gainSection;
    @XmlElement(name = "Dead_Detectors")
    protected TDeadDetectors deadDetectors;

    /**
     * Gets the value of the bandindex property.
     * 
     * @return
     *     possible object is
     *     {@link TBANDINDEX }
     *     
     */
    public TBANDINDEX getBANDINDEX() {
        return bandindex;
    }

    /**
     * Sets the value of the bandindex property.
     * 
     * @param value
     *     allowed object is
     *     {@link TBANDINDEX }
     *     
     */
    public void setBANDINDEX(TBANDINDEX value) {
        this.bandindex = value;
    }

    /**
     * Gets the value of the gainSection property.
     * 
     * @return
     *     possible object is
     *     {@link TGainSection }
     *     
     */
    public TGainSection getGainSection() {
        return gainSection;
    }

    /**
     * Sets the value of the gainSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link TGainSection }
     *     
     */
    public void setGainSection(TGainSection value) {
        this.gainSection = value;
    }

    /**
     * Gets the value of the deadDetectors property.
     * 
     * @return
     *     possible object is
     *     {@link TDeadDetectors }
     *     
     */
    public TDeadDetectors getDeadDetectors() {
        return deadDetectors;
    }

    /**
     * Sets the value of the deadDetectors property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDeadDetectors }
     *     
     */
    public void setDeadDetectors(TDeadDetectors value) {
        this.deadDetectors = value;
    }

}
