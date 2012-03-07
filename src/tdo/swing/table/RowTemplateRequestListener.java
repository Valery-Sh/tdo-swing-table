/*
 * RowTemplateRequestListener.java
 *
 * Created on 4 Май 2007 г., 10:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

/**
 * @RUS
 * Определяет методы для обработки события {@link RowTemplateRequestEvent}, возбуждаемого
 * при запросе шаблона для заданного ряда.
 * @see RowTemplateRequestHandler
 * @ENDRUS
 * 
 */
public interface RowTemplateRequestListener {
    /**
     * Определяет значение ключа шаблона, применяемого для заданного ряда.
     * @param e событие, требующее определить шаблон ряда.
     * @param rowIndex индекс ряда, для которого определяется шаблон.
     */
    void defineKey(RowTemplateRequestEvent e, int rowIndex);
}//interface
