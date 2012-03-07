/*
 * RowTemplate.java
 *
 * Created on 18 ������ 2007 �., 20:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * 
 * 
 * @ENDRUS 
 * @RUS ���������� ������ ������������ ����� ������� {@link tdo.swing.table.TableViewer}.
 * ��������� ���� ������� ����� ����� ��������� ������������� ��� �����������.
 * ������ ��� ����� ������������ ���� ����� ������� �� ������ ��������� �������,
 * ������������� <code>tableColumnModel</code>, ���������������� �� ��������� 
 * ��� ��������� ��������� <code>tableModel</code> . ���� � �� �� ������� �����
 * ������������ �� ������� � ������ �����.
 *  ������ ��������� �������, �������� �������� <code>height</height>.
 * �������� <code>height</code> ����� ���� ������ ����, ������� ������ {@link #setHeight}
 * ��� ����������� �� ���������:
 * <OL>
 *   <LI>
 *      ���� �������� �������� <code>table</code> ����� <code>null</code>, �� 
 *      �������� �������� <code>height</code> ����������� ������ 16.
 *   </LI>
 *   <LI>
 *      ���� �������� �������� <code>table</code> �� ����� <code>null</code>, �� 
 *      �������� �������� <code>height</code> ����������� ������ 
 *      <code>table.getRowHeight()</code>.
 *   </LI>
 * </OL>
 */
public class RowTemplate implements TableColumnModelListener {
    
    /*** TableColumnModel interface ***/
    protected int columnMargin;
    
    protected List<PropertyChangeListener> propertyChangeListeners;
    /**
     * 
     * 
     * @see tdo.swing.table.ColumnViewList
     * @see #getColumnList
     * @ENDRUS 
     * @RUS ��������� �������, �.�. �������� ���� <code>TableCell</code>.
     */
    protected ColumnViewList<TableCell> columnList;
    
    /**
     * ������ ������, ������������ ��������� 
     * {@link tdo.swing.table.ColumnLayoutManager}.
     * @see #getColumnLayout
     * @see #setColumnLayout
     */
    protected ColumnLayoutManager columnLayout;
    
    /**
     * ������� ���� {@link tdo.swing.table.TableViewer}, 
     * ������� ���������� ������ ������.
     */
    protected TableViewer table;

    /**
     * ������ ����� ������� <code>TableViewer</code> ���
     * ������� ������������ ������.
     * 
     * @see #getHeight
     * @see setHeight
     */
    protected int height;
    
    
    /**
     * Creates a new instance of RowTemplate.
     * Property {@link #table} has <code>null</code> value and must be 
     * explicitly set later using {@link #setTable}.
     */
    
    public RowTemplate() {
        this(null,1);
    }
    
    /**
     * Creates a new instance of a RowTemplate for a given {@link TableViewer}
     * and capacity equal to 1.
     */
    public RowTemplate(TableViewer table) {
        this(table,1);
    }

    /**
     *  Creates a new instance of a RowTemplate for a given {@link TableViewer}
     * and capacity.
     *  This constructor creates internal column list  and sets property 
     * {@link #columnList}. If a value of the <code>table</code> parameter is null
     * then constructor sets {@link #height} property to 16. Otherwise, the value
     * is set to {@link tdo.swing.table.TableViewer.getRowHeight()}.
     * Then constructor creates an instance of the 
     * {@link tdo.swing.table.FlowColumnLayout} and assigns it's value to
     * the {@link #columnLayout} property.
     * The {@link #columnMargin} property is set to 1.
     */
    public RowTemplate(TableViewer table, int capacity) {
        this.table = table;
        columnList = new ColumnViewList(capacity);
        if ( table == null ) {
            height = 16; 
        }
        else {
            height = table.getRowHeight();
        }
        
        this.propertyChangeListeners = new ArrayList(2);
        
        this.setColumnLayout( new FlowColumnLayout(this) );
        setColumnMargin(1);
    }
    
    /**
     * @return true if the columnList collection is empty. false  otherwise.
     * @see #getColumnList
     */
    public boolean isEmpty() {
        return columnList.isEmpty();
    }
    
