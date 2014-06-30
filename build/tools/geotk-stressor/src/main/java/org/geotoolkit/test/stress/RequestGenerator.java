/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2010-2012, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2010-2012, Geomatys
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
package org.geotoolkit.test.stress;

import java.util.Random;

import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.datum.PixelInCell;

import org.apache.sis.math.MathFunctions;
import org.geotoolkit.resources.Errors;
import org.geotoolkit.coverage.grid.GeneralGridEnvelope;
import org.geotoolkit.coverage.grid.GeneralGridGeometry;
import org.geotoolkit.referencing.operation.MathTransforms;
import org.apache.sis.referencing.operation.transform.LinearTransform;


/**
 * Generates random grid geometries in order to create random requests.
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.15
 *
 * @since 3.14
 */
public class RequestGenerator {
    /**
     * The random number generator.
     */
    protected final Random random;

    /**
     * Contains the maximal extent of the random envelopes to be generated by
     * {@link #getRandomGrid()}. This grid geometry must have a valid CRS.
     */
    protected final GeneralGridGeometry domain;

    /**
     * The minimal grid size to be requested.
     */
    private final int[] minimalGridSize;

    /**
     * The maximal grid size to be requested.
     */
    private final int[] maximalGridSize;

    /**
     * The maximal scale factor, relative to the domain <cite>grid to CRS</cite>. Shall always
     * be greater or equals than 1. The default value is computed in such a way that the image
     * at the largest resolution is 100 pixels width or height.
     *
     * @since 3.15
     */
    private double maximumScale;

    /**
     * The value of <code>sqrt({@linkplain #maximumScale} - 1)</code>.
     */
    private double sqrtScale1;

    /**
     * The domain <cite>grid to CRS</cite> transform, represented as a matrix.
     */
    private final Matrix gridToCRS;

    /**
     * The resolution of the domain.
     */
    private final double[] domainResolution;

    /**
     * Creates a new request generator for the given domain. The domain shall use an
     * affine <cite>grid to CRS</cite> transform.
     *
     * @param domain Contains the maximal extent of the random envelopes to be generated.
     */
    public RequestGenerator(final GeneralGridGeometry domain) {
        this(domain, 100, 2000);
    }

    /**
     * Creates a new request generator for the given domain, minimum and maximum sizes.
     * The domain shall use an affine <cite>grid to CRS</cite> transform.
     *
     * @param minSize The minimal grid size to be requested.
     * @param maxSize The maximal grid size to be requested.
     * @param domain Contains the maximal extent of the random envelopes to be generated.
     *
     * @since 3.15
     */
    public RequestGenerator(final GeneralGridGeometry domain, final int minSize, final int maxSize) {
        this.random    = new Random();
        this.domain    = domain;
        this.gridToCRS = ((LinearTransform) domain.getGridToCRS(PixelInCell.CELL_CORNER)).getMatrix();
        this.domainResolution = getResolution(domain);
        final GridEnvelope gridExtent = domain.getExtent();
        final int dimension = gridExtent.getDimension();
        minimalGridSize = new int[dimension];
        maximalGridSize = new int[dimension];
        for (int i=0; i<dimension; i++) {
            final int max = Math.min(maxSize, gridExtent.getSpan(i));
            maximalGridSize[i] = max;
            minimalGridSize[i] = Math.min(minSize, max);
        }
        updateScale();
    }

    /**
     * Returns the minimal grid size along each dimension.
     *
     * @return The minimal grid size along each dimension.
     *
     * @since 3.15
     */
    public int[] getMinimalGridSize() {
        return minimalGridSize.clone();
    }

    /**
     * Sets the minimal grid size along each dimension. If a minimal size is greater than
     * a maximal size, the minimal size is set to the maximal size (the maximal size is
     * <strong>not</strong> modified by this method. This is done that way because the
     * maximal size is typically computed from the {@linkplain #domain}).
     * <p>
     * Invoking this method causes the {@linkplain #getMaximumScale() maximum scale} to
     * be recomputed.
     *
     * @param size The minimal grid size along each dimension.
     *
     * @since 3.15
     */
    public void setMinimalGridSize(final int... size) {
        if (size.length != minimalGridSize.length) {
            throw new IllegalArgumentException(Errors.format(Errors.Keys.MISMATCHED_ARRAY_LENGTH));
        }
        for (int i=0; i<size.length; i++) {
            if (size[i] < 1) {
                throw new IllegalArgumentException(Errors.format(
                        Errors.Keys.ILLEGAL_ARGUMENT_2, "size[" + i + ']', size[i]));
            }
        }
        System.arraycopy(size, 0, minimalGridSize, 0, size.length);
        for (int i=0; i<minimalGridSize.length; i++) {
            if (minimalGridSize[i] > maximalGridSize[i]) {
                minimalGridSize[i] = maximalGridSize[i];
            }
        }
        updateScale();
    }

    /**
     * Returns the maximal grid size along each dimension.
     *
     * @return The maximal grid size along each dimension.
     *
     * @since 3.15
     */
    public int[] getMaximalGridSize() {
        return maximalGridSize.clone();
    }

