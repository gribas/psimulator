package com.automatak.dnp3.tools.testset.ui;

import com.automatak.dnp3.StaticBinaryResponse;

import javax.swing.*;

public class StaticBinaryComboBox extends JComboBox<StaticBinaryResponse> {

    private static final ComboBoxModel<StaticBinaryResponse> staticBinaryModel = new DefaultComboBoxModel<StaticBinaryResponse>(StaticBinaryResponse.values());

    public StaticBinaryComboBox()
    {
        super(staticBinaryModel);
        this.setSelectedItem(StaticBinaryResponse.GROUP1_VAR2);
    }
    StaticBinaryResponse getResponse()
    {
        return (StaticBinaryResponse) getSelectedItem();
    }
}
