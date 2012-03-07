/*
 * DataViewerUI.java
 *
 * Created on 18 Апрель 2007 г., 12:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tdo.swing.table;

import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author valery
 */
public class DataViewerUI extends BasicTableUI {

    protected TableViewer tableViewer;
    protected CellRendererPane cellRendererPane;
    protected KeyListener keyListener;
    protected MouseInputListener mouseInputListener;
    protected FocusListener FocusListener;

    /**
     * Creates a new instance of DataViewerUI
     */
    public DataViewerUI() {
        super();
//        this.installUI();
    }

    /**
     * Configures the specified component appropriate for the look and feel.
     * This method is invoked when the
     * <code>ComponentUI</code> instance is being installed as the UI delegate
     * on the specified component. This method should completely configure the
     * component for the look and feel, including the following: <ol>
     * <li>Install any default property values for color, fonts, borders, icons,
     * opacity, etc. on the component. Whenever possible, property values
     * initialized by the client program should <i>not</i> be overridden.
     * <li>Install a
     * <code>LayoutManager</code> on the component if necessary. <li>Create/add
     * any required sub-components to the component. <li>Create/install event
     * listeners on the component. <li>Create/install a
     * <code>PropertyChangeListener</code> on the component in order to detect
     * and respond to component property changes appropriately. <li>Install
     * keyboard UI (mnemonics, traversal, etc.) on the component. <li>Initialize
     * any appropriate instance data. </ol>
     *
     * @param c the component where this UI delegate is being installed
     *
     * @see #uninstallUI
     * @see javax.swing.JComponent#setUI
     * @see javax.swing.JComponent#updateUI
     */
    @Override
    public void installUI(JComponent c) {
        //c.setBackground( Color.RED);
        super.table = (TableViewer) c;
        tableViewer = (TableViewer) super.table;
        cellRendererPane = new CellRendererPane();
        tableViewer.add(cellRendererPane);
        installDefaults();
        //installDefaults2();
        installListeners();
        //installKeyboardActions();
    }

    /**
     * Initialize JTable properties, e.g. font, foreground, and background. The
     * font, foreground, and background properties are only set if their current
     * value is either null or a UIResource, other properties are set if the
     * current value is null.
     *
     * @see #installUI
     */
    @Override
    protected void installDefaults() {
        LookAndFeel.installColorsAndFont(tableViewer, "Table.background",
                "Table.foreground", "Table.font");
        // JTable's original row height is 16.  To correctly display the
        // contents on Linux we should have set it to 18, Windows 19 and
        // Solaris 20.  As these values vary so much it's too hard to
        // be backward compatable and try to update the row height, we're
        // therefor NOT going to adjust the row height based on font.  If the
        // developer changes the font, it's there responsability to update
        // the row height.

        LookAndFeel.installProperty(tableViewer, "opaque", Boolean.TRUE);

        Color sbg = tableViewer.getSelectionBackground();
        if (sbg == null || sbg instanceof UIResource) {
            tableViewer.setSelectionBackground(UIManager.getColor("Table.selectionBackground"));
        }

        Color sfg = tableViewer.getSelectionForeground();
        if (sfg == null || sfg instanceof UIResource) {
            tableViewer.setSelectionForeground(UIManager.getColor("Table.selectionForeground"));
        }

        Color gridColor = tableViewer.getGridColor();
        if (gridColor == null || gridColor instanceof UIResource) {
            gridColor = UIManager.getColor("Table.gridColor");
            //MyNew t.setGridColor(UIManager.getColor("Table.gridColor"));
            tableViewer.setGridColor(gridColor != null ? gridColor : Color.GRAY);
        }

        // install the scrollpane border
        Container parent = tableViewer.getParent();  // should be viewport
        if (parent != null) {
            parent = parent.getParent();  // should be the scrollpane
            if (parent != null && parent instanceof JScrollPane) {
                LookAndFeel.installBorder((JScrollPane) parent, "Table.scrollPaneBorder");
            }
        }


    }

