/*
 * @(#)TableViewer.java	1.287 06/08/08
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package tdo.swing.table;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import tdo.DataColumn;
import tdo.DataTable;
import tdo.swing.util.DataUtil;


/**
 * @RUS
 *   <code>TableViewer</code> используется для отображения табличных данных
 *  в виде последовательности рядов и колонок. <p>
 *
 *  <b>Сравнение с компонентом javax.swing.JTable</b> <p>
 *
 *  Класс наследует от стандартного компонента {@link javax.swing.JTable} и
 *  использует многие его свойства и методы. Компонент JTable также состоит из
 *  последовательности рядов. При этом, i-й ряд для размещения данных использует
 *  один и тот же набор колонок, определяемый свойством <ode>columnModel</code>,
 *  что и j-й ряд. Это означает, что колонки каждого ряда следуют одна за другой
 *  вдоль оси X и колонки с одинаковым индексом имеют одну и туже ширину во
 *  всех рядах. Использованиеv программных трюков с создание специализированных
 *  компонент, реализующих интерфейс <code>CellRenderer</code> можно изменить
 *  такое поведение. Однако, чаще всего это не будут повторно используемые
 *  объекты. <p>
 *
 *  Компонент  <code>TableViewer</code> использует другой подход. Сохраняется
 *  модель колонок <code>columnModel</code> но ее функциональность используется
 *  лишь частично. С точки зрения <code>TableViewer</code> columnModel - это
 *  элемент разметки тавлицы по горизонтали. В <code>JTable</code> разметка по
 *  вертикали определяется свойством <code>rowModel</code>, которое, к
 *  сожалению объявлено с модификаторм <code>private</code>.
 *  В <code>TableViewer</code> свойство заменено на {@link #rowHeights} и имеет
 *  тот же тип, и, частично, назначение и функциональность.
 *  Суммарная ширина по всем колонкам  как и раньше - ширина компонента
 *  <code>TableViewer</code>, но для отображения рядов используются другие
 *  объекты. Для описания группы рядов используется понятие
 *  <i>шаблона ряда</i> ( row template ) и реализуется
 *  шаблон ряда как класс {@link tdo.swing.table.RowTemplate). Шаблон ряда
 *  содержит коллекцию элементов типа {@link tdo.swing.table.TableCell}.
 *  Элемент <code>TableCell</code> связан с моделью данных свойствами
 *  <code>modelIndex</code> и <code>identifier</code> и содержит всю
 *  необходимую информацию для отображения. В частности, координаты x,y
 *  относительно ряда таблицы, свойства  <code>width</code> и
 *  <code>height</code>. А, поскольку <code>TableCell</code> наследует
 *  от <code>TableColumn</code>, то и другие, необходимые для отображения
 *  свойства также доступны. Специально отметим, что <code>TableCell</code>,
 *  входяший в шаблон может служить для отображение и редактирования любой
 *  колонки модели данной, а не только той, которая содержится в
 *  <code>columnModel</code>. Когда ряд с индексом, например, <b>i</>,
 *  выбирается для отображения, то вызывается метод
 *  <code>getRowTemplate(i)</code> (см. {@link #getRowTemplate} )
 *  задачей которого является определить
 *  требуемый этому ряду шаблон. Дело в том, что все шаблоны
 *  <code>RowTemplate</code> размещаются в коллекции {@link RowTemplates}.
 *  Эта коллекция является расширением <code>java.util.HashMap</code> и каждый
 *  шаблон регистрируется в коллекции с помощью строкового ключа (key).
 *  Метод <code>getRowTemplate</code> возбуждает событие
 * {@link RowTemplateRequestEvent}. Приложение может зарегистрировать
 *  обработчик типа {@link RowTemplateRequestListener} этого события. Задачей
 *  обработчика является определить, зная индекс ряда, ключ в коллекции
 *  <code>RowTemplates</code> и возвратить его методу
 *  <code>getRowTemplate</code>. По ключу метод получит
 *  <code>RowTemplate</code>.<p>
 *
 *  Коллекция шаблонов создается автоматически при создании таблицы
 *  <code>TableViewer</code>, а шаблоны рядов могут быть подготовлены заранее
 *  без какой либо привязки к таблице <code>TableViewer</code>, а после ее
 *  создания  помещаются в <code>RowTemplates</code>  с определяемыми
 *  приложением ключами. Приложение, в свою очередь, знает как по индексу ряда
 *  определить ключ необходимого шаблона.<p>
 *
 *  Когда в <code>TableViewer</code> создается <code>RowTemplates</code>, то
 *  сразу же создается шаблон по умолчанию и помещается в коллекцию с ключом
 *  "COLUMNMODEL". Этот шаблон по составу элементов, их порядку и другим
 *  свойствам точно соответствует модели колонок <code>columnModel</code> и,
 *  при любом изменении последней, также меняется. Если такой шаблон
 *  применяется для отображения некоторого ряда, то  ряд отображается точно
 *  также, как если бы мы имели дело с <code>JTable</code>. <p>
 *  Шаблон по умолчанию используется  во всех случаях когда вовсе не
 *  назначен обработчик события <code>RowTemplateRequestEvent</code> или
 *  для конкретного ряда обработчик вернул значение <code>null</code>.
 *  Практически для разработчика приложения не требуется никаких дополнительных
 *  усилий при применеии <code>TableViewer</code> по сравнению с использованием
 *  <code>JTable</code>. <p>
 *
 *  Каждый шаблон <code>RowTemplate</code> имеет свойство
 *  {@link RowTemplate#columnLayout} типа {@link ColumnLayoutManager}.
 *  <code>ColumnLayoutmanager</code> для шаблона имеет такое же назначение, как
 *  <code>java.awt.LayoutManager</code> для <code>java.awt.Container</code>.
 *  Шаблон по умолчанию всегда имеет тип класса {@link ColumnModelLayout},
 *  специально разработанный для такого шаблона таким образом, чтобы
 *  синхронизировать <code>TableCell</code> с <code>TableColumn</code> из
 *  <code>columnModel</code>.<p>
 *
 *  Для других шаблонов, по умолчанию используется шаблон типа
 * {@link FlowColumnLayout}, который размещает ячейки ряда таким образом, что
 *  они отображаются последовательно друг за другом согдасно значениям
 *  свойств <code>preferredWidth</code> элементов <code>TableCell</code>.
 *  Любой другой объект класса, реализующего интерфейс
 *  <code>ColumnLayoutmanager</code> может быть использован для таких шаблонов.
 *
 *
 *
 *
 * @ENDRUS
 */
public class TableViewer extends JTable {
    public static final int VIEW_SCROLL = 0;
    public static final int VIEW_FIXED = 1;
    public static final int VIEW_SHARE = 2; // for future
    
    protected int viewKind;
    
    /**
     * Когда  ряды таблицы могут иметь разную высоту, используется это свойство.<p>
     * Если ряды могут быть разной высоты, то следует использовать
     * <code>rowHeight</code>
     * @see #getRowHeights
     * @see #setRowHeights
     * @see javax.swing.JTable.getRowHeigh
     */
    protected SizeSequence rowHeights = null;
    
    /**
     * Если равен <code>true</code>, то метод {@link #resizeAndRepaint} действительно
     * проводит revalidate and repaint.
     */
    protected boolean repaintNeeded;
    
    protected boolean enterAsTab;
    
    protected RowTemplate columnModelTemplate;
    
    protected boolean rowSelectionAdjusting;
    
    protected PropertyChangeListener editorRemover = null;
    
    protected int activeColumn;

    /**
     * An integer specifying the number of clicks needed to start editing.
     * Even if <code>clickCountToStart</code> is defined as zero, it
     * will not initiate until a click occurs.
     */
    protected int clickCountToStart = 2;
    
    protected RowTemplateRequestListener rowTemplateRequestListener;
    
    protected RowTemplateRequestEvent rowTemplateRequestEvent = new RowTemplateRequestEvent(this);
    
    /**
     * A table of objects that display and edit the contents of a cell,
     * indexed by class as declared in <code>getColumnClass</code>
     * in the <code>TableModel</code> witch is the instance of the <code>DataTable</code>.
     */
    //transient protected Hashtable dataTableEditorsByColumnClass;
    
/*    private List<Integer> fixedColumnList;
 
    public List<Integer> getFixedColumnList() {
        return this.fixedColumnList;
    }
 */
    
    //*******************************************************************]
    
    /**
     *
     *
     * @ENDRUS
     * @RUS Хранит значение свойства типа {@link tdo.swing.RowTemplateTemplate}, 
     * определяющего шаблоны различных рядов.
     */
    protected RowTemplates rowTemplates;
    
    /**
     * @RUS
     *  Значение типа {@link tdo.data.DataTable}, если модель ( TableModel ) 
     *  имеет такой тип или  <code>null</code>.
     * @ENDRUS
     */
    protected DataTable dataTable;
    //
    // Constructors
    //
    
    /**
     * Constructs a default <code>TableViewer</code> that is initialized with a
     * default data model, a default column model, and a default selection
     * model.
     *
     *
     *
     *
     *
     *
     * @see #createDefaultDataModel
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public TableViewer() {
        this(null, null, null);
    }
    
    /**
     * Constructs a <code>TableViewer</code> that is initialized with
     * <code>dm</code> as the data model, a default column model,
     * and a default selection model.
     *
     * @param dm        the data model for the table
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public TableViewer(TableModel dm) {
        this(dm, null, null);
    }
    
    /**
     * Constructs a <code>TableViewer</code> that is initialized with
     * <code>dm</code> as the data model, <code>cm</code>
     * as the column model, and a default selection model.
     *
     * @param dm        the data model for the table
     * @param cm        the column model for the table
     * @see #createDefaultSelectionModel
     */
    public TableViewer(TableModel dm, TableColumnModel cm) {
        this(dm, cm, null);
    }
    
