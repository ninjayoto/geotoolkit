
package org.geotools.wms.v111;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotoolkit.referencing.CRS;
import org.geotools.wms.AbstractGetMap;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;

/**
 * @author Johann Sorel (Geomatys)
 */
public class GetMap111 extends AbstractGetMap {

    public GetMap111(String serverURL){
        super(serverURL,"1.1.1");
    }

    @Override
    protected Map<String,String> toString(Envelope env) {
        Map<String,String> map = new HashMap<String,String>();
        final StringBuilder sb = new StringBuilder();
        final double minx = env.getMinimum(0);
        final double maxx = env.getMaximum(0);
        final double miny = env.getMinimum(1);
        final double maxy = env.getMaximum(1);
        sb.append(minx).append(',').append(miny).append(',').append(maxx).append(',').append(maxy);

        map.put("BBOX", sb.toString());

        try {
            map.put("SRS", CRS.lookupIdentifier(env.getCoordinateReferenceSystem(), true));
        } catch (FactoryException ex) {
            Logger.getLogger(GetMap111.class.getName()).log(Level.SEVERE, null, ex);
        }

        return map;
    }

}
