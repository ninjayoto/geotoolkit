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
 * <p>Java class for t_Raw_Attitudes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="t_Raw_Attitudes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}Aocs_Attitude"/>
 *         &lt;element ref="{}Star_Tracker_Attitude" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "t_Raw_Attitudes", propOrder = {

})
public class TRawAttitudes {

    @XmlElement(name = "Aocs_Attitude", required = true)
    protected TAocsAttitude aocsAttitude;
    @XmlElement(name = "Star_Tracker_Attitude")
    protected TStarTrackerAttitude starTrackerAttitude;

    /**
     * Gets the value of the aocsAttitude property.
     * 
     * @return
     *     possible object is
     *     {@link TAocsAttitude }
     *     
     */
    public TAocsAttitude getAocsAttitude() {
        return aocsAttitude;
    }

    /**
     * Sets the value of the aocsAttitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAocsAttitude }
     *     
     */
    public void setAocsAttitude(TAocsAttitude value) {
        this.aocsAttitude = value;
    }

    /**
     * Gets the value of the starTrackerAttitude property.
     * 
     * @return
     *     possible object is
     *     {@link TStarTrackerAttitude }
     *     
     */
    public TStarTrackerAttitude getStarTrackerAttitude() {
        return starTrackerAttitude;
    }

    /**
     * Sets the value of the starTrackerAttitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link TStarTrackerAttitude }
     *     
     */
    public void setStarTrackerAttitude(TStarTrackerAttitude value) {
        this.starTrackerAttitude = value;
    }

}
