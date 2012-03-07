/*
 * FreezeTableViewer.java
 *
 * Created on 27 јпрель 2007 г., 18:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

import javax.swing.SizeSequence;

/**
 *
 * @author Valera
 */
public class FreezeTableViewer extends TableViewer{
    
    protected TableViewer scrollTable;
    /**
     * Creates a new instance of FreezeTableViewer
     */
    protected FreezeTableViewer() {
        super();
    }
    public FreezeTableViewer(TableViewer table) {
        super(table);
        this.scrollTable = table;
        super.initViewer();
    }
    
    /**
     *
     * @return
     */
    @Override
    public SizeSequence getRowHeights() {
        return scrollTable.getRowHeights();
    }
    
    /**
     * Sets the height, in pixels, of all cells to <code>rowHeight</code>,
     * revalidates, and repaints.
     * This metod is overriden due to reposition firePropertyChange and resizeAndRepaint
     * invocation.
     *
     * @param   rowHeight                       new row height
     * @exception IllegalArgumentException      if <code>rowHeight</code> is
     *                                          less than 1
     * @see     #getRowHeight
     * @beaninfo
     *  bound: true
     *  description: The height of the specified row.
     */
    @Override
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        //scrollTable.setRowHeight(rowHeight);
    }
    
    /**
     * Sets the height for <code>row</code> to <code>rowHeight</code>,
     * revalidates, and repaints. The height of the cells in this row
     * will be equal to the row height minus the row margin.
     *
     * @param   rowIndex                        the row whose height is being
     * changed
     * @param   rowHeight                       new row height, in pixels
     * @exception IllegalArgumentException      if <code>rowHeight</code> is
     *                                          less than 1
     * @beaninfo
     *  bound: true
     *  description: The height in pixels of the cells in <code>row</code>
     * @since 1.3
     */
    @Override
    public void setRowHeight(int rowIndex, int rowHeight) {
        //scrollTable.setRowHeight(rowIndex,rowHeight);
        super.setRowHeight(rowIndex,rowHeight);        
    }
    
    /**
     * Sets the height for <code>row</code> to <code>rowHeight</code>,
     * revalidates, and repaints. The height of the cells in this row
     * will be equal to the row height minus the row margin.
     *
     * @param   rowIndex                        the row whose height is being
     * changed
     * @param   rowHeight                       new row height, in pixels
     * @param repaintNeeded 
     * @exception IllegalArgumentException      if <code>rowHeight</code> is
     *                                          less than 1
     * @beaninfo
     *  bound: true
     *  description: The height in pixels of the cells in <code>row</code>
     * @since 1.3
     */
    @Override
    public void setRowHeight(int rowIndex, int rowHeight, boolean repaintNeeded) {
        //scrollTable.setRowHeight(rowIndex, rowHeight, repaintNeeded);
    }

    @Override
    public int getRowHeight(int rowIndex) {
        return scrollTable.getRowHeight(rowIndex);
    }
    
    @Override
    public int getRowHeight() {
        return scrollTable.getRowHeight();
    }
  /**
     *
     * @param rowHeights
     */
    @Override
    public void setRowHeights(SizeSequence rowHeights) {
        this.rowHeights = rowHeights;
    }    
    /**
     * 
     */
    @Override
    public void refreshRowHeights() {
        if ( scrollTable == null )
            return;
        this.rowHeights = scrollTable.getRowHeights();
    }
    @Override
   public RowTemplate getRowTemplate(int rowIndex) {
        RowTemplate result = rowTemplates.getTemplate("COLUMNMODEL");
        
        if ( scrollTable.rowTemplateRequestListener == null )
            return result;
        
        scrollTable.rowTemplateRequestEvent.setKey(null);
        scrollTable.rowTemplateRequestListener.defineKey(rowTemplateRequestEvent,rowIndex);
        String key = rowTemplateRequestEvent.getKey();
        result = rowTemplates.getTemplate(key);
        if ( result == null ) {
            result = rowTemplates.getTemplate("COLUMNMODEL");
        }
        return result;
    }
        
}