    /**
     * Sets the maximal grid size along each dimension.
     * The maximal size can not be less than the minimal size.
     * <p>
     * Invoking this method causes the {@linkplain #getMaximumScale() maximum scale} to
     * be recomputed.
     *
     * @param size The maximal grid size along each dimension.
     *
     * @since 3.15
     */
    public void setMaximalGridSize(final int... size) {
        if (size.length != maximalGridSize.length) {
            throw new IllegalArgumentException(Errors.format(Errors.Keys.MISMATCHED_ARRAY_LENGTH));
        }
        for (int i=0; i<size.length; i++) {
            if (size[i] < minimalGridSize[i]) {
                throw new IllegalArgumentException(Errors.format(
                        Errors.Keys.ILLEGAL_ARGUMENT_2, "size[" + i + ']', size[i]));
            }
        }
        System.arraycopy(size, 0, maximalGridSize, 0, size.length);
        updateScale();
    }

    /**
     * Computes automatically a new scale value after the minimal or maximal grid size has
     * been modified.
     */
    private void updateScale() {
        double maxScale = Double.POSITIVE_INFINITY;
        final GridEnvelope gridExtent = domain.getExtent();
        final int dimension = gridExtent.getDimension();
        for (int i=0; i<dimension; i++) {
            final double scale = gridExtent.getSpan(i) / (double) minimalGridSize[i];
            if (scale < maxScale) {
                maxScale = scale;
            }
        }
        setMaximumScale(maxScale);
    }

    /**
     * Returns the current maximal scale factor relative to the domain resolution.
     *
     * @return The maximal scale factor, as a value equals or greater than 1.
     *
     * @since 3.15
     */
    public double getMaximumScale() {
        return maximumScale;
    }

    /**
     * Sets the maximal scale factor, relative to the domain <cite>grid to CRS</cite>. The scale
     * shall always be greater or equals than 1. The default value is computed in such a way that
     * the image at the largest resolution has the {@linkplain #getMinimalGridSize() minimal grid
     * size}.
     *
     * @param scale
     *
     * @since 3.15
     */
    public void setMaximumScale(final double scale) {
        if (!(scale >= 1)) {
            throw new IllegalArgumentException("Illegal maximum scale: " + scale);
        }
        maximumScale = scale;
        sqrtScale1 = Math.sqrt(scale - 1);
    }

    /**
     * Returns a random grid geometry inside the {@linkplain #domain}. The grid envelope
     * size will range from {@link #minimalGridSize} to {@link #maximalGridSize} inclusive.
     * The <cite>grid to CRS</cite> transform is scaled to a maximal factor of
     * {@link #maximumScale} compare to the original grid.
     *
     * @return A new random grid geometry inside the domain.
     */
    public GeneralGridGeometry getRandomGrid() {
        double scale = sqrtScale1 * random.nextDouble();
        scale = (scale * scale) + 1;
        assert (scale >= 1 && scale <= maximumScale) : scale;

        final GridEnvelope gridExtent = domain.getExtent();
        final int dimension = gridExtent.getDimension();
        final int[] lower = new int[dimension];
        final int[] upper = new int[dimension];
        final Matrix mx  = gridToCRS.clone();
        for (int i=0; i<dimension; i++) {
            /*
             * Select a scale between 1 and maximumScale,
             * with highest probability for low values.
             */
            mx.setElement(i, i, scale * mx.getElement(i, i));
            /*
             * Compute the bounds.
             */
            int span = minimalGridSize[i];
            span += random.nextInt(maximalGridSize[i] - span + 1);
            int min = (int) Math.ceil ( gridExtent.getLow (i)      / scale);
            int max = (int) Math.floor((gridExtent.getHigh(i) + 1) / scale);
            final int remainingSpace = (max - min) - span;
            if (remainingSpace > 0) {
                min += random.nextInt(remainingSpace + 1);
                max = min + span;
            }
            lower[i] = min;
            upper[i] = max;
        }
        return new GeneralGridGeometry(new GeneralGridEnvelope(lower, upper, false),
                PixelInCell.CELL_CORNER, MathTransforms.linear(mx),
                domain.getCoordinateReferenceSystem());
    }

    /**
     * Returns the scale of the given grid geometry, relative to the domain.
     *
     * @param  request The grid geometry from which to extract the scale.
     * @return The scale (unitless).
     *
     * @since 3.15
     */
    public double[] getScale(final GeneralGridGeometry request) {
        final double[] scale = getResolution(request);
        for (int i=0; i<scale.length; i++) {
            scale[i] /= domainResolution[i];
        }
        return scale;
    }

    /**
     * Returns the resolution of the given grid geometry, in units of the envelope.
     *
     * @param  request The grid geometry from which to extract the resolution.
     * @return The resolution, in units of the envelope.
     */
    public static double[] getResolution(final GeneralGridGeometry request) {
        final Matrix gridToCRS = ((LinearTransform) request.getGridToCRS()).getMatrix();
        final double[] row = new double[gridToCRS.getNumCol() - 1];
        final double[] res = new double[gridToCRS.getNumRow() - 1];
        for (int j=0; j<res.length; j++) {
            for (int i=0; i<row.length; i++) {
                row[i] = gridToCRS.getElement(j, i);
            }
            res[j] = MathFunctions.magnitude(row);
        }
        return res;
    }

    /**
     * Returns the mean value of the given vector.
     * This method is used mostly for information purpose.
     *
     * @param  vector The vector for which to compute the main value.
     * @return The mean value.
     *
     * @since 3.15
     */
    public static double mean(final double[] vector) {
        double sum = 0;
        for (final double r : vector) {
            sum += r;
        }
        return sum / vector.length;
    }
}