    /**
     * Reverses configuration which was done on the specified component during
     * <code>installUI</code>. This method is invoked when this
     * <code>UIComponent</code> instance is being removed as the UI delegate for
     * the specified component. This method should undo the configuration
     * performed in
     * <code>installUI</code>, being careful to leave the
     * <code>JComponent</code> instance in a clean state (no extraneous
     * listeners, look-and-feel-specific property objects, etc.). This should
     * include the following: <ol> <li>Remove any UI-set borders from the
     * component. <li>Remove any UI-set layout managers on the component.
     * <li>Remove any UI-added sub-components from the component. <li>Remove any
     * UI-added event/property listeners from the component. <li>Remove any
     * UI-installed keyboard UI from the component. <li>Nullify any allocated
     * instance data objects to allow for GC. </ol>
     *
     * @param c the component from which this UI delegate is being removed; this
     * argument is often ignored, but might be used if the UI object is
     * stateless and shared by multiple components
     *
     * @see #installUI
     * @see javax.swing.JComponent#updateUI
     */
    @Override
    public void uninstallUI(JComponent c) {
        //uninstallDefaults();
        uninstallListeners();
        //uninstallKeyboardActions();

        tableViewer.remove(cellRendererPane);
        cellRendererPane = null;

        tableViewer = null;
    }

    /**
     * Attaches listeners to the JTable.
     */
    @Override
    protected void installListeners() {
        keyListener = createKeyListener();
        tableViewer.addKeyListener(keyListener);
        mouseInputListener = createMouseInputListener();
        tableViewer.addMouseListener(mouseInputListener);

        /*
         * focusListener = createFocusListener();
         *
         *
         * t.addFocusListener(focusListener);
         *
         * t.addMouseMotionListener(mouseInputListener);
         * t.addPropertyChangeListener(getHandler()); if (isFileList) {
         * t.getSelectionModel().addListSelectionListener(getHandler()); }
         */
    }

    @Override
    protected void uninstallListeners() {
        //table.removeFocusListener(focusListener);
        tableViewer.removeKeyListener(keyListener);
        tableViewer.removeMouseListener(mouseInputListener);
        /*
         * t.removeMouseMotionListener(mouseInputListener);
         * t.removePropertyChangeListener(getHandler()); if (isFileList) {
         * t.getSelectionModel().removeListSelectionListener(getHandler());
         * }
         *
         * focusListener = null;
         */
        keyListener = null;
        mouseInputListener = null;
        //     handler = null;
    }

    /**
     * Paint a representation of the
     * <code>t</code> instance that was set in installUI().
     */
    @Override
    public void paint(Graphics g, JComponent comp) {
        //super.paint(g,comp);
        Rectangle c = this.tableViewer.getVisibleRect();
        //if (this.t.getModel() == null || gridModelSupport.isClosed()) {
        if (this.tableViewer.getModel() == null) {
            g.setColor(this.tableViewer.getBackground());
            return;
        }
        update(g);
    }

    public void update(Graphics g) {
        if (this.tableViewer.getModel() == null) {
            return;
        }

        Rectangle c;
        c = this.tableViewer.getVisibleRect();
        if (c == null) {
            return;
        }
        //Rectangle c = getViewportRect();
        Point upperLeft = new Point(c.x, c.y);
        Point lowerRight = new Point(c.x + c.width - 1, c.y
                + c.height - 1);

        int rMin = tableViewer.rowAtPoint(upperLeft);
        int rMax = tableViewer.rowAtPoint(lowerRight);

        // This should never happen.
        if (rMin == -1) {
            rMin = 0;
        }
        if (rMax == -1) {
            // В таблице меньше рядов, чем требуется для заполнения
            // clip-области
            rMax = tableViewer.getModel().getRowCount() - 1;
        }
        /**
         * Найдем ряд с минимальным индексом и такой, что его прямоугольник
         * пересекается с clipRect.
         */
        if (tableViewer.getModel() != null) {
            int rc = tableViewer.getModel().getRowCount();
            for (int i = rMin; i <= rMax; i++) {
                paintRow(g, i);
            }
        }
        int cMin = 0;
        int cMax = tableViewer.getColumnCount() - 1;

        // Remove any renderers that may be left in the cellRendererPane.
        cellRendererPane.removeAll();

    }