    /**
     * Removes all items from the columnList collection.
     * @see #getColumnList
     */
    public void clear() {
        this.columnList.clear();
    }
    /**
     * @RUS
     *  ������� ������ ������� ��� ��������
     *  ������ ������� .
     *  @param fromModel ������ �������, ������������ ��� ������ 
     *  ������� �������.
     * @ENDRUS
     */
    public void createFrom(TableColumnModel fromModel) {
        if ( fromModel == null || table == null )
            return;
        Enumeration<TableColumn> en = fromModel.getColumns();
        while ( en.hasMoreElements() ) {
            TableColumn tc = en.nextElement();
            TableCell tmex = createColumnFrom(tc);
            this.columnList.add(tmex);
        }
    }
    
    /**
     * ������� ����� ��������� ���� {@link tdo.swing.tableTableCell}, 
     * �������� �������� ������� ��������� � ��������� ������.
     * 
     * @param src �������� ������ ���� <code>TableColumn</code>, 
     * �������� �������� ����������� ��������������� ������� ���� 
     * <code>TableCell</code>.
     * @return ����� ��������� ������. ��������� �������� �������, ��������� 
     *    ���������� <code>src</code> ����������� ����������.
     * <UL>
     *   <LI>
     *      <code>identifier</code>
     *   </LI>
     *   <LI>
     *      <code>modelIndex</code>
     *   </LI>
     *   <LI>
     *      <code>minWidth</code> 
     *   </LI>
     *   <LI>
     *      <code>maxWidth</code> 
     *   </LI>
     *   <LI>
     *      <code>preferredWidth</code> 
     *   </LI>
     *   <LI>
     *      <code>width</code> 
     *   </LI>
     *   <LI>
     *      <code>resizable</code> 
     *   </LI>
     *   <LI>
     *      <code>cellEditor</code> 
     *   </LI>
     *   <LI>
     *      <code>cellRenderer</code> 
     *   </LI>
     *   <LI>
     *      <code>headerRenderer</code> 
     *   </LI>
     *   <LI>
     *      <code>headerValue</code> 
     *   </LI>
     * </UL>
     * ������������� ��������������� �������� �������, �� ������������ � ������
     * {@link javax.swing.table.TableColumn} :
     * <UL>
     *   <LI>
     *      <code>preferredHeight</code> ������ <code>table.getRowHeight()</code>
     *      <code>maxHeight</code> ������ <code>this.height</code>
     *      <code>minHeight</code> ������ 0.
     *      <code>height</code> ������ <code>this.height</code>
     *   </LI>
     * </UL>
     */
    public TableCell createColumnFrom( TableColumn src ) {
        TableCell r = new TableCell();
        
        String s = src.getIdentifier().toString();
        r.setIdentifier(s);
        
        r.setModelIndex(src.getModelIndex());
        
        r.setMinWidth(src.getMinWidth());
        r.setMaxWidth(src.getMaxWidth());
        r.setPreferredWidth( src.getPreferredWidth());
        r.setWidth( src.getWidth());
        
        r.setResizable(src.getResizable());
        r.setCellEditor(src.getCellEditor());
        r.setCellRenderer(src.getCellRenderer());
        r.setHeaderRenderer(src.getHeaderRenderer());
        r.setHeaderValue(src.getHeaderValue());
        
        //r.setPreferredHeight(this.table.getRowHeight());
        r.setPreferredHeight(this.height);        
        //r.setMaxHeight(this.table.getRowHeight());
        r.setMaxHeight(this.height);        
        r.setMinHeight(0);
        r.setHeight(this.height);
        
        //r.setOriginColumn( src );
        return r;
    }

