/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2012, Geomatys
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
package org.geotoolkit.coverage.xmlstore;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import javax.xml.bind.JAXBException;

import org.geotoolkit.storage.coverage.AbstractCoverageStore;
import org.geotoolkit.storage.coverage.CoverageReference;
import org.geotoolkit.storage.coverage.CoverageStoreFactory;
import org.geotoolkit.storage.coverage.CoverageStoreFinder;
import org.geotoolkit.storage.coverage.CoverageType;
import org.geotoolkit.coverage.grid.ViewType;
import org.geotoolkit.util.NamesExt;
import org.apache.sis.storage.DataStoreException;
import org.geotoolkit.parameter.Parameters;
import org.geotoolkit.storage.DataNode;
import org.geotoolkit.storage.DefaultDataNode;
import org.opengis.util.GenericName;
import org.opengis.parameter.ParameterValueGroup;

/**
 * Coverage store relying on an xml file.
 *
 * @author Johann Sorel (Geomatys)
 * @module pending
 */
public class XMLCoverageStore extends AbstractCoverageStore {

    private final File root;
    private final DataNode rootNode = new DefaultDataNode();

    final boolean cacheTileState;

    public XMLCoverageStore(File root) throws URISyntaxException, MalformedURLException {
        this(root,true);
    }
    
    public XMLCoverageStore(URL rootPath) throws URISyntaxException {
        this(rootPath, true);
    }

    public XMLCoverageStore(File root, boolean cacheTileStateInMemory) throws URISyntaxException, MalformedURLException {
        this(toParameters(root,cacheTileStateInMemory));
    }

    public XMLCoverageStore(URL rootPath, boolean cacheTileStateInMemory) throws URISyntaxException {
        this(toParameters(rootPath,cacheTileStateInMemory));
    }
        
    public XMLCoverageStore(ParameterValueGroup params) throws URISyntaxException {
        super(params);
        final URL rootPath = Parameters.value(XMLCoverageStoreFactory.PATH, params);
        root = new File(rootPath.toURI());
        Boolean tmpCacheState = Parameters.value(XMLCoverageStoreFactory.CACHE_TILE_STATE, params);
        cacheTileState = (tmpCacheState == null)? true : tmpCacheState;
        explore();
    }

    private static ParameterValueGroup toParameters(File root, boolean cacheState) throws MalformedURLException {
        final ParameterValueGroup params = XMLCoverageStoreFactory.PARAMETERS_DESCRIPTOR.createValue();
        Parameters.getOrCreate(XMLCoverageStoreFactory.PATH, params).setValue(root.toURI().toURL());
        Parameters.getOrCreate(XMLCoverageStoreFactory.CACHE_TILE_STATE, params).setValue(cacheState);
        return params;
    }
    
    private static ParameterValueGroup toParameters(URL rootPath, boolean cacheState) {
        final ParameterValueGroup params = XMLCoverageStoreFactory.PARAMETERS_DESCRIPTOR.createValue();
        Parameters.getOrCreate(XMLCoverageStoreFactory.PATH, params).setValue(rootPath);
        Parameters.getOrCreate(XMLCoverageStoreFactory.CACHE_TILE_STATE, params).setValue(cacheState);
        return params;
    }
    
    @Override
    public CoverageStoreFactory getFactory() {
        return CoverageStoreFinder.getFactoryById(XMLCoverageStoreFactory.NAME);
    }

    @Override
    public DataNode getRootNode() {
        return rootNode;
    }

    /**
     * Search all xml files in the folder which define a pyramid model.
     */
    private void explore() {

        if (!root.exists()) {
            root.mkdirs();
        }

        if (root.isFile()) {
            createReference(root);
        } else {
            final File[] children = root.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".xml");
                }
            });
            if (children != null) {
                for (File f : children) {
                    //try to parse the file
                    createReference(f);
                }
            }
        }
    }

    private void createReference(File refDescriptor) {
        try {
            //TODO useless copy here
            final XMLCoverageReference set = XMLCoverageReference.read(refDescriptor);
            final GenericName name = NamesExt.create(getDefaultNamespace(), set.getId());
            final XMLCoverageReference ref = new XMLCoverageReference(this,name,set.getPyramidSet());
            ref.copy(set);
            rootNode.getChildren().add(ref);
        } catch (JAXBException ex) {
            getLogger().log(Level.INFO, "file is not a pyramid : {0}", refDescriptor.getPath());
        } catch (DataStoreException ex) {
            getLogger().log(Level.WARNING, "Pyramid descriptor contains an invalid CRS : "+refDescriptor.getAbsolutePath(), ex);
        }
    }

    @Override
    public void close() {
    }

    @Override
    public CoverageReference create(GenericName name) throws DataStoreException {
        return create(name, null, null);
    }

    /**
     * Create a CoverageReference with a specific data type and preferred image tile format.
     * Default is ViewType.RENDERED and PNG tile format.
     *
     * @param name name of the new CoverageReference.
     * @param packMode data type (Geophysic or Rendered). Can be null.
     * @param preferredFormat pyramid tile format. Can be null.
     * @return new CoverageReference.
     * @throws DataStoreException
     */
    public CoverageReference create(GenericName name, ViewType packMode, String preferredFormat) throws DataStoreException {
        if (root.isFile()) {
            throw new DataStoreException("Store root is a file, not a directory, no reference creation allowed.");
        }
        name = NamesExt.create(getDefaultNamespace(), name.tip().toString());
        final Set<GenericName> names = getNames();
        if(names.contains(name)){
            throw new DataStoreException("Name already used in store : " + name.tip().toString());
        }

        final XMLPyramidSet set = new XMLPyramidSet();
        final XMLCoverageReference ref = new XMLCoverageReference(this,name,set);
        ref.initialize(new File(root, name.tip().toString()+".xml"));

        if (packMode != null) {
            ref.setPackMode(packMode);
        }

        if (preferredFormat != null) {
            ref.setPreferredFormat(preferredFormat);
        }

        rootNode.getChildren().add(ref);
        ref.save();
        return ref;
    }

    @Override
    public CoverageType getType() {
        return CoverageType.PYRAMID;
    }
}
