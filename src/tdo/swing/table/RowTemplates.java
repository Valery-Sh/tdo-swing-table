/*
 * RowTemplates.java
 *
 * Created on 18 ������ 2007 �., 15:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.table.TableColumnModel;

/**
 * @RUS 
 *     ��������� ��������� �������� ���� {@link #RowTemplate} ��� HashMap.
 *  ��� �������� �������� ���� {@link TableViewer} ��������� ���������
 *  ������� ������. ��������� ������ �������� �� ������� ���� ���� �������-������
 *  ���� <code>RowTemplate</code> �� ��������� ����� ������ "COLUMNMODEL". �����
 *  ������ ����� ������ �������� � ������ ��������������� � ������� �������,
 *  ������� ����� ���� �������� ������� ������ <code>getColumnModel()<code>
 *  ������� <code>TableViewer</code>. ��� �������������� �����
 *  ���������� ���������:
 *  <OL>
 *    <LI>
 *       ��� ��������� ������ ����� ���� � ���� ���������� ���������;
 *    </LI>
 *    <LI>
 *       i-� ������� ��������� ������� � ������ "COLUMNMODEL" �������������
 *       i-�� �������� ��������� � <code>table.getColumnModel()<code> ��� � 
 *          ������ �������, ��� � � ���, ��� �������� ����������� ���� ����� 
 *          ���������� ��������
 *      
 *      <UL>
 *          <LI>
 *              <code>identifier</code>
 *          </LI>
 *          <LI>
 *              <code>modelIndex</code>
 *          </LI>
 *          <LI>
 *              <code>minWidth</code>
 *          </LI>
 *          <LI>
 *              <code>maxWidth</code>
 *          </LI>
 *          <LI>
 *              <code>preferredWidth</code>
 *          </LI>
 *          <LI>
 *              <code>width</code>
 *          </LI>
 *          <LI>
 *              <code>resizable</code>
 *          </LI>
 *          <LI>
 *              <code>cellEditor</code>
 *          </LI>
 *          <LI>
 *              <code>cellRenderer</code>
 *          </LI>
 *          <LI>
 *              <code>headerRenderer</code>
 *          </LI>
 *          <LI>
 *              <code>headerValue</code>
 *          </LI>
 *        ��� �������� ������ � ������ "COLUMNMODEL" �������� ������� ������
 *        {@link TableViewer#getColumnModelTemplate}.
 *      </UL>
 *    </LI>
 *    <LI>
 *       ��� ��������, ���������� ��� ����������� ������� �� 
 *       <code>table.getColumnModel()</code> ��������������� �������� 
 *       ����������� ��� ��������� ������� "COLUMNMODEL". ��� ������������ 
 *       ��������� ������ "COLUMNMODEL" �������������� ��� ���������� �������
 *       <code>TableColumnModelEvent</code>, ������������� ������� �������
 *       <code>table.getColumnModel()</code>. 
 *    </LI>
 *    <LI>
 *      ��� ������ ������ ������� ������� ������ 
 *      <code>table.setColumnModel</code> ��� ��� ��������� ������ ������, 
 *      ������������� ��������� ����� ��������� �������� ���� 
 *      <code>TableCell</code> � ������� � ������ "COLUMNMODEL".
 *      ��� �������������� ���, ��� ������ ������ <code>RowTemplates</code>
 *      �������������� ��� ���������� ������� <code>PropertyChangeEvent</code>,
 *      ������������� ������� ������� <code>table.getColumnModel()</code>.
 *    </LI>  
 *  </OL>
 *      ������ ��������� ������ �������� �������� {@link #modelColumns} ������� 
 *      ��� <code>RowTemplate</code>. ��� �������� ��������� ��� �������� ������
 *      ������ � ���������� ������ ��� ���������� ������ 
 *      <code>table.setModel()</code>. ����� �������, <code>modelColumns</code>
 *      ������ ������ ����� �������, ��������������� ������ ������ � �� 
 *      ���������� ��� ����������, ��������� ��� ����������� ������� � ������ 
 *      ������� <code>table.getColumnModel()</code>. ��� ��������� ������������
 *      ������� <code>RowTemplate</code> � ���������, �� ��������� � 
 *      <code>table.getColumnModel()</code>. <p>
 *       ����� ��������, ��� <code>modelColumns</code>
 *      �� ���������� �� ���������� map-��������� ������ � ������ � ��� �������������� �������
 *      {@link #getModelColumns}. ��� ��������, ����� {@link TableViewer} �����
 *      ����� ����������� ����� ��� ������� � <code>modelColumns</code>.
 * @ENDRUS 
 */