    /**
     * ������� ����� ��������� ���� {@link tdo.swing.table.TableCell}, 
     * �������� �������� ������� ��������� � ��������� ������.
     * 
     * @param src �������� ������ ���� <code>TableCell</code>, 
     * �������� �������� ����������� ��������������� ������� ���� 
     * <code>TableCell</code>.
     * @return ����� ��������� ������. ��������� �������� �������, ��������� 
     *    ���������� <code>src</code> ����������� ����������.
     * <UL>
     *   <LI>
     *      <code>identifier</code>
     *   </LI>
     *   <LI>
     *      <code>modelIndex</code>
     *   </LI>
     *   <LI>
     *      <code>minWidth</code> 
     *   </LI>
     *   <LI>
     *      <code>maxWidth</code> 
     *   </LI>
     *   <LI>
     *      <code>preferredWidth</code> 
     *   </LI>
     *   <LI>
     *      <code>width</code> 
     *   </LI>
     *   <LI>
     *      <code>resizable</code> 
     *   </LI>
     *   <LI>
     *      <code>cellEditor</code> 
     *   </LI>
     *   <LI>
     *      <code>cellRenderer</code> 
     *   </LI>
     *   <LI>
     *      <code>headerRenderer</code> 
     *   </LI>
     *   <LI>
     *      <code>headerValue</code> 
     *   </LI>
     *   <LI>
     *      <code>preferredHeight</code>
     *   </LI>
     *   <LI>  
     *      <code>maxHeight</code>
     *   </LI>
     *   <LI>
     *      <code>minHeight</code>
     *   </LI>
     *   <LI>
     *      <code>height</code>
     *   </LI>
     * </UL>
     */
    public TableCell createColumnFrom( TableCell src ) {
        TableCell r = new TableCell();
        
        r.setIdentifier(src.getIdentifier());
        
        r.setModelIndex(src.getModelIndex());
        r.setPreferredHeight(src.getHeight());
        r.setMaxHeight(src.getMaxHeight());
        r.setMinHeight(src.getMinHeight());
        r.setHeight(src.getHeight());
        
        r.setMinWidth(src.getMinWidth());
        r.setMaxWidth(src.getMaxWidth());
        r.setPreferredWidth( src.getPreferredWidth());
        r.setWidth( src.getWidth());
        
        r.setResizable(src.getResizable());
        r.setCellEditor(src.getCellEditor());
        r.setCellRenderer(src.getCellRenderer());
        r.setHeaderRenderer(src.getHeaderRenderer());
        r.setHeaderValue(src.getHeaderValue());
       // r.setOriginColumn( src );
        
        return r;
    }
    
    /**
     * @RUS
     *  ������������� ���������� ������� ��� ����� ������� ���������������
     *  ������� �������.
     *  @param layout ��������������� Layout Manager.
     *  @see #getColumnLayout
     * @ENDRUS
     */
    public void setColumnLayout(ColumnLayoutManager layout) {
        this.columnLayout = layout;
    }
    /**
     * @RUS
     *  ���������� ���������� ������� ��� ����� ������� ���������������
     *  ������� �������.
     *  @return ������������ Layout Manager.
     *  @see #setColumnLayout
     * @ENDRUS
     */
    public ColumnLayoutManager getColumnLayout() {
        return this.columnLayout;
    }
    
    /**
     * @RUS
     *  ��������� ���������� ���������  ������� ��� �����
     *  ������� ��������������� ������� �������.
     * @ENDRUS
     */
    public void doLayout() {
//        int oldHeight = height;
        columnLayout.layoutTemplate(this);
        
/*        if ( oldHeight != height ) {
            // repeat lay out. Now layout manager will consider that the height is set explicitly.
            columnLayout.layoutTemplate(this);
        }
 */
    }
    
    public int getHeight() {
        return height;
    }
    /**
     * @RUS
     *  ������������� ������ �����, ��������� � ������ ��������.
     *  ����� ��������� �������� ���������� ����� {@link #doLayout() }
     * @param height ����� �������� ������ �����, ��������� � ������ ��������.
     * @ENDRUS
     */
    public void setHeight( int height ) {
        this.height = height;
        fireLayout();
    }
    
    /**
     * @RUS
     * ������������� ������ �����, ��������� � ������ ��������.
     * � ������� �� ������ {@link #setHeight} ����� {@link #doLayout() } �� ����������.
     * @param height ����� �������� ������ �����, ��������� � ������ ��������.
     * @ENDRUS
     */
    public void updateHeight( int height ) {
        this.height = height;
    }
    
    /**
     * @RUS
     *  @return ���������� ������ �������, ������������� ������� � �����, ��� ��� ������������
     *   ������� ������ columnModel ������� � �������� <code>columnIndex</code> �����
     *   ����������� ��������������.
     * @param columnIndex ������ ������� �������. ������ ������������� JTable.getColumnModel().
     * @ENDRUS
     */
    public TableCell[] getFixCapableColumns(int columnIndex) {
        if ( getColumnLayout() instanceof FreezeCapableLayout ) {
            FreezeCapableLayout fcl = (FreezeCapableLayout)getColumnLayout();
            return fcl.getFreezeCapableCells(columnIndex);
        } 
        return null;
    }

