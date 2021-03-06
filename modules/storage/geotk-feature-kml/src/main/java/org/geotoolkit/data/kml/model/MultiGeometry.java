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
package org.geotoolkit.data.kml.model;

import java.util.List;

/**
 * <p>This interface maps MultiGeometry element.</p>
 *
 * <pre>
 * &lt;element name="MultiGeometry" type="kml:MultiGeometryType" substitutionGroup="kml:AbstractGeometryGroup"/>
 *
 * &lt;complexType name="MultiGeometryType" final="#all">
 *  &lt;complexContent>
 *      &lt;extension base="kml:AbstractGeometryType">
 *          &lt;sequence>
 *              &lt;element ref="kml:AbstractGeometryGroup" minOccurs="0" maxOccurs="unbounded"/>
 *              &lt;element ref="kml:MultiGeometrySimpleExtensionGroup" minOccurs="0" maxOccurs="unbounded"/>
 *              &lt;element ref="kml:MultiGeometryObjectExtensionGroup" minOccurs="0" maxOccurs="unbounded"/>
 *          &lt;/sequence>
 *      &lt;/extension>
 *  &lt;/complexContent>
 * &lt;/complexType>
 *
 * &lt;element name="MultiGeometrySimpleExtensionGroup" abstract="true" type="anySimpleType"/>
 * &lt;element name="MultiGeometryObjectExtensionGroup" abstract="true" substitutionGroup="kml:AbstractObjectGroup"/>
 * </pre>
 *
 * @author Samuel Andrés
 * @module
 */
public interface MultiGeometry extends AbstractGeometry {

    /**
     *
     * @return
     */
    List<AbstractGeometry> getGeometries();

    /**
     *
     * @param geometries
     */
    void setGeometries(List<AbstractGeometry> geometries);

    /**
     * <p>This method is based on JTS geometries method getCentroid() and
     * get an center point for multigeometry based on coordinates of the whole
     * geometries conained in MultiGeometry.</p>
     *
     * @return
     */
    org.locationtech.jts.geom.Point getCentroid();

    /**
     * <p>This method returns coordinates of the whole geometries contained in
     * MultyGeometry.</p>
     *
     * @return
     */
    org.locationtech.jts.geom.Coordinate[] getCoordinates();

}
