/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2014-2017, Geomatys
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
package org.geotoolkit.internal.data;

import java.util.Map;
import java.util.stream.Stream;
import org.geotoolkit.feature.FeatureTypeExt;
import org.geotoolkit.feature.ReprojectMapper;
import org.geotoolkit.feature.TransformMapper;
import org.geotoolkit.feature.ViewMapper;
import org.apache.sis.storage.DataStoreException;
import org.geotoolkit.data.AbstractFeatureCollection;
import org.geotoolkit.data.FeatureCollection;
import org.geotoolkit.data.FeatureReader;
import org.geotoolkit.data.FeatureStoreRuntimeException;
import org.geotoolkit.data.FeatureStreams;
import org.geotoolkit.data.query.Query;
import org.geotoolkit.data.session.Session;
import org.geotoolkit.factory.Hints;
import org.geotoolkit.geometry.jts.transform.GeometryScaleTransformer;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureType;
import org.opengis.feature.MismatchedFeatureException;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author Johann Sorel (Geomatys)
 */
public class GenericQueryFeatureIterator {

    public static FeatureReader wrap(FeatureReader reader, final Query remainingParameters) throws DataStoreException{

        final Integer start = remainingParameters.getStartIndex();
        final Integer max = remainingParameters.getMaxFeatures();
        final Filter filter = remainingParameters.getFilter();
        final String[] properties = remainingParameters.getPropertyNames();
        final SortBy[] sorts = remainingParameters.getSortBy();
        final double[] resampling = remainingParameters.getResolution();
        final CoordinateReferenceSystem crs = remainingParameters.getCoordinateSystemReproject();
        final Hints hints = remainingParameters.getHints();

        //we should take care of wrapping the reader in a correct order to avoid
        //unnecessary calculations. fast and reducing number wrapper should be placed first.
        //but we must not take misunderstanding assumptions neither.
        //exemple : filter is slow than startIndex and MaxFeature but must be placed before
        //          otherwise the result will be illogic.


        //wrap sort by ---------------------------------------------------------
        //This can be really expensive, and force the us to read the full iterator.
        //that may cause out of memory errors.
        if(sorts != null && sorts.length != 0){
            reader = FeatureStreams.sort(reader, sorts);
        }

        //wrap filter ----------------------------------------------------------
        //we must keep the filter first since it impacts the start index and max feature
        if(filter != null && filter != Filter.INCLUDE){
            if(filter == Filter.EXCLUDE){
                //filter that exclude everything, use optimzed reader
                reader = FeatureStreams.emptyReader(reader.getFeatureType());
                //close original reader
                reader.close();
            }else{
                reader = FeatureStreams.filter(reader, filter);
            }
        }

        //wrap start index -----------------------------------------------------
        if(start != null && start > 0){
            reader = FeatureStreams.skip(reader, start);
        }

        //wrap max -------------------------------------------------------------
        if(max != null){
            if(max == 0){
                //use an optimized reader
                reader = FeatureStreams.emptyReader(reader.getFeatureType());
                //close original reader
                reader.close();
            }else{
                reader = FeatureStreams.limit(reader, max);
            }
        }

        //wrap properties  -----------------------------------------------------
        final FeatureType original = reader.getFeatureType();

        if(properties!=null && !FeatureTypeExt.isAllProperties(original, properties)) {
            try {
                reader = FeatureStreams.decorate(reader,  new ViewMapper(original, properties),hints);
            } catch (MismatchedFeatureException | IllegalStateException ex) {
                throw new DataStoreException(ex);
            }
        }

        //wrap resampling ------------------------------------------------------
        if(resampling != null){
            final GeometryScaleTransformer trs = new GeometryScaleTransformer(resampling[0], resampling[1]);
            final TransformMapper ttype = new TransformMapper(reader.getFeatureType(), trs);
            reader = FeatureStreams.decorate(reader, ttype, hints);
        }

        //wrap reprojection ----------------------------------------------------
        if(crs != null){
            try {
                reader = FeatureStreams.decorate(reader, new ReprojectMapper(reader.getFeatureType(), crs), hints);
            } catch (MismatchedFeatureException ex) {
                throw new DataStoreException(ex);
            }
        }

        return reader;
    }

    public static FeatureCollection wrap(final FeatureCollection col, final Query query){
        return new AbstractFeatureCollection("wrap", col.getSession()) {

            private FeatureType type = null;

            @Override
            public FeatureType getType() {
                if(type==null){
                    try (FeatureReader ite = iterator(null)) {
                        type = ite.getFeatureType();
                    }
                }
                return type;
            }

            @Override
            public boolean isWritable() {
                return false;
            }

            @Override
            public FeatureReader iterator(Hints hints) throws FeatureStoreRuntimeException {
                final FeatureReader ite = (FeatureReader) col.iterator();
                try {
                    return wrap(ite, query);
                } catch (DataStoreException ex) {
                    throw new FeatureStoreRuntimeException(ex);
                }
            }

            @Override
            public void update(Filter filter, Map values) throws DataStoreException {
                throw new DataStoreException("Not supported.");
            }

            @Override
            public void remove(Filter filter) throws DataStoreException {
                throw new DataStoreException("Not supported.");
            }
        };
    }

    public static Stream<Feature> wrap(Stream<Feature> stream, FeatureType type, Query query) throws DataStoreException {
        final FeatureReader reader = FeatureStreams.asReader(stream.iterator(),type);
        return FeatureStreams.asStream(wrap(reader, query));
    }

}