    /**
     * @return �������� �������� <code>table</code>.
     */
    public TableViewer getTable() {
        return this.table;
    }
    
    /**
     * 
     * 
     * @param table ������� ���� <code>TableViewer</code>.
     * @see #getTable
     * @ENDRUS 
     * @RUS ������������� ������ �� ������� ���� 
     * {@link TableViewer} ��� ����� ������� ������������ 
     * ������ ������.
     */
    public void setTable(TableViewer table) {
        this.table = table;
        if ( height < 0 )
            setHeight(table.getRowHeight());
    }
    
    /**
     * @return ���������� ������ �������.
     */
    protected ColumnViewList<TableCell> getColumnList() {
        return this.columnList;
    }
    
    //=======================================================
    // Methods  TableColumnModel
    //=======================================================
    
    
    /**
     * @RUS
     * @return the size of the <code>columnList</code> property value.
     * @see #getColumnList
     * @ENDRUS
     */
    public int getColumnCount() {
        return this.columnList.size();
    }
    
    /**
     * @RUS
     * ���������� ������� � �������� ��������.
     * @ENDRUS
     * Returns a column with the specified index.
     * @param columnIndex the index of a column in the columnList.
     * @return a column with the specified index.
     */
    public TableCell getColumn(int columnIndex) {
        return columnList.get(columnIndex);
    }
    
    /**
     * 
     * 
     * @param column ����������� �������.
     * @return ����������� �������.
     * @see #add(String)
     * @see #add(String,Object)
     * @see #add(String,Dimension,Object)
     * @ENDRUS 
     * @RUS ��������� ������������ ������ ���� <code>TableCell</code> � 
     *  ��������� {@link #columnList}.
     *  ����� ���������� ������� ���������� ����� {@link #fireLayout}.
     */
    public TableCell add(TableCell column) {
        columnList.add(column);
        fireLayout();
        return column;
    }
    
    /**
     * 
     * 
     * @param identifier ������������� ����������� �������.
     * @return ����������� �������.
     * @see #add(TableCell)
     * @see #add(String,Object)
     * @see #add(String,Dimension,Object)
     * @ENDRUS 
     * @RUS ������� ����� ��������� ���� <code>TableCell</code>  � ������������
     * 75 �� ������ � 16 �� ������, � �������� ��������� �������� 
     * <code>identifier</code> � ��������� �������� <code>headerValue</code> 
     * ������ �������� <code>identifier</code>.
     *      ����� ���������� ������� ���������� ����� {@link #fireLayout}.
     */
    public TableCell add(String identifier) {
        return this.add(identifier, new Dimension(75,16),identifier);
    }

    /**
     * 
     * 
     * @param identifier ������������� ����������� �������.
     * @param headerValue �������� ��������� ����������� �������.
     * @return ����������� �������.
     * @see #add(TableCell)
     * @see #add(String)
     * @see #add(String,Dimension,Object)
     * @ENDRUS 
     * @RUS ������� ����� ��������� ���� <code>TableCell</code>  � ������������
     * 75 �� ������ � 16 �� ������, � ��������� ���������� ������� 
     * <code>identifier</code> � <code>headerValue</code> 
     *  ����� ���������� ������� ���������� ����� {@link #fireLayout}.
     */
    public TableCell add(String identifier,Object headerValue) {
        return this.add(identifier, new Dimension(75,16),headerValue);
    }

    /**
     * 
     * 
     * @param identifier ������������� ����������� �������.
     * @param prefSize ������ � ������ ������� ��� ������ 
     *        <code>Dimension</code>.
     * @param headerValue �������� ��������� ����������� �������.
     * @return ����������� �������.
     * @see #add(TableCell)
     * @see #add(String)
     * @see #add(String,Object)
     * @ENDRUS 
     * @RUS ������� ����� ��������� ���� <code>TableCell</code>  � 
     * �������� ������������, � ��������� ���������� ������� 
     * <code>identifier</code> � <code>headerValue</code> 
     *  ����� ���������� ������� ���������� ����� {@link #fireLayout}.
     */
    public TableCell add(String identifier, Dimension prefSize, Object headerValue) {
        TableCell tc = new TableCell();
        tc.setIdentifier(identifier);
        tc.setPreferredHeight(prefSize.height);
        tc.setHeight(prefSize.height);
        tc.setMaxHeight(prefSize.height);
        tc.setMinHeight(0);
        
        tc.setPreferredWidth(prefSize.width);
        tc.setWidth(prefSize.width);
        tc.setMaxWidth(prefSize.width);
        tc.setMinWidth(0);
        
        tc.setHeaderValue(headerValue);

        this.add(tc);
        adjustTemplateModelIndexes();                
        return tc;
    }
    
