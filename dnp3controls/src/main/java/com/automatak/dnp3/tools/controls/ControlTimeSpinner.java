package com.automatak.dnp3.tools.controls;

import javax.swing.*;

public class ControlTimeSpinner extends JSpinner {

    private SpinnerNumberModel model;

    public ControlTimeSpinner()
    {
       model = new SpinnerNumberModel(500, 0, 65535, 100);
       this.setModel(model);
    }

    public int getControlTime()
    {
        return (Integer) this.getValue();
    }
}
