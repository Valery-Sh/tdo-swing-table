/*
 * RowChangesRenderer.java
 * 
 * Created on 22.05.2007, 13:27:19
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table.editor;

import javax.swing.JLabel;
import tdo.DataRow;
import tdo.RowState;

/**
 *
 * @author valery
 */
public class RowChangeMarkCellRenderer extends AbstractTableViewCellRenderer{

    public RowChangeMarkCellRenderer() {
        super();
        setHorizontalAlignment(JLabel.CENTER);        
    }

    protected void setValue(Object value) {
        if ( rowIndex < 0 )
            return;
        DataRow row = getRow(rowIndex);
        if ( row == null )
            return;
        RowState rs = row.getState();
        if ( rs.isLoaded() )
            setText("=");
        else
        if ( rs.isDeleted() )
            setText("-");
        if ( rs.isUpdated() )
            setText("~");
        if ( rs.isManMade() )
            setText("+");
            
            
        
    }

}
