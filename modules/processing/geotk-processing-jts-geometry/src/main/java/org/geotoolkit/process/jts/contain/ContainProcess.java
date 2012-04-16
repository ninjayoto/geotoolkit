/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2011, Geomatys
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
package org.geotoolkit.process.jts.contain;

import com.vividsolutions.jts.geom.Geometry;

import org.geotoolkit.process.jts.JTSProcessingUtils;
import org.geotoolkit.process.AbstractProcess;
import org.geotoolkit.process.ProcessException;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.FactoryException;

import static org.geotoolkit.process.jts.contain.ContainDescriptor.*;
import static org.geotoolkit.parameter.Parameters.*;

/**
 * Compute if the first geometry contained the second one.
 * The process ensure that two geometries are into the same CoordinateReferenceSystem.
 * @author Quentin Boileau (Geomatys)
 * @module pending
 */
public class ContainProcess extends AbstractProcess {

    public ContainProcess(final ParameterValueGroup input) {
        super(INSTANCE,input);
    }

    @Override
    protected void execute() throws ProcessException {
        try {
            final Geometry geom1 = value(GEOM1, inputParameters);
            Geometry geom2 = value(GEOM2, inputParameters);

            // ensure geometries are in the same CRS
            final CoordinateReferenceSystem resultCRS = JTSProcessingUtils.getCommonCRS(geom1, geom2);
            if (JTSProcessingUtils.isConversionNeeded(geom1, geom2)) {
                geom2 = JTSProcessingUtils.convertToCRS(geom2, resultCRS);
            }

            final Boolean result = (Boolean) geom1.contains(geom2);

            getOrCreate(RESULT, outputParameters).setValue(result);

        } catch (FactoryException ex) {
            throw new ProcessException(null, this, ex);
        } catch (TransformException ex) {
            throw new ProcessException(null, this, ex);
        }
    }

}
