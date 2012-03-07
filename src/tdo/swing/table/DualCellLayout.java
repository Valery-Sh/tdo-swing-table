/*
 * DualCellLayout.java
 *
 * Created on 30 Апрель 2007 г., 13:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tdo.swing.table;

import java.util.HashMap;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Valera
 */
public class DualCellLayout implements ColumnLayoutManager, FreezeCapableLayout {

    protected RowTemplate template;
    protected HashMap<TableCell, String[]> layoutInfo;
    protected HashMap<String, TableCell[]> idColumns;

    public DualCellLayout(RowTemplate template) {
        this.template = template;
        initMap();
    }

    public DualCellLayout() {
    }

    public void setTemplate(RowTemplate template) {
        this.template = template;
        initMap();
    }

    private void initMap() {
        layoutInfo = new HashMap(template.getColumnCount());
        idColumns = new HashMap(template.getColumnCount()); // actually it must be table.getColumnCount()
    }

    public void addLayoutCell(TableCell cell, String targetIdentifier) {
        addLayoutCell(cell, targetIdentifier, null);
    }

    /**
     * Добавляет колонку из шаблона привявывая ее к колонке из коллекции
     * <code>JTable.getColumnModel()</code> используя
     * <code>targetIdentifier</code> в позицию, заданную
     * <code>constraints</code>.
     *
     * @param cell
     * @param targetIdentifier
     * @param constraints
     */
    public void addLayoutCell(TableCell cell, String targetIdentifier, String constraints) {

        String[] info = new String[]{targetIdentifier, constraints};

        layoutInfo.put((TableCell) cell, info);

        TableCell[] a = idColumns.get(targetIdentifier);
        if (a == null) {
            a = new TableCell[2];
            a[0] = null;
            a[1] = null;
        }

        if (constraints == null || constraints.toUpperCase().equals("TOP") || constraints.toUpperCase().equals("CENTER")) {
            a[0] = cell;
        } else {
            a[1] = cell;
        }

        idColumns.put(targetIdentifier, a);

    }

    public void layoutTemplate(RowTemplate rowTemplate) {
        if (rowTemplate.getTable().getClass() == FreezeTableViewer.class) {
            System.out.println("************ tableClass" + rowTemplate.getTable().getClass());
        }
        this.template = rowTemplate;

        int w = 0;
        int h;// = 0;

        TableViewer table = rowTemplate.table;
        if (table == null) {
            return;
        }
        TableColumnModel tcm = table.getColumnModel();
        //RowTemplate cmt = table.getRowTemplates().get("COLUMNMODEL");
        if (tcm == null) {
            return;
        }

        String targetId;// = null;
        String constraints;// = null;

        for (TableCell tc : rowTemplate.getColumnList()) {

            String[] info = layoutInfo.get(tc);
            if (info == null || info[0] == null) {
                //TODO
                continue;
            }

            targetId = info[0];
            constraints = info[1] == null ? "CENTER" : info[1].toUpperCase();
            int targetIndex = tcm.getColumnIndex(targetId);
            int targetX = table.getColumnXOffset(targetIndex, false);
            TableCell[] a = this.idColumns.get(targetId);
            TableCell topCell = null;
            TableCell bottomCell = null;

            if (constraints.equals("CENTER")) {
                topCell = a[0];
            } else {
                topCell = a[0];
                bottomCell = a[1];
            }

            int idx = tcm.getColumnIndex(targetId);
            if (idx < 0) {
                continue;
            }

            TableColumn c = tcm.getColumn(idx);

            if (constraints.equals("CENTER")) {
                tc.setX(targetX);
                tc.setMaxWidth(c.getMaxWidth());
                tc.setPreferredWidth(c.getPreferredWidth());
                tc.setWidth(c.getWidth());
                w += tc.getWidth();

                h = rowTemplate.getHeight();
                tc.setY(0);
                tc.setHeight(h);
                continue;
            }
            if (constraints.equals("TOP")) {
                tc.setX(targetX);
                tc.setMaxWidth(c.getMaxWidth());
                tc.setPreferredWidth(c.getPreferredWidth());
                tc.setWidth(c.getWidth());
                //w += tc.getWidth();

                //h = rowTemplate.getHeight();
                tc.setY(0);
                tc.setHeight(tc.getPreferredHeight());
                System.out.println("TOP HEIGHT=" + tc.getPreferredHeight());
                continue;
            }
            if (constraints.equals("BOTTOM")) {
                tc.setX(targetX);
                tc.setMaxWidth(c.getMaxWidth());
                tc.setPreferredWidth(c.getPreferredWidth());

                tc.setWidth(c.getWidth());
                w += tc.getWidth();

                h = rowTemplate.getHeight();
                tc.setY(h - tc.getPreferredHeight());
                tc.setHeight(tc.getPreferredHeight());
                continue;
            }


        }//for

        //rowTemplate.updateWidth(w);
    }

