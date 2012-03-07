/*
 * PNumberDateCellEditor.java
 *
 * Created on 30 Октябрь 2006 г., 13:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tdo.swing.table.editor;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumn;
import tdo.DataColumn;
import tdo.DataColumn.PDBIntegerColumn;
import tdo.Table;
import tdo.swing.util.DataUtil;

/**
 * <p>Title: Filis Application</p> <p>Description: Freq Sensor's Support</p>
 * <p>Copyright: Copyright (c) 2003</p> <p>Company: IS</p>
 *
 * @author VNS
 * @version 1.0
 */
public class NumberDateCellEditor
        extends JTextField {
    // # я удалил для комп.implements TableCellEditor {

    /**
     * Могут быть заданы маски редактирования числовых полей и полей, содержащих
     * дату. Поддерживаются простейшие маски редактирования: <UL> <LI>
     * <i>знак-числа <b>шаблон-целых</b></i> <p> где <i>знак-числа</i> - это
     * символ <b>+</b> или <b>-</b>, а <b><i>цифровой шаблон</I></b> - это или
     * последовательность знаков <b>#</b> или (и) знаков <b>0</b>. При этом,
     * если применяется смешанный шаблон, т.е. используется как символ
     * <b>'0'</b>, так и символ <b>'#'</b>, то вначале должны идти символы
     * <b>'#'</b>, а затем символы <b>'0'</b>. Например: <p> ####00 означает:
     * <OL> <LI> допустим ввод только целых чисел ;</LI> <LI> обязательно должно
     * присутствовать хотя бы 2 цифры.</LI> </OL> </LI> <LI> <i>знак-числа
     * <b>шаблон-целых.шаблон-дроби</b></i> <p> <i>знак-числа</i> и
     * <b><i>шаблон-целых</I></b> описаны выше. Для <i><b>шаблона-дроби</b></i>
     * действуют правила: <p> <OL> <LI> Если применяется смешанный шаблон, то
     * вначале должны быть символы <b>'0'</b>, а затем символы <b>#</b>. </LI>
     * <LI> Если использованы только символы <b>#</b>, то дробная часть
     * необязательна. </LI> </OL> </LI> <LI> <i>dd.MM.yy</i> или
     * <i>dd.MM.YYYY</i> - формат даты.
     *
     * </LI> </UL>
     */
    private AWTEvent initEvent = null;
    private char initChar;
    private String initText = "";
//!!  private PDateEditSupport dateManager;
    private PTableDateEditorSupport dateManager;
////////////
    DataColumn dataColumn = null;
    TableColumn tableColumn = null;
    private Table dataTable = null;
    private int keyPressedEventCode;
    int caretPosition = -1;
//  String newText = null;
    protected char[] dateMask = null;
    /**
     * Хранит информацию о размещении в dateMask шаблонов дня, месяца и года.
     * <p> Элемент с индексом 0 - начальный индекс для дня, с индексом 1 -
     * конечный индекс для дня, с индексом 2 - начальный индекс месяца, с
     * индексом 3 - конечный индекс месяца, с индексом 4 - начальный индекс
     * года, с индексом 5 - конечный индекс года.
     *
     * @see calcDateAreas
     * @see dateMask
     */
    protected int digitCount; //всего цифр
    protected int fracCount; //цифр дробной части
    protected int maskType;
    protected static int INTEGER = 1;
    protected static int DECIMAL = 2;
    protected static int DATE_YY = 3;
    protected static int DATE_YYYY = 4;
    protected static int OTHER = 100;
    /**
     * Хранит ссылку на объект типа
     * <code>EventObject</code>.<p> Необходимо в случаях, когда обработчику
     * некоторого события, параметром которого является ссылка на экземпляр типа
     * <code>ChangeEvent</code> недостаточно информации об объекте-источнике
     * события, но также требуется информация об оригинальном событии. Например,
     * объект типа
     * <code>PCellEditor</code> вызывает метод
     * <code>editingStoped</code> обработчика, которым, как правило является
     * <code>PTable</code>. PTable должен принять решение о дальнейших действий,
     * которые, в свою очередь зависят от того, каким образом произведен запрос
     * на окончание редактирования ячейки.
     */
    private EventObject originalEvent = null;
    private DataViewCellEditorListener cellEditorListener = null;
//   public void paint(Graphics g ) {
//     Rectangle r = g.getClipBounds();

//     g.fillRect(r.x,r.y,r.width,r.height);
//     g.drawString(this.getText(), r.x + 5, r.y + 5);
//     g.setColor(this.getBackground());
//     g.fillRect(r.x,r.y,r.width,r.height);
//     g.setColor(this.getForeground());
//     g.drawString("valery", r.x + 5, r.y + 5);
//   }
  /*
     * public void update(Graphics g ) { paint(g); }
     */
////////////////////////////////////////////////////
////////////////////////////////////////////////////
    /**
     * Returns the value contained in the editor.
     *
     * @return the value contained in the editor
     */
    public Object getCellEditorValue() {
        return DataUtil.valueOf(dataColumn, this.getText());
        // # return this.getText();
    }

    /**
     * Asks the editor if it can start editing using
     * <code>anEvent</code>.
     * <code>anEvent</code> is in the invoking component coordinate system. The
     * editor can not assume the Component returned by
     * <code>getCellEditorComponent</code> is installed. This method is intended
     * for the use of client to avoid the cost of setting up and installing the
     * editor component if editing is not possible. If editing can be started
     * this method returns true.
     *
     * @param	anEvent	the event the editor should use to consider whether to
     * begin editing or not
     * @return	true if editing can be started
     * @see #shouldSelectCell
     */
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    /**
     * Returns true if the editing cell should be selected, false otherwise.
     * Typically, the return value is true, because is most cases the editing
     * cell should be selected. However, it is useful to return false to keep
     * the selection from changing for some types of edits. eg. A table that
     * contains a column of check boxes, the user might want to be able to
     * change those checkboxes without altering the selection. (See Netscape
     * Communicator for just such an example) Of course, it is up to the client
     * of the editor to use the return value, but it doesn't need to if it
     * doesn't want to.
     *
     * @param	anEvent	the event the editor should use to start editing
     * @return	true if the editor would like the editing cell to be selected;
     * otherwise returns false
     * @see #isCellEditable
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    /**
     * Tells the editor to stop editing and accept any partially edited value as
     * the value of the editor. The editor returns false if editing was not
     * stopped; this is useful for editors that validate and can not accept
     * invalid entries.<p> Метод возвращает
     * <code>true</code>, если не назначен обработчик типа
     * <code>PCellEditorListener</code>. Если обработчик назначен, то перед
     * вызовом его метода
     * <code>editingStoped</code>, производится вызов метода
     * <code>editingValid</code>, контролирующего введенное значение. И, если
     * метод
     * <code>editingValid</code> возвратит <codetrue</code>, то тогда и только
     * тогда будет вызван метод
     * <code>editingStoped</code>.
     *
     * @return	true if editing was stopped; false otherwise
     */
    public boolean stopCellEditing() {
        return stopCellEditing(null);
    }

    /**
     * Указывает, что произошло событие требующее прекращения редактирования.<p>
     * Метод возвращает
     * <code>true</code>, если не назначен обработчик типа
     * <code>PCellEditorListener</code>. Если обработчик назначен, то перед
     * вызовом его метода
     * <code>editingStoped</code>, производится вызов метода
     * <code>editingValid</code>, контролирующего введенное значение. И, если
     * метод
     * <code>editingValid</code> возвратит <codetrue</code>, то тогда и только
     * тогда будет вызван метод
     * <code>editingStoped</code>.
     *
     * @param e
     * @return	true if editing was stopped; false otherwise
     */
    public boolean stopCellEditing(EventObject e) {
        if (!isValid()) {
            this.requestFocus();
            return false;
        }

        if (this.cellEditorListener != null) {
            ChangeEvent changeEvent = new ChangeEvent(this);
            //changeEvent.setOriginalEvent(e);
            if (this.cellEditorListener.editingValid(changeEvent)) {
                this.cellEditorListener.editingStopped(changeEvent);
            } else {
                return false;
            }
        }
        return true;

    }

    @Override
    public boolean isValid() {
        if (dataColumn.getSqlType() == java.sql.Types.TIMESTAMP) {
            // return dateManager.isValid();
        }
        return true;
    }

    /**
     * Указывает, что произошло событие требующее прекращения редактирования.<p>
     * Метод возвращает
     * <code>true</code>, если не назначен обработчик типа
     * <code>PCellEditorListener</code>. Если обработчик назначен, то перед
     * вызовом его метода
     * <code>editingStoped</code>, производится вызов метода
     * <code>editingValid</code>, контролирующего введенное значение. И, если
     * метод
     * <code>editingValid</code> возвратит <codetrue</code>, то тогда и только
     * тогда будет вызван метод
     * <code>editingStoped</code>.
     *
     * @return	true if editing was stopped; false otherwise
     * @see stopCellEditing
     */
    public boolean validateCellEditing() {
        if (this.cellEditorListener != null) {
            ChangeEvent changeEvent = new ChangeEvent(this);
            if (!this.cellEditorListener.editingValid(changeEvent)) {
                return false;
            }
        }
        return true;

    }

    /**
     * Tells the editor to cancel editing and not accept any partially edited
     * value.
     */
    public void cancelCellEditing() {
    }

    /**
     * Adds a listener to the list that's notified when the editor stops, or
     * cancels editing.
     *
     * @param	l	the CellEditorListener
     */
    public void addCellEditorListener(CellEditorListener l) {
        this.cellEditorListener = (DataViewCellEditorListener) l;
    }

    /**
     * Removes a listener from the list that's notified
     *
     * @param	l	the CellEditorListener
     */
    public void removeCellEditorListener(CellEditorListener l) {
        this.cellEditorListener = null;
    }

    /**
     * my
     *
     * @param value
     */
    public void setCellEditorValue(Object value) {
//    Integer.parseInt(".");
        this.setText((String) value);
    }

    /*
     * public void setVisible(boolean visible) { super.setVisible(visible);
     * validate(); repaint(); }
     */
    public void keyPressedGeneral(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_DOWN:
                break;
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_BACK_SPACE:
                this.setText(null);
                this.selectAll();
                break;
            case KeyEvent.VK_ENTER:
                this.stopCellEditing(e);
                break;
            case KeyEvent.VK_LEFT:
                break;

        } //switch
    }

    public void keyTypedInteger(KeyEvent e) {

        int code = e.getKeyChar();
        switch (code) {
            case KeyEvent.VK_ENTER:
                break;
            default:
                if (!checkInteger(e)) {
                    e.consume();

                } else {
                }
        }
    }

    public void keyTypedNumeric(KeyEvent e) {

        int code = e.getKeyChar();
        switch (code) {
            case KeyEvent.VK_ENTER:
                break;
            default:
                if (!checkNumeric(e, dataColumn.getSize(), dataColumn.getScale())) {
                    e.consume();
                }
        }
    }

    public void keyTypedVarchar(KeyEvent e) {

        int code = e.getKeyChar();
        switch (code) {
            case KeyEvent.VK_ENTER:
                break;
        }
    }

    public void keyTypedChar(KeyEvent e) {

        int code = e.getKeyChar();
        switch (code) {
            case KeyEvent.VK_ENTER:
                break;
        }
    }

