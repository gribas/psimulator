package com.automatak.dnp3.tools.controls;

import javax.swing.*;

public class NoCommaSpinner extends JSpinner {

    public NoCommaSpinner()
    {

    }

    protected void setNoComma()
    {
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(this, "#");
        this.setEditor(editor);
    }
}
