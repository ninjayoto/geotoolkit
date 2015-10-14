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

package org.geotoolkit.xml;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import org.apache.sis.util.logging.Logging;
import org.apache.sis.xml.Namespaces;


/**
 * An abstract class for all stax stream writer.<br/>
 * Writers for a given specification should extend this class and
 * provide appropriate write methods.<br/>
 * <br/>
 * Example : <br/>
 * <pre>
 * {@code
 * public class UserWriter extends StaxStreamWriter{
 *
 *   public void write(User user) throws XMLStreamException{
 *      //casual stax writing operations
 *      writer.writeStartElement(...
 *   }
 * }
 * }
 * </pre>
 * And should be used like :<br/>
 * <pre>
 * {@code
 * final UserWriter instance = new UserWriter();
 * try{
 *     instance.setOutput(stream);
 *     instance.write(aUser);
 * }finally{
 *     instance.dispose();
 * }
 * </pre>
 *
 * @author Johann Sorel (Geomatys)
 * @module pending
 */
public abstract class StaxStreamWriter extends AbstractConfigurable {

    /**
     * Logger for this writer.
     */
    protected static final Logger LOGGER = Logging.getLogger("org.geotoolkit.xml");

    protected XMLStreamWriter writer;

    /**
     * Store the output stream if it was generated by the parser itself.
     * It will closed on the dispose method or when a new input is set.
     */
    private OutputStream targetStream;

    private int lastUnknowPrefix = 0;

    private final Map<String, String> unknowNamespaces = new HashMap<String, String>();

    public StaxStreamWriter(){
    }

    /**
     * Acces the underlying stax writer.
     * This method is used when several writer are wrapping a single writer.
     * Like when an Symbology Encoding writer wraps a Filter writer.
     * <br>
     * It can also be used to write tag before or after this writer is used.
     */
    public XMLStreamWriter getWriter(){
        return writer;
    }

    /**
     * close potentiel previous stream and cache if there are some.
     * This way the writer can be reused for a different output later.
     * The underlying stax writer will be closed.
     */
    public void reset() throws IOException, XMLStreamException{
        if(writer != null){
            writer.close();
            writer = null;
        }
        if(targetStream != null){
            targetStream.close();
            targetStream = null;
        }
    }

    /**
     * Release potentiel locks or opened stream.
     * Must be called when the writer is not needed anymore.
     * It should not be used after this method has been called.
     */
    public void dispose() throws IOException, XMLStreamException{
        reset();
    }

    /**
     * Set the output for this writer.<br/>
     * Handle types are :<br/>
     * - java.io.File<br/>
     * - java.io.Writer<br/>
     * - java.io.OutputStream<br/>
     * - javax.xml.stream.XMLStreamWriter<br/>
     * - javax.xml.transform.Result<br/>
     *
     * @param output
     * @throws IOException
     * @throws XMLStreamException
     */
    public void setOutput(Object output) throws IOException, XMLStreamException{
        reset();

        if(output instanceof XMLStreamWriter){
            writer = (XMLStreamWriter) output;
            return;
        }

        if(output instanceof File){
            targetStream = new FileOutputStream((File)output);
            final BufferedOutputStream bout = new BufferedOutputStream(targetStream);
            output = bout;
        }

        writer = toWriter(output);
    }

    /**
     * Write a new tag with the text corresponding to the given value.
     * The tag won't be written if the value is null.
     * @param namespace : namespace of the wanted tag
     * @param localName : local name of the wanted tag
     * @param value : text value to write
     * @throws XMLStreamException
     */
    protected void writeSimpleTag(final String namespace, final String localName, final Object value) throws XMLStreamException{
        if(value != null){
            writer.writeStartElement(namespace, localName);
            writer.writeCharacters(value.toString());
            writer.writeEndElement();
        }
    }

    /**
     * Creates a new XMLStreamWriter.
     * @param output
     * @return XMLStreamWriter
     * @throws XMLStreamException if the output is not handled
     */
    private static XMLStreamWriter toWriter(final Object output)
            throws XMLStreamException{
        final XMLOutputFactory XMLfactory = XMLOutputFactory.newInstance();
        XMLfactory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.TRUE);

        if(output instanceof OutputStream){
            return XMLfactory.createXMLStreamWriter((OutputStream)output,"UTF-8");
        }else if(output instanceof Result){
            return XMLfactory.createXMLStreamWriter((Result)output);
        }else if(output instanceof Writer){
            return XMLfactory.createXMLStreamWriter((Writer)output);
        }else{
            throw new XMLStreamException("Output type is not supported : "+ output);
        }
    }

    /**
     * Returns the prefix for the given namespace.
     *
     * @param namespace The namespace for which we want the prefix.
     */
    protected Prefix getPrefix(final String namespace) {
        String prefix = Namespaces.getPreferredPrefix(namespace, null);
        /*
         * temporary hack todo remove
         */
        if ("http://www.opengis.net/gml/3.2".equals(namespace)) {
            return new Prefix(false, "gml");
        }
        boolean unknow = false;
        if (prefix == null) {
            prefix = unknowNamespaces.get(namespace);
            if (prefix == null) {
                prefix = "ns" + lastUnknowPrefix;
                lastUnknowPrefix++;
                unknow = true;
                unknowNamespaces.put(namespace, prefix);
            }
        }
        return new Prefix(unknow, prefix);
    }

    /**
     * Inner class for handling prefix and if it is already known.
     */
    protected final class Prefix {
        public boolean unknow;
        public String prefix;

        public Prefix(final boolean unknow, final String prefix) {
            this.prefix = prefix;
            this.unknow = unknow;
        }
    }
}