///////////////////////////////
    public boolean keyPressedDate(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
                /*
                 * if ( this.getSelectionStart() == 0 &&
                 * this.getSelectionStart() >= getText().length() ) {
                 * this.setText(" . . "); this.selectAll(); }
                 */
                break;
            case KeyEvent.VK_DELETE:
                int i = this.getSelectionStart();
                int j = this.getSelectionEnd();
                if (getText().charAt(this.getCaretPosition()) >= 0 && i - j == 0) {
                    e.consume();
                }
                break;
            case KeyEvent.VK_LEFT:
                e.consume();
                break;
            case KeyEvent.VK_RIGHT:
                e.consume();
                break;
            case KeyEvent.VK_END:
                e.consume();
                break;
            case KeyEvent.VK_HOME:
                e.consume();
                break;

        }
        return true;
    }

    public boolean keyReleasedDate(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (this.getSelectionStart() == 0
                    && this.getSelectionStart() >= getText().length()) {
                this.setText("  .  .  ");
                this.selectAll();
            }
            return true;
        }
        if (this.dateManager.isNewTextRequired()) {
            setText(dateManager.getNewText());
        }
        if (this.dateManager.getNewCaretPosition() >= 0) {
            this.setCaretPosition(dateManager.getNewCaretPosition());
        }


        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                break;
            case KeyEvent.VK_LEFT:
                this.dateManager.selectPrevious();
                break;
            case KeyEvent.VK_RIGHT:
                this.dateManager.selectNext();
                break;
            case KeyEvent.VK_END:
                this.dateManager.selectLast();
                break;
            case KeyEvent.VK_HOME:
                this.dateManager.selectFirst();
                break;

        }
        return true;
    }

    public boolean keyTypedDate(KeyEvent e) {
        if (e.getKeyChar() == '.') {
            e.consume();
            this.dateManager.selectNext();
            return true;
        }
        boolean result = dateManager.checkDateField(e);
        if (!result) ///!!
        {
            e.consume();
        }
        return result;
    }

    protected boolean checkNumeric(KeyEvent e, int size, int scale) {
        int code = e.getKeyChar();
        boolean result;//My 06.03.2012 = true;
        char c = (char) code;
        switch (c) {
            case '+':
                result = checkSign(code);
                break;
            case '-':
                result = checkSign(code);
                break;
            case '.':
                result = checkDot(code);
                break;
            default:
                result = Character.isDigit(c);
        }//switch
        return result;
    }

    protected boolean checkSign(int code) {
        boolean result = true;
        if ((char) code == '+' || (char) code == '-') {
            int i = getText().indexOf('+');
            if (i < 0) {
                i = getText().indexOf('-');
            }
            if (i >= 0) {
                result = false;
            } else if (this.getCaretPosition() > 0) {
                result = false;
            }
        }
        return result;
    }

    protected boolean checkInteger(KeyEvent e) {
        //Повторный ввод знака числа.
        boolean result = true;
        int code = e.getKeyChar();
        if ((char) code == '+' || (char) code == '-') {
            int i = getText().indexOf('+');
            if (i < 0) {
                i = getText().indexOf('-');
            }
            if (i >= 0) {
                result = false;
            } else if (this.getCaretPosition() > 0) {
                result = false;
            }
        } else {
            if (!Character.isDigit((char) code)) {
                result = false;
            }
        }
        return result;
    }

    protected boolean checkDot(int code) {
        //Повторный ввод знака дес. точки
        boolean result = true;

        if ((char) code == '.') {
            int i = getText().indexOf('.');
            if (i >= 0) {
                result = false;
            }
        }
        return result;
    }

    protected int getDateAreaIndex(int caretPos) {

        if (caretPos >= dateMask.length || dateMask[caretPos] == '.') {
            caretPos--;
        }
        int result = 0;
        switch (dateMask[caretPos]) {
            case 'd':
                break;
            case 'M':
                result = 2;
                break;
            case 'y':
                result = 4;
        }

        return result;
    }

    public void processMouseEvent1(MouseEvent e) {
        int id = e.getID();
        if (dataColumn.getSqlType() == java.sql.Types.TIMESTAMP) {
            if (id == MouseEvent.MOUSE_RELEASED) {
                dateManager.selectCurrent();
            }
            /*
             * int caretPos = this.getCaretPosition(); int idx =
             * getDateAreaIndex(caretPos);
             *
             * if ( caretPos >= dateArea[idx] && caretPos <= dateArea[idx+1] +
             * 1) { this.setCaretPosition(dateArea[idx]);
             * this.select(dateArea[idx],dateArea[idx+1] + 1); } }
             */
        }
    }

    public void processIntegerKeyEvent(KeyEvent e) {
        switch (e.getID()) {
            case KeyEvent.KEY_PRESSED:
                keyPressedGeneral(e);
                break;
            case KeyEvent.KEY_TYPED:
                keyTypedInteger(e);
                break;
            case KeyEvent.KEY_RELEASED:
                break;
        }
    }

    public void processNumericKeyEvent(KeyEvent e) {
        boolean result = true;
        switch (e.getID()) {
            case KeyEvent.KEY_PRESSED:
                keyPressedGeneral(e);
                break;
            case KeyEvent.KEY_TYPED:
                if (dataColumn.getScale() == 0) {
                    keyTypedInteger(e);
                } else {
                    keyTypedNumeric(e);
                }
                break;
            case KeyEvent.KEY_RELEASED:
                break;
        }
    }

    public void processTimestampKeyEvent(KeyEvent e) {
        switch (e.getID()) {
            case KeyEvent.KEY_PRESSED:
                this.keyPressedEventCode = e.getKeyCode();
                keyPressedGeneral(e);
                dateManager.setConsumed(true);
                dateManager.setNewCaretPosition(-1);
                dateManager.setNewTextRequired(false);
                keyPressedDate(e);
                break;
            case KeyEvent.KEY_TYPED:
                if (this.keyPressedEventCode == KeyEvent.VK_DELETE) {
                    return;
                }
                if (this.keyPressedEventCode == KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                    return;
                }
                if (this.keyPressedEventCode == KeyEvent.VK_ESCAPE) {
                    e.consume();
                    return;
                }


                keyTypedDate(e);
                break;
            case KeyEvent.KEY_RELEASED:
                this.keyReleasedDate(e);
                break;
        }
    }

    public void processVarcharKeyEvent(KeyEvent e) {
        switch (e.getID()) {
            case KeyEvent.KEY_PRESSED:
                keyPressedGeneral(e);
                break;
            case KeyEvent.KEY_TYPED:
                keyTypedVarchar(e);
                break;
            case KeyEvent.KEY_RELEASED:
                break;
        }
    }

    public void processCharKeyEvent(KeyEvent e) {
        switch (e.getID()) {
            case KeyEvent.KEY_PRESSED:
                keyPressedGeneral(e);
                break;
            case KeyEvent.KEY_TYPED:
                keyTypedChar(e);
                break;
            case KeyEvent.KEY_RELEASED:
                break;
        }
    }

    @Override
    public void processKeyEvent(KeyEvent e) {
        int code = e.getKeyChar();
        int cc = e.getKeyChar();
        if (e.getID() == KeyEvent.KEY_RELEASED
                && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            restoreOriginText();
        }

        int sqlType = dataColumn.getSqlType();
        int i = java.sql.Types.INTEGER;
        boolean result = true;
        switch (sqlType) {
            case java.sql.Types.INTEGER:
                processIntegerKeyEvent(e);
                super.processKeyEvent(e);
                break;
            case java.sql.Types.SMALLINT:
                processIntegerKeyEvent(e);
                break;
            case java.sql.Types.DOUBLE:
                processNumericKeyEvent(e);
                break;
            case java.sql.Types.VARCHAR:
                processVarcharKeyEvent(e);
                break;
            case java.sql.Types.CHAR:
                processCharKeyEvent(e);
                break;
            case java.sql.Types.NUMERIC:
                processNumericKeyEvent(e);
                break;

            case java.sql.Types.DECIMAL:
                processNumericKeyEvent(e);
                break;
            case java.sql.Types.TIMESTAMP:
                processTimestampKeyEvent(e);


            case java.sql.Types.OTHER:
                break;

        } //switch

//  return result;
    }

    public void processTextEvent1(TextEvent e) {
        if (dataColumn.getSqlType() == java.sql.Types.TIMESTAMP) {
            dateManager.textValueChanged(e);
        }
    }

