/*
 * ColumnModelLayout.java
 *
 * Created on 21 Апрель 2007 г., 18:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Valera
 */
public class ColumnModelLayout implements ColumnLayoutManager {
    
    
    protected RowTemplate template;
    
    public ColumnModelLayout(RowTemplate template) {
        this.template = template;
    }
    public ColumnModelLayout() {
    }
    
    public void setTemplate(RowTemplate template ) {
        this.template = template;
    }
        
    public void addLayoutCell(TableCell cell, String constraints) {
    }

    public void layoutTemplate(RowTemplate rowTemplate) {
        
        this.template = rowTemplate;
        
        int w = 0;
        int h;//My 06.03.2012 = 0;
        
        TableViewer table = rowTemplate.table;
        if ( table == null )
            return;
        TableColumnModel tcm = table.getColumnModel();
        if ( tcm == null )
            return;
        
        for ( TableCell tc : rowTemplate.getColumnList() ) {
            
            TableColumn c = tcm.getColumn( tcm.getColumnIndex( tc.getIdentifier() ) );
            
            tc.setX(w);            
            tc.setWidth(c.getWidth());
            w += tc.getWidth();
            
            h = table.getRowHeight();
            tc.setY(0);            
            tc.setHeight(h);
        }//for
        
        //rowTemplate.updateHeight(h);
        //rowTemplate.updateWidth(w);
    }

    public void removeLayoutCell(TableCell cell) {
    }
    
  /**
     * @RUS
     *   Возвращает массив колонок, принадлежащих шаблону и таких, что при фиксировании 
     *   колонки модели columnModel тавлицы с индексом <code>columnIndex</code> также
     *   становятися фиксированными.
     * @ENDRUS
     */
    public TableCell[] getFreezeCapableColumns(int columnIndex)  {
        
        if ( template == null )
            return null;
        return new TableCell[] {template.getColumnList().get(columnIndex)};
    }
    
    
}//class
