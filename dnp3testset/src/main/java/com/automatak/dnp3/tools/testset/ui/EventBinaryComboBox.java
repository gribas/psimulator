package com.automatak.dnp3.tools.testset.ui;

import com.automatak.dnp3.EventBinaryResponse;

import javax.swing.*;

public class EventBinaryComboBox extends JComboBox<EventBinaryResponse> {

    private static final ComboBoxModel<EventBinaryResponse> eventBinaryModel = new DefaultComboBoxModel<EventBinaryResponse>(EventBinaryResponse.values());

    public EventBinaryComboBox()
    {
        super(eventBinaryModel);
        super.setSelectedItem(EventBinaryResponse.GROUP2_VAR1);
    }
    EventBinaryResponse getResponse()
    {
        return (EventBinaryResponse) getSelectedItem();
    }
}