/////////
    //******* TEST
    //DataColumn testDc;
    //********END TEST
    public NumberDateCellEditor() {
        super();
        dataColumn = new PDBIntegerColumn();
        installListeners();
    }

    private void installListeners() {
        //enableEvents(AWTEvent.KEY_EVENT_MASK);
        //enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        //this.enableEvents(AWTEvent.TEXT_EVENT_MASK);
    }
    JTable table = null;
    int rowIndex = -1;
    int columnIndex = -1;
    /*
     * public Component getTableCellEditorComponent(PTable table, Object value,
     * boolean isSelected, int rowIndex, int columnIndex, java.awt.AWTEvent
     * initEvent) {
     */

    Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected,
            int rowIndex, int columnIndex) {

        this.table = table;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;

        this.tableColumn = (TableColumn) table.getColumnModel().getColumn(columnIndex);
        //this.dataColumn = table.getDataColumnAt(rowIndex, columnIndex);    
        this.dataColumn = dataTable.getColumns().get(tableColumn.getModelIndex());
        if (dataColumn.getSqlType() == java.sql.Types.TIMESTAMP) {
            String df = this.getDateDisplayFormat();
            if (df == null) {
                df = "dd.MM.yy";
            }

            //dateMask = dataColumn.getDisplayFormat().toCharArray();
            dateMask = df.toCharArray();
            //////////////
            if (this.dateManager == null) {
//!!        this.dateManager = new PDateEditSupport(this,dataColumn.getEditMask());
                // # я удалил для компиляции  this.dateManager = new PDateEditorSupport(this,dataColumn.getEditMask());
            }

        }

        this.setInitEvent(initEvent);
        if (initEvent instanceof KeyEvent) {
            this.setInitChar(((KeyEvent) initEvent).getKeyChar());
        }
        /*
         * #
         * String stringValue = table.getEditorFormattedValue(rowIndex,
         * columnIndex); setText(stringValue); setInitText( stringValue);
         */
        selectAll();

//   dateManager.textValueChanged();
//   this.processKeyEvent(ke);
/*
         * public KeyEvent(Component source, int id, long when, int modifiers,
         * int keyCode)
         */
        return this;
    }

