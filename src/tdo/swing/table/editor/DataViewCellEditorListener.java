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
     * ѕроизводит контроль значени€ редактируемой €чейки.<p>
     * ¬озвращает <code>true</code>, если контроль успешен и <code>false</code>
     * в противном случае. <p> ¬ызов метода производитс€ из метода
     * <code>stopCellEditing()</code> и перед вызовом метода
     * <code>editingStoped</code>.
     *
     * @param e
     * @return   - <code>true</code>, если контроль успешен и <code>false</code>
     * в противном случае.

     */
     boolean editingValid(ChangeEvent e);
}
