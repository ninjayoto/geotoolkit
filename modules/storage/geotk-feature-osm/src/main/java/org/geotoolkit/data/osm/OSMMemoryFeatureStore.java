/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2010, Geomatys
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

package org.geotoolkit.data.osm;

import com.vividsolutions.jts.geom.LineString;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;

import org.geotoolkit.data.AbstractFeatureStore;
import org.geotoolkit.data.FeatureStoreFactory;
import org.geotoolkit.data.FeatureStoreFinder;
import org.geotoolkit.data.FeatureStoreRuntimeException;
import org.geotoolkit.data.FeatureReader;
import org.geotoolkit.data.FeatureWriter;
import org.geotoolkit.data.memory.GenericExtendFeatureIterator;
import org.geotoolkit.data.memory.GenericExtendFeatureIterator.FeatureExtend;
import org.geotoolkit.data.memory.MemoryFeatureStore;
import org.geotoolkit.data.osm.xml.OSMXMLReader;
import org.geotoolkit.data.query.Query;
import org.geotoolkit.data.query.QueryBuilder;
import org.geotoolkit.data.query.QueryCapabilities;
import org.geotoolkit.feature.calculated.CalculatedLineStringAttribute;
import org.geotoolkit.factory.FactoryFinder;
import org.geotoolkit.factory.Hints;
import org.geotoolkit.feature.AttributeDescriptorBuilder;
import org.geotoolkit.feature.DefaultAttribute;
import org.geotoolkit.util.NamesExt;
import org.geotoolkit.feature.FeatureTypeBuilder;
import org.apache.sis.storage.DataStoreException;

import org.geotoolkit.feature.Feature;
import org.geotoolkit.feature.Property;
import org.geotoolkit.feature.type.AttributeDescriptor;
import org.geotoolkit.feature.type.GeometryDescriptor;
import org.geotoolkit.feature.type.FeatureType;
import org.opengis.util.GenericName;
import org.geotoolkit.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;

import org.opengis.parameter.ParameterValueGroup;
import static org.geotoolkit.data.osm.model.OSMModelConstants.*;

/**
 * OSM DataStore, holds 3 feature types.
 * - Node
 * - Way
 * - relation
 * 
 * @author Johann Sorel (Geomatys)
 * @module pending
 */
public class OSMMemoryFeatureStore extends AbstractFeatureStore{

    private static final FilterFactory FF = FactoryFinder.getFilterFactory(null);

    private static final FeatureType TYPE_WAY_EXTENDED;
    private static final GeometryDescriptor ATT_WAY_GEOMETRY;
    private static final AttributeDescriptor ATT_NODES_LINK;
    
    static{
        final AttributeDescriptorBuilder adb = new AttributeDescriptorBuilder();
        final FeatureTypeBuilder ftb = new FeatureTypeBuilder();
        ATT_WAY_GEOMETRY = (GeometryDescriptor) adb.create(NamesExt.create(OSM_NAMESPACE, "geometry_calculated"), LineString.class, OSM_CRS, 1, 1, false, null);
        ATT_NODES_LINK = adb.create(TYPE_NODE, NamesExt.create(OSM_NAMESPACE, "nodes_link"), null,0,Integer.MAX_VALUE,false,null);

        ftb.copy(TYPE_WAY);
        ftb.add(ATT_WAY_GEOMETRY);
        ftb.add(ATT_NODES_LINK);
        ftb.setDefaultGeometry(ATT_WAY_GEOMETRY.getName());
        TYPE_WAY_EXTENDED = ftb.buildFeatureType();
    }

    private final FeatureExtend wayExtend = new FeatureExtend() {

        @Override
        public FeatureType getExtendedType(FeatureType original) {
            return TYPE_WAY_EXTENDED;
        }

        @Override
        public void extendProperties(Feature candidate, Collection<Property> props) {
            final Collection<Property> nodeProps = candidate.getProperties(ATT_WAY_NODES.getName());
            for(Property prop : nodeProps){
                final Long l = (Long) prop.getValue();
                props.add(new OSMNodeAttribute(ATT_NODES_LINK, l));
            }

            final CalculatedLineStringAttribute geomAtt = new CalculatedLineStringAttribute(
                ATT_WAY_GEOMETRY,
                ATT_NODES_LINK.getName(), ATT_NODE_POINT.getName());
            props.add(geomAtt);
            geomAtt.setRelated(candidate);
        }
    };

    private final MemoryFeatureStore memoryStore;

