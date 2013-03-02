package com.automatak.dnp3.tools.controls;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.automatak.dnp3.LogEntry;
import com.automatak.dnp3.LogSubscriber;
import javafx.beans.property.ObjectPropertyBase;

public class LogTable extends JTable implements LogSubscriber {

    private final static String[] tableColumns = new String[]{"timestamp", "severity", "logger", "message"};
    private final DefaultTableModel model = new MyTableModel();

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

    public LogTable() {
        super();
        this.setModel(model);
    }

    @Override
    public void onLogEntry(final LogEntry entry)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String[] row = new String[]{entry.getTimestamp().toString(), entry.getLogLevel().toString(), entry.getLoggerName(), entry.getMessage()};
                model.addRow(row);
            }
        });

    }
}
