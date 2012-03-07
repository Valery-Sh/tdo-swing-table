/*
 * RowTemplates.java
 *
 * Created on 18 Апрель 2007 г., 15:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.table.TableColumnModel;

/**
 * @RUS 
 *     Реализует коллекцию объектов типа {@link #RowTemplate} как HashMap.
 *  При создании объектов типа {@link TableViewer} создается экземпляр
 *  данного класса. Коллекция всегда содержит по крайней мере один элемент-шаблон
 *  типа <code>RowTemplate</code> со значением ключа равным "COLUMNMODEL". Такой
 *  шаблон имеет особое значение и всегда синхронизирован с моделью колонок,
 *  которая может быть получена вызовом метода <code>getColumnModel()<code>
 *  таблицы <code>TableViewer</code>. Под синхронизацией здесь
 *  понимается следующее:
 *  <OL>
 *    <LI>
 *       Обе коллекции всегда имеют одно и тоже количество элементов;
 *    </LI>
 *    <LI>
 *       i-й элемент коллекции шаблона с ключом "COLUMNMODEL" соответствует
 *       i-му элементу коллекции в <code>table.getColumnModel()<code> как в 
 *          смысле порядка, так и в том, что свойства приведенные ниже имеют 
 *          одинаковые значения
 *      
 *      <UL>
 *          <LI>
 *              <code>identifier</code>
 *          </LI>
 *          <LI>
 *              <code>modelIndex</code>
 *          </LI>
 *          <LI>
 *              <code>minWidth</code>
 *          </LI>
 *          <LI>
 *              <code>maxWidth</code>
 *          </LI>
 *          <LI>
 *              <code>preferredWidth</code>
 *          </LI>
 *          <LI>
 *              <code>width</code>
 *          </LI>
 *          <LI>
 *              <code>resizable</code>
 *          </LI>
 *          <LI>
 *              <code>cellEditor</code>
 *          </LI>
 *          <LI>
 *              <code>cellRenderer</code>
 *          </LI>
 *          <LI>
 *              <code>headerRenderer</code>
 *          </LI>
 *          <LI>
 *              <code>headerValue</code>
 *          </LI>
 *        Для удобства шаблон с ключем "COLUMNMODEL" доступен вызовом метода
 *        {@link TableViewer#getColumnModelTemplate}.
 *      </UL>
 *    </LI>
 *    <LI>
 *       При удалении, добавлении или перемещении колонки из 
 *       <code>table.getColumnModel()</code> соответствующая операция 
 *       выполняется над колонками шаблона "COLUMNMODEL". Для отслеживания 
 *       изменений шаблон "COLUMNMODEL" регистрируется как обработчик события
 *       <code>TableColumnModelEvent</code>, возбуждаемого моделью колонок
 *       <code>table.getColumnModel()</code>. 
 *    </LI>
 *    <LI>
 *      При замене модели колонок вызовом метода 
 *      <code>table.setColumnModel</code> или при изменении модели данных, 
 *      автоматически создается новая коллекция объектов типа 
 *      <code>TableCell</code> в шаблоне с ключом "COLUMNMODEL".
 *      Это обеспечивается тем, что объект класса <code>RowTemplates</code>
 *      регистрируется как обработчик события <code>PropertyChangeEvent</code>,
 *      возбуждаемого моделью колонок <code>table.getColumnModel()</code>.
 *    </LI>  
 *  </OL>
 *      Важным свойством класса является свойство {@link #modelColumns} имеющее 
 *      тип <code>RowTemplate</code>. Это свойство создается при создании модели
 *      данных и изменяется только при применении метода 
 *      <code>table.setModel()</code>. Таким образом, <code>modelColumns</code>
 *      хранит полный набор колонок, соответствующих модели данных и не 
 *      изменяется при добавлении, изменении или перемещении колонок в модели 
 *      колонок <code>table.getColumnModel()</code>. Это позволяет использовать
 *      шаблоны <code>RowTemplate</code> с колонками, не входящими в 
 *      <code>table.getColumnModel()</code>. <p>
 *       Важно отметить, что <code>modelColumns</code>
 *      не содержится во внутренней map-коллекции класса и доступ к ней обеспечивается методом
 *      {@link #getModelColumns}. Для удобства, класс {@link TableViewer} также
 *      имеет одноименный метод для доступа к <code>modelColumns</code>.
 * @ENDRUS 
 */
