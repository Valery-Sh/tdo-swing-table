/*
 * FlowColumnLayout.java
 *
 * Created on 18 ������ 2007 �., 22:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

/**
 *
 * @author Valera
 */
public class FlowColumnLayout implements ColumnLayoutManager {
    
    ColumnViewList<TableCell> columnList;
    
    private RowTemplate template;
    
    public FlowColumnLayout(RowTemplate template) {
        this.columnList = template.getColumnList();
    }
    
    public void setTemplate( RowTemplate template ) {
        this.columnList = template.getColumnList();
        this.template = template;
    }
    
    public void addLayoutCell(TableCell cell, String constraints) {
    }

    public void layoutTemplate(RowTemplate rowTemplate) {
        int w = 0;
        int h = 0;
        for ( TableCell tc : columnList ) {
            tc.setX(w);            
            tc.setWidth(tc.getPreferredWidth());
            w += tc.getPreferredWidth();

            if ( rowTemplate.getHeight() < 0 )
                h = Math.max(h,tc.getPreferredHeight());
            else
                h = rowTemplate.getHeight();
            tc.setY(0);            
            tc.setHeight(h);
        }//for
        
        rowTemplate.updateHeight(h);
        //rowTemplate.updateWidth(w);
    }


    public void removeLayoutCell(TableCell cell) {
    }
  /**
     * @RUS
     *   ���������� ������ �������, ������������� ������� � �����, ��� ��� ������������ 
     *   ������� ������ columnModel ������� � �������� <code>columnIndex</code> �����
     *   ����������� ��������������.
     * @ENDRUS
     */
    public TableCell[] getFreezeCapableColumns(int columnIndex)  {
        
        if ( template == null )
            return null;
        return null;
    }
    
}
