package com.automatak.dnp3.tools.testset.ui;

import com.automatak.dnp3.EventCounterResponse;

import javax.swing.*;

public class EventCounterComboBox extends JComboBox<EventCounterResponse> {

    private static final ComboBoxModel<EventCounterResponse> eventCounterModel = new DefaultComboBoxModel<EventCounterResponse>(EventCounterResponse.values());

    public EventCounterComboBox()
    {
        super(eventCounterModel);
        this.setSelectedItem(EventCounterResponse.GROUP22_VAR1);
    }

    EventCounterResponse getResponse()
    {
        return (EventCounterResponse) getSelectedItem();
    }
}
