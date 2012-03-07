/*
 * PNumberDateCellEditor.java
 *
 * Created on 30 Октябрь 2006 г., 13:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table.editor;

import java.awt.event.KeyEvent;
import java.util.EventObject;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import tdo.DataColumn;
/**
 * <p>Title: Filis Application</p>
 * <p>Description: Freq Sensor's Support</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: IS</p>
 * @author VNS
 * @version 1.0
 */

public class NumericTextField  extends JTextField {
    
    
    /**
     * Могут быть заданы маски редактирования числовых полей и полей,
     * содержащих дату.
     * Поддерживаются простейшие маски редактирования:
     * <UL>
     *   <LI>
     *       <i>знак-числа <b>шаблон-целых</b></i> <p>
     *       где <i>знак-числа</i> - это символ <b>+</b> или <b>-</b>, а
     *       <b><i>цифровой шаблон</I></b> - это или последовательность знаков <b>#</b> или
     *       (и) знаков <b>0</b>. При этом, если применяется смешанный шаблон, т.е.
     *       используется как символ <b>'0'</b>, так и символ <b>'#'</b>, то вначале
     *       должны идти символы  <b>'#'</b>, а затем символы <b>'0'</b>. Например: <p>
     *       ####00 означает:
     *       <OL>
     *         <LI> допустим ввод только целых чисел ;</LI>
     *         <LI> обязательно должно присутствовать хотя бы 2 цифры.</LI>
     *       </OL>
     *   </LI>
     *   <LI>
     *       <i>знак-числа <b>шаблон-целых.шаблон-дроби</b></i> <p>
     *        <i>знак-числа</i> и <b><i>шаблон-целых</I></b> описаны выше. Для
     *        <i><b>шаблона-дроби</b></i> действуют правила: <p>
     *        <OL>
     *          <LI>  Если применяется смешанный шаблон, то вначале должны
     *            быть символы <b>'0'</b>, а затем символы <b>#</b>.
     *          </LI>
     *          <LI> Если использованы только символы <b>#</b>, то дробная
     *               часть необязательна.
     *          </LI>
     *        </OL>
     *   </LI>
     *   <LI> <i>dd.MM.yy</i> или <i>dd.MM.YYYY</i> - формат даты.
     *
     *   </LI>
     * </UL>
     */
    DataColumn dataColumn    = null;
    TableColumn tableColumn  = null;
    //private BaseDataTable dataTable = null;
    private int keyPressedEventCode;
    
    //protected int precision; //всего цифр
    //protected int scale; //цифр дробной части
    protected char decimalSeparator; //разделитель целой и дробной части    
    protected String strDecimalSeparator; //разделитель целой и дробной части    
    
    protected int maskType;
    protected static int INTEGER = 1;
    protected static int DECIMAL = 2;
    protected static int OTHER = 100;
    
