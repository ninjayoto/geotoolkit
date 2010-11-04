/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2010, Geomatys
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
package org.geotoolkit.xal.model;

/**
 *
 * <p>This interface maps PostOfficeNumber type.</p>
 *
 * <pre>
 * &lt;xs:complexType mixed="true">
 *  &lt;xs:attribute name="Indicator">...
 *  &lt;/xs:attribute>
 *  &lt;xs:attribute name="IndicatorOccurrence">...
 *  &lt;/xs:attribute>
 *  &lt;xs:attributeGroup ref="grPostal"/>
 *  &lt;xs:anyAttribute namespace="##other"/>
 * &lt;/xs:complexType>
 * </pre>
 *
 * @author Samuel Andrés
 * @module pending
 */
public interface PostOfficeNumber {

    /**
     *
     * @return
     */
    String getContent();

    /**
     * <p>MS in MS 62, # in MS # 12, etc.</p>
     * @return
     */
    String getIndicator();

    /**
     * <p>MS occurs before 62 in MS 62.</p>
     * @return
     */
    AfterBeforeEnum getIndicatorOccurrence();

    /**
     * 
     * @return
     */
    GrPostal getGrPostal();

    /**
     *
     * @param content
     */
    void setContent(String content);

    /**
     *
     * @param indicator
     */
    void setIndicator(String indicator);

    /**
     *
     * @param indicatorOccurrence
     */
    void setIndicatorOccurrence(AfterBeforeEnum indicatorOccurrence);

    /**
     * 
     * @param grPostal
     */
    void setGrPostal(GrPostal grPostal);
}
