/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2008 - 2011, Geomatys
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
package org.geotoolkit.map;

import java.util.Collection;
import org.geotoolkit.storage.coverage.DefaultCoverageResource;
import org.geotoolkit.coverage.grid.GridCoverage2D;
import org.geotoolkit.coverage.io.CoverageStoreException;
import org.geotoolkit.factory.FactoryFinder;
import org.geotoolkit.factory.Hints;
import org.geotoolkit.util.NamesExt;
import org.apache.sis.referencing.CommonCRS;
import org.apache.sis.storage.DataStoreException;
import org.apache.sis.storage.FeatureSet;
import org.geotoolkit.style.MutableStyle;
import org.geotoolkit.style.MutableStyleFactory;
import org.geotoolkit.style.DefaultStyleFactory;
import org.geotoolkit.style.MutableFeatureTypeStyle;
import org.geotoolkit.style.MutableRule;
import org.geotoolkit.style.RandomStyleBuilder;
import org.geotoolkit.style.StyleConstants;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.geotoolkit.storage.coverage.CoverageResource;

/**
 * Utility class to create MapLayers, MapContexts and Elevation models from different sources.
 * This class is thread safe.
 *
 * @author Johann Sorel (Geomatys)
 * @module
 */
public final class MapBuilder {

    private MapBuilder(){}

    /**
     * Create a Default Mapcontext object using coordinate reference system : CRS:84.
     * @return MapContext
     */
    public static MapContext createContext(){
        return createContext(CommonCRS.WGS84.normalizedGeographic());
    }

    /**
     * Create a Default Mapcontext object with the given coordinate reference system.
     * The crs is not used for rendering, it is only used when calling the getEnvelope
     * method.
     * @param crs : mapcontext CoordinateReferenceSystem
     * @return MapContext
     */
    public static MapContext createContext(final CoordinateReferenceSystem crs){
        return new DefaultMapContext(crs);
    }

    /**
     * Create a Default MapItem object. It can be used to group layers.
     * @return MapItem
     */
    public static MapItem createItem(){
        return new DefaultMapItem();
    }

    /**
     * Create an empty map layer without any datas. It can be useful in different
     * kind of applications, like holding a space in the map context for a layer
     * when a featurestore is unavailable.
     * @return EmptyMapLayer
     */
    public static EmptyMapLayer createEmptyMapLayer(){
        final Hints hints = new Hints();
        hints.put(Hints.STYLE_FACTORY, MutableStyleFactory.class);
        final MutableStyleFactory factory = (MutableStyleFactory)FactoryFinder.getStyleFactory(hints);
        return new EmptyMapLayer(factory.style());
    }

    //geometryType
    /**
     * Create a default collection map layer with a collection.
     * The style expect the default geometry property to be named 'Geometry'
     *
     * @param collection layer data collection
     * @param style layer style
     * @return CollectionMapLayer
     */
    public static CollectionMapLayer createCollectionLayer(final Collection<?> collection){
        final MutableStyleFactory sf = new DefaultStyleFactory();
        final FilterFactory ff = FactoryFinder.getFilterFactory(null);
        final MutableStyle style = sf.style();
        final MutableFeatureTypeStyle fts = sf.featureTypeStyle();

        final MutableRule rulePoint = sf.rule(StyleConstants.DEFAULT_POINT_SYMBOLIZER);
        rulePoint.setFilter(ff.or(
                                ff.equals(ff.function("geometryType", ff.property("geometry")), ff.literal("Point")),
                                ff.equals(ff.function("geometryType", ff.property("geometry")), ff.literal("MultiPoint"))
                            ));
        final MutableRule ruleLine = sf.rule(StyleConstants.DEFAULT_LINE_SYMBOLIZER);
        ruleLine.setFilter(ff.or(
                                ff.equals(ff.function("geometryType", ff.property("geometry")), ff.literal("LineString")),
                                ff.equals(ff.function("geometryType", ff.property("geometry")), ff.literal("MultiLineString"))
                            ));
        final MutableRule rulePolygon = sf.rule(StyleConstants.DEFAULT_POLYGON_SYMBOLIZER);
        rulePolygon.setFilter(ff.or(
                                ff.equals(ff.function("geometryType", ff.property("geometry")), ff.literal("Polygon")),
                                ff.equals(ff.function("geometryType", ff.property("geometry")), ff.literal("MultiPolygon"))
                            ));

        fts.rules().add(rulePoint);
        fts.rules().add(ruleLine);
        fts.rules().add(rulePolygon);
        style.featureTypeStyles().add(fts);

        return createCollectionLayer(collection, style);
    }

