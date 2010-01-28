/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2007-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotoolkit.image.io.plugin;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.metadata.IIOMetadataFormat;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import ucar.nc2.Group;
import ucar.ma2.DataType;
import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;
import ucar.nc2.VariableIF;
import ucar.nc2.VariableSimpleIF;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.CoordSysBuilderIF;
import ucar.nc2.dataset.EnhanceScaleMissing;
import ucar.nc2.dataset.Enhancements;

import org.opengis.metadata.content.TransferFunctionType;

import org.geotoolkit.image.io.metadata.SpatialMetadata;
import org.geotoolkit.image.io.metadata.SpatialMetadataFormat;
import org.geotoolkit.image.io.metadata.MetadataAccessor;
import org.geotoolkit.internal.image.io.SampleMetadataFormat;
import org.geotoolkit.internal.image.io.DimensionAccessor;
import org.geotoolkit.referencing.adapters.NetcdfAxis;
import org.geotoolkit.referencing.adapters.NetcdfCRS;
import org.geotoolkit.resources.Errors;


/**
 * Metadata from NetCDF file. This implementation assumes that the NetCDF file follows the
 * <A HREF="http://www.cfconventions.org/">CF Metadata conventions</A>.
 *
 * {@section Limitation}
 * Current implementation retains only the first {@linkplain CoordinateSystem coordinate system}
 * found in the NetCDF file or for a given variable. The {@link org.geotoolkit.coverage.io} package
 * would not know what to do with the extra coordinate systems anyway.
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.08
 *
 * @since 3.08 (derived from 2.4)
 * @module
 */
final class NetcdfMetadata extends SpatialMetadata {
    /**
     * The name of the native format. It has no version number because this is
     * a "dynamic" format inferred from the actual content of the NetCDF file.
     */
    private static final String NATIVE_FORMAT = "NetCDF";

    /**
     * Forces usage of UCAR libraries in some places where we use our own code instead.
     * This may result in rounding errors and absence of information regarding fill values,
     * but is useful for checking if we are doing the right thing compared to the UCAR way.
     */
    private static final boolean USE_UCAR_LIB = false;

    /**
     * The mapping from COARD attribute names to ISO 19115-2 attribute names.
     */
    private static final String[] BBOX = {
        "west_longitude", "westBoundLongitude",
        "east_longitude", "eastBoundLongitude",
        "south_latitude", "southBoundLatitude",
        "north_latitude", "northBoundLatitude"
    };

    /**
     * The format inferred from the content of the NetCDF file.
     * Will be created only when first needed.
     */
    private IIOMetadataFormat nativeFormat;

    /**
     * On construction, the {@link NetcdfFile} or {@link VariableIF} given to the constructor.
     * After {@code getAsTree("NetCDF")} has been invoked, the metadata tree we have built.
     */
    private Object netcdf;

    /**
     * Creates image metadata from the specified file. Note that
     * {@link CoordSysBuilderIF#buildCoordinateSystems(NetcdfDataset)}
     * should have been invoked (if needed) before this constructor.
     *
     * @param reader The reader for which to assign the metadata.
     * @param file The file for which to read metadata.
     */
    public NetcdfMetadata(final ImageReader reader, final NetcdfFile file) {
        super(SpatialMetadataFormat.STREAM, reader, null);
        nativeMetadataFormatName = NATIVE_FORMAT;
        Attribute attr = file.findGlobalAttribute("project_name");
        if (attr != null) {
            final MetadataAccessor ac = new MetadataAccessor(this, "DiscoveryMetadata");
            ac.setAttribute("citation", attr.getStringValue());
        }
        MetadataAccessor ac = null;
        for (int i=0; i<BBOX.length; i+=2) {
            attr = file.findGlobalAttribute(BBOX[i]);
            if (attr != null) {
                if (ac == null) {
                    ac = new MetadataAccessor(this, "DiscoveryMetadata/Extent/GeographicElement");
                    ac.setAttribute("inclusion", true);
                }
                ac.setAttribute(BBOX[i+1], attr.getStringValue());
            }
        }
        netcdf = file;
    }

    /**
     * Creates image metadata from the specified variable. Note that
     * {@link CoordSysBuilderIF#buildCoordinateSystems(NetcdfDataset)}
     * should have been invoked (if needed) before this constructor.
     *
     * @param reader The reader for which to assign the metadata.
     * @param variable The variable for which to read metadata.
     */
    public NetcdfMetadata(final ImageReader reader, final VariableIF variable) {
        super(SpatialMetadataFormat.IMAGE, reader, null);
        nativeMetadataFormatName = NATIVE_FORMAT;
        if (variable instanceof Enhancements) {
            final List<CoordinateSystem> systems = ((Enhancements) variable).getCoordinateSystems();
            if (systems != null && !systems.isEmpty()) {
                setCoordinateSystem(systems.get(0));
            }
        }
        addSampleDimension(variable);
        netcdf = variable;
    }

    /**
     * Sets the Coordinate Reference System to a value inferred from the specified
     * NetCDF object. This method wraps the given NetCDF coordinate system in to a
     * GeoAPI {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem
     * Coordinate Reference System} implementation.
     *
     * @param cs The coordinate system to define in metadata.
     */
    private void setCoordinateSystem(final CoordinateSystem cs) {
        final NetcdfCRS crs = NetcdfCRS.wrap(cs);
        final int dimension = crs.getDimension();
        for (int i=0; i<dimension; i++) {
            final NetcdfAxis axis = crs.getAxis(i);
            final String units = axis.delegate().getUnitsString();
            final int offset = units.lastIndexOf('_');
            if (offset >= 0) {
                final String direction = units.substring(offset + 1).trim();
                final String opposite = axis.getDirection().opposite().name();
                if (direction.equalsIgnoreCase(opposite)) {
                    warning("setCoordinateSystem", Errors.Keys.INCONSISTENT_AXIS_ORIENTATION_$2,
                            new String[] {axis.getCode(), direction});
                }
            }
        }
    }

