package com.automatak.dnp3.tools.testset.ui;

import com.automatak.dnp3.StaticAnalogResponse;

import javax.swing.*;

public class StaticAnalogComboBox extends JComboBox<StaticAnalogResponse> {

    private static final ComboBoxModel<StaticAnalogResponse> staticAnalogModel = new DefaultComboBoxModel<StaticAnalogResponse>(StaticAnalogResponse.values());

    public StaticAnalogComboBox()
    {
        super(staticAnalogModel);
        this.setSelectedItem(StaticAnalogResponse.GROUP30_VAR1);
    }
    StaticAnalogResponse getResponse()
    {
        return (StaticAnalogResponse) getSelectedItem();
    }
}
