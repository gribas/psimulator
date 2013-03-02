package com.automatak.dnp3.tools.controls;

import javax.swing.*;

public class ControlCountSpinner extends JSpinner {

    private SpinnerNumberModel model;

    public ControlCountSpinner()
    {
       model = new SpinnerNumberModel(1, 0, 255, 1);
       this.setModel(model);
    }

    public int getControlCount()
    {
        return (Integer) this.getValue();
    }
}