    /**
     * Constructs a <code>TableViewer</code> that is initialized with
     * <code>dm</code> as the data model, <code>cm</code> as the
     * column model, and <code>sm</code> as the selection model.
     * If any of the parameters are <code>null</code> this method
     * will initialize the table with the corresponding default model.
     * The <code>autoCreateColumnsFromModel</code> flag is set to false
     * if <code>cm</code> is non-null, otherwise it is set to true
     * and the column model is populated with suitable
     * <code>TableColumns</code> for the columns in <code>dm</code>.
  
     *
     * @param dm        the data model for the table
     * @param cm        the column model for the table
     * @param sm        the row selection model for the table
     * @see #createDefaultDataModel
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public TableViewer(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm,cm,sm);
        initViewer();
/*        this.rowTemplates = new RowTemplates(this);
        this.addPropertyChangeListener("model",rowTemplates);
        this.addPropertyChangeListener("columnModel",rowTemplates);
        this.addPropertyChangeListener("rowHeight",rowTemplates);
 
        firePropertyChange("columnModel", null, getColumnModel() );
        firePropertyChange("rowHeight", 16, getRowHeight());
 
        this.viewKind = VIEW_SCROLL;
        this.enterAsTab = true;
        this.activeColumn = -1;
 */
        
/*        setLayout(null);
 
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                           JComponent.getManagingFocusForwardTraversalKeys());
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                           JComponent.getManagingFocusBackwardTraversalKeys());
        if (cm == null) {
            cm = createDefaultColumnModel();
            autoCreateColumnsFromModel = true;
        }
        setColumnModel(cm);
 
        if (sm == null) {
            sm = createDefaultSelectionModel();
        }
        setSelectionModel(sm);
 
    // Set the model last, that way if the autoCreatColumnsFromModel has
    // been set above, we will automatically populate an empty columnModel
    // with suitable columns for the new model.
        if (dm == null) {
            dm = createDefaultDataModel();
        }
        setModel(dm);
 
        initializeLocalVars();
        updateUI();
 */
    }
    
    public TableViewer( TableViewer viewer) {
        super();
    }
    
    protected void initViewer() {
        this.rowTemplates = new RowTemplates(this);
        this.addPropertyChangeListener("model",rowTemplates);
        this.addPropertyChangeListener("columnModel",rowTemplates);
        this.addPropertyChangeListener("rowHeight",rowTemplates);
        
        firePropertyChange("columnModel", null, getColumnModel() );
        firePropertyChange("rowHeight", 16, getRowHeight());
        
        this.viewKind = VIEW_SCROLL;
        this.enterAsTab = true;
        this.activeColumn = -1;
    }
    /**
     * Constructs a <code>TableViewer</code> with <code>numRows</code>
     * and <code>numColumns</code> of empty cells using
     * <code>DefaultTableModel</code>.  The columns will have
     * names of the form "A", "B", "C", etc.
     *
     *
     * @param numRows           the number of rows the table holds
     * @param numColumns        the number of columns the table holds
     * @see javax.swing.table.DefaultTableModel
     */
    public TableViewer(int numRows, int numColumns) {
        this(new DefaultTableModel(numRows, numColumns));
    }
    
    /**
     * Constructs a <code>TableViewer</code> to display the values in the
     * <code>Vector</code> of <code>Vectors</code>, <code>rowData</code>,
     * with column names, <code>columnNames</code>.  The
     * <code>Vectors</code> contained in <code>rowData</code>
     * should contain the values for that row. In other words,
     * the value of the cell at row 1, column 5 can be obtained
     * with the following code:
     * <p>
     * <pre>((Vector)rowData.elementAt(1)).elementAt(5);</pre>
     * <p>
     *
     *
     * @param rowData           the data for the new table
     * @param columnNames       names of each column
     */
    public TableViewer(Vector rowData, Vector columnNames) {
        super(rowData,columnNames);
        //this(new DefaultTableModel(rowData, columnNames));
    }
    
    /**
     * Constructs a <code>TableViewer</code> to display the values in the two dimensional array,
     * <code>rowData</code>, with column names, <code>columnNames</code>.
     * <code>rowData</code> is an array of rows, so the value of the cell at row 1,
     * column 5 can be obtained with the following code:
     * <p>
     * <pre> rowData[1][5]; </pre>
     * <p>
     * All rows must be of the same length as <code>columnNames</code>.
     * <p>
     *
     *
     * @param rowData           the data for the new table
     * @param columnNames       names of each column
     */
    public TableViewer(final Object[][] rowData, final Object[] columnNames) {
        super(rowData,columnNames);
/*        this(new AbstractTableModel() {
            public String getColumnName(int column) { return columnNames[column].toString(); }
            public int getRowCount() { return rowData.length; }
            public int getColumnCount() { return columnNames.length; }
            public Object getValueAt(int row, int col) { return rowData[row][col]; }
            public boolean isCellEditable(int row, int column) { return true; }
            public void setValueAt(Object value, int row, int col) {
                rowData[row][col] = value;
                fireTableCellUpdated(row, col);
            }
        });
 */
    }
    
    public void addRowTemplateRequestListener(RowTemplateRequestListener listener) {
        this.rowTemplateRequestListener = listener;
    }
    
    public void removeRowTemplateRequestListener(RowTemplateRequestListener listener) {
        this.rowTemplateRequestListener = null;
    }
    
    /**
     * @param point проверяемая точка.
     * @param rowIndex индекс ряда, соответствующий проверяемой точке.
     * @param point   the location of interest
     * @param rowIndex  the index of the row.
     * @return Возвращает индекс колонки, внутри границ которой лежит заданная точка или -1,
     *  если такая колонка не существует.
     * @return the index of the column that <code>point</code> lies in,
     * 		or -1 if such column doen't exist.
     * @see #rowAtPoint
     * @see #getRowTemplates
     * @see #rowAtPoint
     * @see #getRowTemoplates
     * @ENDRUS Returns the index of the column that <code>point</code> lies in,
     * or -1 if such column doen't exist. Since column layout inside a row calculates
     * dynamically the second parameter defines row index.
     * @RUS Возвращает индекс колонки заданного ряда, внутри границ которой лежит заданная точка.
     *  Если такая колонка не существует, то возвращается значение -1.
     */
    
    public int columnAtPoint(Point p, int rowIndex ) {
        RowTemplate rt = this.getRowTemplate(rowIndex);
        return rt.columnAtPoint(p,rowIndex);
    }
    
    /**
     *
     *
     *
     * @param row     the row to be edited
     * @param column  the column to be edited
     * @param e       event to pass into <code>shouldSelectCell</code>;
     *                  note that as of Java 2 platform v1.2, the call to
     *                  <code>shouldSelectCell</code> is no longer made
     * @return false if for any reason the cell cannot be edited,
     *                or if the indices are invalid
     * @ENDRUS Programmatically starts editing the cell at <code>row</code> and
     * <code>column</code>, if those indices are in the valid range, and
     * the cell at those indices is editable.
     * To prevent the <code>VTable</code> from
     * editing a particular table, column or cell value, return false from
     * the <code>isCellEditable</code> method in the <code>TableModel</code>
     * interface.
     * <p>The method is overriden since column layout inside a row calculates dynamically using
     *      {@link tdo.swing.table.VRowTemplates}.
     * Internally the method defines row template for the row and then invokes
     * <code<columnAtPoint(VRowTemplate,int,column,EventObject)</code>.
     * @RUS
     */
