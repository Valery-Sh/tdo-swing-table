/*
 * DataViewCellEditorListener.java
 * 
 * Created on 08.05.2007, 14:35:03
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table.editor;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author valery
 */
public interface DataViewCellEditorListener extends CellEditorListener{
   /**
     * ���������� �������� �������� ������������� ������.<p>
     * ���������� <code>true</code>, ���� �������� ������� � <code>false</code>
     * � ��������� ������. <p> ����� ������ ������������ �� ������
     * <code>stopCellEditing()</code> � ����� ������� ������
     * <code>editingStoped</code>.
     *
     * @param e
     * @return   - <code>true</code>, ���� �������� ������� � <code>false</code>
     * � ��������� ������.

     */
     boolean editingValid(ChangeEvent e);
}