    /**
     * Adds sample dimension information for the specified variables.
     *
     * @param variables The variables to add as sample dimensions.
     */
    private void addSampleDimension(final VariableIF... variables) {
        final DimensionAccessor accessor = new DimensionAccessor(this);
        for (final VariableIF variable : variables) {
            final NetcdfVariable m;
            if (USE_UCAR_LIB && variable instanceof EnhanceScaleMissing) {
                m = new NetcdfVariable((EnhanceScaleMissing) variable);
            } else {
                m = new NetcdfVariable(variable);
            }
            accessor.selectChild(accessor.appendChild());
            accessor.setValueRange(m.minimum, m.maximum);
            accessor.setFillSampleValues(m.fillValues);
            accessor.setTransfertFunction(m.scale, m.offset, TransferFunctionType.LINEAR);
        }
    }

    /**
     * The metadata format for the encloding {@link NetcdfMetadata} instance.
     */
    private final class Format extends SampleMetadataFormat {
        /**
         * Creates a new instance.
         */
        Format() {
            super(NATIVE_FORMAT);
        }

        /**
         * Returns the metadata to use for inferring the format.
         * This is invoked when first needed.
         */
        @Override
        protected Node getDataRootNode() {
            return getAsTree(NATIVE_FORMAT);
        }

        /**
         * Returns the data type associated to the given attribute.
         */
        @Override
        protected int getDataType(final Node attribute, final int index) {
            final Node element = ((Attr) attribute).getOwnerElement();
            if (element instanceof IIOMetadataNode) { // Paranoiac check (should always be true)
                final List<?> attributes = (List<?>) ((IIOMetadataNode) element).getUserObject();
                /*
                 * The list may be shorter than 'index' because of the hard-coded
                 * attributes added by the getAsTree(...) method.
                 */
                if (index < attributes.size()) {
                    final DataType type = ((Attribute) attributes.get(index)).getDataType();
                    switch (type) {
                        case BOOLEAN: return DATATYPE_BOOLEAN;
                        case BYTE:    // Fall through
                        case SHORT:   // Fall through
                        case INT:     return DATATYPE_INTEGER;
                        case FLOAT:   return DATATYPE_FLOAT;
                        case LONG:    // Fall through
                        case DOUBLE:  return DATATYPE_DOUBLE;
                    }
                }
            }
            return DATATYPE_STRING;
        }
    }

    /**
     * If the given format name is {@code "NetCDF"}, returns a "dynamic" metadata format
     * inferred from the actual content of the NetCDF file. Otherwise returns the usual
     * metadata format as defined in the super-class.
     */
    @Override
    public IIOMetadataFormat getMetadataFormat(final String formatName) {
        if (NATIVE_FORMAT.equals(formatName)) {
            if (nativeFormat == null) {
                nativeFormat = new Format();
            }
            return nativeFormat;
        }
        return super.getMetadataFormat(formatName);
    }

    /**
     * If the given format name is {@code "NetCDF"}, returns the native metadata.
     * Otherwise returns the usual metadata as defined in the super-class.
     */
    @Override
    public Node getAsTree(final String formatName) {
        if (NATIVE_FORMAT.equals(formatName)) {
            if (netcdf instanceof Node) {
                return (Node) netcdf;
            }
            final IIOMetadataNode node = new IIOMetadataNode(NATIVE_FORMAT);
            if (netcdf instanceof VariableSimpleIF) {
                final VariableSimpleIF var = (VariableSimpleIF) netcdf;
                appendAttributes(var.getAttributes(), node);
                node.setAttribute("data_type", String.valueOf(var.getDataType()));
                if (var instanceof EnhanceScaleMissing) {
                    final EnhanceScaleMissing eh = (EnhanceScaleMissing) netcdf;
                    node.setAttribute("valid_min", String.valueOf(eh.getValidMin()));
                    node.setAttribute("valid_max", String.valueOf(eh.getValidMax()));
                }
            } else {
                buildTree(((NetcdfFile) netcdf).getRootGroup(), node);
            }
            netcdf = node;
            return node;
        }
        return super.getAsTree(formatName);
    }

    /**
     * Appends attributes to the given node. The node user object is set to the list of attributes.
     */
    private static void appendAttributes(final List<Attribute> attributes, final IIOMetadataNode node) {
        for (final Attribute attribute : attributes) {
            node.setAttribute(attribute.getName(), attribute.getStringValue());
        }
        node.setUserObject(attributes);
    }

    /**
     * Invoked recursively for building the tree.
     */
    private static void buildTree(final Group group, final IIOMetadataNode parent) {
        if (group != null) {
            appendAttributes(group.getAttributes(), parent);
            for (final Group subgroup : group.getGroups()) {
                final IIOMetadataNode child = new IIOMetadataNode(subgroup.getShortName());
                buildTree(subgroup, child);
                parent.appendChild(child);
            }
        }
    }

    /**
     * Convenience method for logging a warning.
     */
    private void warning(final String method, final int key, final Object value) {
        LogRecord record = Errors.getResources(getLocale()).getLogRecord(Level.WARNING, key, value);
        record.setSourceClassName(NetcdfMetadata.class.getName());
        record.setSourceMethodName(method);
        warningOccurred(record);
    }
}