/*    public boolean editCellAt(int row, int column, EventObject e){
        RowTemplate rt = getRowTemplate(row);
        return editCellAt( rt,row,column,e);
    }
 */
    
    /**
     * Programmatically starts editing the cell at <code>row</code> and
     * <code>columnIndex</code> with known RowTemplate, if those indices are in the valid
     * range, and the cell at those indices is editable.
     * To prevent the <code>TableViewer</code> from
     * editing a particular table, column or cell value, return false from
     * the <code>isCellEditable</code> method in the <code>TableModel</code>
     * interface.
     *
     *
     * @param rt the template for the row
     * @param row     the row to be edited
     * @param columnIndex  the column to be edited
     * @param e       event to pass into <code>shouldSelectCell</code>;
     *                  note that as of Java 2 platform v1.2, the call to
     *                  <code>shouldSelectCell</code> is no longer made
     * @return false if for any reason the cell cannot be edited,
     *                or if the indices are invalid
     */
    public boolean editCellAt(RowTemplate rt, int rowIndex,int columnIndex, EventObject e){
        if (cellEditor != null && !cellEditor.stopCellEditing()) {
            return false;
        }
        
        if (rt == null || rt.isEmpty() ||
                rowIndex < 0 || rowIndex >= getRowCount() ||
                columnIndex < 0 || columnIndex >= rt.getColumnCount()) {
            return false;
        }
        
        if (!isCellEditable(rt, rowIndex, columnIndex))
            return false;
        
        if (editorRemover == null) {
            KeyboardFocusManager fm =
                    KeyboardFocusManager.getCurrentKeyboardFocusManager();
            editorRemover = new CellEditorRemover(fm);
            fm.addPropertyChangeListener("permanentFocusOwner", editorRemover);
        }
        
        TableCellEditor editor = getCellEditor(rt, rowIndex, columnIndex);
        if (editor != null && editor.isCellEditable(e)) {
            editorComp = prepareEditor(editor, rt,rowIndex, columnIndex);
            if (editorComp == null) {
                removeEditor();
                return false;
            }
            editorComp.setBounds(rt.getCellRect(rowIndex, columnIndex, false));
            add(editorComp);
            editorComp.validate();
            editorComp.repaint();
            
            setCellEditor(editor);
            setEditingRow(rowIndex);
            setEditingColumn(columnIndex);
            editor.addCellEditorListener(this);
            
            return true;
        }
        return false;
    }
    
    /**
     * Prepares the renderer by querying the data model for the
     * value and selection state
     * of the cell at <code>row</code>, <code>column</code>.
     * Returns the component (may be a <code>Component</code>
     * or a <code>JComponent</code>) under the event location.
     * <p>
     * During a printing operation, this method will configure the
     * renderer without indicating selection or focus, to prevent
     * them from appearing in the printed output. To do other
     * customizations based on whether or not the table is being
     * printed, you can check the value of
     * {@link javax.swing.JComponent#isPaintingForPrint()}, either here
     * or within custom renderers.
     * <p>
     * <b>Note:</b>
     * Throughout the table package, the internal implementations always
     * use this method to prepare renderers so that this default behavior
     * can be safely overridden by a subclass.
     *
     * @param renderer  the <code>TableCellRenderer</code> to prepare
     * @param row       the row of the cell to render, where 0 is the first row
     * @param column    the column of the cell to render,
     *			where 0 is the first column
     * @return          the <code>Component</code> under the event location
     */
    public Component prepareViewRenderer(TableCellRenderer renderer, int row, int column) {
        
        Object value = getValue(row, column);
        
        boolean isSelected = false;
        boolean hasFocus = false;
        
        // Only indicate the selection and focused cell if not printing
        if ( row == 2 && column == 2)
            System.out.println("ttttt");
        boolean rowIsLead =
                (selectionModel.getLeadSelectionIndex() == row);
        if (!isPaintingForPrint()) {
            //isSelected = (column == activeColumn && row == selectionModel.getMinSelectionIndex() );
            isSelected = selectionModel.isSelectedIndex(row);
            boolean colIsLead = (column == activeColumn ) && isSelected;
            
            //hasFocus = (rowIsLead && colIsLead) && isFocusOwner();
            hasFocus = (rowIsLead && colIsLead);            
            //hasFocus = true;
        }
        
        return renderer.getTableCellRendererComponent(this, value,
                isSelected, hasFocus,
                row, column);
    }
    
    /**
     * Prepares the editor by querying the data model for the value and
     * selection state of the cell at <code>row</code>, <code>column</code>.
     * <p>
     * <b>Note:</b>
     * Throughout the table package, the internal implementations always
     * use this method to prepare editors so that this default behavior
     * can be safely overridden by a subclass.
     *
     * @param editor  the <code>TableCellEditor</code> to set up
     * @param rt 
     * @param row     the row of the cell to edit,
     *		      where 0 is the first row
     * @param column  the column of the cell to edit,
     *		      where 0 is the first column
     * @return the <code>Component</code> being edited
     */
    public Component prepareEditor(TableCellEditor editor, RowTemplate rt, int row, int column) {
        int modelIndex = rt.getColumn(column).getModelIndex();
        Object value = getValue(row, column);
        boolean isColumnActive = getSelectionModel().isSelectedIndex(row) && column == activeColumn;
        Component c = editor.getTableCellEditorComponent(this, value, isColumnActive,
                row, column);
        return c;
    }
    
    @Override
    public void removeEditor() {
        int editedColumn = editingColumn;
        int erow = this.getEditingRow();
        TableCellEditor editor = getCellEditor();

        super.removeEditor();
/*        if ( this.isEnterAsTab() && getCellEditor() != null ) {
           if( setActiveColumn(erow,editedColumn + 1) ) {
               TableUtility.makeCellVisible(this,erow,editedColumn + 1);
           }
               
        }
 */
        repaint( this.getRowRect(erow));
        //requestFocusInWindow();        
        //repaint( getCellRect(erow,editedColumn,false));        
    }
    /**
     * Overrided due to an error in JTable when move column.
     */
    @Override
    public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
        JTableHeader th = this.getTableHeader();
        
        TableColumn tc = th.getDraggedColumn();
        if ( tc != null  ) {
            //th.getColumnModel().addColumn()
            int d = th.getDraggedDistance();
            return new Rectangle(getVisibleRect().x + d,getVisibleRect().y,1,1);
        }
        return super.getCellRect(row,column,includeSpacing);
    }
    
    public int getColumnXOffset( int column, boolean includeSpacing) {
        int x = 0;
        boolean valid = true;
        
        TableColumnModel cm = getColumnModel();
        if( getComponentOrientation().isLeftToRight() ) {
            for(int i = 0; i < column; i++) {
                x += cm.getColumn(i).getWidth();
            }
        } else {
            for(int i = cm.getColumnCount()-1; i > column; i--) {
                x += cm.getColumn(i).getWidth();
            }
        }
        
        if (valid && !includeSpacing) {
            // Bound the margins by their associated dimensions to prevent
            // returning bounds with negative dimensions.
            int d = cm.getColumnMargin();
            // This is not the same as grow(), it rounds differently.
            x += d/2;
        }
        return x;
    }
    
    public Rectangle getViewCellRect(int row, int column, boolean includeSpacing) {
        JTableHeader th = this.getTableHeader();
        
        RowTemplate rt;
        TableColumn tc = th.getDraggedColumn();
        if ( tc != null  ) {
            //th.getColumnModel().addColumn()
            int d = th.getDraggedDistance();
            return new Rectangle(getVisibleRect().x + d,getVisibleRect().y,1,1);
        } else
            rt = this.getRowTemplate(row);
        return rt.getCellRect(row,column,includeSpacing);
    }
    
    
    /**
     * Invoked when editing is finished. The changes are saved and the
     * editor is discarded.
     * <p>
     * Application code will not use these methods explicitly, they
     * are used internally by JTable.
     *
     * @param  e  the event received
     * @see CellEditorListener
     */
    @Override
    public void editingStopped(ChangeEvent e) {
        // Take in the new value
        TableCellEditor editor = getCellEditor();
        if (editor != null) {
            Object value = editor.getCellEditorValue();
            int modelIndex = this.getRowTemplate(editingRow).getColumn(editingColumn).getModelIndex();
            getModel().setValueAt(value,editingRow,modelIndex);
            //setValueAt(value, editingRow, editingColumn);
            removeEditor();
            //KeyEvent ke = new KeyEvent(this);
            
        }
    }
    
    public void editingStopped(ActionEvent e, int rowIndex, int columnIndex) {    
        if ( this.isEnterAsTab() ) {
           if( setActiveColumn(rowIndex,columnIndex + 1) ) {
                TableUtility.makeCellVisible(this,rowIndex,columnIndex + 1);
                repaint( this.getRowRect(rowIndex));
           }
               
        }
        
    }
    
    public boolean isCellEditable(RowTemplate rt, int rowIndex, int column ) {
        return getModel().isCellEditable(rowIndex, rt.getColumn(column).getModelIndex());
    }
    
    /**
     * Returns true if the cell at <code>row</code> and <code>column</code>
     * is editable.  Otherwise, invoking <code>setValueAt</code> on the cell
     * will have no effect.
     * <p>
     * <b>Note</b>: The column is specified in the table view's display
     *              order, and not in the <code>TableModel</code>'s column
     *		    order.  This is an important distinction because as the
     *		    user rearranges the columns in the table,
     *		    the column at a given index in the view will change.
     *              Meanwhile the user's actions never affect the model's
     *              column ordering.
     *
     *
     * @param   row      the row whose value is to be queried
     * @param   column   the column whose value is to be queried
     * @return  true if the cell is editable
     * @see #setValueAt
     * @see #isCellEditable(VRowTemplate,int,int)
     */
/*    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return getModel().isCellEditable(rowIndex,
                                         convertColumnIndexToModel(rowIndex,columnIndex));
    }
 */
    /**
     * Returns true if the specified indices are in the valid range of rows
     * and columns and the cell at the specified position is selected.
     * @param rowIndex   the row being queried
     * @param columnIndex  the column being queried
     *
     * @return true if <code>row</code> and <code>column</code> are valid indices
     *              and the cell at index <code>(row, column)</code> is selected,
     *              where the first row and first column are at index 0
     */
/*    public boolean isCellSelected(int rowIndex, int columnIndex) {
        if (!getRowSelectionAllowed() && !getColumnSelectionAllowed()) {
            return false;
        }
 
        return (!getRowSelectionAllowed() || isRowSelected(rowIndex)) &&
               (!getColumnSelectionAllowed() || isColumnSelected(columnIndex));
    }
 */
    /**
     * Returns an appropriate editor for the cell specified by
     * <code>row</code> and <code>column</code>. If the
     * <code>TableColumn</code> for this column has a non-null editor,
     * returns that.  If not, finds the class of the data in this
     * column (using <code>getColumnClass</code>)
     * and returns the default editor for this type of data.
     * <p>
     * <b>Note:</b>
     * Throughout the table package, the internal implementations always
     * use this method to provide editors so that this default behavior
     * can be safely overridden by a subclass.
     *
     * @param rt 
     * @param rowIndex       the row of the cell to edit, where 0 is the first row
     * @param columnIndex    the column of the cell to edit,
     *			where 0 is the first column
     * @return          the editor for this cell;
     *			if <code>null</code> return the default editor for
     *  		this type of cell
     * @see DefaultCellEditor
     */
    public TableCellEditor getCellEditor(RowTemplate rt, int rowIndex,int columnIndex) {
        TableCell tableColumn = rt.getColumn(columnIndex);
        TableCellEditor editor = tableColumn.getCellEditor();
        if (editor == null) {
            Class cl = getModel().getColumnClass(tableColumn.getModelIndex());
//            if ( getModel() instanceof DataTable )
//                editor = getDataTableDefaultEditor(cl);
//            if ( editor == null  )
            editor = getDefaultEditor(cl);
        }
        return editor;
    }
    
   /**
     * Returns the editor to be used when no editor has been set in
     * a <code>TableColumn</code>. During the editing of cells the editor is fetched from
     * a <code>Hashtable</code> of entries according to the class of the cells in the column. If
     * there is no entry for this <code>columnClass</code> the method returns
     * the entry for the most specific superclass. The <code>JTable</code> installs entries
     * for <code>Object</code>, <code>Number</code>, and <code>Boolean</code>, all of which can be modified
     * or replaced.
     *
     * @param   columnClass  return the default cell editor for this columnClass
     * @return the default cell editor to be used for this columnClass
     * @see     #setDefaultEditor
     * @see     #getColumnClass
     */