    /**
     * @RUS
     * ������� ������� � �������� ��������������� �� ���������.
     * ���� ��������� ������� �� �������� �������� � �������� ���������������,
     * �� ������������ �������� <code>null</code>.
     * ���� ������� �������, �� ���������� ����� {@link #fireLayout}.
     * @param identifier ������������� ��������� �������.
     * @return ��������� �������, ��� <code>null</code>, ���� �� ������� �������
     *    � �������� ���������������. 
     * @ENDRUS
     */
    public TableCell remove(String identifier) {
        TableCell tc = find(identifier);
        if ( tc != null ) {
            columnList.remove(tc);
        } else 
            return null;
        fireLayout();
        return tc;
    }
    
    /**
     * @RUS
     * ������� ������� � �������� �������� �� ���������.
     * ���� ��������� ������� �� �������� �������� � �������� ���������������,
     * �� ������������ �������� <code>null</code>.
     * ���� ������� �������, �� ���������� ����� {@link #fireLayout} �������.
     * @param columnIndex ������ ��������� �������.
     * @return ��������� �������, ��� <code>null</code>, ���� �� ������� �������
     *    � �������� ��������. 
     * @ENDRUS
     */
    public TableCell remove(int columnIndex) {
        TableCell tc = columnList.remove(columnIndex);
        if ( tc == null )
            return null;
        fireLayout();
        return tc;
    }

    /**
     * @RUS
     * ������� �������� ������� �� ���������.
     * ���� ��������� ������� �� �������� �������� � �������� ���������������,
     * �� ������������ �������� <code>false</code>.
     * ���� ������� �������, �� ���������� ����� {@link #fireLayout} �������.
     * @param column ��������� �������.
     * @return true ��� �������� ��������. <code>false</code> - � ���������
     *    ������.
     * @ENDRUS
     */
    public boolean remove(TableCell column) {
        boolean b =  columnList.remove(column);
        if ( b )
          fireLayout();
        return b;
    }
    
    /**
     * @RUS
     * ��������� ����� ������� �� ��������� �������������� � ����������
     * �� ��� ���������.
     * @param identifier ������������� ������� �������.
     * @return ��������� ������� ��� <code>null</code> � ������ ����������
     *    ������.
     * @ENDRUS
     */
    public TableCell find(Object identifier) {
        TableCell f = null;
        for ( TableCell tc : columnList) {
            if ( tc.getIdentifier().equals(identifier) ) {
                f = tc;
                break;
            }
        }
        return f;
    }
    
    /**
     * 
     * 
     * @ENDRUS 
     * @RUS �������� � ������������ �������� �������� <code>modelIndex</code> ������
     * ������� ��������� �������.
     * ��� ����� ������������ �������� <code>defaultTemplate</code> ������� 
     * {@link tdo.swing.table.TableViewer}, �������� ��������� ���� 
     * RowTemplate �� ������� �������, ���������� ��� ���������� ������� ������
     * ������ ������� {@link tdo.swing.table.TableViewer#setModel} � �� ���������� � ����������,
     * ���� ������ �� �������� ������ ������.
     */
    protected void adjustTemplateModelIndexes() {
        
        for ( int i=0; i < columnList.size(); i++ ) {
            TableCell tc = columnList.get(i);
            int modelIndex = table.getModelColumns().find(tc.getIdentifier()).getModelIndex();
            tc.setModelIndex(modelIndex);
        }
    }
    
    /**
     * 
     * <RUS>
     * �������� ����� {@link RowTemplates#doLayout() }, ������������ ���������� ������� 
     * ��� ���� ��������. 
     * ����� �� ���������� ������� ��������, ���� �������� {@link #table} ����� <code>null</code>.
     * </RUS>
     */
    protected void fireLayout() {
        if ( table != null && table.getRowTemplates() != null )
            table.getRowTemplates().doLayout();
    }
    
