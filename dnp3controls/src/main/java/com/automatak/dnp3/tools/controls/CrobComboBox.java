package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.ControlCode;

import javax.swing.*;

public class CrobComboBox extends JComboBox {

    public CrobComboBox()
    {
        for(ControlCode code : ControlCode.values()) {
            this.addItem(code);
            this.setSelectedItem(ControlCode.LATCH_ON);
        }
    }

    public ControlCode getControlCode()
    {
        return (ControlCode) this.getSelectedItem();
    }
}
