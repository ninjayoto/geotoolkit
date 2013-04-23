/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2011-2013, Geomatys
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
package org.geotoolkit.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.geotoolkit.data.*;
import org.geotoolkit.data.memory.GenericFilterFeatureIterator;
import org.geotoolkit.data.memory.GenericReprojectFeatureIterator;
import org.geotoolkit.data.memory.GenericRetypeFeatureIterator;
import org.geotoolkit.data.query.DefaultQueryCapabilities;
import org.geotoolkit.data.query.Join;
import org.geotoolkit.data.query.Query;
import org.geotoolkit.data.query.QueryBuilder;
import org.geotoolkit.data.query.QueryCapabilities;
import org.geotoolkit.data.query.Selector;
import org.geotoolkit.data.query.Source;
import org.geotoolkit.data.query.TextStatement;
import org.geotoolkit.db.dialect.SQLDialect;
import org.geotoolkit.db.dialect.SQLQueryBuilder;
import org.geotoolkit.db.reverse.ColumnMetaModel;
import org.geotoolkit.db.reverse.DataBaseModel;
import org.geotoolkit.db.reverse.PrimaryKey;
import org.geotoolkit.factory.Hints;
import org.geotoolkit.feature.DefaultName;
import org.geotoolkit.feature.FeatureTypeBuilder;
import org.geotoolkit.feature.SchemaException;
import org.geotoolkit.filter.visitor.CRSAdaptorVisitor;
import org.geotoolkit.filter.visitor.FIDFixVisitor;
import org.geotoolkit.filter.visitor.FilterAttributeExtractor;
import org.geotoolkit.jdbc.ManageableDataSource;
import org.geotoolkit.parameter.Parameters;
import org.geotoolkit.referencing.CRS;
import org.geotoolkit.storage.DataStoreException;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.FactoryException;

/**
 *
 * @author Johann Sorel (Geomatys)
 * @module pending
 */
public class DefaultJDBCFeatureStore extends AbstractFeatureStore implements JDBCFeatureStore{
    
    protected static final QueryCapabilities DEFAULT_CAPABILITIES = new DefaultQueryCapabilities(false, false, new String[]{Query.GEOTK_QOM, CUSTOM_SQL});
    private final DataBaseModel dbmodel; 
    private final String factoryId;
    private DataSource source;
    private SQLDialect dialect;
    private String baseSchema;
    
    //number of records to retrieve with each db call.
    private final int fetchSize;
    
    public DefaultJDBCFeatureStore(final ParameterValueGroup params,final String factoryId){
        super(params);
        this.factoryId = factoryId;
        
        fetchSize = (Integer)Parameters.getOrCreate(AbstractJDBCFeatureStoreFactory.FETCHSIZE, params).getValue();
        final boolean simpleTypes = (Boolean)Parameters.getOrCreate(AbstractJDBCFeatureStoreFactory.SIMPLETYPE, params).getValue();        
        dbmodel = new DataBaseModel(this, simpleTypes); 
        
        try{
            baseSchema = (String)Parameters.getOrCreate(AbstractJDBCFeatureStoreFactory.SCHEMA, params).getValue();
        }catch(ParameterNotFoundException ex){
            //parameter migth not exist on all database implementations
        }
    }