/*    public TableCellEditor getDataTableDefaultEditor(Class<?> columnClass) {
        if (columnClass == null) {
            return null;
        }
        else {
            Object editor = dataTableEditorsByColumnClass.get(columnClass);
            if (editor != null) {
                return (TableCellEditor)editor;
            }
            else {
                return getDefaultEditor(columnClass);
            }
        }
    }
*/    
    
    /**
     * Creates default cell editors for objects, numbers, and boolean values.
     * @see DefaultCellEditor
     */
    @Override
    protected void createDefaultEditors() {
        defaultEditorsByColumnClass = new UIDefaults(20, 0.75f);

        // Objects
    	String s =  "javax.swing.JTable$GenericEditor";
	defaultEditorsByColumnClass.put(Object.class, new UIDefaults.ProxyLazyValue(s));

        // Numbers
        s = "javax.swing.JTable$NumberEditor";
	defaultEditorsByColumnClass.put(Number.class, new UIDefaults.ProxyLazyValue(s));

        // Booleans
        s = "javax.swing.JTable$BooleanEditor";
	defaultEditorsByColumnClass.put(Boolean.class, new UIDefaults.ProxyLazyValue(s));
        s = "tdo.swing.table.editor.NumericCellEditor";
	defaultEditorsByColumnClass.put(Integer.class, new UIDefaults.ProxyLazyValue(s));
	defaultEditorsByColumnClass.put(Short.class, new UIDefaults.ProxyLazyValue(s));
	defaultEditorsByColumnClass.put(Long.class, new UIDefaults.ProxyLazyValue(s));
	defaultEditorsByColumnClass.put(BigDecimal.class, new UIDefaults.ProxyLazyValue(s));
	defaultEditorsByColumnClass.put(Byte.class, new UIDefaults.ProxyLazyValue(s));
	defaultEditorsByColumnClass.put(Double.class, new UIDefaults.ProxyLazyValue(s));
	defaultEditorsByColumnClass.put(Float.class, new UIDefaults.ProxyLazyValue(s));
        
        
    }
   /**
     * Creates default cell renderers for objects, numbers, doubles, dates,
     * booleans, and icons.
     * @see javax.swing.table.DefaultTableCellRenderer
     *
     */
    @Override
    protected void createDefaultRenderers() {
        defaultRenderersByColumnClass = new UIDefaults(18, 0.75f);
    	String s =  "javax.swing.table.DefaultTableCellRenderer$UIResource";
        // Objects
        defaultRenderersByColumnClass.put(Object.class, new UIDefaults.ProxyLazyValue(s));        

	// Numbers
        s = "javax.swing.JTable$NumberRenderer";
        defaultRenderersByColumnClass.put(Number.class, new UIDefaults.ProxyLazyValue(s));        
        
        s = "tdo.swing.table.editor.NumericCellRenderer";
        defaultRenderersByColumnClass.put(Short.class, new UIDefaults.ProxyLazyValue(s));        
        defaultRenderersByColumnClass.put(Integer.class, new UIDefaults.ProxyLazyValue(s));        
        defaultRenderersByColumnClass.put(BigDecimal.class, new UIDefaults.ProxyLazyValue(s));        
        

	// Doubles and Floats
        s = "javax.swing.JTable$DoubleRenderer";
        defaultRenderersByColumnClass.put(Float.class, new UIDefaults.ProxyLazyValue(s));        
        defaultRenderersByColumnClass.put(Double.class, new UIDefaults.ProxyLazyValue(s));        
        

	// Dates
        s = "javax.swing.JTable$DateRenderer";
        defaultRenderersByColumnClass.put(Date.class, new UIDefaults.ProxyLazyValue(s));        

        // Icons and ImageIcons
        s = "javax.swing.JTable$IconRenderer";
        defaultRenderersByColumnClass.put(Icon.class, new UIDefaults.ProxyLazyValue(s));        
        defaultRenderersByColumnClass.put(ImageIcon.class, new UIDefaults.ProxyLazyValue(s));        


        // Booleans
        s = "javax.swing.JTable$BooleanRenderer";
        defaultRenderersByColumnClass.put(Boolean.class, new UIDefaults.ProxyLazyValue(s));        
    }
    
    /*
    public int fixedColumnAtPoint(Point point) {
        int x = point.x - this.getVisibleRect().x;
        if (x < 0) {
            return -1;
        }
        int len = this.fixedColumnList.size();
        for(int f = 0; f < len; f++) {
            TableColumn tc = getColumnModel().getColumn( fixedColumnList.get(f) );
            x -= tc.getWidth();
            if (x < 0) {
                return f;
            }
        }
        return -1;    }
 
    protected boolean isFixedColumn(Point point) {
        if ( this.fixedColumnList == null || this.fixedColumnList.isEmpty() )
            return false;
        int x = point.x;
        if( !getComponentOrientation().isLeftToRight() ) {
            x = getWidth() - x;
        }
        int tcw = getTotalFixedColumnsWidth();
        return point.x - getVisibleRect().x >= 0 && point.x - getVisibleRect().x < tcw;
 
    }
    public int getTotalFixedColumnsWidth() {
        Enumeration enumeration = this.getColumnModel().getColumns();
        int tcw = 0;
        for ( int i=0; i < this.fixedColumnList.size(); i++ ) {
            TableColumn tc = this.getColumnModel().getColumn( fixedColumnList.get(i) );
            tcw += tc.getWidth();
        }
        return tcw;
    }
 */
    /**
     * Returns a rectangle for the cell that lies at the intersection of
     * <code>row</code> and <code>column</code>.
     * If <code>includeSpacing</code> is true then the value returned
     * has the full height and width of the row and column
     * specified. If it is false, the returned rectangle is inset by the
     * intercell spacing to return the true bounds of the rendering or
     * editing component as it will be set during rendering.
     * <p>
     * If the column index is valid but the row index is less
     * than zero the method returns a rectangle with the
     * <code>y</code> and <code>height</code> values set appropriately
     * and the <code>x</code> and <code>width</code> values both set
     * to zero. In general, when either the row or column indices indicate a
     * cell outside the appropriate range, the method returns a rectangle
     * depicting the closest edge of the closest cell that is within
     * the table's range. When both row and column indices are out
     * of range the returned rectangle covers the closest
     * point of the closest cell.
     * <p>
     * In all cases, calculations that use this method to calculate
     * results along one axis will not fail because of anomalies in
     * calculations along the other axis. When the cell is not valid
     * the <code>includeSpacing</code> parameter is ignored.
     *
     * @param   row                   the row index where the desired cell
     *                                is located
     * @param   column                the column index where the desired cell
     *                                is located in the display; this is not
     *                                necessarily the same as the column index
     *                                in the data model for the table; the
     *                                {@link #convertColumnIndexToView(int)}
     *                                method may be used to convert a data
     *                                model column index to a display
     *                                column index
     * @param   includeSpacing        if false, return the true cell bounds -
     *                                computed by subtracting the intercell
     *				      spacing from the height and widths of
     *				      the column and row models
     *
     * @return  the rectangle containing the cell at location
     *          <code>row</code>,<code>column</code>
     * @see #getIntercellSpacing
     */
/*    public Rectangle getFixedCellRect(int row, int column, boolean includeSpacing) {
        Rectangle r = new Rectangle();
        boolean valid = true;
        if (row < 0) {
            // y = height = 0;
            valid = false;
        } else if (row >= getRowCount()) {
            r.y = getHeight();
            valid = false;
        } else {
            r.height = getRowHeight(row);
            //r.y = (rowModel == null) ? row * r.height : rowModel.getPosition(row);
            r.y = row * r.height;
        }
 
 
        TableColumnModel cmod = getColumnModel();
 
        if( getComponentOrientation().isLeftToRight() ) {
            for(int i = 0; i < this.fixedColumnList.size(); i++) {
                if ( fixedColumnList.get(i) == column )
                    break;
                r.x += cmod.getColumn(fixedColumnList.get(i)).getWidth();
            }
        } else {
            for(int i = cmod.getColumnCount()-1; i > column; i--) {
                r.x += cmod.getColumn(i).getWidth();
            }
        }
        r.x += this.getVisibleRect().x;
        r.width = cmod.getColumn(column).getWidth();
 
 
        if (valid && !includeSpacing) {
            // Bound the margins by their associated dimensions to prevent
            // returning bounds with negative dimensions.
            int rm = Math.min(getRowMargin(), r.height);
            int cm = Math.min(getColumnModel().getColumnMargin(), r.width);
            // This is not the same as grow(), it rounds differently.
            r.setBounds(r.x + cm/2, r.y + rm/2, r.width - cm, r.height - rm);
        }
        return r;
    }
 */
    //===========================================================================================
    // overriden
    //===========================================================================================
    
    /**
     * Sets the data model for this table to <code>newModel</code> and registers
     * with it for listener notifications from the new data model.
     *
     * @param   dataModel        the new data source for this table
     * @exception IllegalArgumentException      if <code>newModel</code> is <code>null</code>
     * @see     #getModel
     * @beaninfo
     *  bound: true
     *  description: The model that is the source of the data for this view.
     */
    
    /*
     * TODO подключить обработку DataTable ( see dataTable field )
     */
    @Override
    public void setModel(TableModel dataModel) {
        if (dataModel == null) {
            throw new IllegalArgumentException("Cannot set a null TableModel");
        }
        if (this.dataModel != dataModel) {
            TableModel old = this.dataModel;
            if (old != null) {
                old.removeTableModelListener(this);
            }
            this.dataModel = dataModel;
            dataModel.addTableModelListener(this);
            
            tableChanged(new TableModelEvent(dataModel, TableModelEvent.HEADER_ROW));
            
            firePropertyChange("model", old, dataModel);
            
            if (getAutoCreateRowSorter()) {
                setRowSorter(new TableRowSorter(dataModel));
            }
        }
    }//setModel
    
    /**
     * Creates default columns for the table from
     * the data model using the <code>getColumnCount</code> method
     * defined in the <code>TableModel</code> interface.
     * <p>
     * Clears any existing columns before creating the
     * new columns based on information from the model.
     *
     * @see     #getAutoCreateColumnsFromModel
     */
    @Override
    public void createDefaultColumnsFromModel() {
        TableModel m = getModel();
        if (m != null) {
            // Remove any current columns
            TableColumnModel cm = getColumnModel();
            while (cm.getColumnCount() > 0) {
                cm.removeColumn(cm.getColumn(0));
            }
            
            // Create new columns from the data model info
            for (int i = 0; i < m.getColumnCount(); i++) {
                TableColumn newColumn = new TableColumn(i);
                addColumn(newColumn);
            }
        }
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
        rowHeights = null;
        refreshRowHeights();
        resizeAndRepaint();
    }
    
    /**
     * Sets the column model for this table to <code>newModel</code> and registers
     * for listener notifications from the new column model. Also sets
     * the column model of the <code>JTableHeader</code> to <code>columnModel</code>.
     *
     * @param   columnModel        the new data source for this table
     * @exception IllegalArgumentException      if <code>columnModel</code> is <code>null</code>
     * @see     #getColumnModel
     * @beaninfo
     *  bound: true
     *  description: The object governing the way columns appear in the view.
     */
    @Override
    public void setColumnModel(TableColumnModel columnModel) {
        if (columnModel == null) {
            throw new IllegalArgumentException("Cannot set a null ColumnModel");
        }
        TableColumnModel old = this.columnModel;
        if (columnModel != old) {
            if (old != null) {
                old.removeColumnModelListener(this);
            }
            this.columnModel = columnModel;
            columnModel.addColumnModelListener(this);
            
            // Set the column model of the header as well.
            if (tableHeader != null) {
                tableHeader.setColumnModel(columnModel);
            }
            
            firePropertyChange("columnModel", old, columnModel);
            resizeAndRepaint();
        }
    }//setColumnModel
    
    /**
     * Returns the index of the row that <code>point</code> lies in,
     * or -1 if the result is not in the range
     * [0, <code>getRowCount()</code>-1].
     *
     * @param   point   the location of interest
     * @return  the index of the row that <code>point</code> lies in,
     *          or -1 if the result is not in the range
     *          [0, <code>getRowCount()</code>-1]
     * @see     #columnAtPoint
     */
