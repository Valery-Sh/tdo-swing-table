/*
 * NumericCellEditor.java
 *
 * Created on 10.05.2007, 10:24:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import tdo.DataColumn;
import tdo.DataTable;
import tdo.swing.table.TableViewer;
import tdo.swing.util.DataUtil;


/**
 *
 * @author valery
 */
public class NumericCellEditor extends AbstractCellEditor  implements TableCellEditor, ActionListener {
    
    protected NumericTextField editorComponent;
    protected DataColumn dataColumn;
    protected DataTable dataTable;
    
    protected String editMask;
    
    /**
     * An integer specifying the number of clicks needed to start editing.
     * Even if <code>clickCountToStart</code> is defined as zero, it
     * will not initiate until a click occurs.
     */
    protected int clickCountToStart = -1;
    
    public NumericCellEditor() {
        editMask = null;
        editorComponent = new NumericTextField();
        editorComponent.addActionListener(this);
        
    }
    
    public NumericCellEditor( DataTable dataTable ) {
        editMask = null;
        editorComponent = new NumericTextField();
        this.dataTable = dataTable;
        editorComponent.addActionListener(this);
    }
    
    public DataTable getDataTable() {
        return this.dataTable;
    }
    
    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }
    
    /**
     * Returns the value contained in the editor.
     * @return the value contained in the editor
     */
    public Object getCellEditorValue() {
        return DataUtil.valueOf(dataColumn,editorComponent.getText());
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if ( table.getModel() instanceof DataTable ) {
            this.dataTable = (DataTable)table.getModel();
            dataColumn = dataTable.getColumns().get(column);
        } else {
            if ( table instanceof TableViewer ) {
                TableViewer tv = (TableViewer)table;
                TableColumn tc = tv.getRowTemplate(row).getColumn(column);
                dataColumn = this.dataTable.getColumns().createColumn(tv.getColumnClass(row, column), tc.getIdentifier().toString());
            } else {
                TableColumn tc = table.getColumnModel().getColumn(column);
                dataColumn = dataTable.getColumns().createColumn(table.getColumnClass(column), tc.getIdentifier().toString());
            }
        }
       
        //editMask = "#,##0.0";
        editorComponent.setDataColumn(dataColumn);
        editorComponent.setText(DataUtil.toString(dataColumn,value,editMask));
        return editorComponent;
    }
    
    public String getEditMask() {
        return this.editMask;
    }
    
    public void setEditMask(String mask) {
        this.editMask = mask;
    }
    
    /**
     * Returns true to indicate that editing has begun.
     *
     * @param anEvent          the event
     * @return
     */
    public boolean startCellEditing(EventObject anEvent) {
        return true;
    }
    /**
     * Stops editing and
     * returns true to indicate that editing has stopped.
     * This method calls <code>fireEditingStopped</code>.
     *
     * @return  true
     */
    @Override
    public boolean stopCellEditing() {
/*	    String s = this.editorComponent.getText();
	    // Here we are dealing with the case where a user
	    // has deleted the string value in a cell, possibly
	    // after a failed validation. Return null, so that
	    // they have the option to replace the value with
	    // null or use escape to restore the original.
	    // For Strings, return "" for backward compatibility.
	    if ("".equals(s)) {
		if (constructor.getDeclaringClass() == String.class) {
		    value = s;
		}
		super.stopCellEditing();
	    }
*/
            
	    try {
		getCellEditorValue();
	    }
	    catch (Exception e) {
		((JComponent)editorComponent).setBorder(new LineBorder(Color.red));
		return false;
	    }

        fireEditingStopped();
        return true;
    }
    /**
     * When an action is performed, editing is ended.
     * @param e the action event
     * @see #stopCellEditing
     */
    public void actionPerformed(ActionEvent e) {
        TableViewer tv = null;
        int rowIndex   = -1;
        int columnIndex = -1;
        if ( this.editorComponent.getParent() instanceof TableViewer ) {
            tv = (TableViewer)this.editorComponent.getParent();
            rowIndex = tv.getEditingRow();
            columnIndex = tv.getEditingColumn();
        }
        this.stopCellEditing();
        if ( rowIndex >= 0 && columnIndex >= 0 )
            tv.editingStopped(e, rowIndex,columnIndex);
    }
    
    /**
     * Specifies the number of clicks needed to start editing.
     *
     * @param count  an int specifying the number of clicks needed to start editing
     * @see #getClickCountToStart
     */
    public void setClickCountToStart(int count) {
        clickCountToStart = count;
    }
    
    /**
     * Returns the number of clicks needed to start editing.
     * @return the number of clicks needed to start editing
     */
    public int getClickCountToStart() {
        return clickCountToStart;
    }
    
    /**
     * Returns true if <code>anEvent</code> is <b>not</b> a
     * <code>MouseEvent</code>.  Otherwise, it returns true
     * if the necessary number of clicks have occurred, and
     * returns false otherwise.
     *
     * @param   anEvent         the event
     * @return  true  if cell is ready for editing, false otherwise
     * @see #setClickCountToStart
     * @see #shouldSelectCell
     */
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        Object source = anEvent.getSource();
        int clickCount = 2;
        if ( anEvent instanceof MouseEvent && source instanceof TableViewer && getClickCountToStart() <=0 ) {
            clickCount = ((TableViewer)source).getClickCountToStart();
        } else {
            if ( getClickCountToStart() > 0 ) {
                clickCount = getClickCountToStart();
            }
        }
        
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent)anEvent).getClickCount() >= clickCount;
        }
        return true;
    }
}//class NumericCellEditor
