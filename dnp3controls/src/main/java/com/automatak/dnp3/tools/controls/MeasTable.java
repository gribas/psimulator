package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.BinaryInput;
import com.automatak.dnp3.LogEntry;
import com.automatak.dnp3.LogSubscriber;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

public class MeasTable extends JTable {

    private final static String[] tableColumns = new String[]{"index", "value", "quality", "timestamp"};
    private final DefaultTableModel model = new MyTableModel();
    private final Map<Long, String[]> rowMap = new java.util.HashMap<Long, String[]>();

    private class MyTableModel extends DefaultTableModel {

        public MyTableModel() {
            super(new Object[][]{}, tableColumns);
        }

        @Override
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    }

    public MeasTable() {
        super();
        this.setModel(model);
    }

   public void updateBinary(BinaryInput bi, long index)
   {
      int idx = (int) index;
      String[] rowData = rowMap.get(index);
      if(rowData == null) {
         rowData = new String[]{Long.toString(index), Boolean.toString(bi.getValue()), Byte.toString(bi.getQuality()), Long.toString(bi.getMsSinceEpoch())};
         model.insertRow(idx, rowData);
         rowMap.put(index, rowData);
      }
      else {
         model.setValueAt(Long.toString(idx), idx, 0);
         model.setValueAt(Boolean.toString(bi.getValue()), idx, 1);
         model.setValueAt(Byte.toString(bi.getQuality()), idx, 2);
         model.setValueAt(Long.toString(bi.getMsSinceEpoch()), idx, 3);
      }
   }
}
