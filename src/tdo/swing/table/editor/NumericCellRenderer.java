/*
 * NumericCellRenderer.java
 *
 * Created on 16.05.2007, 19:55:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table.editor;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import tdo.DataColumn;
import tdo.DataTable;
import tdo.swing.table.TableViewer;
import tdo.swing.util.DataUtil;

/**
 *
 * @author Valera
 */
public class NumericCellRenderer extends DefaultTableCellRenderer.UIResource {
    protected DataColumn dataColumn;
    protected DataTable dataTable;
    
    public NumericCellRenderer() {
        super();
        setHorizontalAlignment(JLabel.RIGHT);
    }
  /**
     *
     * Returns the default table cell renderer.
     * <p>
     * During a printing operation, this method will be called with
     * <code>isSelected</code> and <code>hasFocus</code> values of
     * <code>false</code> to prevent selection and focus from appearing
     * in the printed output. To do other customization based on whether
     * or not the table is being printed, check the return value from
     * {@link javax.swing.JComponent#isPaintingForPrint()}.
     *
     * @param table  the <code>JTable</code>
     * @param value  the value to assign to the cell at
     *   <code>[row, column]</code>
     * @param isSelected true if cell is selected
     * @param hasFocus true if cell has focus
     * @param row  the row of the cell to render
     * @param column the column of the cell to render
     * @return the default table cell renderer
     * @see javax.swing.JComponent#isPaintingForPrint()
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {
        
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
                dataColumn = this.dataTable.getColumns().createColumn(table.getColumnClass(column), tc.getIdentifier().toString());
            }
        }        
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }    
  /**
     * Sets the <code>String</code> object for the cell being rendered to
     * <code>value</code>.
     * 
     * @param value  the string value for this cell; if value is
     *  <code>null</code> it sets the text value to an empty string
     * @see JLabel#setText
     * 
     */
    @Override
    protected void setValue(Object value) {
//        if ( dataColumn.getName() != null && dataColumn.getName().equals("PDECIMAL"))
//            System.out.println("NumericCellRenderer.setValue");
        setText(DataUtil.toString(dataColumn,value));
        //setText((value == null) ? "" : value.toString());
    }
     
}//class NumericCellRenderer