    /**
     *
     * @param g
     * @param rowIndex
     */
    public void paintRow(Graphics g, int rowIndex) {
        TableViewer t = (TableViewer) this.tableViewer;
        //String columnModelKey = this.gridModelSupport.getColumnModelKey(rowIndex);
        RowTemplate rowTemplate = t.getRowTemplate(rowIndex);

        Rectangle r = t.getRowRect(rowIndex);
        if (t.isRowActive(rowIndex)) {
            // Заполним ряд цветом для НЕ активного ряда
            g.setColor(t.getBackground());
            g.fillRect(r.x, r.y, r.width, r.height);
        } else {
            // Заполним ряд цветом для НЕ активного ряда

            g.setColor(t.getBackground());
            g.fillRect(r.x, r.y, r.width, r.height);

        }

        if (t.getColumnModel() == null) {
            return;
        }
        TableCell column;//My 06.03.2012 = null;
        for (int i = 0; i < rowTemplate.getColumnCount(); i++) {
            column = rowTemplate.getColumn(i);
            boolean isLastCol = false;
            if (i == rowTemplate.getColumnCount() - 1) {
                isLastCol = true;
            }
            paintCell(g, rowTemplate, column, rowIndex, i, isLastCol);
        }

    }

    /**
     *
     * @param g
     * @param rowTemplate
     * @param column
     * @param columnIndex
     * @param isLastColumn
     * @param rowIndex
     */
    public void paintCell(Graphics g, RowTemplate rowTemplate,
            TableCell column,
            int rowIndex, int columnIndex, boolean isLastColumn) {
        TableViewer t = (TableViewer) this.tableViewer;

        if (column.getModelIndex() < 0) {
            return;
        }
        //My 06.03.2012 int modelIndex = column.getModelIndex();
        int x = column.getX();
        int y;
        if (t.getRowHeights() == null) {
            y = rowIndex * t.getRowHeight() + column.getY();
        } else {
            y = t.getRowHeights().getPosition(rowIndex) + column.getY();
        }

        TableCellRenderer renderer = t.getViewCellRenderer(rowIndex, columnIndex);
        if (renderer == null) {
            return;
        }
        Component renderComp = null;
        try {
            renderComp = t.prepareViewRenderer(renderer, rowIndex, columnIndex);
        } catch (Exception e) {
            System.out.println("$$$$$$$$$$ row:column " + rowIndex + " : " + columnIndex
                    + "###### " + e.getMessage());
        }
        Rectangle cellRect = new Rectangle(x, y, column.getWidth(),
                column.getHeight());
        int rm = Math.min(t.getRowMargin(), cellRect.height);
        int cm = Math.min(t.getColumnModel().getColumnMargin(), cellRect.width);
        // This is not the same as grow(), it rounds differently.
        if (t.isEditing() && t.getEditingRow() == rowIndex
                && t.getEditingColumn() == columnIndex) {
            Component component = t.getEditorComponent();
            component.setBounds(cellRect);
            component.validate();
        }

        cellRect.setBounds(cellRect.x + cm / 2, cellRect.y + rm / 2,
                cellRect.width - cm, cellRect.height - rm);

        boolean actionFlag = true;
        if (actionFlag) {
            cellRendererPane.paintComponent(g, renderComp, t, cellRect.x, cellRect.y,
                    cellRect.width, cellRect.height, true);

        }


    }

    @Override
    protected KeyListener createKeyListener() {
        return new KeyBoardHandler();
    }

    @Override
    protected FocusListener createFocusListener() {
        //this.focusListener = new FocusHandler();
        return null;
    }

    @Override
    protected MouseInputListener createMouseInputListener() {

        return new MouseInputHandler();
    }