    @Override
    public FeatureStoreFactory getFactory() {
        return FeatureStoreFinder.getFactoryById(factoryId);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getDefaultNamespace() {
        return super.getDefaultNamespace();
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Logger getLogger() {
        return super.getLogger();
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public SQLDialect getDialect() {
        return dialect;
    }

    public void setDialect(SQLDialect dialect) {
        this.dialect = dialect;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public DataSource getDataSource() {
        return source;
    }
    
    public void setDataSource(DataSource ds){
        this.source = ds;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getDatabaseSchema() {
        return baseSchema;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public DataBaseModel getDatabaseModel() {
        return dbmodel;
    }

    @Override
    public Set<Name> getNames() throws DataStoreException {
        ensureOpen();
        return dbmodel.getNames();
    }

    @Override
    public FeatureType getFeatureType(final Name typeName) throws DataStoreException {
        ensureOpen();
        return dbmodel.getFeatureType(typeName);
    }

    @Override
    public FeatureType getFeatureType(Query query) throws DataStoreException, SchemaException {
        if(CUSTOM_SQL.equalsIgnoreCase(query.getLanguage())){
            final TextStatement txt = (TextStatement) query.getSource();
            final String sql = txt.getStatement();
            Connection cx = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                cx = getDataSource().getConnection();
                stmt = cx.createStatement();
                rs = stmt.executeQuery(sql);
                return getDatabaseModel().analyzeResult(rs, query.getTypeName().getLocalPart());
            } catch (SQLException ex) {
                throw new DataStoreException(ex);
            }finally{
                JDBCFeatureStoreUtilities.closeSafe(getLogger(),cx,stmt,rs);
            }
        }
        return super.getFeatureType(query);
    }
    
    @Override
    public QueryCapabilities getQueryCapabilities() {
        return DEFAULT_CAPABILITIES;
    }
    
    @Override
    public FeatureReader getFeatureReader(final Query query) throws DataStoreException {
        final Source source = query.getSource();
        
        final FeatureReader reader;
        if(source instanceof Selector){
            reader = getQOMFeatureReader(query);
        }else if(source instanceof TextStatement){
            reader = getSQLFeatureReader(query);
        }else{
            throw new DataStoreException("Unsupported source type : " + source);
        }
        
        //take care of potential hints, like removing primary keys
        final QueryBuilder qb = new QueryBuilder();
        qb.setTypeName(new DefaultName("remaining"));
        qb.setHints(query.getHints());
        return handleRemaining(reader, qb.buildQuery());
    }

    /**
     * Get reader with geotk query model.
     * @param query
     * @return FeatureReader
     * @throws DataStoreException 
     */
    private FeatureReader getQOMFeatureReader(final Query query) throws DataStoreException {
        
        if(!query.isSimple()){
            throw new DataStoreException("Query is not simple.");
        }
        
        final FeatureType baseType = getFeatureType(query.getTypeName());
        final PrimaryKey pkey = dbmodel.getPrimaryKey(query.getTypeName());
                
        
        //replace any PropertyEqualsTo in true ID filters
        Filter baseFilter = query.getFilter();
        baseFilter = (Filter) baseFilter.accept(new FIDFixVisitor(), null);
        
        //split the filter between what can be send and must be handle by code
        final Filter[] divided = getDialect().splitFilter(baseFilter);
        Filter preFilter = divided[0];
        Filter postFilter = divided[1];
        
        //ensure spatial filters are in featuretype geometry crs
        preFilter = (Filter)preFilter.accept(new CRSAdaptorVisitor(baseType),null);
        
        // rebuild a new query with the same params, but just the pre-filter
        final QueryBuilder builder = new QueryBuilder(query);
        builder.setFilter(preFilter);
        if(query.getResolution() != null){ //attach resampling in hints; used later by postgis dialect
            builder.getHints().add(new Hints(RESAMPLING, query.getResolution()));
        }
        final Query preQuery = builder.buildQuery();
        
        // Build the feature type returned by this query. Also build an eventual extra feature type
        // containing the attributes we might need in order to evaluate the post filter
        final FeatureType queryFeatureType;
        final FeatureType returnedFeatureType;
        if(query.retrieveAllProperties()) {
            returnedFeatureType = queryFeatureType = baseType;
        } else {
            returnedFeatureType = FeatureTypeBuilder.retype(baseType, query.getPropertyNames());
            final FilterAttributeExtractor extractor = new FilterAttributeExtractor(baseType);
            postFilter.accept(extractor, null);
            final Name[] extraAttributes = extractor.getAttributeNames();
            final List<Name> allAttributes = new ArrayList<Name>(Arrays.asList(query.getPropertyNames()));
            for (Name extraAttribute : extraAttributes) {
                if(!allAttributes.contains(extraAttribute)) {
                    allAttributes.add(extraAttribute);
                }
            }

            //ensure we have the primarykeys
            pkLoop :
            for(ColumnMetaModel pkc : pkey.getColumns()){
                final String pkcName = pkc.getName();
                for(Name n : allAttributes){
                    if(n.getLocalPart().equals(pkcName)){
                        continue pkLoop;
                     }
                 }
                //add the pk attribut
                allAttributes.add(baseType.getDescriptor(pkcName).getName());
             }

            final Name[] allAttributeArray = allAttributes.toArray(new Name[allAttributes.size()]);
            queryFeatureType = FeatureTypeBuilder.retype(baseType, allAttributeArray);
        }
        
        
        
        final String sql;
        
        FeatureReader reader;
        try {
            final SQLQueryBuilder queryBuilder = new SQLQueryBuilder(this);
            sql = queryBuilder.selectSQL(queryFeatureType, preQuery);
            reader = new JDBCFeatureReader(sql, queryFeatureType, this);
        } catch (SQLException ex) {
            throw new DataStoreException(ex.getMessage(), ex);
        }
        
        
        // if post filter, wrap it
        if (postFilter != null && postFilter != Filter.INCLUDE) {
            reader = GenericFilterFeatureIterator.wrap(reader, postFilter);
        }

        //if we need to reproject data
        final CoordinateReferenceSystem reproject = query.getCoordinateSystemReproject();
        if(reproject != null && !CRS.equalsIgnoreMetadata(reproject,baseType.getCoordinateReferenceSystem())){
            try {
                reader = GenericReprojectFeatureIterator.wrap(reader, reproject,query.getHints());
            } catch (FactoryException ex) {
                throw new DataStoreException(ex);
            } catch (SchemaException ex) {
                throw new DataStoreException(ex);
            }
        }

        //if we need to constraint type
        if(!returnedFeatureType.equals(queryFeatureType)){
            reader = GenericRetypeFeatureIterator.wrap(reader, returnedFeatureType, query.getHints());
        }

        return reader;
    }
    
    /**
     * Get reader with SQL query.
     * @param query
     * @return FeatureReader
     * @throws DataStoreException 
     */
    private FeatureReader getSQLFeatureReader(final Query query) throws DataStoreException {

        final TextStatement stmt = (TextStatement) query.getSource();
        final String sql = stmt.getStatement();

        try {
            final FeatureType ft = getFeatureType(query);
            final JDBCFeatureReader reader = new JDBCFeatureReader(sql, ft, this);
            return reader;
        } catch (SchemaException ex) {
            throw new DataStoreException(ex);
        } catch (SQLException ex) {
            throw new DataStoreException(ex);
        } catch (IOException ex) {
            throw new DataStoreException(ex);
        }
    }
    
    @Override
    public Envelope getEnvelope(Query query) throws DataStoreException, FeatureStoreRuntimeException {
        if(CUSTOM_SQL.equalsIgnoreCase(query.getLanguage())){
            //can not optimize this query
            //iterator is closed by method
            return FeatureStoreUtilities.calculateEnvelope(getSQLFeatureReader(query));
        }
        return super.getEnvelope(query);
    }
    
    @Override
    public long getCount(Query query) throws DataStoreException {
        if(CUSTOM_SQL.equalsIgnoreCase(query.getLanguage())){
            //iterator is closed by method
            return FeatureStoreUtilities.calculateCount(getSQLFeatureReader(query));
        }
        return super.getCount(query);
    }

    @Override
    public FeatureWriter getFeatureWriter(Name typeName, Filter filter, Hints hints) throws DataStoreException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void refreshMetaModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Schema manipulation /////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    @Override
    public void createSchema(final Name typeName, final FeatureType featureType) throws DataStoreException {
        ensureOpen();
        throw new DataStoreException("Not supported yet.");
    }

    @Override
    public void updateSchema(final Name typeName, final FeatureType featureType) throws DataStoreException {
        ensureOpen();
        throw new DataStoreException("Not supported yet.");
    }

    @Override
    public void deleteSchema(final Name typeName) throws DataStoreException {
        ensureOpen();
        throw new DataStoreException("Not supported yet.");
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Fallback on reader/write iterator methods ///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
        
    /**
     * {@inheritDoc }
     */
    @Override
    public List<FeatureId> addFeatures(Name groupName, Collection<? extends Feature> newFeatures, Hints hints) throws DataStoreException {
        return handleAddWithFeatureWriter(groupName, newFeatures, hints);
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void updateFeatures(final Name groupName, final Filter filter, final Map<? extends PropertyDescriptor, ? extends Object> values) throws DataStoreException {
        handleUpdateWithFeatureWriter(groupName, filter, values);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void removeFeatures(final Name groupName, final Filter filter) throws DataStoreException {
        handleRemoveWithFeatureWriter(groupName, filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // other utils /////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    
    /**
     * Check the feature store is open.
     */
    private void ensureOpen() throws DataStoreException{
        if(source == null){
            throw new DataStoreException("JDBC Feature store has been disposed already.");
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        if (source != null) {
            getLogger().log(Level.WARNING,
                    "JDBC feature store has not been disposed properly. "+
                    "This may cause connexions to remain open, "+
                    "dispose feature stores when not needed anymore before dereferencing.");
            dispose();
        }
        super.finalize();
    }

    @Override
    public void dispose() {
        if (source instanceof ManageableDataSource) {
            try {
                final ManageableDataSource mds = (ManageableDataSource) source;
                source = null;
                mds.close();
            } catch (SQLException e) {
                getLogger().log(Level.WARNING, "Failed to close datasource.", e);
            }
        }
    }

}
