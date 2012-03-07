/*
 * RowTemplate.java
 *
 * Created on 18 Апрель 2007 г., 20:02
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
 * @RUS Определяет шаблон подмножества рядов таблицы {@link tdo.swing.table.TableViewer}.
 * Различные ряды таблицы могут иметь различное представление при отображении.
 * Каждый ряд может использовать свой набор колонок из общего множества колонок,
 * определяемого <code>tableColumnModel</code>, устанавливаемого по умолчанию 
 * при установке свойчства <code>tableModel</code> . Одни и те же колонки могут
 * отображаться по разному в разных рядах.
 *  Важным свойством шаблона, являются свойства <code>height</height>.
 * Свойство <code>height</code> может быть задано явно, вызовом метода {@link #setHeight}
 * или установлено по умолчанию:
 * <OL>
 *   <LI>
 *      Если значение свойства <code>table</code> равно <code>null</code>, то 
 *      значение свойства <code>height</code> принимается равным 16.
 *   </LI>
 *   <LI>
 *      Если значение свойства <code>table</code> не равно <code>null</code>, то 
 *      значение свойства <code>height</code> принимается равным 
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
     * @RUS Коллекция колонок, т.е. объектов типа <code>TableCell</code>.
     */
    protected ColumnViewList<TableCell> columnList;
    
    /**
     * Объект класса, реализующнго интерфейс 
     * {@link tdo.swing.table.ColumnLayoutManager}.
     * @see #getColumnLayout
     * @see #setColumnLayout
     */
    protected ColumnLayoutManager columnLayout;
    
    /**
     * Тавлица типа {@link tdo.swing.table.TableViewer}, 
     * которая использует данный шаблон.
     */
    protected TableViewer table;

    /**
     * Высота рядов таблицы <code>TableViewer</code> для
     * которых используется шаблон.
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
     *  Создает список колонок для заданной
     *  модели колонок .
     *  @param fromModel модель колонок, используемая при списка 
     *  колонок шаблона.
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
     * Создает новый экземпляр типа {@link tdo.swing.tableTableCell}, 
     * копирует значения свойств параметра в созданный объект.
     * 
     * @param src исходный объект типа <code>TableColumn</code>, 
     * свойства которого назначаются результирующему объекту типа 
     * <code>TableCell</code>.
     * @return вновь созданный объект. Следующие свойства объекта, заданного 
     *    параметром <code>src</code> назначаются результату.
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
     * Дополнительно устанавливаются значения свойств, не определенных в классе
     * {@link javax.swing.table.TableColumn} :
     * <UL>
     *   <LI>
     *      <code>preferredHeight</code> равным <code>table.getRowHeight()</code>
     *      <code>maxHeight</code> равным <code>this.height</code>
     *      <code>minHeight</code> равным 0.
     *      <code>height</code> равным <code>this.height</code>
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
     * Создает новый экземпляр типа {@link tdo.swing.table.TableCell}, 
     * копирует значения свойств параметра в созданный объект.
     * 
     * @param src исходный объект типа <code>TableCell</code>, 
     * свойства которого назначаются результирующему объекту типа 
     * <code>TableCell</code>.
     * @return вновь созданный объект. Следующие свойства объекта, заданного 
     *    параметром <code>src</code> назначаются результату.
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
     *  Устанавливает размещение колонок для рядов таблицы соответствующих
     *  данному шаблону.
     *  @param layout устанавливаемый Layout Manager.
     *  @see #getColumnLayout
     * @ENDRUS
     */
    public void setColumnLayout(ColumnLayoutManager layout) {
        this.columnLayout = layout;
    }
    /**
     * @RUS
     *  Возвращает размещение колонок для рядов таблицы соответствующих
     *  данному шаблону.
     *  @return используемый Layout Manager.
     *  @see #setColumnLayout
     * @ENDRUS
     */
    public ColumnLayoutManager getColumnLayout() {
        return this.columnLayout;
    }
    
    /**
     * @RUS
     *  Выполняет размещение коллекции  колонок для рядов
     *  таблицы соответствующих данному шаблону.
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
     *  Устанавливает высоту рядов, связанных с данным шаблоном.
     *  После установки значения вызывается метод {@link #doLayout() }
     * @param height новое значение высоты рядов, связанных с данным шаблоном.
     * @ENDRUS
     */
    public void setHeight( int height ) {
        this.height = height;
        fireLayout();
    }
    
    /**
     * @RUS
     * Устанавливает высоту рядов, связанных с данным шаблоном.
     * В отличии от метода {@link #setHeight} метод {@link #doLayout() } НЕ вызывается.
     * @param height новое значение высоты рядов, связанных с данным шаблоном.
     * @ENDRUS
     */
    public void updateHeight( int height ) {
        this.height = height;
    }
    
    /**
     * @RUS
     *  @return Возвращает массив колонок, принадлежащих шаблону и таких, что при фиксировании
     *   колонки модели columnModel тавлицы с индексом <code>columnIndex</code> также
     *   становятися фиксированными.
     * @param columnIndex индекс колонки тавлицы. Индекс соответствует JTable.getColumnModel().
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
     * @return значение свойства <code>table</code>.
     */
    public TableViewer getTable() {
        return this.table;
    }
    
    /**
     * 
     * 
     * @param table таблица типа <code>TableViewer</code>.
     * @see #getTable
     * @ENDRUS 
     * @RUS Устанавливает ссылку на таблицу типа 
     * {@link TableViewer} для рядов которой используется 
     * данный шаблон.
     */
    public void setTable(TableViewer table) {
        this.table = table;
        if ( height < 0 )
            setHeight(table.getRowHeight());
    }
    
    /**
     * @return внутренний список колонок.
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
     * Возвращает колонку с заданным индексом.
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
     * @param column добавляемая колонка.
     * @return добавленная колонка.
     * @see #add(String)
     * @see #add(String,Object)
     * @see #add(String,Dimension,Object)
     * @ENDRUS 
     * @RUS Добавляет существующий объект типа <code>TableCell</code> к 
     *  коллекции {@link #columnList}.
     *  После добавления колонки вызывается метод {@link #fireLayout}.
     */
    public TableCell add(TableCell column) {
        columnList.add(column);
        fireLayout();
        return column;
    }
    
    /**
     * 
     * 
     * @param identifier идентификатор создаваемой колонки.
     * @return добавленная колонка.
     * @see #add(TableCell)
     * @see #add(String,Object)
     * @see #add(String,Dimension,Object)
     * @ENDRUS 
     * @RUS Создает новый экземпляр типа <code>TableCell</code>  с размерностью
     * 75 по ширине и 16 по высоте, с заданным значением свойства 
     * <code>identifier</code> и значением свойства <code>headerValue</code> 
     * равным значению <code>identifier</code>.
     *      После добавления колонки вызывается метод {@link #fireLayout}.
     */
    public TableCell add(String identifier) {
        return this.add(identifier, new Dimension(75,16),identifier);
    }

    /**
     * 
     * 
     * @param identifier идентификатор создаваемой колонки.
     * @param headerValue значение заголовка создаваемой колонки.
     * @return добавленная колонка.
     * @see #add(TableCell)
     * @see #add(String)
     * @see #add(String,Dimension,Object)
     * @ENDRUS 
     * @RUS Создает новый экземпляр типа <code>TableCell</code>  с размерностью
     * 75 по ширине и 16 по высоте, с заданными значениями свойств 
     * <code>identifier</code> и <code>headerValue</code> 
     *  После добавления колонки вызывается метод {@link #fireLayout}.
     */
    public TableCell add(String identifier,Object headerValue) {
        return this.add(identifier, new Dimension(75,16),headerValue);
    }

    /**
     * 
     * 
     * @param identifier идентификатор создаваемой колонки.
     * @param prefSize ширина и высота колонки как объект 
     *        <code>Dimension</code>.
     * @param headerValue значение заголовка создаваемой колонки.
     * @return добавленная колонка.
     * @see #add(TableCell)
     * @see #add(String)
     * @see #add(String,Object)
     * @ENDRUS 
     * @RUS Создает новый экземпляр типа <code>TableCell</code>  с 
     * заданной размерностью, с заданными значениями свойств 
     * <code>identifier</code> и <code>headerValue</code> 
     *  После добавления колонки вызывается метод {@link #fireLayout}.
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
     * Удаляет колонку с заданным идентификатором из коллекции.
     * Если коллекция колонок не содержит элемента с заданным идентификатором,
     * то возвращается значение <code>null</code>.
     * Если колонка удалена, то вызывается метод {@link #fireLayout}.
     * @param identifier идентификатор удаляемой колонки.
     * @return удаленная колонка, или <code>null</code>, если не найдена колонка
     *    с заданным идентификатором. 
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
     * Удаляет колонку с заданным индексом из коллекции.
     * Если коллекция колонок не содержит элемента с заданным идентификатором,
     * то возвращается значение <code>null</code>.
     * Если колонка удалена, то вызывается метод {@link #fireLayout} шавлона.
     * @param columnIndex индекс удаляемой колонки.
     * @return удаленная колонка, или <code>null</code>, если не найдена колонка
     *    с заданным индексом. 
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
     * Удаляет заданную колонку из коллекции.
     * Если коллекция колонок не содержит элемента с заданным идентификатором,
     * то возвращается значение <code>false</code>.
     * Если колонка удалена, то вызывается метод {@link #fireLayout} шавлона.
     * @param column удаляемая колонка.
     * @return true при успешном удалении. <code>false</code> - в противном
     *    случае.
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
     * Выполняет поиск колонки по заданному идентификатору и возвращает
     * ее как результат.
     * @param identifier идентификатор искомой колонки.
     * @return найденная колонка или <code>null</code> в случае неудачного
     *    поиска.
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
     * @RUS Приводит в соответствия значение свойства <code>modelIndex</code> каждой
     * колонки коллекции шаблона.
     * Для этого используется свойство <code>defaultTemplate</code> таблицы 
     * {@link tdo.swing.table.TableViewer}, хранящее экземпляр типа 
     * RowTemplate со списком колонок, полученных при назначении таблице модели
     * данных методом {@link tdo.swing.table.TableViewer#setModel} и не изменяемой в дальнейшем,
     * если только не меняктся модель данных.
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
     * Вызывает метод {@link RowTemplates#doLayout() }, выполняющего размещение колонок 
     * для всех шаблонов. 
     * Метод не производит никаких действий, если свойство {@link #table} равно <code>null</code>.
     * </RUS>
     */
    protected void fireLayout() {
        if ( table != null && table.getRowTemplates() != null )
            table.getRowTemplates().doLayout();
    }
    
    /**
     * <RUS>
     *   Определяет и возвращает индекс колонки для заданного ряда.
     *   Индекс колонки учитывает layout шаблона.
     *   @param p точка, для которой определяется колонка
     *   @param rowIndex индекс ряда.
     *   @return индекс колонки для данного шаблона.
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
     *   Определяет и возвращает индекс колонки по заданной точке на канве компонента.
     *   Индекс колонки вычисляется для шаблона с ключом "COLUMNMODEL".
     *   @param p точка, для которой определяется колонка
     *   @return индекс колонки для шаблона "COLUMNMODEL".
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
     *  Определяет и возвращает прямоугольник <code>Rectangle</code> для колонки коллекции данного 
     *  шаблона по заданным индексам ряда и колонки с учетом или без учета размера промежутка 
     *  между ячейками. 
     *  @param rowIndex индекс ряда, для которой проводится вычисление
     *  @param columnIndex индекс колонки, для которой проводится вычисление
     *  @param includeSpacing значение <code>true</code> учитываются промежутки между ячейками
     *           <code>false</code> - в противном случае.
     *  @return объект типа <code>Rectangle</code>.
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
     * @return максимальное значение ширины промежутка между колонками.
     * @return the maximum width for the <code>TableCell</code>
     * @see #setColumnMargin
     * @see #setColumnMargin
     * @ENDRUS Returns the width margin for <code>TableCell</code>.
     * The default <code>columnMargin</code> is 1.
     * @RUS Возвращает ширину промежутка для колонок.
     *  Значение <code>columnMargin</code> по умолчанию равно 1.
     */
    public int getColumnMargin() {
        return columnMargin;
    }
    
    /**
     * @RUS 
     *  Устанавливает новое значение ширины промежутка между колонками в пикселях.
     *  После установки нового значения, если новое и старое значения не совпадают
     *  вызывается метод {@link #fireLayout} а затем возбуждается событие 
     * <code>PropertyChangeEvent</code> для оповещения всех зарегистрированных обработчиков.
     * @param newMargin  новое значение ширины промежутка между колонками в пикселях.
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
     *  Создает и возвращает объект типа <code>Enumeration</code> для коллекции колонок
     *  шаблона.
     *  @return вновь созданный объект типа  <code>Enumeration</code>
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
     *  Возвращает индекс колонки в коллекции колонок шаблона по заданному
     *  идентификатору колонки ( свойство <code>identifier</code>). 
     *  @param identifier идентификатор требуемой колонки.
     *  @return значение -1, если колонка с заданным идентификатором не найдена
     *         или индекс найденной колонки.
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
     * Оповещает всех зарегистрированных обработчиков события <codePropertyChangeEvent</code>.
     * @param propertyName имя свойства, значение которого изменено
     * @param oldValue значения свойства до изменения
     * @param newValue значения свойства после изменения
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
     *  Регистрирует обработчик события <code>PropertyChangeEvent</code>.
     *  @param listener регистрируемый обработчик
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
     *  Удаляет обработчик события <code>PropertyChangeEvent</code> из списка зарегистрированных.
     *  @param listener удаляемый обработчик
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
     * @param e объект типа {@link TableColumnModelEvent}.
     * @ENDRUS 
     * @RUS Реализует метод интерфейса {@link TableColumnModelListener}.
     * Объект <code>RowTemplate</code> может быть зарегистрирован как
     * обработчик события TableColumnModelEvent. Метод вызывается при 
     * добавлении колонки к модели колонок <code>table.getColumnModel</code>.
     * При этом создается новый объект типа {@link TableCell} и добавляется 
     * к коллекции колонок шаблона.
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
     * @param e объект типа {@link TableColumnModelEvent}.
     * @ENDRUS 
     * @RUS Реализует метод интерфейса {@link TableColumnModelListener}.
     * Объект <code>RowTemplate</code> может быть зарегистрирован как
     * обработчик события TableColumnModelEvent. Метод вызывается при 
     * добавлении колонки к модели колонок <code>table.getColumnModel</code>.
     * При этом создается новый объект типа {@link TableCell} и добавляется 
     * к коллекции колонок шаблона.
     */    
    public void columnRemoved(TableColumnModelEvent e) {
        int columnIndex = e.getFromIndex();
        TableCell tc = columnList.get(columnIndex);
        remove(tc);
        fireLayout();
    }
    
    /**
     * @RUS
     * Реализует метод интерфейса {@link TableColumnModelListener}.
     * Объект <code>RowTemplate</code> может быть зарегистрирован как
     * обработчик события TableColumnModelEvent. Метод вызывается при 
     * удалении колонки таблицы. Всегда регистрируется шаблон с ключом
     * "COLUMNMODEL" как обработчик, что позволяет удалять колонку этого 
     * шаблона синхронно с удалением колонки модели
     * <code>table.getColumnModel()</code>  в данном методе.
     * После удаления вызывается метод {@link #fireLayout}.
     *
     * @param e событие типа {@link TableColumnModelEvent}.
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
     * Реализует метод интерфейса {@link TableColumnModelListener}.
     * Объект <code>RowTemplate</code> может быть зарегистрирован как
     * обработчик события <code>TableColumnModelEvent</code>. 
     * Всегда регистрируется шаблон с ключом "COLUMNMODEL" как обработчик, 
     * что позволяет изменить для такого шаблона значение свойства
     * {@link #columnMargin} при изменени в модели колонок 
     * <code>table.getColumnModel()<code>. 
     *
     * @param e объект события <code>ChangeEvent</code>.     
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
     * @return Возвращает объект типа <code>Iterator</code> для объектов типа
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
     * Реализует метод интерфейса {@link TableColumnModelListener}.
     * В данной реализации не выполняет никаких действий.
     * @param e объект события <code>ListSelectionEvent</code>.
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
