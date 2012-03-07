/*
 * ColumnLayoutManager.java
 *
 * Created on 18 Апрель 2007 г., 15:36:00
 *
 */

package tdo.swing.table;

/**
 * 
 * Определяет интерфейс для классов, его реализующих, которые знают 
 * как выполнить размещение колонок внутри шаблона (RowTemplate) рядов таблицы {@link tdo.swing.table.TableEx}.
 * 
 * 
 *
 */
public interface ColumnLayoutManager {
    
    void setTemplate( RowTemplate template);
    /**
     * @RUS
     *  Добавляет колонку, которая становится участником в процессе размещения внутри 
     *  шаблона рядов таблицы.
     * @param column добавляемая колонка.
     * @param constraints определяет как и где внутри шаблона размещается колонка. Допустимые
     *        значения параметра и их трактовка определяются классом, реализующим интерфейс.
     * @see RowTemplate
     * @ENDRUS
     * Adds the specified column to the layout, using the specified constraint object.
     * @param column the object to be added.
     * @param constraints defines where and how the column will be placed inside a row template. 
     *        Permissible values and there interpretation are a class implementation specific.
     * @see RowTemplate
     */
    void addLayoutCell(TableCell cell, String constraints);
    /**
     * @RUS
     *  Выполняет размещение колонок внутри заданного параметром шаблона рядов, используя
     *  правила, определяемые вторым параметром. Может изменить значения свойств <code>height</height>
     *  и (или) <code>width</code> шаблона, для которого выполняется размещение.
     * 
     * @param rowTemplate шаблон рядов, для которого применяется размещение.
     * @param constraints принимает одно из значений <code>RowTemplate.COLUMNLIST</code> или
     *        <code>RowTemplate.FIXED_COLUMNLIST</code>.
     * @see RowTemplate
     * @ENDRUS
     *
     * Lays out the specified row template using the specified constraints. 
     * The method may change <code>height</code> and/or <code>width</code> property values of the row template.
     * @param rowTemplate the row template to be laid out.
     * @param constaints may be one of <code>RowTemplate.COLUMNLIST</code> or
     * <code>RowTemplate.FIXED_COLUMNLIST</code>
     * @see RowTemplate
     */
    void layoutTemplate(RowTemplate rowTemplate);

    /**
     * @RUS
     *  Удаляет специфицированную колонку из процесса размещения.
     * @param column колонка, подлежащая удалению.
     * @ENDRUS
     *
     *  Removes the specified column from the lay out.
     *  @param column to be removed.
     */
    void removeLayoutCell(TableCell cell);
    
    
   
            
}//interface
