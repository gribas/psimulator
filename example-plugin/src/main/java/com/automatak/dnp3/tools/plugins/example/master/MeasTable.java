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
package com.automatak.dnp3.tools.plugins.example.master;

import com.automatak.dnp3.BaseMeasurement;
import com.automatak.dnp3.tools.pluginapi.ui.QualityConverter;
import com.automatak.dnp3.tools.pluginapi.StaticResources;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Map;

public class MeasTable extends JTable {

    private final static String[] tableColumns = new String[]{"index", "value", "quality", "timestamp"};
    private final DefaultTableModel model = new MyTableModel();
    private final Map<Long, String> rowMap = new java.util.HashMap<Long, String>();

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

    @Override
    public String getToolTipText(MouseEvent e)
    {
        long row = rowAtPoint(e.getPoint());
        String tip = rowMap.get(row);
        return tip;
    }

    public void update(String value, BaseMeasurement meas, QualityConverter converter, long index)
    {
      int idx = (int) index;

      String ts = StaticResources.defaulUTCDateFormat.format(new Date(meas.getMsSinceEpoch()));
      String qualityPneumonic = converter.getQualitySummary(meas.getQuality());
      String qualityDesc = converter.getQualityDescription(meas.getQuality());
      String rowData = rowMap.get(index);

      if(rowData == null) {
         String[] row = new String[]{ Long.toString(index), value, qualityPneumonic, ts };
         model.insertRow(idx, row);
         rowMap.put(index, qualityDesc);
      }
      else {
         rowMap.put(index,qualityDesc);
         model.setValueAt(Long.toString(idx), idx, 0);
         model.setValueAt(value, idx, 1);
         model.setValueAt(qualityPneumonic, idx, 2);
         model.setValueAt(ts, idx, 3);
      }
    }

}
