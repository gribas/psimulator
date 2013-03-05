package com.automatak.dnp3.tools.testset.ui;

import com.automatak.dnp3.StaticAnalogOutputStatusResponse;

import javax.swing.*;

public class StaticAnalogOutputStatusComboBox extends JComboBox<StaticAnalogOutputStatusResponse> {

    private static final ComboBoxModel<StaticAnalogOutputStatusResponse> staticAnalogOutputStatusModel = new DefaultComboBoxModel<StaticAnalogOutputStatusResponse>(StaticAnalogOutputStatusResponse.values());

    public StaticAnalogOutputStatusComboBox()
    {
        super(staticAnalogOutputStatusModel);
        this.setSelectedItem(StaticAnalogOutputStatusResponse.GROUP40_VAR1);
    }
    StaticAnalogOutputStatusResponse getResponse()
    {
        return (StaticAnalogOutputStatusResponse) getSelectedItem();
    }
}
