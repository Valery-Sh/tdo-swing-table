/*
 * DataViewer.java
 *
 * Created on 27 Апрель 2007 г., 18:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import tdo.Table;
import tdo.event.ActiveRowEvent;
import tdo.event.ActiveRowListener;
import tdo.event.PendingEditingListener;
import tdo.impl.ValidateException;

/**
 *
 * Метод setColumnView(String columnList)
 * Элементы columnList имеют структуру: <p>
 * <i>имя-колонки</i>(размер)</i>
 * <i>размер</i> ::= <i>число</i> | <i>число%</i>
 */
public class DataViewer extends JScrollPane{
    
    protected TableViewer table;
    protected FreezeTableViewer freezeTable;
    
    TableSelectionHandler handler;
    DataTableEventHandler dataTableHandler;
    TableSelectionHandler freezeHandler;
    DataTableEventHandler freezeDataTableHandler;
    ActiveRowHandler activeRowHandler;
    ActiveRowHandler freezeActiveRowHandler;
    PropertyChangeListener propertyListener;
    PropertyChangeListener freezePropertyListener;

    
    
    /** Creates a new instance of DataViewer */
    public DataViewer() {
        installScrollPart();
    }
    
    private void installScrollPart() {
        table = new TableViewer();
        table.setUI( (DataViewerUI)DataViewerUI.createUI(table) );
        table.setEnterAsTab(false);
        
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        setViewportView(table);
        handler = new TableSelectionHandler(table);
        table.getSelectionModel().addListSelectionListener(handler);
    }
    
    protected void installFreezePart() {
        freezeTable = new FreezeTableViewer(this.table);
        freezeTable.setEnterAsTab( isEnterAsTab() );
        freezeTable.setUI( (DataViewerUI)DataViewerUI.createUI(freezeTable) );
        if ( getModel() != null  ) {
            //freezeTable.setModel(getModel());
            setFreezeModel(getModel());
            clearColumnModel(freezeTable);
        }
        freezeTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        setRowHeaderView( freezeTable );
        setCorner(JScrollPane.UPPER_LEFT_CORNER,freezeTable.getTableHeader());
        getRowHeader().setPreferredSize( new Dimension( 0, 0)  );

        
        propertyListener = new PropertyHandler(table,freezeTable);    
        table.addPropertyChangeListener(propertyListener);
        
        freezePropertyListener = new PropertyHandler(freezeTable,table);    
        freezeTable.addPropertyChangeListener(freezePropertyListener);

        for ( RowTemplate rt : table.getRowTemplates().values() ) {
            if ( rt == table.getColumnModelTemplate() )
                continue;
            ColumnLayoutManager clm = rt.getColumnLayout();
            if (clm instanceof FreezeCapableLayout ) {
                RowTemplate frt = new RowTemplate(freezeTable);
                frt.setHeight( rt.getHeight());
                FreezeCapableLayout fclm = (FreezeCapableLayout)clm;
                
                frt.setColumnLayout(fclm.createFreezeCellLayout(frt) );
                freezeTable.getRowTemplates().put("DUAL",frt);
                
            }
        }//for
        
    }
    
    protected TableColumnModel getFreezeColumnModel() {
        return this.freezeTable.getColumnModel();
    }
    
    protected void clearColumnModel(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        while ( columnModel.getColumnCount() != 0 ) {
            TableColumn tc = columnModel.getColumn(0);
            columnModel.removeColumn(tc);
        }
    }
    
    //=============================================================
    // Properties
    //=============================================================
    public TableModel getModel() {
        if ( this.table != null )
            return this.table.getModel();
        return null;
    }
    