    /**
     * Create a default collection map layer with a collection and a style.
     * @param collection layer data collection
     * @param style layer style
     * @return CollectionMapLayer
     */
    public static CollectionMapLayer createCollectionLayer(final Collection<?> collection, final MutableStyle style){
        return new DefaultCollectionMapLayer(collection, style);
    }

    /**
     * Create a default feature map layer with a feature collection and a style.
     * @param collection layer data collection
     * @return FeatureMapLayer
     */
    public static FeatureMapLayer createFeatureLayer(final FeatureSet collection){
        MutableStyle style;
        try {
            style = RandomStyleBuilder.createDefaultVectorStyle(collection.getType());
        } catch (DataStoreException ex) {
            style = ((MutableStyleFactory)FactoryFinder.getStyleFactory(null)).style(RandomStyleBuilder.createRandomPointSymbolizer());
        }
        return new DefaultFeatureMapLayer(collection, style);
    }

    /**
     * Create a default feature map layer with a feature collection and a style.
     * @param collection layer data collection
     * @param style layer style
     * @return FeatureMapLayer
     */
    public static FeatureMapLayer createFeatureLayer(final FeatureSet collection, final MutableStyle style){
        return new DefaultFeatureMapLayer(collection, style);
    }

    /**
     * Create a default coverage map layer with a gridCoverage, a style and the grid name.
     * @param grid GridCoverage2D
     * @param style layer style
     * @return  CoverageMapLayer
     */
    public static CoverageMapLayer createCoverageLayer(final GridCoverage2D grid, final MutableStyle style, final String name){
        final CoverageResource ref = new DefaultCoverageResource(grid, NamesExt.create(name));
        return createCoverageLayer(ref, style);
    }

    /**
     * Create a default coverage map layer with a image input.
     * Default style is used.
     *
     * @param ref input
     * @return  CoverageMapLayer
     */
    public static CoverageMapLayer createCoverageLayer(final Object input){
        final CoverageResource reference = new DefaultCoverageResource(input, NamesExt.create("image"));
        return createCoverageLayer(reference);
    }

    /**
     * Create a default coverage map layer with a coveragrReference.
     * Default style is used.
     *
     * @param ref CoverageResource
     * @return  CoverageMapLayer
     */
    public static CoverageMapLayer createCoverageLayer(final CoverageResource ref){
        return new DefaultCoverageMapLayer(ref, RandomStyleBuilder.createDefaultRasterStyle());
    }

    /**
     * Create a default coverage map layer with a coveragrReference, a style and the grid name.
     * @param ref CoverageResource
     * @param style layer style
     * @return  CoverageMapLayer
     */
    public static CoverageMapLayer createCoverageLayer(final CoverageResource ref, final MutableStyle style){
        return new DefaultCoverageMapLayer(ref, style);
    }

    /**
     * Create a default elevation model based on a grid coverage reader.
     *
     * @param grid : Coverage reader holding elevation values
     * @return ElevationModel
     */
    public static ElevationModel createElevationModel(final CoverageResource ref) throws CoverageStoreException {
        return createElevationModel(ref, 130, 2, 55);
    }

    /**
     * Create a default elevation model based on a grid coverage reader.
     *
     * @param grid : Coverage reader holding elevation values
     * @param offset : expression used to modified on the fly the elevation value
     * @param scale : a multiplication factor to use on the coverage values
     * @return ElevationModel
     */
    public static ElevationModel createElevationModel(final CoverageResource ref, final double azimuthAngle, final double altitudeAngle, final double altitudeScale) throws CoverageStoreException {
        return new ElevationModel(ref, azimuthAngle, altitudeAngle, altitudeScale);
    }
 }