    /**
     * Paints the specified component appropriate for the look and feel. This
     * method is invoked from the
     * <code>ComponentUI.update</code> method when the specified component is
     * being painted. Subclasses should override this method and use the
     * specified
     * <code>Graphics</code> object to render the content of the component.
     *
     * @param g the
     * <code>Graphics</code> context in which to paint
     * @param c the component being painted; this argument is often ignored, but
     * might be used if the UI object is stateless and shared by multiple
     * components
     *
     * @see #update
     */
    /*
     * public void paint(Graphics g, JComponent c) { Rectangle r =
     * c.getBounds(); g.fillRoundRect(r.x,r.y,r.width,r.height, 10,10); }
     */
    /**
     * Notifies this UI delegate that it's time to paint the specified
     * component. This method is invoked by
     * <code>JComponent</code> when the specified component is being painted. By
     * default this method will fill the specified component with its background
     * color (if its
     * <code>opaque</code> property is
     * <code>true</code>) and then immediately call
     * <code>paint</code>. In general this method need not be overridden by
     * subclasses; all look-and-feel rendering code should reside in the
     * <code>paint</code> method.
     *
     * @param g the
     * <code>Graphics</code> context in which to paint
     * @param c the component being painted; this argument is often ignored, but
     * might be used if the UI object is stateless and shared by multiple
     * components
     *
     * @see #paint
     * @see javax.swing.JComponent#paintComponent
     */
    @Override
    public void update(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
        paint(g, c);
    }

    /**
     * Returns an instance of the UI delegate for the specified component. Each
     * subclass must provide its own static
     * <code>createUI</code> method that returns an instance of that UI delegate
     * subclass. If the UI delegate subclass is stateless, it may return an
     * instance that is shared by multiple components. If the UI delegate is
     * stateful, then it should return a new instance per component. The default
     * implementation of this method throws an error, as it should never be
     * invoked.
     */
    public static ComponentUI createUI(JComponent c) {
        return new DataViewerUI();
    }

    ///////////////////////////////////////////
    //
    ///////////////////////////////////////////
    protected void selectRow(int direction) {

        int rowCount = tableViewer.getModel().getRowCount();
        if (rowCount == 0) {
            return;
        }

        ListSelectionModel lsm = tableViewer.getSelectionModel();
        //ListSelectionModel lcsm = t.getColumnModel().getSelectionModel();
        if (lsm == null) {
            return;
        }

        int selectedRow = lsm.getMinSelectionIndex();
        int selectedColumn = tableViewer.getActiveColumn();

        if (selectedRow < 0 || selectedRow > rowCount - 1) {
            if (selectedRow < 0) {
                selectedRow = 0;
            }
            if (selectedRow >= rowCount - 1) {
                selectedRow = rowCount - 1;
            }
            Rectangle r = tableViewer.getViewCellRect(selectedRow, 0, false);
            tableViewer.setRowSelectionInterval(selectedRow, selectedRow);
            tableViewer.setActiveColumn(0);
            //table.setColumnSelectionInterval(0,0);
            TableUtility.makeCellVisible(tableViewer, selectedRow, 0);
            return;
        }

        if (direction < 0 && selectedRow != 0) {
            selectedRow--;
        } else if (direction > 0 && selectedRow < rowCount - 1) {
            selectedRow++;
        }

        if (selectedColumn < 0) {
            selectedColumn = 0;
        } else {
            RowTemplate rt = tableViewer.getRowTemplate(selectedRow);
            if (selectedColumn >= rt.getColumnCount() - 1) {
                selectedColumn = rt.getColumnCount() - 1;
            }
        }

        tableViewer.setRowSelectionInterval(selectedRow, selectedRow);
        //table.setColumnSelectionInterval(selectedColumn,selectedColumn);
        tableViewer.setActiveColumn(selectedColumn);
        TableUtility.makeCellVisible(tableViewer, selectedRow, selectedColumn);

    }

