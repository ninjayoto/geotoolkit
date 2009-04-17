

package org.geotools.wms;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import org.geotools.internal.jaxb.backend.AbstractWMSCapabilities;
import org.opengis.metadata.citation.OnLineResource;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 *
 * @author Johann Sorel (Geomatys)
 */
 class XMLUtilities {

    private static final JAXBContext jaxbContext111;
    private static final JAXBContext jaxbContext130;

    static{
        JAXBContext temp = null;
        try{
            temp = JAXBContext.newInstance(org.geotools.internal.jaxb.backend.v111.WMT_MS_Capabilities.class);
        }catch(JAXBException ex){
            ex.printStackTrace();
        }
        jaxbContext111 = temp;

        temp = null;
        try{
            temp = JAXBContext.newInstance(org.geotools.internal.jaxb.backend.v130.WMSCapabilities.class);
        }catch(JAXBException ex){
            ex.printStackTrace();
        }
        jaxbContext130 = temp;
    }

     static AbstractWMSCapabilities unmarshall(Object source, WebMapServer.Version version) throws JAXBException{
         
         final Unmarshaller unMarshaller;
         switch(version){
             case v111 : unMarshaller = jaxbContext111.createUnmarshaller(); break;
             case v130 : unMarshaller = jaxbContext130.createUnmarshaller(); break;
             default: throw new IllegalArgumentException("unknonwed version");
         }

        return (AbstractWMSCapabilities) unmarshall(source, unMarshaller);
     }

     private static final Object unmarshall(final Object source, final Unmarshaller unMarshaller)
            throws JAXBException{
        if(source instanceof File){
            return unMarshaller.unmarshal( (File)source );
        }else if(source instanceof InputSource){
            return unMarshaller.unmarshal( (InputSource)source );
        }else if(source instanceof InputStream){
            return unMarshaller.unmarshal( (InputStream)source );
        }else if(source instanceof Node){
            return unMarshaller.unmarshal( (Node)source );
        }else if(source instanceof Reader){
            return unMarshaller.unmarshal( (Reader)source );
        }else if(source instanceof Source){
            return unMarshaller.unmarshal( (Source)source );
        }else if(source instanceof URL){
            return unMarshaller.unmarshal( (URL)source );
        }else if(source instanceof XMLEventReader){
            return unMarshaller.unmarshal( (XMLEventReader)source );
        }else if(source instanceof XMLStreamReader){
            return unMarshaller.unmarshal( (XMLStreamReader)source );
        }else if(source instanceof OnLineResource){
            final OnLineResource online = (OnLineResource) source;
            try {
                final URL url = online.getLinkage().toURL();
                return unMarshaller.unmarshal(url);
            } catch (MalformedURLException ex) {
                Logger.getLogger(XMLUtilities.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

        }else{
            throw new IllegalArgumentException("Source object is not a valid class :" + source.getClass());
        }

    }

}
