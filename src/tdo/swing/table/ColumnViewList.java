/*
 * ColumnViewList.java
 *
 * Created on 20 јпрель 2007 г., 14:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

import java.util.ArrayList;

/**
 *
 * @author valery
 */
public class ColumnViewList<T> extends ArrayList<TableCell> {
    //private boolean fixedPart;
    private int rowHeight;
    private int rowWidth;
    
    /** Creates a new instance of ColumnViewList */
    public ColumnViewList() {
        this(20);
    }
    
    //public ColumnViewList(boolean isFixedPart, int capacity) {
    public ColumnViewList(int capacity) {    
        super(capacity);
        //this.fixedPart = isFixedPart;
    }
/*    
    public boolean isFixedPart() {
        return fixedPart;
    }
    
    public void setFixedPart(boolean fixedPart) {
        this.fixedPart = fixedPart;
    }
*/
    @Override
    public boolean add(TableCell element) {
        //element.setOriginIndex( this.size());
        return super.add(element);
    }
    
    @Override
    public void add(int index, TableCell element) {
        //element.setOriginIndex( this.size());
        super.add(index, element);
        //for ( int i=index
    }
    
    public int getRowHeight() {
        return this.rowHeight;
    }

    public void setRowHeight( int rowHeight ) {
        this.rowHeight = rowHeight;
    }
    public int getRowWidth() {
        return this.rowWidth;
    }

    public void setRowWidth( int rowWidth ) {
        this.rowWidth = rowWidth;
    }
    
}//class
