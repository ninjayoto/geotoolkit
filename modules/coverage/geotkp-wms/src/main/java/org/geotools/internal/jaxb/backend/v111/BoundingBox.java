package org.geotools.internal.jaxb.backend.v111;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * @author Guilhem Legal
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "BoundingBox")
public class BoundingBox {

   
    @XmlAttribute(name = "SRS")
    private String srs;
    @XmlAttribute(required = true)
    private double minx;
    @XmlAttribute(required = true)
    private double miny;
    @XmlAttribute(required = true)
    private double maxx;
    @XmlAttribute(required = true)
    private double maxy;
    @XmlAttribute
    private Double resx;
    @XmlAttribute
    private Double resy;

    /**
     * An empty constructor used by JAXB.
     */
    BoundingBox() {
    }

    /**
     * Build a new bounding box version 1.1.1
     *
     */
    public BoundingBox(final String crs, final double minx, final double miny,
            final double maxx, final double maxy, final double resx, final double resy, String version) {
        this.maxx = maxx;
        this.maxy = maxy;
        this.minx = minx;
        this.miny = miny;
        this.srs  = crs;
        this.resx = resx;
        this.resy = resy;
    }
    
    /**
     * Gets the value of the crs property.
     */
    public String getSRS() {
        return srs;
    }

    /**
     * Gets the value of the minx property.
     * 
     */
    public double getMinx() {
        return minx;
    }

    /**
     * Gets the value of the miny property.
     * 
     */
    public double getMiny() {
        return miny;
    }

    /**
     * Gets the value of the maxx property.
     * 
     */
    public double getMaxx() {
        return maxx;
    }

    /**
     * Gets the value of the maxy property.
     * 
     */
    public double getMaxy() {
        return maxy;
    }

    /**
     * Gets the value of the resx property.
     */
    public Double getResx() {
        return resx;
    }

    /**
     */
    public Double getResy() {
        return resy;
    }

}