    /**
     * Хранит ссылку на объект типа <code>EventObject</code>.<p>
     * Необходимо в случаях, когда обработчику некоторого события,
     * параметром которого является ссылка на экземпляр типа
     * <code>ChangeEvent</code> недостаточно информации об
     * объекте-источнике события, но также требуется информация об
     * оригинальном событии. Например, объект типа <code>PCellEditor</code>
     * вызывает метод <code>editingStoped</code> обработчика, которым, как
     * правило является <code>PTable</code>. PTable должен принять решение
     * о дальнейших действий, которые, в свою очередь зависят от того, каким
     * образом произведен запрос на окончание редактирования ячейки.
     */
    private EventObject originalEvent = null;
    
    
    // *****************************************************************************************
    // TableCelEditor implementation
    // *****************************************************************************************
    
    
    //**********************************************************************************************
    public void keyPressedGeneral(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
        case KeyEvent.VK_DOWN:
            break;
        case KeyEvent.VK_UP:
            break;
        case KeyEvent.VK_BACK_SPACE:
            super.processKeyEvent(e);
            break;
        case KeyEvent.VK_ENTER:
            super.processKeyEvent(e);
            break;
        case KeyEvent.VK_HOME:
            super.processKeyEvent(e);
            break;
        case KeyEvent.VK_END:
            super.processKeyEvent(e);
            break;
        case KeyEvent.VK_LEFT:
            super.processKeyEvent(e);
            break;
        case KeyEvent.VK_RIGHT:
            super.processKeyEvent(e);
            break;
        case KeyEvent.VK_DELETE:
            super.processKeyEvent(e);
            break;
            
        } //switch
    }
    
    
    public void keyTypedInteger(KeyEvent e) {
        
        int code = e.getKeyChar();
        char keyChar = e.getKeyChar();
        switch (code) {
        case KeyEvent.VK_ENTER:
            break;
        default:
            if (!checkInteger(e)) {
                e.consume();
                return;
            } 
            
            if ( getText() == null )
                return;
            
            if ( dataColumn.getSize() <= 0 ) 
                return;
            
            StringBuilder b = new StringBuilder(getText());
            
            b.insert(this.getCaretPosition(), keyChar ) ;
            
            if ( b.length()  > dataColumn.getSize() ) {
                e.consume();
                //My 06.03.2012 return;
            }                        
            
        }
    }
   public void keyTypedDecimalAsInteger(KeyEvent e) {
        
        int code = e.getKeyChar();
        char keyChar = e.getKeyChar();
        switch (code) {
        case KeyEvent.VK_ENTER:
            break;
        default:
            if (!checkInteger(e)) {
                e.consume();
                return;
            } 
            if ( getText() == null )
                return;
            
            if ( dataColumn.getSize() <= 0 ) 
                return;
            
            StringBuilder b = new StringBuilder(getText());
            
            b.insert(this.getCaretPosition(), keyChar ) ;
            
            if ( b.length()  > dataColumn.getSize() ) {
                e.consume();
                //My 06.03.2012return;
            }                        
        }
    }   
    public void keyTypedNumeric(KeyEvent e) {
        
        int code = e.getKeyChar();
        char keyChar = e.getKeyChar();
        switch (code) {
        case KeyEvent.VK_ENTER:
            break;
        default:
            
            if (! checkNumeric( e, dataColumn.getSize(), dataColumn.getScale()) ) {
                e.consume();
                return;
            }
            
            if ( getText() == null )
                return;
            
            if ( dataColumn.getSize() <= 0 ) 
                return;
            
            StringBuilder b = new StringBuilder(getText());
            
            b.insert(this.getCaretPosition(), keyChar ) ;
            
            if ( b.length()  > dataColumn.getSize() ) {
                e.consume();
                return;
            }
            
            int i = b.indexOf(Character.toString(getDecimalSeparator() ) );
            if ( i < 0 ) {
                return;
            }
            
            if ( b.length() - i - 1 > dataColumn.getScale() ) {
                e.consume();
                //My 06.03.2012return;
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
    
    
    
    
    protected boolean checkNumeric(KeyEvent e, int size, int scale) {
        int code = e.getKeyChar();
        boolean result;//My 06.03.2012 = true;
        char c = (char)code;
        switch ( c ) {
        case '+' :
            result = checkSign(code);
            break;
        case '-' :
            result = checkSign(code);
            break;
        case '.' :
            result = checkDot(code);
            break;
        default :
            result = Character.isDigit(c);
        }//switch
        return result;
    }
    
    protected boolean checkSign(int code) {
        boolean result = true;
        if ( (char) code == '+' || (char) code == '-') {
            int i = getText().indexOf('+');
            if (i < 0) {
                i = getText().indexOf('-');
            }
            if (i >= 0) {
                result = false;
            } else
                if (this.getCaretPosition() > 0) {
                    result = false;
                }
        }
        return result;
    }
    
    protected boolean checkInteger(KeyEvent e) {
        //Повторный ввод знака числа.
        boolean result = true;
        int code = e.getKeyChar();
        if ( (char) code == '+' || (char) code == '-') {
            int i = getText().indexOf('+');
            if (i < 0) {
                i = getText().indexOf('-');
            }
            if (i >= 0) {
                result = false;
            } else
                if (this.getCaretPosition() > 0) {
                    result = false;
                }
        } else {
            if (!Character.isDigit( (char) code)) {
                result = false;
            }
        }
        return result;
    }
    
    protected boolean checkDot(int code) {
        //Повторный ввод знака дес. точки
        boolean result = true;
        
        if ( (char) code == '.') {
            int i = getText().indexOf('.');
            if (i >= 0) {
                result = false;
            }
        }
        return result;
    }
    
  /*  public void processMouseEvent1( MouseEvent e ) {
        int id = e.getID();
        if ( dataColumn.getSqlType() == java.sql.Types.TIMESTAMP ) {
            if ( id == MouseEvent.MOUSE_RELEASED ) {
                //        dateManager.selectCurrent();
            }
   */
/*        int caretPos = this.getCaretPosition();
        int idx = getDateAreaIndex(caretPos);
 
        if ( caretPos >= dateArea[idx] && caretPos <= dateArea[idx+1] + 1) {
          this.setCaretPosition(dateArea[idx]);
          this.select(dateArea[idx],dateArea[idx+1] + 1);
        }
      }
 
        }
    }
 */
    public void processIntegerKeyEvent( KeyEvent e) {
        switch ( e.getID() ) {
        case KeyEvent.KEY_PRESSED  :
            keyPressedGeneral(e);
            break;
        case KeyEvent.KEY_TYPED    :
            keyTypedInteger( e );
            break;
        case KeyEvent.KEY_RELEASED :
            break;
        }
    }
    
    public void processNumericKeyEvent( KeyEvent e) {
        boolean result = true;
        switch ( e.getID() ) {
        case KeyEvent.KEY_PRESSED  :
            keyPressedGeneral(e);
            break;
        case KeyEvent.KEY_TYPED    :
            if (dataColumn.getScale() == 0) {
                keyTypedDecimalAsInteger(e);
            } else {
                keyTypedNumeric(e);
            }
            break;
        case KeyEvent.KEY_RELEASED :
            break;
        }
    }
    
    
    public void processDoubleKeyEvent( KeyEvent e) {
        boolean result = true;
        switch ( e.getID() ) {
        case KeyEvent.KEY_PRESSED  :
            keyPressedGeneral(e);
            break;
        case KeyEvent.KEY_TYPED    :
            if (dataColumn.getScale() == 0) {
                keyTypedInteger(e);
            } else {
                keyTypedNumeric(e);
            }
            break;
        case KeyEvent.KEY_RELEASED :
            break;
        }
    }
    
    public void processVarcharKeyEvent( KeyEvent e) {
        switch ( e.getID() ) {
        case KeyEvent.KEY_PRESSED  :
            keyPressedGeneral(e);
            break;
        case KeyEvent.KEY_TYPED    :
            keyTypedVarchar(e);
            break;
        case KeyEvent.KEY_RELEASED :
            break;
        }
    }
    
    public void processCharKeyEvent( KeyEvent e) {
        switch ( e.getID() ) {
        case KeyEvent.KEY_PRESSED  :
            keyPressedGeneral(e);
            break;
        case KeyEvent.KEY_TYPED    :
            keyTypedChar(e);
            break;
        case KeyEvent.KEY_RELEASED :
            break;
        }
    }
    
    @Override
    public void processKeyEvent( KeyEvent e) {
        int code = e.getKeyChar();
        int cc = e.getKeyChar();
        if (e.getID() == KeyEvent.KEY_RELEASED &&
                e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
            restoreOriginText();
        } else {
            if (e.getID() != KeyEvent.KEY_TYPED ) {
                this.keyPressedGeneral(e);
                return;
            }
        }
        
        int sqlType = dataColumn.getSqlType();
        int i = java.sql.Types.INTEGER;
        boolean result = true;
        switch (sqlType) {
        case java.sql.Types.INTEGER:
            processIntegerKeyEvent( e );
            super.processKeyEvent(e);
            break;
        case java.sql.Types.SMALLINT:
            processIntegerKeyEvent( e );
            super.processKeyEvent(e);
            break;
        case java.sql.Types.TINYINT:
            processIntegerKeyEvent( e );
            super.processKeyEvent(e);
            break;
            
        case java.sql.Types.DOUBLE:
            processDoubleKeyEvent( e );
            super.processKeyEvent(e);
            break;
        case java.sql.Types.VARCHAR:
            processVarcharKeyEvent( e );
            break;
        case java.sql.Types.CHAR:
            processCharKeyEvent( e );
            break;
        case java.sql.Types.NUMERIC:
            System.out.println("1: " + getText() +"; caret=" + this.getCaretPosition());
            processNumericKeyEvent( e );
            System.out.println("2: " + getText()+"; caret=" + this.getCaretPosition());
            super.processKeyEvent(e);
            System.out.println("3: " + getText()+"; caret=" + this.getCaretPosition());
            break;
            
        case java.sql.Types.DECIMAL:
            processNumericKeyEvent( e );
            super.processKeyEvent(e);
            break;
            
        } //switch
        
        //  return result;
    }
    
    
    
    public NumericTextField() {
        super();
        //dataColumn = new PDBIntegerColumn();
        installListeners();
    }
    public NumericTextField(DataColumn column ) {
        super();
        dataColumn = column;
        installListeners();
    }
    
    public DataColumn getDataColumn() {
        return this.dataColumn;
    }
    public void setDataColumn(DataColumn column) {
        this.dataColumn = column;
    }
    
    private void installListeners() {
        //enableEvents(AWTEvent.KEY_EVENT_MASK);
        //enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        //this.enableEvents(AWTEvent.TEXT_EVENT_MASK);
        
    }
    
    public void restoreOriginText() {
        
    }
    //private Object BigDecimal;

    public char getDecimalSeparator() {
        char r;
        if ( strDecimalSeparator == null ) {
/*            if ( dataColumn instanceof PDBNumberColumn ) {
                r = ((PDBNumberColumn)dataColumn).getDecimalSeparator();
            }
*/          r = '.'; 
        } else {
            r = this.decimalSeparator;
        }
        return r;
    }
    
    public void setDecimalSeparator(char decimalSeparator) {
        this.strDecimalSeparator = String.valueOf(decimalSeparator);
        this.decimalSeparator = decimalSeparator;
    }
    
}//class