    public void removeLayoutCell(TableCell cell) {

        String[] info = layoutInfo.get(cell);

        String targetIdentifier = info[0];
        String constraints = info[1];

        TableCell[] a = idColumns.get(targetIdentifier);
        if (a == null) {
            a = new TableCell[2];
            a[0] = null;
            a[1] = null;
        }

        if (constraints == null || constraints.toUpperCase().equals("TOP") || constraints.toUpperCase().equals("CENTER")) {
            a[0] = null;
        } else {
            a[1] = null;
        }

        layoutInfo.remove(cell);

        if (a[0] == null && a[1] == null) {
            idColumns.remove(targetIdentifier);

        }


    }

    /**
     * @RUS Возвращает массив колонок, принадлежащих шаблону и таких, что при
     * фиксировании колонки модели columnModel тавлицы с индексом
     * <code>columnIndex</code> также становятися фиксированными. @ENDRUS
     */
    public TableCell[] getFreezeCapableCells(int columnIndex) {

        if (template == null) {
            return null;
        }
        return new TableCell[]{template.getColumnList().get(columnIndex)};
    }
    /*
     * protected class TargetLayoutItem { public String identifier; public
     * String constraints;
     *
     * public TargetLayoutItem(String id, String constraints) { this.identifier
     * = id; this.constraints = constraints; } }//class TargetLayoutItem
     */

    public ColumnLayoutManager createFreezeCellLayout(RowTemplate freezeTemplate) {
        if (freezeTemplate == null || freezeTemplate.getTable() == null) {
            return null;
        }
        TableViewer ftable = freezeTemplate.getTable();
        DualCellLayout result = new DualCellLayout(freezeTemplate);
        return result;
    }

    /**
     * @RUS
     *
     * @ENDRUS
     */
    public void moveCells(RowTemplate targetTemplate, int columnIndex) {
        DualCellLayout dlm = (DualCellLayout) targetTemplate.getColumnLayout();
        String targetIdentifier = this.template.getTable().getColumnModel().getColumn(columnIndex).getIdentifier().toString();

        TableCell[] a = idColumns.get(targetIdentifier);
        TableCell[] anew = new TableCell[]{a[0], a[1]};

        String[] info;//My 06.03.2012 = null;

        if (a[0] != null) {
            info = this.layoutInfo.get(a[0]);
            targetTemplate.add(anew[0]);
            dlm.addLayoutCell(anew[0], targetIdentifier, info[1]);
            this.template.remove(a[0]);
            this.template.getColumnLayout().removeLayoutCell(a[0]);

        }
        if (a[1] != null) {
            info = this.layoutInfo.get(a[1]);
            targetTemplate.add(anew[1]);
            dlm.addLayoutCell(anew[1], targetIdentifier, info[1]);
            this.template.remove(a[1]);
            this.template.getColumnLayout().removeLayoutCell(a[1]);

        }


    }
}//class
