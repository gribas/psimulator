/**
 * Copyright 2013 Automatak, LLC
 *
 * Licensed to Automatak, LLC under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Automatak, LLC licenses this file to you
 * under the GNU Affero General Public License Version 3.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/agpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.automatak.dnp3.tools.plugins.example.outstationgui;

import com.automatak.dnp3.*;
import com.automatak.dnp3.tools.pluginapi.StaticResources;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 3/4/13
 * Time: 5:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExampleOutstationUI extends JFrame {

    private JCheckBox input1CheckBox;
    private JPanel panelMain;
    private JCheckBox checkBoxOutput1State;
    private JCheckBox input2CheckBox;
    private JCheckBox checkBoxOutput2State;
    private JSlider sliderAnalog1;
    private JSlider sliderAnalog2;
    private JProgressBar progressBarSetpoint1;
    private JProgressBar progressBarSetpoint2;
    private JButton buttonCommit;


    private final DataObserver observer;

    public void setBinaryOutput1(final boolean value) {
        final ExampleOutstationUI frame = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               checkBoxOutput1State.setSelected(value);
            }
        });
    }

    public void setBinaryOutput2(final boolean value) {
        final ExampleOutstationUI frame = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                checkBoxOutput2State.setSelected(value);
            }
        });
    }

    public void setAnalogOutput1(final double value) {
        final ExampleOutstationUI frame = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBarSetpoint1.setValue((int) value*10);
            }
        });
    }

    public void setAnalogOutput2(final double value) {
        final ExampleOutstationUI frame = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBarSetpoint2.setValue((int) value*10);
            }
        });
    }

    public ExampleOutstationUI(final DataObserver observer) {

        super("Example Outstation UI");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setResizable(false);
        this.setContentPane(panelMain);
        this.setIconImage(StaticResources.dnpIcon);


        this.observer = observer;

        buttonCommit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                long timestamp = System.currentTimeMillis();
                observer.start();
                observer.update(new BinaryInput(input1CheckBox.isSelected(), BinaryInputQuality.ONLINE.toByte(), timestamp), 0);
                observer.update(new BinaryInput(input2CheckBox.isSelected(), BinaryInputQuality.ONLINE.toByte(), timestamp), 1);
                observer.update(new AnalogInput(sliderAnalog1.getValue()/10.0, AnalogInputQuality.ONLINE.toByte(), timestamp), 0);
                observer.update(new AnalogInput(sliderAnalog2.getValue()/10.0, AnalogInputQuality.ONLINE.toByte(), timestamp), 1);
                observer.end();
            }
        });
    }
}
