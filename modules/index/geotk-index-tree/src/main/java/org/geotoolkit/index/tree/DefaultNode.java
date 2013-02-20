/*
 *    Geotoolkit.org - An Open Source Java GIS Toolkit
 *    http://www.geotoolkit.org
 *
 *    (C) 2009-2012, Geomatys
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
package org.geotoolkit.index.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.geotoolkit.geometry.GeneralDirectPosition;
import org.geotoolkit.geometry.GeneralEnvelope;
import org.geotoolkit.gui.swing.tree.Trees;
import static org.geotoolkit.index.tree.Node.PROP_ISLEAF;
import org.geotoolkit.util.ArgumentChecks;
import org.geotoolkit.util.NumberRange;
import org.geotoolkit.util.collection.NotifiedCheckedList;
import org.geotoolkit.util.converter.Classes;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;

/**Create a Node adapting with Euclidean dimensions datas.
 *
 * @author Rémi Marechal (Geomatys)
 * @author Johann Sorel  (Geomatys)
 */
public class DefaultNode extends Node {

    private final List<Node> children = new NotifiedCheckedList<Node>(Node.class) {

        @Override
        protected void notifyAdd(Node e, int i) {
            setBound(null);
        }

        @Override
        protected void notifyAdd(Collection<? extends Node> clctn, NumberRange<Integer> nr) {
            setBound(null);
        }

        @Override
        protected void notifyRemove(Node e, int i) {
            setBound(null);
        }

        @Override
        protected void notifyRemove(Collection<? extends Node> clctn, NumberRange<Integer> nr) {
            setBound(null);
        }

        @Override
        protected void notifyChange(Node oldItem, Node newItem, int index) {
            setBound(null);
        }
    };

    private final List<Envelope> entries = new NotifiedCheckedList<Envelope>(Envelope.class) {
        @Override
        protected void notifyAdd(Envelope e, int i) {
            setBound(null);
        }
        @Override
        protected void notifyAdd(Collection<? extends Envelope> clctn, NumberRange<Integer> nr) {
            setBound(null);
        }
        @Override
        protected void notifyRemove(Envelope e, int i) {
            setBound(null);
        }
        @Override
        protected void notifyRemove(Collection<? extends Envelope> clctn, NumberRange<Integer> nr) {
            setBound(null);
        }

        @Override
        protected void notifyChange(Envelope oldItem, Envelope newItem, int index) {
            setBound(null);
        }
    };

    /**Create an empty {@code DefaultNode}.
     *
     * @param tree
     */
    public DefaultNode(final Tree tree) {
        this(tree, null, null, null, null, null);
    }

    /**Create {@code DefaultNode}.
     *
     * @param tree pointer on {@code Tree}.
     * @param parent pointer on {@code Node} parent.
     * @param children subNode.
     * @param entries data(s) to add.
     * @throws IllegalArgumentException if tree pointer is null.
     */
    public DefaultNode(final Tree tree, final Node parent, final DirectPosition lowerCorner, final DirectPosition upperCorner, final List<Node> children, final List<Envelope> entries) {
        ArgumentChecks.ensureNonNull("tree", tree);
        this.tree = tree;
        this.parent = parent;
        if(children!=null){
            for(Node n3d : children){
                n3d.setParent(this);
            }
            this.children.addAll(children);
        }
        if(entries!=null)this.entries.addAll(entries);
        if(lowerCorner != null && upperCorner != null){
            if(lowerCorner.getDimension() != upperCorner.getDimension()){
                throw new IllegalArgumentException("DefaultNode constructor : envelope corners are not in same dimension");
            }
            this.boundary = new GeneralEnvelope(new GeneralDirectPosition(lowerCorner), new GeneralDirectPosition(upperCorner));
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Node> getChildren() {
        return children;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaf() {
        final Object userPropIsLeaf = getUserProperty(PROP_ISLEAF);
        if(userPropIsLeaf != null){
            return (Boolean)userPropIsLeaf;
        }
        return getChildren().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        final Object userPropIsLeaf = getUserProperty(PROP_ISLEAF);
        if(userPropIsLeaf != null && ((Boolean)userPropIsLeaf)){
            for(Node cell : getChildren()){
                if(!cell.isEmpty()){
                    return false;
                }
            }
            return true;
        }
        return (getChildren().isEmpty() && getEntries().isEmpty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFull() {
        final Object userPropIsLeaf = getUserProperty(PROP_ISLEAF);
        if(userPropIsLeaf != null && ((Boolean)userPropIsLeaf)){
            for(Node cell : getChildren()){
                if(!cell.isFull()){
                    return false;
                }
            }
            return true;
        }
        return (getChildren().size()+getEntries().size())>=getTree().getMaxElements();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Envelope getBoundary() {
        if(boundary==null){
            calculateBounds();
        }
        return boundary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Envelope> getEntries() {
        return entries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tree getTree() {
        return tree;
    }

    /**
     * Compute {@code Node} boundary from stocked sub-node or entries .
     */
    protected void calculateBounds(){
        for(Envelope ent2D : getEntries()){
            addBound(ent2D);
        }
        for(Node n2D : getChildren()){
            if(!n2D.isEmpty()){
                addBound(n2D.getBoundary());
            }
        }
    }

    /**Update boundary size from {@code Envelope}.
     *
     * @param env {@code Node} or entry boundary.
     */
    protected void addBound(final Envelope env){
        if(boundary==null){
            boundary = new GeneralEnvelope(env);
        }else{
            boundary.add(env);
        }
    }

    /**Affect a new parent pointer.
     *
     * @param parent
     */
    @Override
    public void setParent(final Node parent){
        this.parent = parent;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public String toString() {
        final Collection col = new ArrayList(entries);
        col.addAll(children);
        String strparent =  (parent == null)?"null":String.valueOf(parent.hashCode());
        return Trees.toString(Classes.getShortClassName(this)+" : "+this.hashCode()+" parent : "+strparent
                + " leaf : "+isLeaf()+ " userPropLeaf : "+(Boolean)getUserProperty(PROP_ISLEAF), col);
    }
}
