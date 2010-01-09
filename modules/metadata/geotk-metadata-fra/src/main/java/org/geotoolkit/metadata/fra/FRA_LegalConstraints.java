/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2008-2010, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2009-2010, Geomatys
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotoolkit.metadata.fra;

import java.util.Collection;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.constraint.LegalConstraints;

import org.geotoolkit.lang.ThreadSafe;
import org.geotoolkit.metadata.iso.constraint.DefaultLegalConstraints;


/**
 * AFNOR extension to ISO {@link LegalConstraints}.
 * The following schema fragment specifies the expected content contained within this class.
 *
 * {@preformat xml
 *   <complexType name="FRA_LegalConstraints_Type">
 *     <complexContent>
 *       <extension base="{http://www.isotc211.org/2005/gmd}MD_LegalConstraints_Type">
 *         <sequence>
 *           <element name="citation" type="{http://www.isotc211.org/2005/gmd}CI_Citation_PropertyType" maxOccurs="unbounded" minOccurs="0"/>
 *         </sequence>
 *       </extension>
 *     </complexContent>
 *   </complexType>
 * }
 *
 * @author Cédric Briançon (Geomatys)
 * @author Guilhem Legal (Geomatys)
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.03
 *
 * @since 3.00
 * @module
 */
@ThreadSafe
@XmlType(name = "FRA_LegalConstraints_Type")
@XmlRootElement(name="FRA_LegalConstraints")
public class FRA_LegalConstraints extends DefaultLegalConstraints {
    /**
     * For serialization purpose.
     */
    private static final long serialVersionUID = -4139267154783806229L;

    /**
     * The documents that specifies the nature of the constraints.
     */
    private Collection<Citation> citations;

    /**
     * Constructs an initially empty constraints.
     */
    public FRA_LegalConstraints() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @param source The metadata to copy.
     */
    public FRA_LegalConstraints(final LegalConstraints source) {
        super(source);
    }

    /**
     * Returns the documents that specifies the nature of the constraints.
     *
     * @return Citations to the current documents.
     */
    @XmlElement(name = "citation")
    public synchronized Collection<Citation> getCitations() {
        return xmlOptional(citations = nonNullCollection(citations, Citation.class));
    }

    /**
     * Sets the documents that specifies the nature of the constraints.
     *
     * @param newValues Citation to the new documents.
     */
    public synchronized void setCitations(final Collection<? extends Citation> newValues) {
        citations = copyCollection(newValues, citations, Citation.class);
    }
}