    public void setModel(TableModel model) {
        if ( table.getModel() != null && table.getModel() instanceof Table && dataTableHandler != null ) {
            ((Table)table.getModel()).removePendingEditingListener(dataTableHandler);
            ((Table)table.getModel()).removeActiveRowListener(activeRowHandler);
        }
        if ( table.getSelectionModel() != null && handler != null )
            table.getSelectionModel().removeListSelectionListener(handler);
        
        table.setModel(model);
        
        if ( model instanceof Table ) {
            dataTableHandler = new DataTableEventHandler(table);
            ((Table)model).addPendingEditingListener(dataTableHandler);
            
            activeRowHandler = new ActiveRowHandler(table);
            ((Table)table.getModel()).addActiveRowListener(activeRowHandler);
        }
        handler = new TableSelectionHandler(table);
        table.getSelectionModel().addListSelectionListener(handler);
        
        table.resizeAndRepaint();
        
        setFreezeModel(model);
        
        
    }

    /**
     * Returns the height of a table row, in pixels.
     * The default row height is 16.0.
     *
     * @return  the height in pixels of a table row
     * @see     #setRowHeight
     */
    public int getRowHeight() {
        return this.getTableViewer().getRowHeight();
    }
    
    /**
     * Sets the height, in pixels, of all cells to <code>rowHeight</code>,
     * revalidates, and repaints.
     * The height of the cells will be equal to the row height minus
     * the row margin.
     *
     * @param   rowHeight                       new row height
     * @exception IllegalArgumentException      if <code>rowHeight</code> is
     *                                          less than 1
     * @see     #getRowHeight
     * @beaninfo
     *  bound: true
     *  description: The height of the specified row.
     */
    public void setRowHeight(int rowHeight) {
        this.getTableViewer().setRowHeight(rowHeight);
    }
    

    protected void setFreezeModel(TableModel model) {
        
        if ( freezeTable == null ) // usualy when no freezed column
            return;
        
        if ( freezeTable.getModel() != null && freezeTable.getModel() instanceof Table && freezeDataTableHandler != null ) {
            ((Table)table.getModel()).removePendingEditingListener(freezeDataTableHandler);
            ((Table)table.getModel()).removeActiveRowListener(freezeActiveRowHandler);
        }
        if ( freezeTable.getSelectionModel() != null && freezeHandler != null )
            freezeTable.getSelectionModel().removeListSelectionListener(freezeHandler);
        
        freezeTable.setModel(model);
        
        if ( model instanceof Table ) {
            freezeDataTableHandler = new DataTableEventHandler(freezeTable);
            ((Table)model).addPendingEditingListener(freezeDataTableHandler);
            
            freezeActiveRowHandler = new ActiveRowHandler(freezeTable);
            ((Table)freezeTable.getModel()).addActiveRowListener(freezeActiveRowHandler);
        }
        freezeHandler = new TableSelectionHandler(freezeTable);
        freezeTable.getSelectionModel().addListSelectionListener(freezeHandler);
        
        freezeTable.resizeAndRepaint();
        
    }
    protected ArrayList<Integer> freezedColumns;
    //=============================================================
    // Methods
    //=============================================================
    public void freezeColumn( int columnIndex ) {
        if ( this.freezeTable == null ) {
            installFreezePart();
            if ( freezedColumns == null )
                freezedColumns = new ArrayList(3);
        }
        //freezedColumns.add()
        
        this.freezeTable.addColumn( table.getColumnModel().getColumn(columnIndex));
        freezeTemplateCells(columnIndex);
        this.table.removeColumn(table.getColumnModel().getColumn(columnIndex));
        Dimension d = freezeTable.getPreferredSize();
        getRowHeader().setPreferredSize( d );
        table.getRowTemplates().doLayout();
        freezeTable.getRowTemplates().doLayout();
        
    }
    
    protected void freezeTemplateCells(int columnIndex) {
        Set<Entry<String,RowTemplate>> s = table.getRowTemplates().entrySet();        
        for ( Map.Entry me : s ) {
            RowTemplate rt = (RowTemplate)me.getValue();
            String key = (String)me.getKey();
            RowTemplate frt = freezeTable.getRowTemplates().getTemplate(key);
            ColumnLayoutManager clm = rt.getColumnLayout();
            FreezeCapableLayout fcl;
            if ( clm instanceof FreezeCapableLayout ) {
                fcl = (FreezeCapableLayout)clm;
                fcl.moveCells(frt,columnIndex);
            }
        }//for
    }

