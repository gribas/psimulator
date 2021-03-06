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
package com.automatak.dnp3.tools.testset.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.automatak.dnp3.LogEntry;
import com.automatak.dnp3.LogSubscriber;
import com.automatak.dnp3.tools.pluginapi.StaticResources;

public class LogTable extends JTable implements LogSubscriber {

    private final static String[] tableColumns = new String[]{"timestamp", "severity", "logger", "message"};
    private final DefaultTableModel model = new MyTableModel();
    private int maxTableSize = 500;

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private boolean isEnabled = true;

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

    public void setMaxTableSize(int maxTableSize)
    {
        assert(maxTableSize > 0);
        this.maxTableSize = maxTableSize;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                checkTableSize();
            }
        });
    }

    private void checkTableSize()
    {
        while(model.getRowCount() > maxTableSize)
        {
            model.removeRow(0); //delete oldest log entries
        }
    }

    public LogTable() {
        super();
        this.setModel(model);
    }

    @Override
    public void onLogEntry(final LogEntry entry)
    {
        if(isEnabled) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    String timestamp = StaticResources.defaulUTCDateFormat.format(entry.getTimestamp());
                    String[] row = new String[]{timestamp, entry.getLogLevel().toString(), entry.getLoggerName(), entry.getMessage()};
                    model.addRow(row);
                    checkTableSize();
                }
            });
        }
    }
}