    protected Point prepareSelection() {
        int rowCount = tableViewer.getModel().getRowCount();
        if (rowCount == 0) {
            return null;
        }
        ListSelectionModel lsm = tableViewer.getSelectionModel();
        //ListSelectionModel lcsm = t.getColumnModel().getSelectionModel();
        if (lsm == null) {
            return null;
        }

        int selectedRow = lsm.getMinSelectionIndex();
        int selectedColumn = tableViewer.getActiveColumn();
        //int selectedColumn = lcsm.getMinSelectionIndex();
        if (selectedRow < 0 || selectedRow > rowCount - 1) {
            if (selectedRow < 0) {
                selectedRow = 0;
            }
            if (selectedRow > rowCount - 1) {
                selectedRow = rowCount - 1;
            }
            Rectangle r = tableViewer.getViewCellRect(selectedRow, 0, false);
            tableViewer.setRowSelectionInterval(selectedRow, selectedRow);
            //table.setColumnSelectionInterval(0,0);
            tableViewer.setActiveColumn(0);
            TableUtility.makeCellVisible(tableViewer, selectedRow, 0);
            return null;
        }
        return new Point(selectedRow, selectedColumn);
    }

    protected void selectNextPage() {
        Point p = prepareSelection();
        if (p == null) {
            return;
        }
        int selectedRow = p.x;
        int selectedColumn = p.y;

        if (selectedColumn < 0) {
            selectedColumn = 0;
        }

        Rectangle r = tableViewer.getRowRect(selectedRow);
        int incr = tableViewer.getScrollableBlockIncrement(tableViewer.getVisibleRect(), SwingConstants.VERTICAL, 1);

        selectedRow = tableViewer.rowAtPoint(new Point(0, r.y + incr));
        if (selectedRow < 0) {
            selectedRow = tableViewer.getModel().getRowCount() - 1;
        }

        RowTemplate rt = tableViewer.getRowTemplate(selectedRow);
        if (selectedColumn >= rt.getColumnCount() - 1) {
            selectedColumn = rt.getColumnCount() - 1;
        }

        tableViewer.setRowSelectionInterval(selectedRow, selectedRow);
        //table.setColumnSelectionInterval(selectedColumn,selectedColumn);
        tableViewer.setActiveColumn(selectedColumn);
        TableUtility.makeCellVisible(tableViewer, selectedRow, selectedColumn);

    }

    protected void selectPriorPage() {
        Point p = prepareSelection();
        if (p == null) {
            return;
        }
        int selectedRow = p.x;
        int selectedColumn = p.y;

        if (selectedColumn < 0) {
            selectedColumn = 0;
        }

        Rectangle r = tableViewer.getRowRect(selectedRow);
        int incr = tableViewer.getScrollableBlockIncrement(tableViewer.getVisibleRect(), SwingConstants.VERTICAL, 1);

        selectedRow = tableViewer.rowAtPoint(new Point(0, r.y - incr));
        if (selectedRow <= 0) {
            selectedRow = 0;
        }

        RowTemplate rt = tableViewer.getRowTemplate(selectedRow);
        if (selectedColumn >= rt.getColumnCount() - 1) {
            selectedColumn = rt.getColumnCount() - 1;
        }

        tableViewer.setRowSelectionInterval(selectedRow, selectedRow);
//        t.setColumnSelectionInterval(selectedColumn,selectedColumn);
        tableViewer.setActiveColumn(selectedColumn);
        TableUtility.makeCellVisible(tableViewer, selectedRow, selectedColumn);
    }

    protected void selectNextColumn() {
        Point p = prepareSelection();
        if (p == null) {
            return;
        }
        int selectedRow = p.x;
        int selectedColumn = p.y;

        if (selectedColumn < 0) {
            selectedColumn = 0;
        } else {
            RowTemplate rt = tableViewer.getRowTemplate(selectedRow);
            if (selectedColumn >= rt.getColumnCount() - 1) {
                selectedColumn = rt.getColumnCount() - 1;
            } else {
                selectedColumn++;
            }
        }

//        t.setColumnSelectionInterval(selectedColumn,selectedColumn);
        tableViewer.setActiveColumn(selectedColumn);
        TableUtility.makeCellVisible(tableViewer, selectedRow, selectedColumn);
    }

