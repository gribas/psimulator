package com.automatak.dnp3.tools.controls;

import javax.swing.*;

public class TcpPortSpinner extends JSpinner {

    private SpinnerNumberModel model;

    public TcpPortSpinner()
    {
       model = new SpinnerNumberModel(20000, 0, 65535, 1);
       this.setModel(model);
    }

    public int getPort()
    {
        return (Integer) this.getValue();
    }
}