public class RowTemplates extends HashMap<String, RowTemplate> implements PropertyChangeListener {
    
    
    private TableViewer table;
  
    /**
     * @RUS
     *  ������ �������� ������� ���� ������� ��� ������ ������.
     *  ��� ���������� ������ ������ ������� ���� {@link #TableViewer} ���������
     *  ������ ���� <code>TableColumnModel</code> �� ���������. ����� ��� ������
     *  ������� ������ ��������� ���� �������. � ���������� <code>columnModel</code>
     *  ����� �������� �����������, ��������� ��� ������������ �������, ��, ��� ����
     *  <code>modelColumns</code> �������� ����������. ���������� ����� ������
     *  ��� ���������� ������� ����� ������ ������.
     * @ENDRUS
     */
    private RowTemplate modelColumns;
  
    
    
    /**
     * 
     * @RUS ������� ����� ���������RowTemplatess ��� ������� TableViewer.
     *   <OL>
     *     <LI>
     *        ������� ��������� {@link tdo.swing.table.RowTemplats} � ��������� ��� ��������
     *        {@link #modelColumns}. ��� ����� ������� ���������������
     *        �������� �������� <code>height</code> ������ �������� ��������
     *        <code>height</code> ������� TableViewer.
     *     </LI>
     *     <LI>
     *        ������� ��������� {@link tdo.swing.table.RowTemplate} � ��������� ���
     *        � ����������� ��������� � ������ "COLUMNMODEL" � ����� ������������� �������� 
     *        �������� {@link TableViewer#setColumnModelTemplate}.
     *        ���  ������� ���������������  �������� ��������
     *        <code>height</code> ������ �������� ��������  code>height</code>
     *        ������� {@link TableViewer}. ��������������� ����� layout manager
     *        ������� ������� �� ������ {@link ColumnModelLayout}.
     *     </LI>
     *   </OL>
     * @param table ������� {@link TableViewer}, ��� ������� ������������ �������
     * @see {@link tdo.swing.table.TableViewer#getHeight}.
     * @ENDRUS 
     */
    public RowTemplates(TableViewer table) {
        this.table = table;
        this.modelColumns = new RowTemplate(table);
        this.modelColumns.setHeight(table.getRowHeight());
        //this.defaultTemplateKey = null;
        RowTemplate rt = new RowTemplate(table);
        rt.createFrom(table.getColumnModel());
        put("COLUMNMODEL", rt );
        if ( table != null )
            table.setColumnModelTemplate(rt);
        rt.setHeight(table.getRowHeight());
        
        ColumnLayoutManager lm = new ColumnModelLayout();
        rt.setColumnLayout( lm );
    }
    
    //=======================================================================================
    //  Properties
    //=======================================================================================
    
    /**
     * @RUS 
     *  ��������� �������, ��� ������� ����������� ������� �����.
     *  ������� ����� ���� ����������� ������� ��� �������� � ���������� �������.
     *  �������� <code>columnModelTemplate</code> ������� <code>table</code>
     * ����������� �������� ������� � ������ "COLUMNMODEL".
     * ����� ��������� ����������� ���������� ��� �����.
     * �������� �������� <code>table</code> ������� ������� (RowTemplate)
     * �� ��������� ����� ����� ��������� �������� ���������.<p>
     * �������� ����� {@link #doLayout}.
     * 
     * @param table ����������� �������.
     * @ENDRUS
     */
    public void setTable(TableViewer table) {
        this.table = table;
        for ( RowTemplate rt : this.values() ) {
            rt.setTable(table);
        }
        this.modelColumns.setTable(table);
        
        if ( table != null )
            table.setColumnModelTemplate(get("COLUMNMODEL"));
        
        doLayout();
    }
    /**
     * @RUS
     *  @return ������ ������ ������, ����������� � ���������� ��� ���������� 
     *          ������ <code>table.setModel</code>.
     *  @see #columnModels
     * @ENDRUS
     */
    public RowTemplate getModelColumns() {
        return this.modelColumns;
    }
    