public class RowTemplates extends HashMap<String, RowTemplate> implements PropertyChangeListener {
    
    
    private TableViewer table;
  
    /**
     * @RUS
     *  Хранит значение шаблона всех колонок для модели данных.
     *  При назначении модели данных таблице типа {@link #TableViewer} создается
     *  объект типа <code>TableColumnModel</code> по умолчанию. Здесь для каждой
     *  колонки модели определен свой элемент. В дальнейшем <code>columnModel</code>
     *  может меняться добавлением, удалением или перемещением колонок, но, при этом
     *  <code>modelColumns</code> остается неизменным. Измениться может только
     *  при назначении таблице новой модели данных.
     * @ENDRUS
     */
    private RowTemplate modelColumns;
  
    
    
    /**
     * 
     * @RUS Создает новый экземплярRowTemplatess для таблицы TableViewer.
     *   <OL>
     *     <LI>
     *        Создает экземпляр {@link tdo.swing.table.RowTemplats} и назначает его свойству
     *        {@link #modelColumns}. Для этого шаблона устанавливается
     *        значение свойства <code>height</code> равное значению свойства
     *        <code>height</code> таблицы TableViewer.
     *     </LI>
     *     <LI>
     *        Создает экземпляр {@link tdo.swing.table.RowTemplate} и добавляет его
     *        в собственную коллекцию с ключом "COLUMNMODEL" а также устанавливает значение 
     *        свойства {@link TableViewer#setColumnModelTemplate}.
     *        Для  шаблона устанавливается  значение свойства
     *        <code>height</code> равное значению свойства  code>height</code>
     *        таблицы {@link TableViewer}. Устанавливается также layout manager
     *        колонок шаблона по класса {@link ColumnModelLayout}.
     *     </LI>
     *   </OL>
     * @param table таблица {@link TableViewer}, для которой определяются шаблоны
     * @see {@link tdo.swing.table.TableViewer#getHeight}.
     * @ENDRUS 
     */
    public RowTemplates(TableViewer table) {
        this.table = table;
        this.modelColumns = new RowTemplate(table);
        this.modelColumns.setHeight(table.getRowHeight());
        //this.defaultTemplateKey = null;
        RowTemplate rt = new RowTemplate(table);
        rt.createFrom(table.getColumnModel());
        put("COLUMNMODEL", rt );
        if ( table != null )
            table.setColumnModelTemplate(rt);
        rt.setHeight(table.getRowHeight());
        
        ColumnLayoutManager lm = new ColumnModelLayout();
        rt.setColumnLayout( lm );
    }
    
    //=======================================================================================
    //  Properties
    //=======================================================================================
    
    /**
     * @RUS 
     *  Назначает таблицу, для которой применяются шаблоны рядов.
     *  Шаблоны могут быть разработаны заранее без привязки к конкретной таблице.
     *  Свойству <code>columnModelTemplate</code> таблицы <code>table</code>
     * назначается значение шаблона с ключом "COLUMNMODEL".
     * Метод позволяет динамически установить эту связь.
     * Значению свойства <code>table</code> каждого шаблона (RowTemplate)
     * из коллекции метод также назначает значение параметра.<p>
     * вызывает метод {@link #doLayout}.
     * 
     * @param table назначаемая таблица.
     * @ENDRUS
     */
    public void setTable(TableViewer table) {
        this.table = table;
        for ( RowTemplate rt : this.values() ) {
            rt.setTable(table);
        }
        this.modelColumns.setTable(table);
        
        if ( table != null )
            table.setColumnModelTemplate(get("COLUMNMODEL"));
        
        doLayout();
    }
    /**
     * @RUS
     *  @return шаблон модели данных, создаваемый и изменяемый при выполнении 
     *          метода <code>table.setModel</code>.
     *  @see #columnModels
     * @ENDRUS
     */
    public RowTemplate getModelColumns() {
        return this.modelColumns;
    }
    
