/*
 * FreezeCapableLayout.java
 *
 * Created on 7 ��� 2007 �., 10:02
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
     *   ���������� ������ �������, ������������� ������� � �����, ��� ��� ������������
     *   ������� ������ columnModel ������� � �������� <code>columnIndex</code> �����
     *   ����������� ��������������.
     * @ENDRUS
     */
    TableCell[] getFreezeCapableCells(int columnIndex);
    /**
     * @param freezeTemplate
     * @return
     * @RUS
     * ������� � ���������� layout manager ��� ������������� �������.
     * ��������������, ���:
     * <UL>
     *   <LI>
     *       �������� <code>freezeTemplate</code> �� ����� <code>null</code>
     *   </LI>
     *   <LI>
     *       �������� �������� <code>freezeTemplate.table</code> �� ����� <code>null</code> �
     *       � ��������� �� �������, ������������ ������������� �������.
     *   </LI>
     * </UL>
     * @ENDRUS
     */
    ColumnLayoutManager createFreezeCellLayout(RowTemplate freezeTemplate );
    
    /**
     * @param targetTemplate
     * @param columnIndex
     * @RUS
     *  ���������� ������ ������� layout manager, ��������� � �������� <code>columnIndex</code>
     *  � layout manager ��������� ���������� �������. ��� ����, ������������� ������� ���������
     *  �� �������� ������� � ����������� � ������ ����������.  
     * @ENDRUS
     * @see RowTemplate
     */
    void moveCells(RowTemplate targetTemplate, int columnIndex);
}
