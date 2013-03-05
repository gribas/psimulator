package com.automatak.dnp3.tools.testset.ui;

import com.automatak.dnp3.StaticCounterResponse;

import javax.swing.*;

public class StaticCounterComboBox extends JComboBox<StaticCounterResponse> {

    private static final ComboBoxModel<StaticCounterResponse> staticCounterModel = new DefaultComboBoxModel<StaticCounterResponse>(StaticCounterResponse.values());

    public StaticCounterComboBox()
    {
        super(staticCounterModel);
        this.setSelectedItem(StaticCounterResponse.GROUP20_VAR1);
    }
    StaticCounterResponse getResponse()
    {
        return (StaticCounterResponse) getSelectedItem();
    }
}
