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
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for t_Production complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="t_Production">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}DATASET_PRODUCER_NAME"/>
 *         &lt;element ref="{}DATASET_PRODUCER_URL" minOccurs="0"/>
 *         &lt;element ref="{}DATASET_PRODUCTION_DATE" minOccurs="0"/>
 *         &lt;element ref="{}PRODUCT_TYPE"/>
 *         &lt;element ref="{}PRODUCT_INFO" minOccurs="0"/>
 *         &lt;element ref="{}JOB_ID" minOccurs="0"/>
 *         &lt;element ref="{}Production_Facility"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "t_Production", propOrder = {

})
public class TProduction {

    @XmlElement(name = "DATASET_PRODUCER_NAME", required = true)
    protected String datasetproducername;
    @XmlElement(name = "DATASET_PRODUCER_URL")
    protected TDATASETPRODUCERURL datasetproducerurl;
    @XmlElement(name = "DATASET_PRODUCTION_DATE")
    protected XMLGregorianCalendar datasetproductiondate;
    @XmlElement(name = "PRODUCT_TYPE", required = true)
    protected String producttype;
    @XmlElement(name = "PRODUCT_INFO")
    protected TPRODUCTINFO productinfo;
    @XmlElement(name = "JOB_ID")
    protected String jobid;
    @XmlElement(name = "Production_Facility", required = true)
    protected TProductionFacility productionFacility;

    /**
     * Gets the value of the datasetproducername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATASETPRODUCERNAME() {
        return datasetproducername;
    }

    /**
     * Sets the value of the datasetproducername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATASETPRODUCERNAME(String value) {
        this.datasetproducername = value;
    }

    /**
     * Gets the value of the datasetproducerurl property.
     * 
     * @return
     *     possible object is
     *     {@link TDATASETPRODUCERURL }
     *     
     */
    public TDATASETPRODUCERURL getDATASETPRODUCERURL() {
        return datasetproducerurl;
    }

    /**
     * Sets the value of the datasetproducerurl property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDATASETPRODUCERURL }
     *     
     */
    public void setDATASETPRODUCERURL(TDATASETPRODUCERURL value) {
        this.datasetproducerurl = value;
    }

    /**
     * Gets the value of the datasetproductiondate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATASETPRODUCTIONDATE() {
        return datasetproductiondate;
    }

    /**
     * Sets the value of the datasetproductiondate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATASETPRODUCTIONDATE(XMLGregorianCalendar value) {
        this.datasetproductiondate = value;
    }

    /**
     * Gets the value of the producttype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRODUCTTYPE() {
        return producttype;
    }

    /**
     * Sets the value of the producttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRODUCTTYPE(String value) {
        this.producttype = value;
    }

    /**
     * Gets the value of the productinfo property.
     * 
     * @return
     *     possible object is
     *     {@link TPRODUCTINFO }
     *     
     */
    public TPRODUCTINFO getPRODUCTINFO() {
        return productinfo;
    }

    /**
     * Sets the value of the productinfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPRODUCTINFO }
     *     
     */
    public void setPRODUCTINFO(TPRODUCTINFO value) {
        this.productinfo = value;
    }

    /**
     * Gets the value of the jobid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJOBID() {
        return jobid;
    }

    /**
     * Sets the value of the jobid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJOBID(String value) {
        this.jobid = value;
    }

    /**
     * Gets the value of the productionFacility property.
     * 
     * @return
     *     possible object is
     *     {@link TProductionFacility }
     *     
     */
    public TProductionFacility getProductionFacility() {
        return productionFacility;
    }

    /**
     * Sets the value of the productionFacility property.
     * 
     * @param value
     *     allowed object is
     *     {@link TProductionFacility }
     *     
     */
    public void setProductionFacility(TProductionFacility value) {
        this.productionFacility = value;
    }

}
