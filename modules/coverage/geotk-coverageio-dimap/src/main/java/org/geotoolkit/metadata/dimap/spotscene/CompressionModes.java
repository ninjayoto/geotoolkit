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
 * <p>Java class for Compression_Modes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Compression_Modes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LINEAR"/>
 *     &lt;enumeration value="DPCM"/>
 *     &lt;enumeration value="DCT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Compression_Modes")
@XmlEnum
public enum CompressionModes {

    LINEAR,
    DPCM,
    DCT;

    public String value() {
        return name();
    }

    public static CompressionModes fromValue(String v) {
        return valueOf(v);
    }

}
