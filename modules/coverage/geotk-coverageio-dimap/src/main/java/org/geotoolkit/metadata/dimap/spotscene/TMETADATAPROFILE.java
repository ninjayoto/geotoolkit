//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.29 at 02:39:59 PM CEST 
//


package org.geotoolkit.metadata.dimap.spotscene;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for t_METADATA_PROFILE.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="t_METADATA_PROFILE">
 *   &lt;restriction base="{}String">
 *     &lt;enumeration value="SPOTSCENE_1A"/>
 *     &lt;enumeration value="SPOTSCENE_1B"/>
 *     &lt;enumeration value="SPOTSCENE_2A"/>
 *     &lt;enumeration value="SPOTSEGMENT_0"/>
 *     &lt;enumeration value="SPOTSEGMENT_1A"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "t_METADATA_PROFILE")
@XmlEnum
public enum TMETADATAPROFILE {

    @XmlEnumValue("SPOTSCENE_1A")
    SPOTSCENE_1_A("SPOTSCENE_1A"),
    @XmlEnumValue("SPOTSCENE_1B")
    SPOTSCENE_1_B("SPOTSCENE_1B"),
    @XmlEnumValue("SPOTSCENE_2A")
    SPOTSCENE_2_A("SPOTSCENE_2A"),
    SPOTSEGMENT_0("SPOTSEGMENT_0"),
    @XmlEnumValue("SPOTSEGMENT_1A")
    SPOTSEGMENT_1_A("SPOTSEGMENT_1A");
    private final String value;

    TMETADATAPROFILE(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TMETADATAPROFILE fromValue(String v) {
        for (TMETADATAPROFILE c: TMETADATAPROFILE.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