    /**
     * @RUS
     * Ввозвращает шаблон рядов по заданному ключу шаблона.
     * @param key ключ шаблона. 
     * @return шаблон для заданного ключа. Если значение параметра <code>key</code>
     *         равно <code>null</code>, то возвращается шаблон для ключа "COLUMNMODEL".
     * @ENDRUS
     */
    public RowTemplate getTemplate(String key) {
        if ( key == null ) 
            return super.get("COLUMNMODEL");
        
        RowTemplate rt = super.get(key);
        if ( rt == null )
            rt = super.get("COLUMNMODEL");
        return rt;
    }
  /**
     * @RUS
     *  Перекрытый метод коллекции <code>HashMap</code>.
     *  Возвращает шаблон рядов по заданному ключу шаблона.
     *  Преобразует параметр в строку знаков методом <code>toString</code> и 
     *  вызывает {@link #getTemplate}.
     * @param key ключ шаблона как java.lang.Object. 
     * @return шаблон для заданного ключа. Если значение параметра <code>key</code>
     *         равно <code>null</code>, то возвращается шаблон для ключа "COLUMNMODEL".
     * @ENDRUS
     */    
    public RowTemplate get(Object key ) {
        return getTemplate(key.toString());
    }
    
    /**
     * @RUS
     *  Помещает заданный шаблон в коллекцию <code>HashMap</code> с заданным ключем.
     *  Если значение свойства <code>height</code> размещаемого шаблона меньше 0, то
     *  ему назначается значение свойства <code>height</code> шаблона с ключем "COLUMNMODEL".
     *  Затем выполняется метод {@link ScrollDataView#refreshRowHeights</code> и {@link #doLayout}.
     *  @param key значение ключа, с которым шаблон помещается в коллекцию.
     *  @param rowTemplate размещаемый шаблон.
     *  @return размещенный в коллекции HashMap шаблон
     * @ENDRUS
     */
    public RowTemplate put(String key, RowTemplate rowTemplate ) {
        int h = -1;
        RowTemplate cmrt = get("COLUMNMODEL");
        if ( cmrt == null )
            h = table.getRowHeight();
        else
            h = cmrt.getHeight();
        if ( rowTemplate.getHeight() < 0 )
            rowTemplate.updateHeight(h);
        
        RowTemplate rt = super.put(key, rowTemplate);
        
        table.refreshRowHeights();
        doLayout();
        return rt;
    }
    