/*    public int rowAtPoint(Point point) {
        int y = point.y;
        int result = (rowModel == null) ?  y/getRowHeight() : rowModel.getIndex(y);
        if (result < 0) {
            return -1;
        }
        else if (result >= getRowCount()) {
            return -1;
        }
        else {
            return result;
        }
    }
 */
    /**
     * @RUS
     * Возвращает индекс ряда, содержащего точку, определяемую параметром.<p>
     * Точка должна быть задана относительно левого верхнего угла компонента,
     * а не контейнера, его содержащего.<p>
     * @param point - Point
     * @return  int - индекс ряда, содержащего точку.
     * @ENDRUS
     *
     * Returns the index of the row that <code>point</code> lies in,
     * or -1 if the result is not in the range
     * [0, <code>getRowCount()</code>-1].
     *
     * @param   point   the location of interest
     * @return  the index of the row that <code>point</code> lies in,
     *          or -1 if the result is not in the range
     *          [0, <code>getRowCount()</code>-1]
     * @see     #columnAtPoint
     */
    @Override
    public int rowAtPoint(Point point) {
        int y = point.y;
        
        int result = (rowHeights == null) ? y / rowHeight : rowHeights.getIndex(y);
        if (result < 0) {
            return -1;
        } else if (result >= getModel().getRowCount()) {
            return -1;
        } else {
            return result;
        }
    }
    
    /**
     * @RUS
     * Возвращает прямоугольник, ограничивающий ряд таблицы с индексом
     * <code>rowIndex</code>. <p>
     * Возвращаемый прямоугольник по ширине (width) равен ширине Grid и
     * размещается по оси X==0, а по оси Y - смещение ряда относительно
     * верхней точки Grid.<p>
     *
     * @param rowIndex - индекс ряда
     * @return - прямоугольник
     * @ENDRUS
     */
    public Rectangle getRowRect(int rowIndex) {
        int rH = getRowHeight(rowIndex);
        int rY;
        int rW;
        rY = (rowHeights == null) ? rowIndex * getRowHeight() :
            rowHeights.getPosition(rowIndex);
        System.out.println("POSITION: " + rY);
        rW = getVisibleRect().width;
        return new Rectangle(getVisibleRect().x, rY, rW, rH);
    }
    
    /**
     * Возвращет состояние активности ряда. <p>
     * Метод введен в связи с необходимости в super-классах не принимать
     * во внимание, является ли ряд активным или нет. Например, в классе
     * <code>PTableHeader</code> этот метод переопределен и всегда
     * возвращает <code>false</code>.
     *
     * @param rowIndex типа <code>int</code> - индекс ряда, для которого
     *   производится проверка.
     * @return <code>true</code>, если ряд, индекс которого, задан параметром
     *  активен. <code>false</code> в противном случае.
     *
     */
    public boolean isRowActive(int rowIndex) {
        if ( getModel() instanceof DataTable )
            return ((DataTable)getModel()).getActiveRowIndex() == rowIndex;
            //return ((DataTable)getModel()).isRowActive(rowIndex);
        else
            return false;
    }
    
    /**
     * Equivalent to <code>revalidate</code> followed by <code>repaint</code>.
     */
    @Override
    protected void resizeAndRepaint() {
        if ( ! repaintNeeded )
            return;
        revalidate();
        if ( rowTemplates != null )
            this.rowTemplates.doLayout();
        repaint();
    }
    
    
    //===========================================================================================
    //              new
    //===========================================================================================
    
    /**
     *
     * @return
     */
    public SizeSequence getRowHeights() {
        if (rowHeights == null) {
            rowHeights = new SizeSequence(getRowCount(), getRowHeight());
        }
        return rowHeights;
    }
    
    /**
     *
     * @param rowHeights
     */
    public void setRowHeights(SizeSequence rowHeights) {
        this.rowHeights = rowHeights;
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
        getRowHeights().setSize(rowIndex, rowHeight);
        super.setRowHeight(rowIndex,rowHeight);
    }
    
    /**
     * Sets the height for <code>row</code> to <code>rowHeight</code>,
     * revalidates, and repaints. The height of the cells in this row
     * will be equal to the row height minus the row margin.
     *
     * @param   row                             the row whose height is being
     * changed
     * @param   rowHeight                       new row height, in pixels
     * @exception IllegalArgumentException      if <code>rowHeight</code> is
     *                                          less than 1
     * @beaninfo
     *  bound: true
     *  description: The height in pixels of the cells in <code>row</code>
     * @since 1.3
     */
    public void setRowHeight(int rowIndex, int rowHeight, boolean repaintNeeded) {
        getRowHeights().setSize(rowIndex, rowHeight);
        this.repaintNeeded = repaintNeeded;
        super.setRowHeight(rowIndex,rowHeight);
        this.repaintNeeded = true;
    }
    
    public void refreshRowHeights( boolean forceRefresh) {
        if ( getModel() == null || getModel().getRowCount() == 0)
            return;
        RowTemplates rts = this.getRowTemplates();
        int h = getColumnModelTemplate().getHeight();
        boolean f = false;
        if ( forceRefresh )
            f = true;
        else  {
            for ( RowTemplate rt : rts.values() ) {
                if ( rt.getHeight() != h ) {
                    f = true;
                    break;
                }
            }
        }
        if ( f == true ) {
            TableModel m = getModel();
            for ( int i=0; i < m.getRowCount(); i++ ) {
                RowTemplate rt = this.getRowTemplate(i);
                if ( i != m.getRowCount()-1 )
                    setRowHeight(i,rt.getHeight(),false);
                else
                    setRowHeight(i,rt.getHeight(),true);
            }
        }
    }
    public void refreshRowHeights() {
        this.refreshRowHeights(true);
    }
    
    /**
     * Возвращает индекс активного ряда.<p>
     *
     * @return - значение типа <code>int</code> - индекс активного ряда.
     *   Возвращаемое значение равно -1, если нет активного ряда (например, значение
     *   свойства <code>gridModel == null)</code>.
     */
    public int getActiveRow() {
        int result = -1;
        if ( dataTable != null )
            result = dataTable.getActiveRowIndex();
        return result;
    }
    
    public RowTemplate getRowTemplate(int rowIndex) {
        RowTemplate result = rowTemplates.getTemplate("COLUMNMODEL");
        
        if ( rowTemplateRequestListener == null )
            return result;
        
        rowTemplateRequestEvent.setKey(null);
        this.rowTemplateRequestListener.defineKey(rowTemplateRequestEvent,rowIndex);
        String key = rowTemplateRequestEvent.getKey();
        result = rowTemplates.getTemplate(key);
        if ( result == null ) {
            result = rowTemplates.getTemplate("COLUMNMODEL");
        }
        return result;
        //        return getRowTemplates().get("DUAL");
/*        if ( rowIndex - ( rowIndex / 2)*2 - 1 ==  0 )
            return rowTemplates.getTemplate("0");
        else
            return rowTemplates.getTemplate("1");
 */
    }
    
    
    public RowTemplate getColumnModelTemplate() {
        return this.columnModelTemplate;
    }
    
    public RowTemplate getModelColumns() {
        return this.rowTemplates.getModelColumns();
    }
    
    public void setColumnModelTemplate(RowTemplate rt ) {
        this.columnModelTemplate = rt;
    }
    public RowTemplates getRowTemplates() {
        return rowTemplates;
    }
    /**
     *
     * @param rowIndex
     * @param column
     * @return
     */
    public String getFormattedValue(int rowIndex, TableCell column ) {
        Object value;
        DataColumn dataColumn;
        int modelIndex;
        
        modelIndex = column.getModelIndex();
        value = getModel().getValueAt(rowIndex, modelIndex);
        if ( getModel() instanceof DataTable ) {
            dataColumn = ((DataTable)getModel()).getColumns().get(modelIndex);
            //my 20.04 return dataColumn.toString(value,column.getDisplayFormat() );
            return DataUtil.toString(dataColumn,value);
        } else
            return value.toString();
        
    }
    
    public TableCellRenderer getViewCellRenderer(int row, int column) {
        TableColumn tableColumn = getRowTemplate(row).getColumn(column);
        TableCellRenderer renderer = tableColumn.getCellRenderer();
        if (renderer == null) {
            renderer = getDefaultRenderer(getColumnClass(row,column));
        }
        return renderer;
    }
    
    /**
     * Maps the index of the column in the view at
     * <code>viewColumnIndex</code> to the index of the column
     * in the table model.  Returns the index of the corresponding
     * column in the model.  If <code>viewColumnIndex</code>
     * is less than zero, returns <code>viewColumnIndex</code>.
     *
     * @param   viewColumnIndex     the index of the column in the view
     * @return  the index of the corresponding column in the model
     *
     * @see #convertColumnIndexToView
     */