    public void unfreezeColumn( int columnIndex ) {
        unfreezeColumn(columnIndex,0);
    }

    public void unfreezeColumn( int columnIndex, int toColumnIndex ) {
        if ( freezeTable == null || freezeTable.getColumnCount() == 0) {
            return;
        }
        TableColumn tc = freezeTable.getColumnModel().getColumn(columnIndex);
        
        table.addColumn(tc);
        table.moveColumn(table.getColumnCount()-1, toColumnIndex);

        Set<Entry<String,RowTemplate>> s = freezeTable.getRowTemplates().entrySet();        
        for ( Map.Entry me : s ) {
            RowTemplate sourceTmpl = (RowTemplate)me.getValue(); // source is freeze part
            String key = (String)me.getKey();
            RowTemplate targetTmpl = table.getRowTemplates().getTemplate(key); // target is scroll part
            ColumnLayoutManager clm = sourceTmpl.getColumnLayout();
            FreezeCapableLayout fcl;
            if ( clm instanceof FreezeCapableLayout ) {
                fcl = (FreezeCapableLayout)clm;
                fcl.moveCells(targetTmpl,columnIndex);
            }
        }//for
        
        freezeTable.removeColumn(tc);
        Dimension d = freezeTable.getPreferredSize();
        getRowHeader().setPreferredSize( d );

        table.getRowTemplates().doLayout();
        freezeTable.getRowTemplates().doLayout();
        
    }
    
/*
  public void unfreezeColumn( int columnIndex ) {
        if ( freezeTable == null || freezeTable.getColumnCount() == 0) {
            return;
        }
        TableColumn tc = freezeTable.getColumnModel().getColumn(columnIndex);
        freezeTable.removeColumn(tc);
        table.addColumn(tc);
        table.moveColumn(table.getColumnCount()-1, 0);
        Dimension d = freezeTable.getPreferredSize();
        getRowHeader().setPreferredSize( d );
        
    }
*/    
    
/*    public void resizeAndRepaint() {
        this.freezeTable.resizeAndRepaint();
        this.table.resizeAndRepaint();
 
    }
 */
    public TableViewer getTableViewer() {
        return table;
    }

    /**
     * Specifies the number of clicks needed to start editing.
     *
     * @param count  an int specifying the number of clicks needed to start editing
     * @see #getClickCountToStart
     */
    public void setClickCountToStart(int count) {
        this.table.setClickCountToStart(count);
        if ( this.freezeTable != null )
            this.freezeTable.setClickCountToStart(count);
    }
    
    /**
     * Returns the number of clicks needed to start editing.
     * @return the number of clicks needed to start editing
     */
    public int getClickCountToStart() {
        return this.table.getClickCountToStart();
    }
    public boolean isEnterAsTab() {
        return table.isEnterAsTab();
    }
    
    public void setEnterAsTab(boolean enterAsTab) {
        table.setEnterAsTab(enterAsTab);
        if ( freezeTable != null )
            freezeTable.setEnterAsTab(enterAsTab);
    }
    