    /**
     * @RUS
     * ����������� ������ ����� �� ��������� ����� �������.
     * @param key ���� �������. 
     * @return ������ ��� ��������� �����. ���� �������� ��������� <code>key</code>
     *         ����� <code>null</code>, �� ������������ ������ ��� ����� "COLUMNMODEL".
     * @ENDRUS
     */
    public RowTemplate getTemplate(String key) {
        if ( key == null ) 
            return super.get("COLUMNMODEL");
        
        RowTemplate rt = super.get(key);
        if ( rt == null )
            rt = super.get("COLUMNMODEL");
        return rt;
    }
  /**
     * @RUS
     *  ���������� ����� ��������� <code>HashMap</code>.
     *  ���������� ������ ����� �� ��������� ����� �������.
     *  ����������� �������� � ������ ������ ������� <code>toString</code> � 
     *  �������� {@link #getTemplate}.
     * @param key ���� ������� ��� java.lang.Object. 
     * @return ������ ��� ��������� �����. ���� �������� ��������� <code>key</code>
     *         ����� <code>null</code>, �� ������������ ������ ��� ����� "COLUMNMODEL".
     * @ENDRUS
     */    
    public RowTemplate get(Object key ) {
        return getTemplate(key.toString());
    }
    
    /**
     * @RUS
     *  �������� �������� ������ � ��������� <code>HashMap</code> � �������� ������.
     *  ���� �������� �������� <code>height</code> ������������ ������� ������ 0, ��
     *  ��� ����������� �������� �������� <code>height</code> ������� � ������ "COLUMNMODEL".
     *  ����� ����������� ����� {@link ScrollDataView#refreshRowHeights</code> � {@link #doLayout}.
     *  @param key �������� �����, � ������� ������ ���������� � ���������.
     *  @param rowTemplate ����������� ������.
     *  @return ����������� � ��������� HashMap ������
     * @ENDRUS
     */
    public RowTemplate put(String key, RowTemplate rowTemplate ) {
        int h = -1;
        RowTemplate cmrt = get("COLUMNMODEL");
        if ( cmrt == null )
            h = table.getRowHeight();
        else
            h = cmrt.getHeight();
        if ( rowTemplate.getHeight() < 0 )
            rowTemplate.updateHeight(h);
        
        RowTemplate rt = super.put(key, rowTemplate);
        
        table.refreshRowHeights();
        doLayout();
        return rt;
    }
    