/*    public int convertColumnIndexToModel(int viewColumnIndex) {
        if (viewColumnIndex < 0) {
            return viewColumnIndex;
        }
        //return getColumnModel().getColumn(viewColumnIndex).getModelIndex();
        return -1;
    }
 */
    /**
     * Returns the type of the column appearing in the view at
     * column position <code>column</code>.
     *
     * @param   column   the column in the view being queried
     * @return the type of the column at position <code>column</code>
     * 		in the view where the first column is column 0
     */
    @Override
    public Class<?> getColumnClass(int column) {
        return Object.class;
        //return getModel().getColumnClass(convertColumnIndexToModel(column));
    }
    
    @Override
    public void repaint() {
        System.out.println("repaint()");
        super.repaint();
    }
    /**
     * Returns the type of the column appearing in the view at
     * column position <code>column</code>.
     *
     */
    public Class<?> getColumnClass(int rowIndex,int columnIndex) {
        return getModel().getColumnClass(convertColumnIndexToModel(rowIndex,columnIndex));
    }
    
    
    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
            int condition, boolean pressed) {
        return false;
    }
    
    //
    // Implementing ListSelectionListener interface
    //
    
    /**
     * Invoked when the row selection changes -- repaints to show the new
     * selection.
     * <p>
     * Application code will not use these methods explicitly, they
     * are used internally by JTable.
     *
     * @param e   the event received
     * @see ListSelectionListener
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        boolean isAdjusting = e.getValueIsAdjusting();
        if (rowSelectionAdjusting && !isAdjusting) {
            // The assumption is that when the model is no longer adjusting
            // we will have already gotten all the changes, and therefore
            // don't need to do an additional paint.
            rowSelectionAdjusting = false;
            return;
        }
        rowSelectionAdjusting = isAdjusting;
        
        // The getCellRect() calls will fail unless there is at least one column.
        if (getRowCount() <= 0 || getColumnCount() <= 0) {
            return;
        }
        
        int firstIndex = Math.min(getRowCount()-1,Math.max(e.getFirstIndex(),0));
        int lastIndex = Math.min(getRowCount()-1,Math.max(e.getLastIndex(),0));
/*        RowTemplate rt = this.getRowTemplate(lastIndex);
        Rectangle firstRowRect = getCellRect(firstIndex, 0, false);
        Rectangle lastRowRect = getCellRect(lastIndex, rt.getColumnCount()-1, false);
        Rectangle dirtyRegion = firstRowRect.union(lastRowRect);
 */
        repaint(this.getRowRect(firstIndex));
        repaint(this.getRowRect(lastIndex));
    }
    
    @Override
    public void tableChanged(TableModelEvent e) {
        int modelColumn = e.getColumn();
        TableModelEvent eNew;// = null;
        if (e == null || e.getFirstRow() == TableModelEvent.HEADER_ROW)
            activeColumn = 0;
        
        if ( modelColumn != TableModelEvent.ALL_COLUMNS ) {
            eNew = new TableModelEvent((TableModel)e.getSource(), e.getFirstRow(), e.getLastRow(),TableModelEvent.ALL_COLUMNS,e.getType());
            super.tableChanged(eNew);
        } else {
            super.tableChanged(e);
        }
        
    }
/*    public void columnSelectionChanged(ListSelectionEvent e) {
        //ListSelectionEvent enew;
        if (getRowSelectionAllowed()) {
            int minRow = selectionModel.getMinSelectionIndex();
            int maxRow = selectionModel.getMaxSelectionIndex();
 
            if ( minRow < 0 )
                return;
            else
                repaint(getRowRect(minRow));
            if ( maxRow < 0 )
                return;
            else
                repaint(getRowRect(maxRow));
 
        }
        //super.columnSelectionChanged(e);
    }
 */
    public void activeColumnChanged() {
        //ListSelectionEvent enew;
        if (getRowSelectionAllowed()) {
            int minRow = selectionModel.getMinSelectionIndex();
            int maxRow = selectionModel.getMaxSelectionIndex();
            
            if ( minRow < 0 )
                return;
            else
                repaint(getRowRect(minRow));
            if ( maxRow >= 0 ) {
                repaint(getRowRect(maxRow));
            }
            
        }
        
        //super.columnSelectionChanged(e);
    }
    
    /**
     * Selects the columns from <code>index0</code> to <code>index1</code>,
     * inclusive.
     *
     * @exception IllegalArgumentException      if <code>index0</code> or
     *						<code>index1</code> lie outside
     *                                          [0, <code>getColumnCount()</code>-1]
     * @param   index0 one end of the interval
     * @param   index1 the other end of the interval
     */
    @Override
    public void setColumnSelectionInterval(int index0, int index1) {
        ListSelectionModel lsm = this.getSelectionModel();
        int rowIndex = lsm.getMinSelectionIndex();
        if ( rowIndex < 0 )
            return;
        RowTemplate rt = getRowTemplate(rowIndex);
        if ( index0 < 0 || index0 >= rt.getColumnCount() )
            return;
        if ( index1 < 0 || index1 >= rt.getColumnCount() )
            return;
        
        getColumnModel().getSelectionModel().setSelectionInterval(index0, index1);
    }
    /**
     * Maps the index of the column in the view at
     * <code>viewColumnIndex</code> to the index of the column
     * in the table model.  Returns the index of the corresponding
     * column in the model.  If <code>viewColumnIndex</code>
     * is less than zero, returns <code>viewColumnIndex</code>.
     *
     * @param   viewColumnIndex     the index of the column in the view
     * @return  the index of the corresponding column in the model
     *
     * @see #convertColumnIndexToView
     */
/*    public int convertColumnIndexToModel(int viewColumnIndex) {
        //return -1;
        if (viewColumnIndex < 0) {
            return viewColumnIndex;
        }
        return getColumnModel().getColumn(viewColumnIndex).getModelIndex();
 
    }
 */
    public int convertColumnIndexToModel(int rowIndex, int columnIndex) {
        if (columnIndex < 0 || rowIndex < 0 ) {
            return columnIndex;
        }
        return this.getRowTemplate(rowIndex).getColumn(columnIndex).getModelIndex();
    }
    
    /**
     * Returns the cell value at <code>row</code> and <code>column</code>.
     * <p>
     * <b>Note</b>: The column is specified in the table view's display
     *              order, and not in the <code>TableModel</code>'s column
     *		    order.  This is an important distinction because as the
     *		    user rearranges the columns in the table,
     *		    the column at a given index in the view will change.
     *              Meanwhile the user's actions never affect the model's
     *              column ordering.
     *
     * @param   rowIndex             the row whose value is to be queried
     * @param   columnIndex          the column whose value is to be queried
     * @return  the Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getModel().getValueAt(rowIndex,
                convertColumnIndexToModel(rowIndex,columnIndex));
    }
    
    public Object getValue(int rowIndex, int columnIndex) {
        return getModel().getValueAt(rowIndex,
                convertColumnIndexToModel(rowIndex,columnIndex));
    }
    
    @Override
    public String getToolTipText(MouseEvent event) {
        return getToolTipText();
    }
    
    /**
     * Sets the value for the cell in the table model at <code>row</code>
     * and <code>column</code>.
     * <p>
     * <b>Note</b>: The column is specified in the table view's display
     *              order, and not in the <code>TableModel</code>'s column
     *		    order.  This is an important distinction because as the
     *		    user rearranges the columns in the table,
     *		    the column at a given index in the view will change.
     *              Meanwhile the user's actions never affect the model's
     *              column ordering.
     *
     * <code>value</code> is the new value.
     *
     * @param   value          the new value
     * @param   rowIndex             the row of the cell to be changed
     * @param   columnIndex          the column of the cell to be changed
     * @see #getValueAt
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        getModel().setValueAt(value, rowIndex,
                convertColumnIndexToModel(rowIndex,columnIndex));
    }
    
    
    /**
     * Returns the name of the column appearing in the view at
     * column position <code>column</code>.
     *
     * @param  column    the column in the view being queried
     * @return the name of the column at position <code>column</code>
     * in the view where the first column is column 0
     */
/*    public String getColumnName(int column) {
       // return getModel().getColumnName(convertColumnIndexToModel(column));
        return null;
    }
 */
    ////////// ***********************************************
    
    /**
     * Updates the selection models of the table, depending on the state of the
     * two flags: <code>toggle</code> and <code>extend</code>. Most changes
     * to the selection that are the result of keyboard or mouse events received
     * by the UI are channeled through this method so that the behavior may be
     * overridden by a subclass. Some UIs may need more functionality than
     * this method provides, such as when manipulating the lead for discontiguous
     * selection, and may not call into this method for some selection changes.
     * <p>
     * This implementation uses the following conventions:
     * <ul>
     * <li> <code>toggle</code>: <em>false</em>, <code>extend</code>: <em>false</em>.
     *      Clear the previous selection and ensure the new cell is selected.
     * <li> <code>toggle</code>: <em>false</em>, <code>extend</code>: <em>true</em>.
     *      Extend the previous selection from the anchor to the specified cell,
     *      clearing all other selections.
     * <li> <code>toggle</code>: <em>true</em>, <code>extend</code>: <em>false</em>.
     *      If the specified cell is selected, deselect it. If it is not selected, select it.
     * <li> <code>toggle</code>: <em>true</em>, <code>extend</code>: <em>true</em>.
     *      Apply the selection state of the anchor to all cells between it and the
     *      specified cell.
     * </ul>
     * @param  rowIndex   affects the selection at <code>row</code>
     * @param  columnIndex  affects the selection at <code>column</code>
     * @param  toggle  see description above
     * @param  extend  if true, extend the current selection
     *
     * @since 1.3
     */
    /*
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
     
        RowTemplate rt = this.getRowTemplate(rowIndex);
     
        ListSelectionModel rsm = getSelectionModel();
        ListSelectionModel csm = getColumnModel().getSelectionModel();
     
        int anchorRow = getAdjustedIndex(rt,rsm.getAnchorSelectionIndex(), true);
        int anchorCol = getAdjustedIndex(rt,csm.getAnchorSelectionIndex(), false);
     
        boolean anchorSelected = true;
     
        if (anchorRow == -1) {
            anchorRow = 0;
            anchorSelected = false;
        }
     
        if (anchorCol == -1) {
            anchorCol = 0;
            anchorSelected = false;
        }
     
        // Check the selection here rather than in each selection model.
        // This is significant in cell selection mode if we are supposed
        // to be toggling the selection. In this case it is better to
        // ensure that the cell's selection state will indeed be changed.
        // If this were done in the code for the selection model it
        // might leave a cell in selection state if the row was
        // selected but the column was not - as it would toggle them both.
        boolean selected = isCellSelected(rowIndex, columnIndex);
        anchorSelected = anchorSelected && isCellSelected(anchorRow, anchorCol);
     
        changeSelectionModel(csm, columnIndex, toggle, extend, selected,
                             anchorCol, anchorSelected);
        changeSelectionModel(rsm, rowIndex, toggle, extend, selected,
                             anchorRow, anchorSelected);
     
        // Scroll after changing the selection as blit scrolling is immediate,
        // so that if we cause the repaint after the scroll we end up painting
        // everything!
        if (getAutoscrolls()) {
            Rectangle cellRect = getCellRect(rowIndex, columnIndex, false);
            if (cellRect != null) {
                scrollRectToVisible(cellRect);
            }
        }
    }
     */
/*    private int getAdjustedIndex(RowTemplate rt,int index, boolean row) {
        int compare = row ? getRowCount() : rt.getColumnCount();
        return index < compare ? index : -1;
    }
 */