    /**
     * @RUS
     * Выполняет перераспределение элементов {@link TableCell} для каждого шаблона 
     * согласно назначенному им {@link ColumnLayoutManager}. 
     * @ENDRUS
     */
    public void doLayout() {
        
        for ( RowTemplate t : this.values() ) {
            t.doLayout();
        }
    }
    /**
     * @RUS
     * Метод интерфейса {@link java.beans.PropertyChangeListener}.
     * Обрабатывает события изменения свойств:
     * <OL>
     *   <LI>
     *      <code>columnModel</code> возбуждается компонентом {@link JTable} при изменения
     *                               модели колонок в методе <code>setColumnModel</code>.
     *            Для обработки вызызается метод {@link #adjustColumnModelChanged}.
     *   </LI>
     *   <LI>
     *      <code>tableModel</code> возбуждается компонентом {@link JTable} при изменения
     *                               модели колонок в методе <code>setModel</code>.
     *            Для обработки вызызается метод {@link #adjustModelChanged}.
     *   </LI>
     *   <LI>
     *      <code>rowHeight</code> возбуждается компонентом {@link JTable} при изменения
     *                               модели колонок в методе <code>setRowHeight</code>.
     *              Назначает свойству <code>height</code> шаблона {@link #modelColumns}
     *              и шаблона по умолчанию с ключем "COLUMNMODEL" аналогичное значение. 
     *   </LI>
     * </OL>
     * @param evt событие, обрабатываемое методом.
     * @ENDRUS
     *  {@link java.beans.PropertyChangeListener} interface implementation method.
     *  The method is invoked when the following properties have been changed:
     * <OL>
     *   <LI>
     *      <code>columnModel</code> fired by JTable component when it's columnModel has been changed.
     *   </LI>
     * </OL>
     *
     *  @param evt the object that represent an event to be treated.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        
        if ( evt.getSource() instanceof TableViewer ) {
            if ( evt.getPropertyName().equals("columnModel") ) {
                this.adjustColumnModelChanged((TableColumnModel)evt.getOldValue(), (TableColumnModel)evt.getNewValue());
            }
            if ( evt.getPropertyName().equals("rowHeight") ) {
                this.modelColumns.setHeight(((Integer)evt.getNewValue()).intValue());
                this.get("COLUMNMODEL").setHeight( ((Integer)evt.getNewValue()).intValue() );
            }
            
            if ( evt.getPropertyName().equals("model") ) {
                this.adjustModelChanged(null,table.getColumnModel());
            }
            
        }
        
    }//propertyChange()
    
    /**
     *  @RUS
     *    Создает новый шаблон с ключем "COLUMNMODEL" на основе модели колонок
     *    <code>columnModel</code> тавлицы {@link TableViewer}, и заносит его в RowTemplates,
     *    устанавливает в это значение свойство <code>columnModelTemplate 
     *    таблицы <code>TableViewer</code>.
     *   @param oldM предыдущая модель колонок таблицы
     *   @param newM новая модель колонок таблицы  
     *  @ENDRUS
     */
    protected void adjustColumnModelChanged( TableColumnModel oldM, TableColumnModel newM) {
        
        RowTemplate rt = get("COLUMNMODEL");
        if ( oldM != null )
            oldM.removeColumnModelListener(rt);
        if ( newM != null )
            newM.removeColumnModelListener(rt);
        
        rt.clear();
        rt.createFrom(newM);
        adjustTemplateModelIndexes();
        table.setColumnModelTemplate(rt);
        newM.addColumnModelListener( rt );
        
    }
    
    /**
     * @RUS
     *    Создает новый шавлон колонок модели для свойства {@link #modelColumns} и новый шаблон
     *    по умолчанию с ключем "COLUMNMODEL".
     *    Создает новый шаблон с ключем "COLUMNMODEL" на основе модели колонок
     *    <code>columnModel</code> тавлицы {@link TableViewer}, и заносит его в RowTemplates,
     *    устанавливает в это значение свойство <code>columnModelTemplate 
     *    таблицы <code>TableViewer</code>.
     *   @param oldM предыдущая модель колонок таблицы. Если вызов из {@link #propertyChange},
     *       то значение параметра равно <code>null</code>.
     *   @param newM новая модель колонок таблицы для новой модели данных. 
     * @ENDRUS
     */
    protected void adjustModelChanged( TableColumnModel oldM, TableColumnModel newM) {
        
        this.modelColumns.clear();
        this.modelColumns.createFrom(newM);
        RowTemplate rt = get("COLUMNMODEL");
        if ( oldM != null )
            oldM.removeColumnModelListener(rt);
        if ( newM != null )
            newM.removeColumnModelListener(rt);
        rt.clear();
        rt.createFrom(newM);
        adjustTemplateModelIndexes();
        newM.addColumnModelListener( rt );
        table.setColumnModelTemplate(rt);
        table.refreshRowHeights(true);
    }
    
    /**
     * @RUS
     *  Для каждого шаблона объекта класса и каждого элемента {@link TableCell} шаблона
     *  устанавливает новое значение свойства <code>modelIndex</code>.
     *  Для чего используется шаблон модели данных {@link #modelColumns} в котором по
     *  значению свойства <code>identifier</code> отыскивается соответствующий 
     *  <code>TableCell</code> и из него извлекается требуемый <code>modelIndex</code>.
     * @ENDRUS
     */
    protected void adjustTemplateModelIndexes() {
        
        for ( RowTemplate rt : this.values() ) {
            ColumnViewList<TableCell> cvl = rt.getColumnList();
            for ( int i=0; i < cvl.size(); i++ ) {
                TableCell tc = cvl.get(i);
                int modelIndex = this.modelColumns.find(tc.getIdentifier()).getModelIndex();
                tc.setModelIndex(modelIndex);
            }
        }
    }
    
    
}//class
