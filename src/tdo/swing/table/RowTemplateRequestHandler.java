/*
 * RowTemplateRequestHandler.java
 *
 * Created on 4 Май 2007 г., 10:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

/**
 * 
 * 
 * @ENDRUS 
 * @RUS Абстрактный класс, реализующий интерфейс {@link RowTemplateRequestListener}.
 *  Метод #defineKey класса запоминает значения параметров и делегирует выполнение
 *  методу {@link #setKey()} без параметров. Класс может быть использован, например,
 *  следующим образом:
 *  <code>
 *    <pre>
 *       TableViewer table = ...
 *       RowTemplateRequetHandler rth = new RowTemplateRequetHandler() {
 *              public void setKey() {
 *                  String key;
 *                  int row = this.getRowIndex{};
 *                  TableViewer t = getTable();
 *                  // Код, вычисляющий значение key.
 *                  ......................
 *                  ......................
 *                  setKey(key);
 *              }
 *       };
 *    </pre>  
 *  </code>
 */
public abstract class RowTemplateRequestHandler implements RowTemplateRequestListener {
    private RowTemplateRequestEvent evt;
    private int rowIndex;
    /** 
     * Creates a new instance of RowTemplateRequestHandler 
     */
    public RowTemplateRequestHandler() {
    }

    /**
     * @RUS
     * Австрактный метод, используемый классами наследниками при реализации алгоритма
     * определения шаблона ряда для данной таблици и индекса ряда.
     * @see #getRowIndex
     * @see #getTable
     * @see #setKey(String)
     * @ENDRUS 
     */
    public abstract void setKey(); 
    
    /**
     * Метод интерфейса {@link RowTemplateRequestListener}.
     * Вызывается методом <code>getRowTemplate(rowIndex)</code> класса {@link TableViewer}.
     * Сохраняет значения параметров для дальнейшего использования методами данного класса.
     * 
     * @param e событие при запросе шаблона для ряда таблицы
     * @param rowIndex индекс ряда таблицы, для которого определяется шаблон
     */
    public void defineKey(RowTemplateRequestEvent e, int rowIndex) {
        evt = e;
        this.rowIndex = rowIndex;
        setKey();
    }
    
    /**
     * @return the key used to access to a template from the instance of {@link RowTemplates}.
     */
    public String getKey() {
        return evt.getKey();
    }
    /**
     * Sets the key used to access to a template from the instance of {@link RowTemplates}.
     * @param key 
     */
    public void setKey(String key) {
        evt.setKey(key);
    }
    
    /**
     * @RUS
     * @return Возвращает индекс ряда, для которого необходимо определить ключ шаблона.
     * @ENDRUS
     *
     * @return the row index whose row template to be defined. 
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * 
     * 
     * @return the table of interest of type {@link TableViewer}.
     */
    public TableViewer getTable() {
        return (TableViewer)evt.getSource();
    }
    
}//class
