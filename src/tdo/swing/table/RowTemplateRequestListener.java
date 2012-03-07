/*
 * RowTemplateRequestListener.java
 *
 * Created on 4 ��� 2007 �., 10:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

/**
 * @RUS
 * ���������� ������ ��� ��������� ������� {@link RowTemplateRequestEvent}, �������������
 * ��� ������� ������� ��� ��������� ����.
 * @see RowTemplateRequestHandler
 * @ENDRUS
 * 
 */
public interface RowTemplateRequestListener {
    /**
     * ���������� �������� ����� �������, ������������ ��� ��������� ����.
     * @param e �������, ��������� ���������� ������ ����.
     * @param rowIndex ������ ����, ��� �������� ������������ ������.
     */
    void defineKey(RowTemplateRequestEvent e, int rowIndex);
}//interface
