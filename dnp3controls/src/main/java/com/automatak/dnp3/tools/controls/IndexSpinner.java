package com.automatak.dnp3.tools.controls;

import javax.swing.*;

public class IndexSpinner extends JSpinner {

    private SpinnerNumberModel model;

    public IndexSpinner()
    {
       model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
       this.setModel(model);
    }

    public int getIndex()
    {
        return (Integer) this.getValue();
    }
}