    public OSMMemoryFeatureStore(final ParameterValueGroup params, 
            final Object input) throws IOException, XMLStreamException, DataStoreException{
        super(params);
        memoryStore = new MemoryFeatureStore();
        memoryStore.createFeatureType(TYPE_NODE.getName(), TYPE_NODE);
        memoryStore.createFeatureType(TYPE_WAY.getName(), TYPE_WAY);
        memoryStore.createFeatureType(TYPE_RELATION.getName(), TYPE_RELATION);

        final OSMXMLReader reader = new OSMXMLReader();
        try{
            reader.setInput(input);
            while(reader.hasNext()){
                final Object obj = reader.next();

                if(obj instanceof Feature){
                    final Feature feature = (Feature) obj;
                    final FeatureType ft = feature.getType();

                    if(!memoryStore.getNames().contains(ft.getName())){
                        memoryStore.createFeatureType(ft.getName(), ft);
                    }

                    memoryStore.addFeatures(ft.getName(), Collections.singleton(feature));
                }

            }
        }finally{
            reader.dispose();
        }
    }

    @Override
    public FeatureStoreFactory getFactory() {
        return FeatureStoreFinder.getFactoryById(OSMMemoryFeatureStoreFactory.NAME);
    }
    
    @Override
    public Set<GenericName> getNames() throws DataStoreException {
        final Set<GenericName> names = new HashSet<GenericName>();
        names.add(TYPE_NODE.getName());
        names.add(TYPE_WAY_EXTENDED.getName());
        names.add(TYPE_RELATION.getName());
        return names;
    }

    @Override
    public FeatureType getFeatureType(final GenericName typeName) throws DataStoreException {
        if(TYPE_NODE.getName().equals(typeName)){
            return TYPE_NODE;
        }else if(TYPE_WAY_EXTENDED.getName().equals(typeName)){
            return TYPE_WAY_EXTENDED;
        }else if(TYPE_RELATION.getName().equals(typeName)){
            return TYPE_RELATION;
        }else{
            throw new DataStoreException("No featureType for name : " + typeName);
        }
    }

    @Override
    public FeatureReader getFeatureReader(final Query query) throws DataStoreException {
        final FeatureType ft = getFeatureType(query.getTypeName());

        FeatureReader fr = memoryStore.getFeatureReader(QueryBuilder.all(query.getTypeName()));

        //Add calculated attributs.
        if(ft.getName().equals(TYPE_WAY_EXTENDED.getName())){
            fr = GenericExtendFeatureIterator.wrap(fr, wayExtend, query.getHints());
        }
        
        return handleRemaining(fr, query);
    }

    @Override
    public FeatureWriter getFeatureWriter(final GenericName typeName, final Filter filter, final Hints hints) throws DataStoreException {
        throw new UnsupportedOperationException("Not yet.");
    }

    ////////////////////////////////////////////////////////////////////////////
    // FALLTHROUGHT OR NOT IMPLEMENTED /////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public QueryCapabilities getQueryCapabilities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createFeatureType(final GenericName typeName, final FeatureType featureType) throws DataStoreException {
        throw new DataStoreException("New schema creation not allowed on GPX files.");
    }

    @Override
    public void deleteFeatureType(final GenericName typeName) throws DataStoreException {
        throw new DataStoreException("Delete schema not allowed on GPX files.");
    }

    @Override
    public void updateFeatureType(final GenericName typeName, final FeatureType featureType) throws DataStoreException {
        throw new DataStoreException("Update schema not allowed on GPX files.");
    }

    @Override
    public List<FeatureId> addFeatures(final GenericName groupName, final Collection<? extends Feature> newFeatures, 
            final Hints hints) throws DataStoreException {
        return handleAddWithFeatureWriter(groupName, newFeatures, hints);
    }

    @Override
    public void updateFeatures(final GenericName groupName, final Filter filter, final Map<? extends PropertyDescriptor, ? extends Object> values) throws DataStoreException {
        handleUpdateWithFeatureWriter(groupName, filter, values);
    }

    @Override
    public void removeFeatures(final GenericName groupName, final Filter filter) throws DataStoreException {
        handleRemoveWithFeatureWriter(groupName, filter);
    }

    private class OSMNodeAttribute extends DefaultAttribute<Object, AttributeDescriptor, Identifier> {

        private final long nodeId;

        public OSMNodeAttribute(final AttributeDescriptor desc, final long nodeId) {
            super(null, desc, null);
            this.nodeId = nodeId;
        }

        @Override
        public Object getValue() {
            final QueryBuilder qb = new QueryBuilder(TYPE_NODE.getName());
            qb.setFilter(FF.id(Collections.singleton(FF.featureId(Long.toString(nodeId)))));

            FeatureReader reader= null;
            try {
                reader = memoryStore.getFeatureReader(qb.buildQuery());
                return reader.next();
            } catch (DataStoreException ex) {
                throw new FeatureStoreRuntimeException(ex);
            } finally{
                if(reader != null){
                    reader.close();
                }
            }
        }
    }

	@Override
	public void refreshMetaModel() {
		return;
		
	}

}