//////////////////////////////////////////
    public void restoreOriginText() {
        if (dataColumn.getSqlType() == java.sql.Types.TIMESTAMP) {
            String df = this.getDateDisplayFormat();
            if (df == null) {
                df = "dd.MM.yy";
            }

            //dateMask = dataColumn.getDisplayFormat().toCharArray();
            dateMask = df.toCharArray();
            //////////////
            if (this.dateManager == null) {
//!!        this.dateManager = new PDateEditSupport(this,dataColumn.getEditMask());
                // # я удалил для ком. this.dateManager = new PDateEditorSupport(this,dataColumn.getEditMask());
            }

        }
        // # я удалил для ком. String stringValue = table.getEditorFormattedValue(rowIndex, columnIndex);
        // # я удалил для ком. setText(stringValue);
        selectAll();
    }

    public char getDateFieldSeparator() {
        return '.';
    }

    public AWTEvent getInitEvent() {
        return this.initEvent;
    }

    public void setInitEvent(AWTEvent initEvent) {
        this.initEvent = initEvent;
        if (dateManager != null) {
            this.dateManager.setInitEvent(initEvent);
        }
    }

    public char getInitChar() {
        return this.initChar;
    }

    public void setInitChar(char initChar) {
        this.initChar = initChar;
        if (dateManager != null) {
            this.dateManager.setInitChar(initChar);
        }
    }

    public String getInitText() {
        return this.initText;
    }

    public void setInitText(String initText) {
        this.initText = initText;
        if (dateManager != null) {
            this.dateManager.setInitText(initText);
        }
    }

    public JTable getTable() {
        return this.table;
    }
    private String numberDisplayFormat;
    private String dateDisplayFormat;

    public String getNumberDisplayFormat() {
        return this.numberDisplayFormat;
    }

    public String getDateDisplayFormat() {
        return this.dateDisplayFormat;
    }

    public void setNumberDisplayFormat(String nf) {
        this.numberDisplayFormat = nf;
    }

    public void setDateDisplayFormat(String df) {
        this.dateDisplayFormat = df;
    }
}//class