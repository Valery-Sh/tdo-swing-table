/*
 * FreezeCapableLayout.java
 *
 * Created on 7 Май 2007 г., 10:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

/**
 *
 * @author valery
 */
public interface FreezeCapableLayout {
    
    /**
     * @param columnIndex
     * @return
     * @RUS
     *   Возвращает массив колонок, принадлежащих шаблону и таких, что при фиксировании
     *   колонки модели columnModel тавлицы с индексом <code>columnIndex</code> также
     *   становятися фиксированными.
     * @ENDRUS
     */
    TableCell[] getFreezeCapableCells(int columnIndex);
    /**
     * @param freezeTemplate
     * @return
     * @RUS
     * Создает и возвращает layout manager для фиксированных колонок.
     * Предполагается, что:
     * <UL>
     *   <LI>
     *       параметр <code>freezeTemplate</code> не равен <code>null</code>
     *   </LI>
     *   <LI>
     *       значение свойства <code>freezeTemplate.table</code> не равно <code>null</code> и
     *       и указывает на таблицу, отображающую фиксированные колонки.
     *   </LI>
     * </UL>
     * @ENDRUS
     */
    ColumnLayoutManager createFreezeCellLayout(RowTemplate freezeTemplate );
    
    /**
     * @param targetTemplate
     * @param columnIndex
     * @RUS
     *  Перемещает ячейки данного layout manager, связанные с колонкой <code>columnIndex</code>
     *  в layout manager заданного параметром шаблона. При этом, перемнещаемые колонки удаляются
     *  из текущего шаблона и добавляются в шаблон назначения.  
     * @ENDRUS
     * @see RowTemplate
     */
    void moveCells(RowTemplate targetTemplate, int columnIndex);
}
