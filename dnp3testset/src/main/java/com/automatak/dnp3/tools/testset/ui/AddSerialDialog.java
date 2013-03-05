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
package com.automatak.dnp3.tools.testset.ui;

import com.automatak.dnp3.FlowControl;
import com.automatak.dnp3.LogLevel;
import com.automatak.dnp3.Parity;
import com.automatak.dnp3.SerialSettings;
import com.automatak.dnp3.tools.controls.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddSerialDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextField textFieldPortName;
    private BaudRateSpinner spinnerBaudRate;
    private TimeoutSpinner spinnerOpenRetry;
    private LogComboBox comboBoxLogLevel;
    private UInt16Spinner spinnerDataBits;
    private UInt16Spinner spinnerStopBits;
    private ParityComboBox comboBoxParity;
    private FlowControlComboBox comboBoxFlowControl;

    private final AddSerial listener;

    private SerialSettings getSettings()
    {
        String port = textFieldPortName.getText();
        int baud = spinnerBaudRate.getBaudRate();
        int dataBits = spinnerDataBits.getUInt16();
        int stopBits = spinnerStopBits.getUInt16();
        FlowControl flow = comboBoxFlowControl.getFlowControl();
        Parity parity = comboBoxParity.getParity();
        return new SerialSettings(port, baud, dataBits, parity, stopBits, flow);
    }

    public AddSerialDialog(AddSerial listener) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.listener = listener;
        this.spinnerDataBits.setValue(8);
        this.spinnerStopBits.setValue(1);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        LogLevel level = comboBoxLogLevel.getLogLevel();
        int retry = spinnerOpenRetry.getTimeout();
        listener.onAdd(level, retry, getSettings());
        dispose();
    }
}