    protected class ViewportEventHandler implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
        }
        
    }//class ViewportEventHandler
    
    /**
     * Внутренний класс класса JListBinder. <p>
     * Служит обработчиком события ListSelectionEvent для класса JList.<p>
     * Реализует интерфейс ListSelectionListener, переопределяя его метод valueChanged. <P>
     * Производит попытку установить новый активный ряд для dataTable, для чего вызывает
     * метод moveTo класса PositionManager. Если попытка завершилась неудачно, т.е. метод
     * PositionManager.moveTo(...) вернул булевое значение false, то selectedIndex списка восстанавливается
     * на активную запись.
     */
    public class TableSelectionHandler implements ListSelectionListener {
        
        protected TableViewer table;
        
        public TableSelectionHandler(TableViewer table) {
            this.table = table;
        }
        /**
         * Реализация метода valueChanged интерфейса ListSelectionListener.<p>
         * Производит попытку установить новый активный ряд для dataTable, для чего вызывает
         * метод moveTo класса PositionManager. Если попытка завершилась неудачно, т.е. метод
         * PositionManager.moveTo(...) вернул булевое значение false, то selectedIndex списка восстанавливается
         * на активную запись.
         * @param e типа ListSelectionEvent.
         */
        public void valueChanged( ListSelectionEvent e ) {
            if ( table.getModel() != null && table.getModel() instanceof Table ) {
                Table dataTable = (Table)table.getModel();
                if ( dataTable == null )
                    return;
                
                int rowIndex = table.getSelectedRow();
                
                if ( rowIndex <0 || rowIndex > dataTable.getRowCount() )
                    return;
                
                
                if ( ! moveTo(rowIndex, dataTable) )  {
                    rowIndex = dataTable.getActiveRowIndex();
                    table.setRowSelectionInterval(rowIndex,rowIndex);
                }
                
            } else {
                TableModel model = table.getModel();
                if ( model == null )
                    return;
                
                int rowIndex = table.getSelectedRow();
                
//My 06.03.2012                if ( rowIndex <0 || rowIndex > model.getRowCount() )
//My 06.03.2012                    return;
//                    table.setRowSelectionInterval(rowIndex,rowIndex);
                
            }
            
        }
        
        /**
         * Устанавливает новое значение активного ряда объекта <code>BaseDataTable</code>. <p>
         *
         *
         * @param rowIndex  индекс нового активного ряда.
         * @return true, если метод {@link pdata.BaseDataTable#moveTo(int)} не выбросил
         * исключения. false - в противном случае.
         */
        public boolean moveTo( int rowIndex, Table dataTable ) {
            boolean result = true;
            if ( dataTable == null  )
                return false;
            try {
                //dataTable.moveTo(rowIndex);
                dataTable.setActiveRowIndex(rowIndex);
            } catch ( ValidateException ex) {
                result = false;
            } catch ( Exception ex) {
                result = false;
            }
            return result;
        }
        
    }//class ListSelectionHandler
    
    public class DataTableEventHandler implements PendingEditingListener{
        protected TableViewer table;
        public DataTableEventHandler(TableViewer table) {
            this.table = table;
            
        }
        public void stopPendingEditing() {
            //boolean b = true;
            TableCellEditor tce = table.getCellEditor();
            if ( tce != null )
                tce.stopCellEditing();
            //return b;
        }
    }//class DataTableEventHandler
    

    public class ActiveRowHandler implements ActiveRowListener{
        protected TableViewer table;
        
        public ActiveRowHandler(TableViewer table) {
            this.table = table;
        }
        
        public void activeRowChange(ActiveRowEvent e) {
            Table dataTable = (Table)table.getModel();
            //if ( dataTable == null || dataTable.isClosed() )
            if ( dataTable == null )
                return;
            int rowNo = dataTable.getActiveRowIndex();
            if ( rowNo < 0 )
                return;
            table.getSelectionModel().setSelectionInterval(rowNo,rowNo);
            
        }

        public void activeRowChanging(ActiveRowEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }//class DataTableEventHandler
    public class PropertyHandler implements PropertyChangeListener{
        protected TableViewer table;
        protected TableViewer freezeTable;
        
        public PropertyHandler(TableViewer scrollTable,TableViewer freezeTable) {
            this.table = scrollTable;
            this.freezeTable = freezeTable;
        }
        
        public void propertyChange(PropertyChangeEvent evt) {
            if ( evt.getPropertyName().equals("freezeActiveColumn") ) {
                if ( evt.getSource() instanceof TableViewer ) {
                    TableViewer sdv = (TableViewer)evt.getSource();
                    if ( sdv == table && freezeTable != null )
                        freezeTable.removeEditor();
                    else
                        if ( sdv == freezeTable )
                            table.removeEditor();
                }
            }
            
        }
    }//class DataTableEventHandler
    
}//class DataViewer
