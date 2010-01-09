/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotoolkit.metadata.iso.lineage;

import java.util.Date;
import java.util.Collection;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opengis.metadata.lineage.ProcessStepReport;
import org.opengis.metadata.lineage.Processing;
import org.opengis.util.InternationalString;
import org.opengis.metadata.lineage.Source;
import org.opengis.metadata.lineage.ProcessStep;
import org.opengis.metadata.citation.ResponsibleParty;

import org.geotoolkit.lang.ThreadSafe;
import org.geotoolkit.metadata.iso.MetadataEntity;
import org.geotoolkit.xml.Namespaces;


/**
 * Description of the event, including related parameters or tolerances.
 *
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane (IRD)
 * @author Cédric Briançon (Geomatys)
 * @version 3.07
 *
 * @since 2.1
 * @module
 */
@ThreadSafe
@XmlType(propOrder={
    "description",
    "rationale",
    "date",
    "processors",
    "sources",
    "outputs",
    "processingInformation",
    "reports"
})
@XmlRootElement(name = "LI_ProcessStep")
public class DefaultProcessStep extends MetadataEntity implements ProcessStep {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8151975419390308233L;

    /**
     * Description of the event, including related parameters or tolerances.
     */
    private InternationalString description;

    /**
     * Requirement or purpose for the process step.
     */
    private InternationalString rationale;

    /**
     * Date and time or range of date and time on or over which the process step occurred,
     * in milliseconds ellapsed since January 1st, 1970. If there is no such date, then this
     * field is set to the special value {@link Long#MIN_VALUE}.
     */
    private long date;

    /**
     * Identification of, and means of communication with, person(s) and
     * organization(s) associated with the process step.
     */
    private Collection<ResponsibleParty> processors;

    /**
     * Information about the source data used in creating the data specified by the scope.
     */
    private Collection<Source> sources;

    /**
     * Description of the product generated as a result of the process step.
     */
    private Collection<Source> outputs;

    /**
     * Comprehensive information about the procedure by which the algorithm was applied
     * to derive geographic data from the raw instrument measurements, such as datasets,
     * software used, and the processing environment.
     */
    private Processing processingInformation;

    /**
     * Report generated by the process step.
     */
    private Collection<ProcessStepReport> reports;

    /**
     * Creates an initially empty process step.
     */
    public DefaultProcessStep() {
        date = Long.MIN_VALUE;
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @param source The metadata to copy.
     *
     * @since 2.4
     */
    public DefaultProcessStep(final ProcessStep source) {
        super(source);
    }

    /**
     * Creates a process step initialized to the given description.
     *
     * @param description Description of the event, including related parameters or tolerances.
     */
    public DefaultProcessStep(final InternationalString description) {
        setDescription(description);
    }

     /**
     * Returns the description of the event, including related parameters or tolerances.
     */
    @Override
    @XmlElement(name = "description", required = true)
    public synchronized InternationalString getDescription() {
        return description;
    }

    /**
     * Sets the description of the event, including related parameters or tolerances.
     *
     * @param newValue The new description.
     */
    public synchronized void setDescription(final InternationalString newValue) {
        checkWritePermission();
        description = newValue;
    }

    /**
     * Returns the requirement or purpose for the process step.
     */
    @Override
    @XmlElement(name = "rationale")
    public synchronized InternationalString getRationale() {
        return rationale;
    }

    /**
     * Sets the requirement or purpose for the process step.
     *
     * @param newValue The new rationale.
     */
    public synchronized void setRationale(final InternationalString newValue) {
        checkWritePermission();
        rationale = newValue;
    }

    /**
     * Returns the date and time or range of date and time on or over which
     * the process step occurred.
     */
    @Override
    @XmlElement(name = "dateTime")
    public synchronized Date getDate() {
        return (date != Long.MIN_VALUE) ? new Date(date) : null;
    }

    /**
     * Sets the date and time or range of date and time on or over which the process
     * step occurred.
     *
     * @param newValue The new date.
     */
    public synchronized void setDate(final Date newValue) {
        checkWritePermission();
        date = (newValue != null) ? newValue.getTime() : Long.MIN_VALUE;
    }

    /**
     * Returns the identification of, and means of communication with, person(s) and
     * organization(s) associated with the process step.
     */
    @Override
    @XmlElement(name = "processor")
    public synchronized Collection<ResponsibleParty> getProcessors() {
        return xmlOptional(processors = nonNullCollection(processors, ResponsibleParty.class));
    }

    /**
     * Identification of, and means of communication with, person(s) and
     * organization(s) associated with the process step.
     *
     * @param newValues The new processors.
     */
    public synchronized void setProcessors(final Collection<? extends ResponsibleParty> newValues) {
        processors = copyCollection(newValues, processors, ResponsibleParty.class);
    }

    /**
     * Returns the information about the source data used in creating the data specified
     * by the scope.
     */
    @Override
    @XmlElement(name = "source")
    public synchronized Collection<Source> getSources() {
        return xmlOptional(sources = nonNullCollection(sources, Source.class));
    }

    /**
     * Information about the source data used in creating the data specified by the scope.
     *
     * @param newValues The new sources.
     */
    public synchronized void setSources(final Collection<? extends Source> newValues) {
        sources = copyCollection(newValues, sources, Source.class);
    }

    /**
     * Returns the description of the product generated as a result of the process step.
     *
     * @since 3.03
     */
    @Override
    @XmlElement(name = "output", namespace = Namespaces.GMI)
    public synchronized Collection<Source> getOutputs() {
        return xmlOptional(outputs = nonNullCollection(outputs, Source.class));
    }

    /**
     * Sets the description of the product generated as a result of the process step.
     *
     * @param newValues The new output values.
     *
     * @since 3.03
     */
    public synchronized void setOutputs(final Collection<? extends Source> newValues) {
        outputs = copyCollection(newValues, outputs, Source.class);
    }

    /**
     * Returns the comprehensive information about the procedure by which the algorithm
     * was applied to derive geographic data from the raw instrument measurements, such
     * as datasets, software used, and the processing environment. {@code null} if unspecified.
     *
     * @since 3.03
     */
    @Override
    @XmlElement(name = "processingInformation", namespace = Namespaces.GMI)
    public synchronized Processing getProcessingInformation() {
        return processingInformation;
    }

    /**
     * Sets the comprehensive information about the procedure by which the algorithm was
     * applied to derive geographic data from the raw instrument measurements, such as
     * datasets, software used, and the processing environment.
     *
     * @param newValue The new processing information value.
     *
     * @since 3.03
     */
    public synchronized void setProcessingInformation(final Processing newValue) {
        checkWritePermission();
        processingInformation = newValue;
    }

    /**
     * Returns the report generated by the process step.
     *
     * @since 3.03
     */
    @Override
    @XmlElement(name = "report", namespace = Namespaces.GMI)
    public synchronized Collection<ProcessStepReport> getReports() {
        return xmlOptional(reports = nonNullCollection(reports, ProcessStepReport.class));
    }

    /**
     * Sets the report generated by the process step.
     *
     * @param newValues The new process step report values.
     *
     * @since 3.03
     */
    public synchronized void setReports(final Collection<? extends ProcessStepReport> newValues) {
        reports = copyCollection(newValues, reports, ProcessStepReport.class);
    }
}
