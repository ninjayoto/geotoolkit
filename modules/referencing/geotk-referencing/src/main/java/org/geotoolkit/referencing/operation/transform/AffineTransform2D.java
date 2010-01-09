/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2001-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotoolkit.referencing.operation.transform;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;

import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.NoninvertibleTransformException;

import org.geotoolkit.lang.Immutable;
import org.geotoolkit.io.wkt.Symbols;
import org.geotoolkit.io.wkt.Formatter;
import org.geotoolkit.io.wkt.Formattable;
import org.geotoolkit.io.wkt.FormattableObject;
import org.geotoolkit.geometry.DirectPosition2D;
import org.geotoolkit.referencing.operation.matrix.Matrix2;
import org.geotoolkit.referencing.operation.matrix.Matrix3;
import org.geotoolkit.referencing.operation.matrix.XAffineTransform;
import org.geotoolkit.referencing.operation.provider.Affine;
import org.geotoolkit.resources.Errors;
import org.geotoolkit.util.Cloneable;


/**
 * Transforms two-dimensional coordinate points using an affine transform. This class both
 * extends {@link AffineTransform} and implements {@link MathTransform2D}, so it can be
 * used as a bridge between Java2D and the referencing module. Note that this bridge role
 * involve a tricky issue with the {@link #equals equals} method, hopefully to occur only
 * in exceptional corner cases.
 * <p>
 * See any of the following providers for a list of programmatic parameters:
 * <p>
 * <ul>
 *   <li>{@link org.geotoolkit.referencing.operation.provider.Affine}</li>
 *   <li>{@link org.geotoolkit.referencing.operation.provider.LongitudeRotation}</li>
 * </ul>
 *
 * @author Martin Desruisseaux (IRD)
 * @version 3.00
 *
 * @see ProjectiveTransform
 *
 * @since 1.2
 * @module
 */