    /**
     * <RUS>
     *   ���������� � ���������� ������ ������� ��� ��������� ����.
     *   ������ ������� ��������� layout �������.
     *   @param p �����, ��� ������� ������������ �������
     *   @param rowIndex ������ ����.
     *   @return ������ ������� ��� ������� �������.
     * </RUS>
     *
     */
    public int columnAtPoint(Point p, int rowIndex ) {
        int x = p.x;
        int y = p.y;
        
        if (x < 0 || y < 0 ) {
            return -1;
        }
        Rectangle r = table.getRowRect(rowIndex);
        int cc = getColumnCount();
        for(int i = 0; i < cc; i++) {
            TableCell tc = getColumn(i);
            Rectangle r1 = new Rectangle(tc.getX(),r.y + tc.getY(),tc.getWidth(),tc.getHeight());
            if ( r1.contains(p))
                return i;
        }
        return -1;
    }
    
    /**
     * @RUS
     *   ���������� � ���������� ������ ������� �� �������� ����� �� ����� ����������.
     *   ������ ������� ����������� ��� ������� � ������ "COLUMNMODEL".
     *   @param p �����, ��� ������� ������������ �������
     *   @return ������ ������� ��� ������� "COLUMNMODEL".
     * @ENDRUS
     *
     * @return the column index for the template with "COLUMNMODEL" key.
     * @param p the point
     */
    public int columnAtPoint(Point p) {
        int x = p.x;
        if( ! table.getComponentOrientation().isLeftToRight() ) {
            x = table.getWidth() - x;
        }
        return table.getColumnModel().getColumnIndexAtX(x);
    }
    
    /**
     * @RUS
     *  ���������� � ���������� ������������� <code>Rectangle</code> ��� ������� ��������� ������� 
     *  ������� �� �������� �������� ���� � ������� � ������ ��� ��� ����� ������� ���������� 
     *  ����� ��������. 
     *  @param rowIndex ������ ����, ��� ������� ���������� ����������
     *  @param columnIndex ������ �������, ��� ������� ���������� ����������
     *  @param includeSpacing �������� <code>true</code> ����������� ���������� ����� ��������
     *           <code>false</code> - � ��������� ������.
     *  @return ������ ���� <code>Rectangle</code>.
     * @ENDRUS
     *
     * @param rowIndex the index of a row of interest.
     * @param columnIndex the index of a column of interest.
     * @param includeSpacing if <code>true</code> takes into account spacings between cells.
     *
     * @return the object of type <code>Rectangle</code> for a given rowIndex and columnIndex.
     *   The result takes in account a value of the includeSpacing parameter.
     */
    public Rectangle getCellRect(int rowIndex,int columnIndex, boolean includeSpacing) {
        Rectangle r = table.getRowRect(rowIndex);
        TableCell tc = getColumn(columnIndex);
        int rm = 0;
        int cm = 0;
        if ( !includeSpacing) {
            // Bound the margins by their associated dimensions to prevent
            // returning bounds with negative dimensions.
            rm = Math.min(table.getRowMargin(), tc.getHeight() );
            cm = Math.min( getColumnMargin(), tc.getWidth());
            //int cm = Math.min(getColumnModel().getColumnMargin(), r.width);
            // This is not the same as grow(), it rounds differently.
            //r.setBounds(r.x + cm/2, r.y + rm/2, r.width - cm, r.height - rm);
            
        }
        
        return new Rectangle(tc.x + cm/2, tc.y + r.y + rm/2 ,tc.getWidth() - cm ,tc.getHeight() - rm);
        
    }
    
/*    public void addColumn(TableColumn aColumn) {
    }
    
    public void removeColumn(TableColumn column) {
    }
    
    public void moveColumn(int columnIndex, int newIndex) {
    }
*/    

    /**
     * 
     * 
     * @return ������������ �������� ������ ���������� ����� ���������.
     * @return the maximum width for the <code>TableCell</code>
     * @see #setColumnMargin
     * @see #setColumnMargin
     * @ENDRUS Returns the width margin for <code>TableCell</code>.
     * The default <code>columnMargin</code> is 1.
     * @RUS ���������� ������ ���������� ��� �������.
     *  �������� <code>columnMargin</code> �� ��������� ����� 1.
     */
    public int getColumnMargin() {
        return columnMargin;
    }
    
