package com.automatak.dnp3.tools.plugins.example.outstation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 3/8/13
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExampleOutstationFactoryUI extends JFrame {
    private JSlider sliderRateUpdatesPerSecond;
    private JPanel panelMain;
    private JTextField textFieldNumOutstations;
    private JTextField textFieldNUmUpdates;
    final private ExampleOutstationPluginFactory factory;
    private int numUpdates = 0;
    private int numOutstations = 0;

    public ExampleOutstationFactoryUI(final ExampleOutstationPluginFactory factory)
    {
        this.factory = factory;
        this.setTitle("Example OutstationFactory UI");
        updateNumUpdates();
        sliderRateUpdatesPerSecond.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = sliderRateUpdatesPerSecond.getValue();
                factory.setUpdateRate(value);
            }
        });
    }

    void updateNumUpdates()
    {
        this.textFieldNUmUpdates.setText(Integer.toString(numUpdates));
    }

    void updateNumOutstations()
    {
        this.textFieldNumOutstations.setText(Integer.toString(numOutstations));
    }

    public void addUpdates(final int i)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                numUpdates += i;
                updateNumUpdates();
            }
        });
    }

    public void setNumOutstations(final int i)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                numOutstations = i;
                updateNumOutstations();
            }
        });
    }
}
