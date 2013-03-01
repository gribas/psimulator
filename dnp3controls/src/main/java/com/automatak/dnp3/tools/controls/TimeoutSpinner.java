package com.automatak.dnp3.tools.controls;

import javax.swing.*;

public class TimeoutSpinner extends JSpinner {

    private SpinnerNumberModel model;

    public TimeoutSpinner()
    {
       model = new SpinnerNumberModel(5000, 500, 60000, 500);
       this.setModel(model);
    }

    public int getRetry()
    {
        return (Integer) this.getValue();
    }
}
