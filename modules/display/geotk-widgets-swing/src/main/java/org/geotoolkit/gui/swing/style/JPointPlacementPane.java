/*
 *    Geotoolkit - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2007 - 2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008 - 2009, Johann Sorel
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
package org.geotoolkit.gui.swing.style;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import org.geotoolkit.gui.swing.resource.MessageBundle;
import org.geotoolkit.map.MapLayer;
import org.opengis.style.PointPlacement;

/**
 * Point placement panel
 * 
 * @author Johann Sorel
 * @module
 */
public class JPointPlacementPane extends StyleElementEditor<PointPlacement>{
    
    private MapLayer layer = null;
    
    /** Creates new form JPointPlacementPanel */
    public JPointPlacementPane() {
        super(PointPlacement.class);
        initComponents();
        init();
    }
    
    private void init(){
        guiRotation.setModel(0, 0d, 360d, 1);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        guiAnchor = new JAnchorPointPane();
        guiDisplacement = new JDisplacementPane();
        guiLabelRotation = new JLabel();
        guiRotation = new JNumberExpressionPane();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();

        setOpaque(false);

        guiAnchor.setOpaque(false);
        guiAnchor.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                JPointPlacementPane.this.propertyChange(evt);
            }
        });

        guiDisplacement.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                JPointPlacementPane.this.propertyChange(evt);
            }
        });

        guiLabelRotation.setText(MessageBundle.format("rotation")); // NOI18N

        guiRotation.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                JPointPlacementPane.this.propertyChange(evt);
            }
        });

        jLabel1.setText(MessageBundle.format("anchor")); // NOI18N

        jLabel2.setText(MessageBundle.format("displacement")); // NOI18N

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(guiLabelRotation)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(guiRotation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addComponent(guiAnchor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(guiDisplacement, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel1)
            .addComponent(jLabel2)
        );

        layout.linkSize(SwingConstants.HORIZONTAL, new Component[] {guiAnchor, guiDisplacement});

        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(guiLabelRotation)
                    .addComponent(guiRotation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(guiAnchor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(guiDisplacement, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        layout.linkSize(SwingConstants.VERTICAL, new Component[] {guiLabelRotation, guiRotation});

    }// </editor-fold>//GEN-END:initComponents

    private void propertyChange(PropertyChangeEvent evt) {//GEN-FIRST:event_propertyChange
        // TODO add your handling code here:
        if (PROPERTY_UPDATED.equalsIgnoreCase(evt.getPropertyName())) {            
            firePropertyChange(PROPERTY_UPDATED, null, create());
            parse(create());
        }
    }//GEN-LAST:event_propertyChange

    @Override
    public void setLayer(final MapLayer layer) {
        this.layer = layer;
        guiAnchor.setLayer(layer);
        guiDisplacement.setLayer(layer);
        guiRotation.setLayer(layer);
    }

    @Override
    public MapLayer getLayer() {
        return layer;
    }

    @Override
    public void parse(final PointPlacement placement) {
        if(placement != null){
            guiAnchor.parse(placement.getAnchorPoint());
            guiDisplacement.parse(placement.getDisplacement());
            guiRotation.parse(placement.getRotation());
        }
    }

    @Override
    public PointPlacement create() {
        return getStyleFactory().pointPlacement(
                guiAnchor.create(),
                guiDisplacement.create(),
                guiRotation.create());
    }
    
    @Override
    protected Object[] getFirstColumnComponents() {
        return new Object[]{guiLabelRotation,guiAnchor,guiDisplacement};
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JAnchorPointPane guiAnchor;
    private JDisplacementPane guiDisplacement;
    private JLabel guiLabelRotation;
    private JNumberExpressionPane guiRotation;
    private JLabel jLabel1;
    private JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
    
}
