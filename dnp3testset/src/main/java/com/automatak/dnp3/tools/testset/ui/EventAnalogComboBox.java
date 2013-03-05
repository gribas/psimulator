package com.automatak.dnp3.tools.testset.ui;

import com.automatak.dnp3.EventAnalogResponse;

import javax.swing.*;

public class EventAnalogComboBox extends JComboBox<EventAnalogResponse> {
    private static final ComboBoxModel<EventAnalogResponse> eventAnalogModel = new DefaultComboBoxModel<EventAnalogResponse>(EventAnalogResponse.values());

    public EventAnalogComboBox()
    {
        super(eventAnalogModel);
        this.setSelectedItem(EventAnalogResponse.GROUP32_VAR1);
    }
    EventAnalogResponse getResponse()
    {
        return (EventAnalogResponse) getSelectedItem();
    }
}