@Immutable
public class AffineTransform2D extends XAffineTransform
        implements MathTransform2D, LinearTransform, Parameterized, Formattable, Cloneable
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -5299837898367149069L;

    /**
     * The inverse transform. This field will be computed only when needed.
     */
    private transient volatile AffineTransform2D inverse;

    /**
     * {@code true} if this transform is mutable. This field may be temporarily set
     * to {@code true} during construction, but <strong>must</strong> be reset to
     * {@code false} before a reference to {@link AffineTransform2D} is made public.
     *
     * @since 3.00
     */
    transient boolean mutable;

    /**
     * Constructs an identity affine transform.  This constructor is reserved to code that
     * temporarily set the {@linkplain #mutable} flag to {@code true} for initializing the
     * affine transform.
     *
     * @since 3.00
     */
    AffineTransform2D() {
        super();
    }

    /**
     * Constructs a new affine transform with the same coefficient than the specified transform.
     *
     * @param transform The affine transform to copy.
     */
    public AffineTransform2D(final AffineTransform transform) {
        super(transform);
    }

    /**
     * Constructs a new {@code AffineTransform2D} from 6 values representing the 6 specifiable
     * entries of the 3&times;3 transformation matrix. Those values are given unchanged to the
     * {@link AffineTransform#AffineTransform(double,double,double,double,double,double) super
     * class constructor}.
     *
     * @param m00 the X coordinate scaling.
     * @param m10 the Y coordinate shearing.
     * @param m01 the X coordinate shearing.
     * @param m11 the Y coordinate scaling.
     * @param m02 the X coordinate translation.
     * @param m12 the Y coordinate translation.
     *
     * @since 2.5
     */
    public AffineTransform2D(double m00, double m10, double m01, double m11, double m02, double m12) {
        super(m00, m10, m01, m11, m02, m12);
    }

    /**
     * Throws an {@link UnsupportedOperationException} when a mutable method
     * is invoked, since {@code AffineTransform2D} must be immutable.
     *
     * @throws UnsupportedOperationException Always thrown.
     */
    @Override
    protected final void checkPermission() throws UnsupportedOperationException {
        if (!mutable) {
            super.checkPermission();
        }
    }

    /**
     * Returns the parameter descriptors for this math transform.
     */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Affine.PARAMETERS;
    }

    /**
     * Returns the matrix elements as a group of parameters values. The number of parameters
     * depends on the matrix size. Only matrix elements different from their default value
     * will be included in this group.
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        return ProjectiveTransform.getParameterValues(getMatrix());
    }

    /**
     * Gets the dimension of input points, which is fixed to 2.
     */
    @Override
    public final int getSourceDimensions() {
        return 2;
    }

    /**
     * Gets the dimension of output points, which is fixed to 2.
     */
    @Override
    public final int getTargetDimensions() {
        return 2;
    }

    /**
     * Transforms the specified {@code ptSrc} and stores the result in {@code ptDst}.
     */
    @Override
    public DirectPosition transform(final DirectPosition ptSrc, DirectPosition ptDst) {
        int dimension = ptSrc.getDimension();
        if (dimension != 2) {
            throw new MismatchedDimensionException(Errors.format(
                    Errors.Keys.MISMATCHED_DIMENSION_$3, "ptSrc", dimension, 2));
        }
        /*
         * Try to write directly in the destination point if possible. Following
         * code avoid the creation of temporary objects (except if ptDst is null).
         */
        if (ptDst == ptSrc) {
            if (ptSrc instanceof Point2D) {
                final Point2D point = (Point2D) ptSrc;
                super.transform(point, point);
                return ptSrc;
            }
        } else {
            if (ptDst == null) {
                final DirectPosition2D point = new DirectPosition2D(ptSrc.getOrdinate(0), ptSrc.getOrdinate(1));
                super.transform(point, point);
                return point;
            }
            dimension = ptDst.getDimension();
            if (dimension != 2) {
                throw new MismatchedDimensionException(Errors.format(
                        Errors.Keys.MISMATCHED_DIMENSION_$3, "ptDst", dimension, 2));
            }
            if (ptDst instanceof Point2D) {
                final Point2D point = (Point2D) ptDst;
                point.setLocation(ptSrc.getOrdinate(0), ptSrc.getOrdinate(1));
                super.transform(point, point);
                return ptDst;
            }
        }
        /*
         * At this point, we have no choice to create a temporary Point2D.
         */
        final Point2D.Double point = new Point2D.Double(ptSrc.getOrdinate(0), ptSrc.getOrdinate(1));
        super.transform(point, point);
        ptDst.setOrdinate(0, point.x);
        ptDst.setOrdinate(1, point.y);
        return ptDst;
    }

    /**
     * Transforms the specified shape.
     *
     * @param  shape Shape to transform.
     * @return Transformed shape, or {@code shape} if this transform is the identity transform.
     */
    @Override
    public Shape createTransformedShape(final Shape shape) {
        return transform(this, shape, false);
    }

    /**
     * Returns this transform as an affine transform matrix.
     */
    @Override
    public Matrix getMatrix() {
        return new Matrix3(this);
    }

    /**
     * Gets the derivative of this transform at a point. For an affine transform,
     * the derivative is the same everywhere.
     */
    @Override
    public Matrix derivative(final Point2D point) {
        return new Matrix2(getScaleX(), getShearX(),
                           getShearY(), getScaleY());
    }

    /**
     * Gets the derivative of this transform at a point. For an affine transform,
     * the derivative is the same everywhere.
     */
    @Override
    public Matrix derivative(final DirectPosition point) {
        return derivative((Point2D) null);
    }

    /**
     * Creates the inverse transform of this object.
     *
     * @throws NoninvertibleTransformException if this transform can't be inverted.
     */
    @Override
    public MathTransform2D inverse() throws NoninvertibleTransformException {
        if (inverse == null) {
            if (super.isIdentity()) {
                inverse = this;
            } else synchronized (this) {
                // Double check idiom. Was deprecated before Java 5 (couldn't work reliably).
                // Is okay with the new memory model since Java 5 provided that the field is
                // declared volatile (Joshua Bloch, "Effective Java" second edition).
                if (inverse == null) try {
                    final AffineTransform2D work = new AffineTransform2D(this);
                    work.mutable = true;
                    work.invert();
                    work.mutable = false;
                    work.inverse = this;
                    inverse = work; // Set only on success.
                } catch (java.awt.geom.NoninvertibleTransformException exception) {
                    throw new NoninvertibleTransformException(exception.getLocalizedMessage(), exception);
                }
            }
        }
        return inverse;
    }

    /**
     * Compares this affine transform with the given object for equality. The comparison is
     * performed in the same way than {@link AffineTransform#equals(Object)} with one additional
     * rule: if the other object is also an instance of {@code AffineTransform2D}, then the two
     * objects must be of the exact same class.
     * <p>
     * Most Geotk implementations require that the objects being compared are inconditionnaly of
     * the same class in order to be considered equal. However many JDK implementations, including
     * {@link AffineTransform}, do not have this requirement. Consequently the above condition
     * (i.e. require the same class only if the given object is an {@code AffineTransform2D} or
     * a subclass) is necessary in order to preserve the <cite>symmetricity</cite> contract of
     * {@link Object#equals}.
     * <p>
     * A side-effect of this implementation is that the <cite>transitivity</cite> contract of
     * {@code Object.equals(Object)} may be broken is some corner cases. This contract said:
     *
     * <blockquote>
     * <code>a.equals(b)</code> and <code>b.equals(c)</code> implies <code>a.equals(c)</code>
     * </blockquote>
     *
     * Assuming that <var>a</var>, <var>b</var> and <var>c</var> are instances of
     * {@code AffineTransform} (where "instance of <var>T</var>" means <var>T</var> or any
     * subclass of <var>T</var>), then the transitivity contract is broken if and only if
     * exactly two of those objects are instance of {@code AffineTransform2D} and those two
     * objects are not of the same class. Note that this implies that at least one subclass
     * of {@code AffineTransform2D} is used.
     * <p>
     * In the vast majority of cases, the transitivity contract is <strong>not</strong> broken
     * and the users can ignore this documentation. The transitivity contract is typically not
     * broken either because we usually don't subclass {@code AffineTransform2D}, or because we
     * don't mix {@code AffineTransform} with {@code AffineTransform2D} subclasses in the same
     * collection.
     * <p>
     * This special case exists in order to allow developers to attach additional information to
     * their own subclass of {@code AffineTransform2D}, and still distinguish their specialized
     * subclass from ordinary affine transforms in a pool of {@code MathTransform} instances. The
     * main application is the {@linkplain org.geotoolkit.referencing.operation.projection.Equirectangular}
     * map projection, which can be simplified to an affine transform but still needs to remember
     * the projection parameters.
     *
     * @param  object The object to compare with this affine transform for equality.
     * @return {@code true} if the given object is of appropriate class (as explained in the
     *         above documentation) and the affine transform coefficients are the same.
     */
    @Override
    public boolean equals(final Object object) {
        if (!super.equals(object)) {
            return false;
        }
        if (object instanceof AffineTransform2D) {
            return object.getClass().equals(getClass());
        }
        return true;
    }

    /**
     * Returns a new affine transform which is a modifiable copy of this transform. We override
     * this method because it is {@linkplain AffineTransform#clone defined in the super-class}.
     * However this implementation does not return an instance of the same class than {@code this};
     * it returns always an instance of the {@code AffineTransform} super-class.
     */
    @Override
    public AffineTransform clone() {
        return new AffineTransform(this);
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html#PARAM_MT"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name.
     */
    @Override
    public String formatWKT(final Formatter formatter) {
        final ParameterValueGroup parameters = getParameterValues();
        formatter.append(formatter.getName(parameters.getDescriptor()));
        formatter.append(parameters);
        return "PARAM_MT";
    }

    /**
     * Returns the WKT for this transform.
     */
    @Override
    public String toWKT() {
        final int indentation = FormattableObject.getDefaultIndentation();
        final Formatter formatter = new Formatter(Symbols.DEFAULT, null, indentation);
        formatter.append((Formattable) this);
        return formatter.toString();
    }

    /**
     * Returns the WKT representation of this transform.
     */
    @Override
    public String toString() {
        return toWKT();
    }
}