    /**
     * @RUS
     * ��������� ����������������� ��������� {@link TableCell} ��� ������� ������� 
     * �������� ������������ �� {@link ColumnLayoutManager}. 
     * @ENDRUS
     */
    public void doLayout() {
        
        for ( RowTemplate t : this.values() ) {
            t.doLayout();
        }
    }
    /**
     * @RUS
     * ����� ���������� {@link java.beans.PropertyChangeListener}.
     * ������������ ������� ��������� �������:
     * <OL>
     *   <LI>
     *      <code>columnModel</code> ������������ ����������� {@link JTable} ��� ���������
     *                               ������ ������� � ������ <code>setColumnModel</code>.
     *            ��� ��������� ���������� ����� {@link #adjustColumnModelChanged}.
     *   </LI>
     *   <LI>
     *      <code>tableModel</code> ������������ ����������� {@link JTable} ��� ���������
     *                               ������ ������� � ������ <code>setModel</code>.
     *            ��� ��������� ���������� ����� {@link #adjustModelChanged}.
     *   </LI>
     *   <LI>
     *      <code>rowHeight</code> ������������ ����������� {@link JTable} ��� ���������
     *                               ������ ������� � ������ <code>setRowHeight</code>.
     *              ��������� �������� <code>height</code> ������� {@link #modelColumns}
     *              � ������� �� ��������� � ������ "COLUMNMODEL" ����������� ��������. 
     *   </LI>
     * </OL>
     * @param evt �������, �������������� �������.
     * @ENDRUS
     *  {@link java.beans.PropertyChangeListener} interface implementation method.
     *  The method is invoked when the following properties have been changed:
     * <OL>
     *   <LI>
     *      <code>columnModel</code> fired by JTable component when it's columnModel has been changed.
     *   </LI>
     * </OL>
     *
     *  @param evt the object that represent an event to be treated.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        
        if ( evt.getSource() instanceof TableViewer ) {
            if ( evt.getPropertyName().equals("columnModel") ) {
                this.adjustColumnModelChanged((TableColumnModel)evt.getOldValue(), (TableColumnModel)evt.getNewValue());
            }
            if ( evt.getPropertyName().equals("rowHeight") ) {
                this.modelColumns.setHeight(((Integer)evt.getNewValue()).intValue());
                this.get("COLUMNMODEL").setHeight( ((Integer)evt.getNewValue()).intValue() );
            }
            
            if ( evt.getPropertyName().equals("model") ) {
                this.adjustModelChanged(null,table.getColumnModel());
            }
            
        }
        
    }//propertyChange()
    
    /**
     *  @RUS
     *    ������� ����� ������ � ������ "COLUMNMODEL" �� ������ ������ �������
     *    <code>columnModel</code> ������� {@link TableViewer}, � ������� ��� � RowTemplates,
     *    ������������� � ��� �������� �������� <code>columnModelTemplate 
     *    ������� <code>TableViewer</code>.
     *   @param oldM ���������� ������ ������� �������
     *   @param newM ����� ������ ������� �������  
     *  @ENDRUS
     */
    protected void adjustColumnModelChanged( TableColumnModel oldM, TableColumnModel newM) {
        
        RowTemplate rt = get("COLUMNMODEL");
        if ( oldM != null )
            oldM.removeColumnModelListener(rt);
        if ( newM != null )
            newM.removeColumnModelListener(rt);
        
        rt.clear();
        rt.createFrom(newM);
        adjustTemplateModelIndexes();
        table.setColumnModelTemplate(rt);
        newM.addColumnModelListener( rt );
        
    }
    
    /**
     * @RUS
     *    ������� ����� ������ ������� ������ ��� �������� {@link #modelColumns} � ����� ������
     *    �� ��������� � ������ "COLUMNMODEL".
     *    ������� ����� ������ � ������ "COLUMNMODEL" �� ������ ������ �������
     *    <code>columnModel</code> ������� {@link TableViewer}, � ������� ��� � RowTemplates,
     *    ������������� � ��� �������� �������� <code>columnModelTemplate 
     *    ������� <code>TableViewer</code>.
     *   @param oldM ���������� ������ ������� �������. ���� ����� �� {@link #propertyChange},
     *       �� �������� ��������� ����� <code>null</code>.
     *   @param newM ����� ������ ������� ������� ��� ����� ������ ������. 
     * @ENDRUS
     */
    protected void adjustModelChanged( TableColumnModel oldM, TableColumnModel newM) {
        
        this.modelColumns.clear();
        this.modelColumns.createFrom(newM);
        RowTemplate rt = get("COLUMNMODEL");
        if ( oldM != null )
            oldM.removeColumnModelListener(rt);
        if ( newM != null )
            newM.removeColumnModelListener(rt);
        rt.clear();
        rt.createFrom(newM);
        adjustTemplateModelIndexes();
        newM.addColumnModelListener( rt );
        table.setColumnModelTemplate(rt);
        table.refreshRowHeights(true);
    }
    
    /**
     * @RUS
     *  ��� ������� ������� ������� ������ � ������� �������� {@link TableCell} �������
     *  ������������� ����� �������� �������� <code>modelIndex</code>.
     *  ��� ���� ������������ ������ ������ ������ {@link #modelColumns} � ������� ��
     *  �������� �������� <code>identifier</code> ������������ ��������������� 
     *  <code>TableCell</code> � �� ���� ����������� ��������� <code>modelIndex</code>.
     * @ENDRUS
     */
    protected void adjustTemplateModelIndexes() {
        
        for ( RowTemplate rt : this.values() ) {
            ColumnViewList<TableCell> cvl = rt.getColumnList();
            for ( int i=0; i < cvl.size(); i++ ) {
                TableCell tc = cvl.get(i);
                int modelIndex = this.modelColumns.find(tc.getIdentifier()).getModelIndex();
                tc.setModelIndex(modelIndex);
            }
        }
    }
    
    
}//class
