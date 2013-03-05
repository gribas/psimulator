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
package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CrobDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonSBO;
    private JTextField textFieldStatus;
    private JButton buttonDO;
    private ControlTimeSpinner onTimeSpinner;
    private ControlTimeSpinner offTimeSpinner;
    private CrobComboBox comboBoxCode;
    private IndexSpinner spinnerIndex;
    private ControlCountSpinner spinnerCount;
    private JTabbedPane tabbedPane1;
    private JSpinner spinnerInt16;

    private final CommandProcessor processor;

    private ControlRelayOutputBlock createCROB()
    {
        ControlCode cc =  comboBoxCode.getControlCode();
        short count = (short) spinnerCount.getControlCount();
        int onTime = onTimeSpinner.getControlTime();
        int offTime = offTimeSpinner.getControlTime();
        return new ControlRelayOutputBlock(cc, count, onTime, offTime, CommandStatus.SUCCESS);
    }

    private void beginCommand(String text)
    {
       this.buttonDO.setEnabled(false);
       this.buttonSBO.setEnabled(false);
       this.textFieldStatus.setText(text);
    }

    private void endCommand(final CommandStatus status)
    {
        final CrobDialog myDialog = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              myDialog.textFieldStatus.setText(myDialog.textFieldStatus.getText() + status);
                myDialog.buttonDO.setEnabled(true);
                myDialog.buttonSBO.setEnabled(true);
            }
        });
    }

    public CrobDialog(final CommandProcessor processor) {
        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(buttonSBO);
        setResizable(false);

        this.processor = processor;
        spinnerInt16.setModel(new SpinnerNumberModel(0, 0, 65535, 1));

        final CrobDialog myDialog = this;

        buttonDO.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(myDialog.buttonDO.isEnabled()) {
                    int index = spinnerIndex.getIndex();
                    if(tabbedPane1.getSelectedIndex() == 0) {
                        ControlRelayOutputBlock crob = createCROB();
                        beginCommand("Direct operate... ");
                        processor.directOperate(crob, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myDialog.endCommand(value);
                            }
                        });
                    } else if(tabbedPane1.getSelectedIndex() == 1) {
                        AnalogOutputInt16 ao = new AnalogOutputInt16(((Integer) spinnerInt16.getValue()).shortValue(), CommandStatus.SUCCESS);
                        beginCommand("Direct operate... ");
                        processor.directOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myDialog.endCommand(value);
                            }
                        });
                    }
                }
            }
        });

        buttonSBO.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(myDialog.buttonDO.isEnabled()) {
                    int index = spinnerIndex.getIndex();

                    if(tabbedPane1.getSelectedIndex() == 0) {
                        ControlRelayOutputBlock crob = createCROB();
                        beginCommand("Select and operate... ");
                        processor.selectAndOperate(crob, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myDialog.endCommand(value);
                            }
                        });
                    } else if(tabbedPane1.getSelectedIndex() == 1) {
                        AnalogOutputInt16 ao = new AnalogOutputInt16(((Integer) spinnerInt16.getValue()).shortValue(), CommandStatus.SUCCESS);
                        beginCommand("Direct operate... ");
                        processor.selectAndOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myDialog.endCommand(value);
                            }
                        });
                    }
                }
            }
        });
    }
}