    /**
     * @RUS 
     *  ������������� ����� �������� ������ ���������� ����� ��������� � ��������.
     *  ����� ��������� ������ ��������, ���� ����� � ������ �������� �� ���������
     *  ���������� ����� {@link #fireLayout} � ����� ������������ ������� 
     * <code>PropertyChangeEvent</code> ��� ���������� ���� ������������������ ������������.
     * @param newMargin  ����� �������� ������ ���������� ����� ��������� � ��������.
     * @see #getColumnMargin
     * @ENDRUS
     * Sets the column margin to <code>newMargin</code>.  This method
     * also invokes method {@link #fireLayout} and then posts a <code>propertyChange</code> event to its
     * listeners.
     *
     * @param newMargin  the new margin width, in pixels
     * @see #getColumnMargin
     */
    public void setColumnMargin(int newMargin) {
        int oldMargin = newMargin;
        this.columnMargin = newMargin;
        if ( newMargin != oldMargin ) {
            fireLayout();
            firePropertyChange("columnMargin", oldMargin, newMargin);
        }
    }
    
    /**
     * @RUS
     *  ������� � ���������� ������ ���� <code>Enumeration</code> ��� ��������� �������
     *  �������.
     *  @return ����� ��������� ������ ����  <code>Enumeration</code>
     * @ENDRUS
     *
     *  Creates and returns the <code>Enumeration</code> object for the template
     *  column collection.
     *  @return the newly created <code>Enumeration</code> object 
     */
/*    public Enumeration<TableColumn> getColumns() {
        return new ColumnEnum();
    }
*/
    /**
     * @RUS
     *  ���������� ������ ������� � ��������� ������� ������� �� ���������
     *  �������������� ������� ( �������� <code>identifier</code>). 
     *  @param identifier ������������� ��������� �������.
     *  @return �������� -1, ���� ������� � �������� ��������������� �� �������
     *         ��� ������ ��������� �������.
     * @ENDRUS
     *  
     * Returns the column index for a givven column identifier.
     *  @param identifier the column identifier used to search.
     *  @return the value of -1, if a column doesn't exists otherwise the found column index.
     */
    public int getColumnIndex(Object identifier) {
        int result = -1;
        for ( int i=0; i < this.columnList.size(); i++ ) {
            if ( columnList.get(i).getIdentifier().equals(identifier) ) {
                result = i;
                break;
            }
        }//for
        return result;
    }

    /**
     * @RUS
     * ��������� ���� ������������������ ������������ ������� <codePropertyChangeEvent</code>.
     * @param propertyName ��� ��������, �������� �������� ��������
     * @param oldValue �������� �������� �� ���������
     * @param newValue �������� �������� ����� ���������
     * @ENDRUS
     *  
     * Create a <code>PropertyChangeEvent</code> and posts it to all its registered listeners.
     * @param propertyName the property name whose value has been changed.
     * @param oldValue the old property value
     * @param newValue the old property value
     * 
     */
    public void firePropertyChange( String propertyName, Object oldValue, Object newValue) {
        PropertyChangeEvent e = new PropertyChangeEvent(this, propertyName,oldValue,newValue);
        for ( PropertyChangeListener listener : propertyChangeListeners ) {
            listener.propertyChange(e);
        }
    }
    
