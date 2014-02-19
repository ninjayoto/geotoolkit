/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2008 - 2009, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.wfs.xml.v110;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.geotoolkit.wfs.xml.GetFeature;
import org.geotoolkit.wfs.xml.ResolveValueType;
import org.geotoolkit.wfs.xml.ResultTypeType;
import org.geotoolkit.wfs.xml.StoredQuery;

/**
 * A GetFeature element contains one or more Query elements that describe a query operation on one feature type.
 * In response to a GetFeature request, a Web Feature Service must be able to generate a GML3 response that validates
 * using a schema generated by the DescribeFeatureType request.
 * A Web Feature Service may support other possibly non-XML (and even binary) output formats as long as those formats
 * are advertised in the capabilities document.
 *
 *
 * <p>Java class for GetFeatureType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GetFeatureType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/wfs}BaseRequestType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/wfs}Query" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="resultType" type="{http://www.opengis.net/wfs}ResultTypeType" default="results" />
 *       &lt;attribute name="outputFormat" type="{http://www.w3.org/2001/XMLSchema}string" default="text/xml; subtype=gml/3.1.1" />
 *       &lt;attribute name="maxFeatures" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="traverseXlinkDepth" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="traverseXlinkExpiry" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 * @module pending
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetFeatureType", propOrder = {
    "query"
})
@XmlRootElement(name = "GetFeature")
public class GetFeatureType extends BaseRequestType implements GetFeature {

    @XmlElement(name = "Query", required = true)
    private List<QueryType> query;
    @XmlAttribute
    private ResultTypeType resultType;
    @XmlAttribute
    private String outputFormat;
    @XmlAttribute
    @XmlSchemaType(name = "positiveInteger")
    private Integer maxFeatures;
    @XmlAttribute
    private String traverseXlinkDepth;
    @XmlAttribute
    @XmlSchemaType(name = "positiveInteger")
    private Integer traverseXlinkExpiry;

    public GetFeatureType() {

    }

    public GetFeatureType(final String service, final String version, final String handle, final Integer maxFeatures,
            final List<QueryType> query, final ResultTypeType resultType, final String outputformat) {
        super(service, version, handle);
        this.maxFeatures  = maxFeatures;
        this.query        = query;
        this.resultType   = resultType;
        this.outputFormat = outputformat;
    }

    public GetFeatureType(final String service, final String version, final String handle, final Integer maxFeatures,
            final List<QueryType> query, final ResultTypeType resultType, final String outputformat, final String trXlinkDepth, final Integer trXlinkExpiry) {
        super(service, version, handle);
        this.maxFeatures  = maxFeatures;
        this.query        = query;
        this.resultType   = resultType;
        this.outputFormat = outputformat;
        this.traverseXlinkDepth  = trXlinkDepth;
        this.traverseXlinkExpiry = trXlinkExpiry;
    }

    /**
     * Gets the value of the query property.
     */
    @Override
    public List<QueryType> getQuery() {
        if (query == null) {
            query = new ArrayList<QueryType>();
        }
        return this.query;
    }

    /**
     * Gets the value of the resultType property.
     *
     * @return
     *     possible object is
     *     {@link ResultTypeType }
     *
     */
    @Override
    public ResultTypeType getResultType() {
        if (resultType == null) {
            return ResultTypeType.RESULTS;
        } else {
            return resultType;
        }
    }

    /**
     * Sets the value of the resultType property.
     *
     * @param value
     *     allowed object is
     *     {@link ResultTypeType }
     *
     */
    public void setResultType(final ResultTypeType value) {
        this.resultType = value;
    }

    /**
     * Gets the value of the outputFormat property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    @Override
    public String getOutputFormat() {
        if (outputFormat == null) {
            return "text/xml; subtype=gml/3.1.1";
        } else {
            return outputFormat;
        }
    }

    /**
     * Sets the value of the outputFormat property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOutputFormat(final String value) {
        this.outputFormat = value;
    }

    /**
     * Gets the value of the maxFeatures property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getMaxFeatures() {
        return maxFeatures;
    }

    @Override
    public int getCount() {
        if (maxFeatures != null) {
            return maxFeatures;
        }
        return 0;
    }

    @Override
    public int getStartIndex() {
        return 0;
    }

    /**
     * Sets the value of the maxFeatures property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setMaxFeatures(final Integer value) {
        this.maxFeatures = value;
    }

    /**
     * Gets the value of the traverseXlinkDepth property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTraverseXlinkDepth() {
        return traverseXlinkDepth;
    }

    /**
     * Sets the value of the traverseXlinkDepth property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTraverseXlinkDepth(final String value) {
        this.traverseXlinkDepth = value;
    }

    /**
     * Gets the value of the traverseXlinkExpiry property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getTraverseXlinkExpiry() {
        return traverseXlinkExpiry;
    }

    /**
     * Sets the value of the traverseXlinkExpiry property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setTraverseXlinkExpiry(final Integer value) {
        this.traverseXlinkExpiry = value;
    }

    @Override
    public List<? extends StoredQuery> getStoredQuery() {
        return new ArrayList();
    }

    @Override
    public ResolveValueType getResolve() {
        return null;
    }

    @Override
    public String getResolveDepth() {
        return null;
    }

    @Override
    public int getResolveTimeout() {
        return -1;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        if (maxFeatures != null) {
            sb.append("maxFeatures").append(maxFeatures).append('\n');
        }
        if (outputFormat != null) {
            sb.append("outputFormat").append(outputFormat).append('\n');
        }
        if (resultType != null) {
            sb.append("resultType").append(resultType).append('\n');
        }
        if (traverseXlinkDepth != null) {
            sb.append("traverseXlinkDepth").append(traverseXlinkDepth).append('\n');
        }
        if (traverseXlinkExpiry != null) {
            sb.append("traverseXlinkExpiry").append(traverseXlinkExpiry).append('\n');
        }
        if (query != null) {
            sb.append("query:\n");
            for (QueryType q : query) {
                sb.append(q).append('\n');
            }
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (this.query != null ? this.query.hashCode() : 0);
        hash = 17 * hash + (this.resultType != null ? this.resultType.hashCode() : 0);
        hash = 17 * hash + (this.outputFormat != null ? this.outputFormat.hashCode() : 0);
        hash = 17 * hash + (this.maxFeatures != null ? this.maxFeatures.hashCode() : 0);
        hash = 17 * hash + (this.traverseXlinkDepth != null ? this.traverseXlinkDepth.hashCode() : 0);
        hash = 17 * hash + (this.traverseXlinkExpiry != null ? this.traverseXlinkExpiry.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof GetFeatureType && super.equals(obj)) {
            GetFeatureType that = (GetFeatureType) obj;
            return Objects.equals(this.maxFeatures, that.maxFeatures) &&
                   Objects.equals(this.outputFormat, that.outputFormat) &&
                   Objects.equals(this.query, that.query) &&
                   Objects.equals(this.resultType, that.resultType) &&
                   Objects.equals(this.traverseXlinkDepth, that.traverseXlinkDepth) &&
                   Objects.equals(this.traverseXlinkExpiry, that.traverseXlinkExpiry);
        }
        return false;
    }
}