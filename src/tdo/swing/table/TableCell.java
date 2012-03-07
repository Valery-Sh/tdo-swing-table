/*
 * TableCell.java
 *
 * Created on 18 јпрель 2007 г., 16:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.swing.table.TableColumn;

/**
 *  
 * 
 */
public class TableCell extends TableColumn implements Serializable {
    PropertyChangeSupport propertyChangeSupport;
    
    /**
     * The current x-coordinate of the cell.
     */
    protected int x;
    /**
     * The current y-coordinate of the cell.
     */
    protected int y;    
    
   /** The height of the column. */
    protected int	height;

    /** The minimum height of the column. */
    protected int	minHeight;

    /** The preferred height of the column. */
    protected int         preferredHeight;

    /** The maximum height of the column. */
    
    protected int	maxHeight;
    /**
     *
     */
    protected boolean fixed;
    
    protected boolean selected;
    
    /**
     * Creates a new instance of the <code>TableCell</code>
     */
    public TableCell() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }
    
    /**
     *
     */
    public int getHeight() {
        return this.height;
    }
    /**
     *
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    public int getMinHeight() {
        return this.minHeight;
    }
    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }
    
    public int getMaxHeight() {
        return this.maxHeight;
    }
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
    
    public int getPreferredHeight() {
        return this.preferredHeight;
    }
    public void setPreferredHeight(int preferredHeight) {
        this.preferredHeight = preferredHeight;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    public void setY(int y) {
        this.y = y;
    }
    
    public boolean isFixed() {
        return this.fixed;
    }
    public void setFixed(boolean fixed) {
        if ( this.fixed == fixed )
            return;
        boolean oldValue = this.fixed;
        this.fixed = fixed;
        propertyChangeSupport.firePropertyChange("fixed", oldValue, fixed); 
    }

    public boolean isSelected() {
        return this.selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener("fixed",listener);
    }
    
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener("fixed",listener);
    }
    
    
}//class
