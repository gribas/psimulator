package com.automatak.dnp3.tools.controls;

import javax.swing.*;
import com.automatak.dnp3.LogLevel;

public class LogComboBox extends JComboBox {

    public LogComboBox()
    {
        for(LogLevel level : LogLevel.values()) {
            this.addItem(level);
            this.setSelectedItem(LogLevel.INFO);
        }
    }

    public LogLevel getLogLevel()
    {
        return (LogLevel) this.getSelectedItem();
    }
}
