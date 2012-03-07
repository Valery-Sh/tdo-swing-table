/*
 * RowTemplateRequestHandler.java
 *
 * Created on 4 ��� 2007 �., 10:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.swing.table;

/**
 * 
 * 
 * @ENDRUS 
 * @RUS ����������� �����, ����������� ��������� {@link RowTemplateRequestListener}.
 *  ����� #defineKey ������ ���������� �������� ���������� � ���������� ����������
 *  ������ {@link #setKey()} ��� ����������. ����� ����� ���� �����������, ��������,
 *  ��������� �������:
 *  <code>
 *    <pre>
 *       TableViewer table = ...
 *       RowTemplateRequetHandler rth = new RowTemplateRequetHandler() {
 *              public void setKey() {
 *                  String key;
 *                  int row = this.getRowIndex{};
 *                  TableViewer t = getTable();
 *                  // ���, ����������� �������� key.
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
     * ����������� �����, ������������ �������� ������������ ��� ���������� ���������
     * ����������� ������� ���� ��� ������ ������� � ������� ����.
     * @see #getRowIndex
     * @see #getTable
     * @see #setKey(String)
     * @ENDRUS 
     */
    public abstract void setKey(); 
    
    /**
     * ����� ���������� {@link RowTemplateRequestListener}.
     * ���������� ������� <code>getRowTemplate(rowIndex)</code> ������ {@link TableViewer}.
     * ��������� �������� ���������� ��� ����������� ������������� �������� ������� ������.
     * 
     * @param e ������� ��� ������� ������� ��� ���� �������
     * @param rowIndex ������ ���� �������, ��� �������� ������������ ������
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
     * @return ���������� ������ ����, ��� �������� ���������� ���������� ���� �������.
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