/*    protected void changeSelectionModel(ListSelectionModel sm, int index,
                                      boolean toggle, boolean extend, boolean selected,
                                      int anchor, boolean anchorSelected) {
        if (extend) {
            if (toggle) {
                if (anchorSelected) {
                    sm.addSelectionInterval(anchor, index);
                } else {
                    sm.removeSelectionInterval(anchor, index);
                    // this is a Windows-only behavior that we want for file lists
                    if (Boolean.TRUE == getClientProperty("Table.isFileList")) {
                        sm.addSelectionInterval(index, index);
                        sm.setAnchorSelectionIndex(anchor);
                    }
                }
            }
            else {
                sm.setSelectionInterval(anchor, index);
            }
        }
        else {
            if (toggle) {
                if (selected) {
                    sm.removeSelectionInterval(index, index);
                }
                else {
                    sm.addSelectionInterval(index, index);
                }
            }
            else {
                sm.setSelectionInterval(index, index);
            }
        }
    }
 */
    /**
     * Returns <code>visibleRect.height</code> or
     * <code>visibleRect.width</code>,
     * depending on this table's orientation.  Note that as of Swing 1.1.1
     * (Java 2 v 1.2.2) the value
     * returned will ensure that the viewport is cleanly aligned on
     * a row boundary.
     *
     * @return <code>visibleRect.height</code> or
     * 					<code>visibleRect.width</code>
     * 					per the orientation
     * @see Scrollable#getScrollableBlockIncrement
     */
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        
        if (getRowCount() == 0) {
            // Short-circuit empty table model
            if (SwingConstants.VERTICAL == orientation) {
                int rh = getRowHeight();
                return (rh > 0) ? Math.max(rh, (visibleRect.height / rh) * rh) :
                    visibleRect.height;
            } else {
                return visibleRect.width;
            }
        }
        // Shortcut for vertical scrolling of a table w/ uniform row height
        
        RowTemplate rt = getColumnModelTemplate();
        
        if (null == this.rowHeights && SwingConstants.VERTICAL == orientation) {
            int row = rowAtPoint(visibleRect.getLocation());
            assert row != -1;
            
            int col = rt.columnAtPoint(visibleRect.getLocation(),row);
            Rectangle cellRect = rt.getCellRect(row, col, true);
            
            if (cellRect.y == visibleRect.y) {
                int rh = getRowHeight();
                assert rh > 0;
                return Math.max(rh, (visibleRect.height / rh) * rh);
            }
        }
        if (direction < 0) {
            return getPreviousBlockIncrement(visibleRect, orientation, rt);
        } else {
            return getNextBlockIncrement(visibleRect, orientation,rt);
        }
    }
    
    /**
     * Called to get the block increment for downward scrolling in cases of
     * horizontal scrolling, or for vertical scrolling of a table with
     * variable row heights.
     */
    private int getNextBlockIncrement(Rectangle visibleRect,
            int orientation, RowTemplate rt) {
        // Find the cell at the trailing edge.  Return the distance to put
        // that cell at the leading edge.
        int trailingRow = getTrailingRow(visibleRect);
        int trailingCol = getTrailingCol(visibleRect,rt);
        
        Rectangle cellRect;
        boolean cellFillsVis;
        
        int cellLeadingEdge;
        int cellTrailingEdge;
        int newLeadingEdge;
        int visibleLeadingEdge = leadingEdge(visibleRect, orientation);
        
        // If we couldn't find trailing cell, just return the size of the
        // visibleRect.  Note that, for instance, we don't need the
        // trailingCol to proceed if we're scrolling vertically, because
        // cellRect will still fill in the required dimensions.  This would
        // happen if we're scrolling vertically, and the table is not wide
        // enough to fill the visibleRect.
        if (orientation == SwingConstants.VERTICAL && trailingRow < 0) {
            return visibleRect.height;
        } else if (orientation == SwingConstants.HORIZONTAL && trailingCol < 0) {
            return visibleRect.width;
        }
        
        cellRect = rt.getCellRect(trailingRow, trailingCol, true);
        cellLeadingEdge = leadingEdge(cellRect, orientation);
        cellTrailingEdge = trailingEdge(cellRect, orientation);
        
        if (orientation == SwingConstants.VERTICAL ||
                getComponentOrientation().isLeftToRight()) {
            cellFillsVis = cellLeadingEdge <= visibleLeadingEdge;
        } else { // Horizontal, right-to-left
            cellFillsVis = cellLeadingEdge >= visibleLeadingEdge;
        }
        
        if (cellFillsVis) {
            // The visibleRect contains a single large cell.  Scroll to the end
            // of this cell, so the following cell is the first cell.
            newLeadingEdge = cellTrailingEdge;
        } else if (cellTrailingEdge == trailingEdge(visibleRect, orientation)) {
            // The trailing cell happens to end right at the end of the
            // visibleRect.  Again, scroll to the beginning of the next cell.
            newLeadingEdge = cellTrailingEdge;
        } else {
            // Common case: the trailing cell is partially visible, and isn't
            // big enough to take up the entire visibleRect.  Scroll so it
            // becomes the leading cell.
            newLeadingEdge = cellLeadingEdge;
        }
        return Math.abs(newLeadingEdge - visibleLeadingEdge);
    }
    /**
     * Called to get the block increment for upward scrolling in cases of
     * horizontal scrolling, or for vertical scrolling of a table with
     * variable row heights.
     */
    private int getPreviousBlockIncrement(Rectangle visibleRect,
            int orientation, RowTemplate rt) {
        // Measure back from visible leading edge
        // If we hit the cell on its leading edge, it becomes the leading cell.
        // Else, use following cell
        
        int row;
        int col;
        
        int   newEdge;
        Point newCellLoc;
        
        int visibleLeadingEdge = leadingEdge(visibleRect, orientation);
        boolean leftToRight = getComponentOrientation().isLeftToRight();
        int newLeadingEdge;
        
        // Roughly determine the new leading edge by measuring back from the
        // leading visible edge by the size of the visible rect, and find the
        // cell there.
        if (orientation == SwingConstants.VERTICAL) {
            newEdge = visibleLeadingEdge - visibleRect.height;
            int x = visibleRect.x + (leftToRight ? 0 : visibleRect.width);
            newCellLoc = new Point(x, newEdge);
        } else if (leftToRight) {
            newEdge = visibleLeadingEdge - visibleRect.width;
            newCellLoc = new Point(newEdge, visibleRect.y);
        } else { // Horizontal, right-to-left
            newEdge = visibleLeadingEdge + visibleRect.width;
            newCellLoc = new Point(newEdge, visibleRect.y);
        }
        row = rowAtPoint(newCellLoc);
        col = rt.columnAtPoint(newCellLoc);
        
        // If we're measuring past the beginning of the table, we get an invalid
        // cell.  Just go to the beginning of the table in this case.
        if (orientation == SwingConstants.VERTICAL & row < 0) {
            newLeadingEdge = 0;
        } else if (orientation == SwingConstants.HORIZONTAL & col < 0) {
            if (leftToRight) {
                newLeadingEdge = 0;
            } else {
                newLeadingEdge = getWidth();
            }
        } else {
            // Refine our measurement
            Rectangle newCellRect = rt.getCellRect(row, col, true);
            int newCellLeadingEdge = leadingEdge(newCellRect, orientation);
            int newCellTrailingEdge = trailingEdge(newCellRect, orientation);
            
            // Usually, we hit in the middle of newCell, and want to scroll to
            // the beginning of the cell after newCell.  But there are a
            // couple corner cases where we want to scroll to the beginning of
            // newCell itself.  These cases are:
            // 1) newCell is so large that it ends at or extends into the
            //    visibleRect (newCell is the leading cell, or is adjacent to
            //    the leading cell)
            // 2) newEdge happens to fall right on the beginning of a cell
            
            // Case 1
            if ((orientation == SwingConstants.VERTICAL || leftToRight) &&
                    (newCellTrailingEdge >= visibleLeadingEdge)) {
                newLeadingEdge = newCellLeadingEdge;
            } else if (orientation == SwingConstants.HORIZONTAL &&
                    !leftToRight &&
                    newCellTrailingEdge <= visibleLeadingEdge) {
                newLeadingEdge = newCellLeadingEdge;
            }
            // Case 2:
            else if (newEdge == newCellLeadingEdge) {
                newLeadingEdge = newCellLeadingEdge;
            }
            // Common case: scroll to cell after newCell
            else {
                newLeadingEdge = newCellTrailingEdge;
            }
        }
        return Math.abs(visibleLeadingEdge - newLeadingEdge);
    }
    
    /**
     * @RUS
     *   Вычисление приращения по вертикали основано на высотах рядов. При выполнении
     *   горизонтального скроллинга в качестве основы берется шаблон с ключом "COLUMNMODEL".
     * @ENDRUS
     * Returns the scroll increment (in pixels) that completely exposes one new
     * row or column (depending on the orientation).
     * <p>
     * This method is called each time the user requests a unit scroll.
     *
     * @param visibleRect the view area visible within the viewport
     * @param orientation either <code>SwingConstants.VERTICAL</code>
     *                	or <code>SwingConstants.HORIZONTAL</code>
     * @param direction less than zero to scroll up/left,
     *                  greater than zero for down/right
     * @return the "unit" increment for scrolling in the specified direction
     * @see Scrollable#getScrollableUnitIncrement
     */
    
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation,
            int direction) {
        System.out.println("getScrollableUnitIncrement()");
        int leadingRow;
        int leadingCol;
        Rectangle leadingCellRect;
        
        int leadingVisibleEdge;
        int leadingCellEdge;
        int leadingCellSize;
        
        
        leadingRow = getLeadingRow(visibleRect);
        RowTemplate rt = getRowTemplate(leadingRow);
        
        leadingCol = getLeadingCol(visibleRect, this.getColumnModelTemplate());
        
        if (orientation == SwingConstants.VERTICAL && leadingRow < 0) {
            // Couldn't find leading row - return some default value
            return getRowHeight();
        } else if (orientation == SwingConstants.HORIZONTAL && leadingCol < 0) {
            // Couldn't find leading col - return some default value
            return 100;
        }
        
        // Note that it's possible for one of leadingCol or leadingRow to be
        // -1, depending on the orientation.  This is okay, as getCellRect()
        // still provides enough information to calculate the unit increment.
        leadingCellRect = getColumnModelTemplate().getCellRect(leadingRow, leadingCol, true);
        System.out.println("leadingRow=" + leadingRow + "; leadingCol="+leadingCol+ "; leadingCellRect=" + leadingCellRect);
        leadingVisibleEdge = leadingEdge(visibleRect, orientation);
        leadingCellEdge = leadingEdge(leadingCellRect, orientation);
        
        if (orientation == SwingConstants.VERTICAL) {
            //leadingCellSize = leadingCellRect.height;
            leadingCellSize = rt.getHeight();
            
        } else {
            leadingCellSize = leadingCellRect.width;
        }
        
        // 4 cases:
        // #1: Leading cell fully visible, reveal next cell
        // #2: Leading cell fully visible, hide leading cell
        // #3: Leading cell partially visible, hide rest of leading cell
        // #4: Leading cell partially visible, reveal rest of leading cell
        
        if (leadingVisibleEdge == leadingCellEdge) { // Leading cell is fully
            // visible
            // Case #1: Reveal previous cell
            if (direction < 0) {
                int retVal = 0;
                
                if (orientation == SwingConstants.VERTICAL) {
                    // Loop past any zero-height rows
                    while (--leadingRow >= 0) {
                        retVal = getRowHeight(leadingRow);
                        if (retVal != 0) {
                            break;
                        }
                    }
                } else { // HORIZONTAL
                    // Loop past any zero-width cols
                    while (--leadingCol >= 0) {
                        retVal = getColumnModelTemplate().getCellRect(leadingRow, leadingCol, true).width;
                        if (retVal != 0) {
                            break;
                        }
                    }
                }
                return retVal;
            } else { // Case #2: hide leading cell
                return leadingCellSize;
            }
        } else { // Leading cell is partially hidden
            // Compute visible, hidden portions
            int hiddenAmt = Math.abs(leadingVisibleEdge - leadingCellEdge);
            int visibleAmt = leadingCellSize - hiddenAmt;
            
            if (direction > 0) {
                // Case #3: hide showing portion of leading cell
                return visibleAmt;
            } else { // Case #4: reveal hidden portion of leading cell
                return hiddenAmt;
            }
        }
    }    
