//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.29 at 02:39:59 PM CEST 
//


package org.geotoolkit.metadata.dimap.spotscene;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Correction_Algorithm_Types.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Correction_Algorithm_Types">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DROP_OUT_DETECTION_CORRECTION"/>
 *     &lt;enumeration value="INTER_ARRAY_CORRECTION"/>
 *     &lt;enumeration value="DCT_COMPRESSION_CORRECTION"/>
 *     &lt;enumeration value="REMANENCE_CORRECTION"/>
 *     &lt;enumeration value="ODD_EVEN_LINES_CORRECTION"/>
 *     &lt;enumeration value="ATTITUDE_CORRECTION"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Correction_Algorithm_Types")
@XmlEnum
public enum CorrectionAlgorithmTypes {

    DROP_OUT_DETECTION_CORRECTION,
    INTER_ARRAY_CORRECTION,
    DCT_COMPRESSION_CORRECTION,
    REMANENCE_CORRECTION,
    ODD_EVEN_LINES_CORRECTION,
    ATTITUDE_CORRECTION;

    public String value() {
        return name();
    }

    public static CorrectionAlgorithmTypes fromValue(String v) {
        return valueOf(v);
    }

}
