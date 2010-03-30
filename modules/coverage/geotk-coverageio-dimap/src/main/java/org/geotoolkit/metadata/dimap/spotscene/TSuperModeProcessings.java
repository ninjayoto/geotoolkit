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
 * <p>Java class for t_SuperMode_Processings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="t_SuperMode_Processings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}SM_CORRELATION_NEEDED" minOccurs="0"/>
 *         &lt;element ref="{}SM_RAW_GRID_FILTERING" minOccurs="0"/>
 *         &lt;element ref="{}SM_PROCESSING_TYPE" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "t_SuperMode_Processings", propOrder = {

})
public class TSuperModeProcessings {

    @XmlElement(name = "SM_CORRELATION_NEEDED")
    protected YesNo smcorrelationneeded;
    @XmlElement(name = "SM_RAW_GRID_FILTERING")
    protected YesNo smrawgridfiltering;
    @XmlElement(name = "SM_PROCESSING_TYPE")
    protected SMProcessingTypes smprocessingtype;

    /**
     * Gets the value of the smcorrelationneeded property.
     * 
     * @return
     *     possible object is
     *     {@link YesNo }
     *     
     */
    public YesNo getSMCORRELATIONNEEDED() {
        return smcorrelationneeded;
    }

    /**
     * Sets the value of the smcorrelationneeded property.
     * 
     * @param value
     *     allowed object is
     *     {@link YesNo }
     *     
     */
    public void setSMCORRELATIONNEEDED(YesNo value) {
        this.smcorrelationneeded = value;
    }

    /**
     * Gets the value of the smrawgridfiltering property.
     * 
     * @return
     *     possible object is
     *     {@link YesNo }
     *     
     */
    public YesNo getSMRAWGRIDFILTERING() {
        return smrawgridfiltering;
    }

    /**
     * Sets the value of the smrawgridfiltering property.
     * 
     * @param value
     *     allowed object is
     *     {@link YesNo }
     *     
     */
    public void setSMRAWGRIDFILTERING(YesNo value) {
        this.smrawgridfiltering = value;
    }

    /**
     * Gets the value of the smprocessingtype property.
     * 
     * @return
     *     possible object is
     *     {@link SMProcessingTypes }
     *     
     */
    public SMProcessingTypes getSMPROCESSINGTYPE() {
        return smprocessingtype;
    }

    /**
     * Sets the value of the smprocessingtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link SMProcessingTypes }
     *     
     */
    public void setSMPROCESSINGTYPE(SMProcessingTypes value) {
        this.smprocessingtype = value;
    }

}