    protected void selectOnTab() {
        Point p = prepareSelection();
        if (p == null) {
            return;
        }
        int selectedRow = p.x;
        int selectedColumn = p.y;

        if (selectedColumn < 0) {
            selectedColumn = 0;
        } else {
            RowTemplate rt = tableViewer.getRowTemplate(selectedRow);
            if (selectedColumn > rt.getColumnCount() - 1) {
                selectedColumn = rt.getColumnCount() - 1;
            } else if (selectedColumn == rt.getColumnCount() - 1) {
                int rowCount = tableViewer.getModel().getRowCount();
                if (selectedRow != rowCount - 1) {
                    selectedRow++;
                    selectedColumn = 0;
                    tableViewer.setRowSelectionInterval(selectedRow, selectedRow);
                }

            } else {
                selectedColumn++;
            }
        }

        //table.setColumnSelectionInterval(selectedColumn,selectedColumn);
        tableViewer.setActiveColumn(selectedColumn);
        TableUtility.makeCellVisible(tableViewer, selectedRow, selectedColumn);
    }

    protected void selectOnShiftTab() {
        Point p = prepareSelection();
        if (p == null) {
            return;
        }
        int selectedRow = p.x;
        int selectedColumn = p.y;

        RowTemplate rt;

        if (selectedColumn <= 0) {
            if (selectedRow != 0) {
                selectedRow--;
                rt = tableViewer.getRowTemplate(selectedRow);
                selectedColumn = rt.getColumnCount() - 1;
                tableViewer.setRowSelectionInterval(selectedRow, selectedRow);
            }

        } else {
            rt = tableViewer.getRowTemplate(selectedRow);
            if (selectedColumn > rt.getColumnCount() - 1) {
                selectedColumn = rt.getColumnCount() - 1;
            } else {
                selectedColumn--;
            }
        }

//        t.setColumnSelectionInterval(selectedColumn,selectedColumn);
        tableViewer.setActiveColumn(selectedColumn);
        TableUtility.makeCellVisible(tableViewer, selectedRow, selectedColumn);
    }

    protected void selectPriorColumn() {
        Point p = prepareSelection();
        if (p == null) {
            return;
        }
        int selectedRow = p.x;
        int selectedColumn = p.y;

        if (selectedColumn <= 0) {
            selectedColumn = 0;
        } else {
            RowTemplate rt = tableViewer.getRowTemplate(selectedRow);
            if (selectedColumn > rt.getColumnCount() - 1) {
                selectedColumn = rt.getColumnCount() - 1;
            } else {
                selectedColumn--;
            }
        }

//        t.setColumnSelectionInterval(selectedColumn,selectedColumn);
        tableViewer.setActiveColumn(selectedColumn);
        TableUtility.makeCellVisible(tableViewer, selectedRow, selectedColumn);
    }

    public class KeyBoardHandler implements KeyListener {

        /**
         * Обработчик события
         * <code>KeyPressed.</code><p>
         *
         * Блокируем нажатие любой навигационной клавиши, в случае, если модель
         * данных находится в состоянии "closed".
         *
         * @param e
         */
        public void keyPressed(KeyEvent e) {

            Component component = e.getComponent();
            if (!component.hasFocus()) {
                component.requestFocusInWindow();
            }

            int id = e.getID();
            int code;
            code = e.getKeyCode();

            switch (code) {
                case KeyEvent.VK_DOWN:
                    selectRow(1);
                    e.consume();
                    break;
                case KeyEvent.VK_UP:
                    selectRow(-1);
                    e.consume();
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    selectNextPage();
                    e.consume();
                    break;
                case KeyEvent.VK_PAGE_UP:
                    selectPriorPage();
                    e.consume();
                    break;
                case KeyEvent.VK_RIGHT:

                    selectNextColumn();
                    e.consume();
                    break;
                case KeyEvent.VK_LEFT:
                    selectPriorColumn();
                    e.consume();
                    break;

                case KeyEvent.VK_ENTER:
                    if (tableViewer.isEnterAsTab()) {
                        selectOnTab();
                    } else {
                        selectRow(1);
                    }
                    e.consume();
                    break;
                case KeyEvent.VK_TAB:
                    if (e.getModifiers() == KeyEvent.SHIFT_MASK) {
                        selectOnShiftTab();
                    } else {
                        selectOnTab();
                    }
                    e.consume();
                    break;

            } //switch
        }

