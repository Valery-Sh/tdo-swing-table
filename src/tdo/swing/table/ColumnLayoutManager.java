/*
 * ColumnLayoutManager.java
 *
 * Created on 18 ������ 2007 �., 15:36:00
 *
 */

package tdo.swing.table;

/**
 * 
 * ���������� ��������� ��� �������, ��� �����������, ������� ����� 
 * ��� ��������� ���������� ������� ������ ������� (RowTemplate) ����� ������� {@link tdo.swing.table.TableEx}.
 * 
 * 
 *
 */
public interface ColumnLayoutManager {
    
    void setTemplate( RowTemplate template);
    /**
     * @RUS
     *  ��������� �������, ������� ���������� ���������� � �������� ���������� ������ 
     *  ������� ����� �������.
     * @param column ����������� �������.
     * @param constraints ���������� ��� � ��� ������ ������� ����������� �������. ����������
     *        �������� ��������� � �� ��������� ������������ �������, ����������� ���������.
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
     *  ��������� ���������� ������� ������ ��������� ���������� ������� �����, ���������
     *  �������, ������������ ������ ����������. ����� �������� �������� ������� <code>height</height>
     *  � (���) <code>width</code> �������, ��� �������� ����������� ����������.
     * 
     * @param rowTemplate ������ �����, ��� �������� ����������� ����������.
     * @param constraints ��������� ���� �� �������� <code>RowTemplate.COLUMNLIST</code> ���
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
     *  ������� ����������������� ������� �� �������� ����������.
     * @param column �������, ���������� ��������.
     * @ENDRUS
     *
     *  Removes the specified column from the lay out.
     *  @param column to be removed.
     */
    void removeLayoutCell(TableCell cell);
    
    
   
            
}//interface
