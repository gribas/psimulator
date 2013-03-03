/**
 * Copyright 2013 Automatak, LLC
 *
 * Licensed to Automatak, LLC under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Automatak, LLC licenses this file to you
 * under the GNU Affero General Public License Version 3.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/agpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.BinaryInput;
import com.automatak.dnp3.LogEntry;
import com.automatak.dnp3.LogSubscriber;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class MeasTable extends JTable {

    private final static String[] tableColumns = new String[]{"index", "value", "quality", "timestamp"};
    private final DefaultTableModel model = new MyTableModel();
    private final Map<Long, String> rowMap = new java.util.HashMap<Long, String>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

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
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public String getToolTipText(MouseEvent e)
    {
        long row = rowAtPoint(e.getPoint());
        String tip = rowMap.get(row);
        return tip;
    }

   public void updateBinary(BinaryInput bi, long index)
   {
      int idx = (int) index;
      String qualityPneumonic = QualityConverter.getBinaryQualitySummary(bi.getQuality());
      String timestamp = sdf.format(new Date(bi.getMsSinceEpoch()));
      String rowData = rowMap.get(index);

      if(rowData == null) {
         String[] row = new String[]{ Long.toString(index), Boolean.toString(bi.getValue()), qualityPneumonic, timestamp };
         model.insertRow(idx, row);
         rowMap.put(index, QualityConverter.getBinaryQualityDescription(bi.getQuality()));
      }
      else {
         rowMap.put(index, QualityConverter.getBinaryQualityDescription(bi.getQuality()));
         model.setValueAt(Long.toString(idx), idx, 0);
         model.setValueAt(Boolean.toString(bi.getValue()), idx, 1);
         model.setValueAt(qualityPneumonic, idx, 2);
         model.setValueAt(timestamp, idx, 3);
      }
   }
}