        /**
         * Обработчик события
         * <code>KeyTyped.</code><p> Если ячейка таблицы "активна", но НЕ
         * находится в состоянии редактирования, то метод переводит ее это
         * состояние, вызывая метод
         * <code>adjustCellEditor(e)</code> в задачу которого входит настройка
         * редактора ячейки и передача ему введенного символа.<p> Блокируем
         * нажатие любой навигационной клавиши, в случае, если модель данных
         * находится в состоянии "closed".
         *
         * @param e типа
         * <code>KeyEvent</code>.
         * @see adjustCellEditor
         * @see keyPressed
         */
        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }
    }// class KeyBoardHandler

//********************************************************************************************
    public class MouseInputHandler implements MouseInputListener {

        public void mouseClicked(MouseEvent e) {
            Component component = e.getComponent();
            if (!component.hasFocus()) {
                component.requestFocusInWindow();
            }

        }

        /**
         * Обработчик события нажатия кнопки мыши.<p> Используя координаты
         * курсора мыши определяются номер ряда и колонки, которые содержат
         * позицию курсора. При этом учитывается, что пользователь может
         * щелкнуть мышью ВНЕ какой-либо ячейки таблицы, тогда метод не
         * производит никаких действий.<p> Если вычисленный номер ряда совпадает
         * с текущим активным рядом, то производится установка новой активной
         * ячейки текущего активного ряда. В противном случае, производится
         * попытка активизации нового ряда. попытка установки нового активного
         * ряда может привести к исключительной ситуации типа
         * <code>PropertyVetoException</code>, которая перехватывается и тогда
         * метод
         * <code>mousePressed</code> просто возвратит управление, не производя
         * каких-либо действий.
         *
         * @param e типа MouseEvent
         * @see setActiveColumn
         */
        public void mousePressed(MouseEvent e) {
            //ListSelectionModel lsm =
            //System.out.println("mousePressed");
            //table.getSelectionModel().setSelectionInterval(0,0);
            //table.getColumnModel().getSelectionModel().setSelectionInterval(0,1);
            // Блокируем нажатие левой кнопки, т.к Grid рассматривает ее как
            // навигационную, а модель данных находится в состоянии "closede".
            //    if (this.isDataModelClosed() && e.getModifiers() == e.BUTTON1_MASK )
            //      return;
            // выше строки как надо-бы сделать, т.к., например нажатие правой кнопки -
            // это другое или кнопка в сочетании с CTRL и т.п.
 /*
             * if (columnModels == null || this.isDataModelClosed()) { return; }
             */
            //my old this.requestFocus();
            Component component = e.getComponent();
            if (!e.getComponent().hasFocus()) {
                e.getComponent().requestFocusInWindow();
            }

            Point p = e.getPoint();

            TableViewer table = DataViewerUI.this.tableViewer;

            int oldRowIndex = table.getSelectedRow();
            // int s[] = t.getColumnModel().getSelectedColumns();

            int oldColumnIndex = -1;
            oldColumnIndex = table.getActiveColumn();

            int newRowIndex = table.rowAtPoint(p);

            if (newRowIndex < 0) {
                return;
            }

            RowTemplate rt = table.getRowTemplate(newRowIndex);
            int newColumnIndex = table.columnAtPoint(p, newRowIndex);
            table.setActiveColumn(newColumnIndex);
            if (newColumnIndex == 2 && newRowIndex == 2) {
                System.out.println("KKKKKKKKKKKKKK");
            }
            if (table.isEditing() && !table.getCellEditor().stopCellEditing()) {
                Component editorComponent = table.getEditorComponent();
                editorComponent.requestFocusInWindow();
                return;
            }
            table.editCellAt(rt, newRowIndex, newColumnIndex, e);
            Component editorComponent = table.getEditorComponent();
            if (editorComponent != null) {
                editorComponent.requestFocus();
                //return;
            }

            table.getSelectionModel().setSelectionInterval(newRowIndex, newRowIndex);
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }
    }
}//class DataViewerUI