    /**
     * @RUS
     *  ������������ ���������� ������� <code>PropertyChangeEvent</code>.
     *  @param listener �������������� ����������
     * @ENDRUS
     * 
     * Registers a listener for a <code>PropertyChangeEvent</code>
     * @param listener a listener to be registed.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeListeners.add(listener);
    }
    
    /**
     * @RUS
     *  ������� ���������� ������� <code>PropertyChangeEvent</code> �� ������ ������������������.
     *  @param listener ��������� ����������
     * @ENDRUS
     * 
     * Removes a listener for a <code>PropertyChangeEvent</code>
     * @param listener a listener to be removed.
     */
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeListeners.remove(listener);
    }
    
    // ******************************************
    // TableColumnModelListener implementation
    // ******************************************
    /**
     * 
     * 
     * @param e ������ ���� {@link TableColumnModelEvent}.
     * @ENDRUS 
     * @RUS ��������� ����� ���������� {@link TableColumnModelListener}.
     * ������ <code>RowTemplate</code> ����� ���� ��������������� ���
     * ���������� ������� TableColumnModelEvent. ����� ���������� ��� 
     * ���������� ������� � ������ ������� <code>table.getColumnModel</code>.
     * ��� ���� ��������� ����� ������ ���� {@link TableCell} � ����������� 
     * � ��������� ������� �������.
     */
    public void columnAdded(TableColumnModelEvent e) {
        int columnIndex = e.getToIndex();
        TableColumnModel tcm = (TableColumnModel)e.getSource();
        TableCell tc = this.createColumnFrom(tcm.getColumn(columnIndex));
        add(tc);
        fireLayout();
    }
  /**
     * 
     * 
     * @param e ������ ���� {@link TableColumnModelEvent}.
     * @ENDRUS 
     * @RUS ��������� ����� ���������� {@link TableColumnModelListener}.
     * ������ <code>RowTemplate</code> ����� ���� ��������������� ���
     * ���������� ������� TableColumnModelEvent. ����� ���������� ��� 
     * ���������� ������� � ������ ������� <code>table.getColumnModel</code>.
     * ��� ���� ��������� ����� ������ ���� {@link TableCell} � ����������� 
     * � ��������� ������� �������.
     */    
    public void columnRemoved(TableColumnModelEvent e) {
        int columnIndex = e.getFromIndex();
        TableCell tc = columnList.get(columnIndex);
        remove(tc);
        fireLayout();
    }
    
    /**
     * @RUS
     * ��������� ����� ���������� {@link TableColumnModelListener}.
     * ������ <code>RowTemplate</code> ����� ���� ��������������� ���
     * ���������� ������� TableColumnModelEvent. ����� ���������� ��� 
     * �������� ������� �������. ������ �������������� ������ � ������
     * "COLUMNMODEL" ��� ����������, ��� ��������� ������� ������� ����� 
     * ������� ��������� � ��������� ������� ������
     * <code>table.getColumnModel()</code>  � ������ ������.
     * ����� �������� ���������� ����� {@link #fireLayout}.
     *
     * @param e ������� ���� {@link TableColumnModelEvent}.
     * @ENDRUS
     */
    public void columnMoved(TableColumnModelEvent e) {
            
        int fromIndex = e.getFromIndex();
        int toIndex   = e.getToIndex();
        if ( fromIndex == toIndex )
            return;
        TableCell fromCol = columnList.get(fromIndex);
        TableCell toCol = columnList.get(toIndex);
        columnList.remove(fromIndex);
        //int toIndex1 = scrolledColumnList.indexOf(toCol);
        columnList.add(toIndex,fromCol);
        fireLayout();
        
    }

    /**
     * @RUS
     * ��������� ����� ���������� {@link TableColumnModelListener}.
     * ������ <code>RowTemplate</code> ����� ���� ��������������� ���
     * ���������� ������� <code>TableColumnModelEvent</code>. 
     * ������ �������������� ������ � ������ "COLUMNMODEL" ��� ����������, 
     * ��� ��������� �������� ��� ������ ������� �������� ��������
     * {@link #columnMargin} ��� �������� � ������ ������� 
     * <code>table.getColumnModel()<code>. 
     *
     * @param e ������ ������� <code>ChangeEvent</code>.     
     * @see #getColumnMargin
     * @see #setColumnMargin
     * @ENDRUS
     */
    public void columnMarginChanged(ChangeEvent e) {
        this.columnMargin = table.getColumnModel().getColumnMargin();
        fireLayout();
    }

    /**
     * 
     * 
     * @return ���������� ������ ���� <code>Iterator</code> ��� �������� ����
     * {@link TableCell}.
     * @return the iterator over the list of items of type {@link #TableCell}.
     * @ENDRUS 
     * @RUS 
     */
    public Iterator<TableCell> iterator() {
        return this.columnList.iterator();
    }
    /**
     * @RUS
     * ��������� ����� ���������� {@link TableColumnModelListener}.
     * � ������ ���������� �� ��������� ������� ��������.
     * @param e ������ ������� <code>ListSelectionEvent</code>.
     */
    public void columnSelectionChanged(ListSelectionEvent e) {
    }
    
    // ************************************************************************
    
/*    public class ColumnEnum<T extends TableColumn> implements Enumeration<TableColumn> {
        Iterator<TableCell> it;
        public ColumnEnum() {
            it = RowTemplate.this.columnList.iterator();
        }
        
        public boolean hasMoreElements() {
            return it.hasNext();
        }
        
        public TableColumn nextElement() {
            return it.next();
        }
        
    }
 */
}//class RowTemplate
