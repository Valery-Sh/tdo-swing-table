/*
 * RowTemplateRequestEvent.java
 *
 * Created on 4 Май 2007 г., 9:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

import java.util.EventObject;

/**
 * 
 * 
 * @see #getKey
 * @see #setKey
 * @see #getRowIndex
 * @ENDRUS 
 * @RUS Класс события, возбуждаемого компонентом {@link TableViewer}, когда последний запрашивает
 * шаблон для некоторого ряда таблицы. 
 * Обработчик (listener) события устанавливает строковое значение ключа для доступа к коллекции
 * {@link RowTemplates}, используя метод {@link #setKey}. Если значение устанавливаемого 
 * обработчиком ключа равно <code>null</code>  или не существует такого ключа в 
 * <code>RowTemplates</code>, то будет назначено значение по умолчанию, равное "COLUMNMODEL".
 * Источником события всегда является объект типа {@link TableViewer} или его наследник.
 * Обработчик использует этот объект, а также метод {@link #getRowIndex} для определения
 * требуемого данному ряду ключа шаблона.
 */
public class RowTemplateRequestEvent extends EventObject {
    
    private String key;
    private int rowIndex;
    /**
     * Creates a new instance of RowTemplateRequestEvent.
     * Sets the value of the key property equals to <code>null</code>
     * and rowIndex value to -1.
     * @param source 
     * @see @getKey
     * @see @setKey
     */
    public RowTemplateRequestEvent(TableViewer source) {
        this(source,-1);
    }
    /**
     * Creates a new instance of RowTemplateRequestEvent for a given row index.
     * Sets the value of the key property equals to <code>null</code>.
     * @param source 
     * @param rowIndex 
     * @see @getKey
     * @see @setKey
     */
    public RowTemplateRequestEvent(TableViewer source, int rowIndex) {
        super(source);
        key = null;
        this.rowIndex = rowIndex;
    }
    
    /**
     * @return the key used to access to a template from the instance of {@link RowTemplates}.
     */
    public String getKey() {
        return this.key;
    }
    /**
     * Sets the key used to access to a template from the instance of {@link RowTemplates}.
     * @param key 
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /**
     * @RUS
     * @return Возвращает индекс ряда, для которого необходимо определить ключ шаблона.
     * @ENDRUS
     *
     * @return the row index whose row template to be defined. 
     */
    public int getRowIndex() {
        return this.rowIndex;
    }
    /**
     * 
     * 
     * @param rowIndex 
     * @ENDRUS Sets the row index whose row template is to be defined. 
     * Usually it is a {@link TableViewer} that sets the value.
     * @RUS Устанавливает индекс ряда, для которого необходимо определить ключ шаблона.
     * Обычно предоставляется объектом типа {@link TableViewer}.
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
    
}//class RowTemplateRequestEvent