/*    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation,
            int direction) {
        System.out.println("getScrollableUnitIncrement()");
        int leadingRow;
        int leadingCol;
        Rectangle leadingCellRect;
        
        int leadingVisibleEdge;
        int leadingCellEdge;
        int leadingCellSize;
        
        
        leadingRow = getLeadingRow(visibleRect);
        RowTemplate rt = getRowTemplate(leadingRow);
        
        leadingCol = getLeadingCol(visibleRect, rt);
        
        if (orientation == SwingConstants.VERTICAL && leadingRow < 0) {
            // Couldn't find leading row - return some default value
            return getRowHeight();
        } else if (orientation == SwingConstants.HORIZONTAL && leadingCol < 0) {
            // Couldn't find leading col - return some default value
            return 100;
        }
        
        // Note that it's possible for one of leadingCol or leadingRow to be
        // -1, depending on the orientation.  This is okay, as getCellRect()
        // still provides enough information to calculate the unit increment.
        leadingCellRect = rt.getCellRect(leadingRow, leadingCol, true);
        System.out.println("leadingRow=" + leadingRow + "; leadingCol="+leadingCol+ "; leadingCellRect=" + leadingCellRect);
        leadingVisibleEdge = leadingEdge(visibleRect, orientation);
        leadingCellEdge = leadingEdge(leadingCellRect, orientation);
        
        if (orientation == SwingConstants.VERTICAL) {
            //leadingCellSize = leadingCellRect.height;
            leadingCellSize = rt.getHeight();
            
        } else {
            leadingCellSize = leadingCellRect.width;
        }
        
        // 4 cases:
        // #1: Leading cell fully visible, reveal next cell
        // #2: Leading cell fully visible, hide leading cell
        // #3: Leading cell partially visible, hide rest of leading cell
        // #4: Leading cell partially visible, reveal rest of leading cell
        
        if (leadingVisibleEdge == leadingCellEdge) { // Leading cell is fully
            // visible
            // Case #1: Reveal previous cell
            if (direction < 0) {
                int retVal = 0;
                
                if (orientation == SwingConstants.VERTICAL) {
                    // Loop past any zero-height rows
                    while (--leadingRow >= 0) {
                        retVal = getRowHeight(leadingRow);
                        if (retVal != 0) {
                            break;
                        }
                    }
                } else { // HORIZONTAL
                    // Loop past any zero-width cols
                    while (--leadingCol >= 0) {
                        retVal = rt.getCellRect(leadingRow, leadingCol, true).width;
                        if (retVal != 0) {
                            break;
                        }
                    }
                }
                return retVal;
            } else { // Case #2: hide leading cell
                return leadingCellSize;
            }
        } else { // Leading cell is partially hidden
            // Compute visible, hidden portions
            int hiddenAmt = Math.abs(leadingVisibleEdge - leadingCellEdge);
            int visibleAmt = leadingCellSize - hiddenAmt;
            
            if (direction > 0) {
                // Case #3: hide showing portion of leading cell
                return visibleAmt;
            } else { // Case #4: reveal hidden portion of leading cell
                return hiddenAmt;
            }
        }
    }
*/    
    /*
     * Return the row at the bottom of the visibleRect.
     *
     * May return -1
     */
    private int getTrailingRow(Rectangle visibleRect) {
        Point trailingPoint;
        
        if (getComponentOrientation().isLeftToRight()) {
            trailingPoint = new Point(visibleRect.x,
                    visibleRect.y + visibleRect.height - 1);
        } else {
            trailingPoint = new Point(visibleRect.x + visibleRect.width,
                    visibleRect.y + visibleRect.height - 1);
        }
        return rowAtPoint(trailingPoint);
    }
    
    /*
     * Return the column at the trailing edge of the visibleRect.
     *
     * May return -1
     */
    private int getTrailingCol(Rectangle visibleRect, RowTemplate rt) {
        Point trailingPoint;
        
        if (getComponentOrientation().isLeftToRight()) {
            trailingPoint = new Point(visibleRect.x + visibleRect.width - 1,
                    visibleRect.y);
        } else {
            trailingPoint = new Point(visibleRect.x, visibleRect.y);
        }
        return rt.columnAtPoint(trailingPoint);
    }
    
    private int getLeadingRow(Rectangle visibleRect) {
        Point leadingPoint;
        
        if (getComponentOrientation().isLeftToRight()) {
            leadingPoint = new Point(visibleRect.x, visibleRect.y);
        } else {
            leadingPoint = new Point(visibleRect.x + visibleRect.width,
                    visibleRect.y);
        }
        return rowAtPoint(leadingPoint);
    }
    
    /*
     * Return the column at the leading edge of the visibleRect.
     *
     * May return -1
     */
    private int getLeadingCol(Rectangle visibleRect, RowTemplate rt) {
        Point leadingPoint;
        
        if (getComponentOrientation().isLeftToRight()) {
            leadingPoint = new Point(visibleRect.x, visibleRect.y);
        } else {
            leadingPoint = new Point(visibleRect.x + visibleRect.width,
                    visibleRect.y);
        }
        return rt.columnAtPoint(leadingPoint);
    }
    /*
     * Returns the leading edge ("beginning") of the given Rectangle.
     * For VERTICAL, this is the top, for left-to-right, the left side, and for
     * right-to-left, the right side.
     */
    private int leadingEdge(Rectangle rect, int orientation) {
        if (orientation == SwingConstants.VERTICAL) {
            return rect.y;
        } else if (getComponentOrientation().isLeftToRight()) {
            return rect.x;
        } else { // Horizontal, right-to-left
            return rect.x + rect.width;
        }
    }
    /*
     * Returns the trailing edge ("end") of the given Rectangle.
     * For VERTICAL, this is the bottom, for left-to-right, the right side, and
     * for right-to-left, the left side.
     */
    private int trailingEdge(Rectangle rect, int orientation) {
        if (orientation == SwingConstants.VERTICAL) {
            return rect.y + rect.height;
        } else if (getComponentOrientation().isLeftToRight()) {
            return rect.x + rect.width;
        } else { // Horizontal, right-to-left
            return rect.x;
        }
    }
    
    public boolean isEnterAsTab() {
        return this.enterAsTab;
    }
    
    public void setEnterAsTab(boolean enterAsTab) {
        this.enterAsTab = enterAsTab;
    }
    
    public int getActiveColumn() {
        return activeColumn;
    }
    public void setActiveColumn( int newValue) {
        int oldValue = this.activeColumn;
        this.activeColumn = newValue;
        this.activeColumnChanged();
        firePropertyChange("activeColumn", oldValue, newValue);
        // force fireing when oldValue == newValue. For freezed columns
        firePropertyChange("freezeActiveColumn", newValue+1, newValue);
    }
    
    protected boolean setActiveColumn( int newRow, int newColumn) {
        RowTemplate rt = this.getRowTemplate(newRow);
        if ( newColumn >= rt.getColumnCount() )
            return false;
        int oldValue = this.activeColumn;
        this.activeColumn = newColumn;
        this.activeColumnChanged();
        firePropertyChange("activeColumn", oldValue, newColumn);
        // force fireing when oldValue == newValue. For freezed columns
        firePropertyChange("freezeActiveColumn", newColumn+1, newColumn);
        return true;
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
    
    //////////////////////////***************************************
    // This class tracks changes in the keyboard focus state. It is used
    // when the TableViewer is editing to determine when to cancel the edit.
    // If focus switches to a component outside of the tableex, but in the
    // same window, this will cancel editing.
    /**
     * The source code is borrowed from {@link javax.swing.JTable}
     */
    protected class CellEditorRemover implements PropertyChangeListener {
        KeyboardFocusManager focusManager;
        
        public CellEditorRemover(KeyboardFocusManager fm) {
            this.focusManager = fm;
        }
        
        public void propertyChange(PropertyChangeEvent ev) {
            if (!isEditing() || getClientProperty("terminateEditOnFocusLost") != Boolean.TRUE) {
                return;
            }
            
            Component c = focusManager.getPermanentFocusOwner();
            while (c != null) {
                if (c == TableViewer.this) {
                    // focus remains inside the table
                    return;
                } else if ((c instanceof Window) ||
                        (c instanceof Applet && c.getParent() == null)) {
                    if (c == SwingUtilities.getRoot(TableViewer.this)) {
                        if (!getCellEditor().stopCellEditing()) {
                            getCellEditor().cancelCellEditing();
                        }
                    }
                    break;
                }
                c = c.getParent();
            }
        }
    }
    public static TableViewer table;
    
    
}  // End of Class TableViewer
