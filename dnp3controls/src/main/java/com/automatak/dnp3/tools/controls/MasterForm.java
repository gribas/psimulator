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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MasterForm extends JFrame implements DataObserver {

    private JPanel panelMain;
    private JTabbedPane tabbedPane1;
    private MeasTable binaryInputTable;
    private JPanel paneControls;
    private JButton buttonSBO;
    private JButton buttonDO;
    private JTextField textFieldStatus;
    private IndexSpinner spinnerIndex;
    private CrobComboBox comboBoxCode;
    private ControlCountSpinner spinnerCount;
    private ControlTimeSpinner onTimeSpinner;
    private ControlTimeSpinner offTimeSpinner;
    private JSpinner spinnerInt16;
    private JTabbedPane tabbedPanelControls;

    private Master master = null;

    private void beginCommand(String text)
    {
        this.buttonDO.setEnabled(false);
        this.buttonSBO.setEnabled(false);
        this.textFieldStatus.setText(text);
    }

    private void endCommand(final CommandStatus status)
    {
        final MasterForm myForm = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                myForm.textFieldStatus.setText(myForm.textFieldStatus.getText() + status);
                myForm.buttonDO.setEnabled(true);
                myForm.buttonSBO.setEnabled(true);
            }
        });
    }


    private ControlRelayOutputBlock createCROB()
    {
        ControlCode cc =  comboBoxCode.getControlCode();
        short count = (short) spinnerCount.getControlCount();
        int onTime = onTimeSpinner.getControlTime();
        int offTime = offTimeSpinner.getControlTime();
        return new ControlRelayOutputBlock(cc, count, onTime, offTime, CommandStatus.SUCCESS);
    }

    public void configureWithMaster(Master master) {
        this.master = master;
    }

    public MasterForm(String name)
    {
        super(name);
        this.setContentPane(panelMain);
        this.setIconImage(StaticResources.dnpIcon);
        this.pack();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        spinnerInt16.setModel(new SpinnerNumberModel(0, 0, 65535, 1));

        final MasterForm myForm = this;

        buttonDO.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(myForm.buttonDO.isEnabled()) {
                    int index = spinnerIndex.getIndex();
                    if(myForm.tabbedPanelControls.getSelectedIndex() == 0) {
                        ControlRelayOutputBlock crob = createCROB();
                        beginCommand("Direct operate... ");
                        master.getCommandProcessor().directOperate(crob, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    } else if(myForm.tabbedPanelControls.getSelectedIndex() == 1) {
                        AnalogOutputInt16 ao = new AnalogOutputInt16(((Integer) spinnerInt16.getValue()).shortValue(), CommandStatus.SUCCESS);
                        beginCommand("Direct operate... ");
                        master.getCommandProcessor().directOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    }
                }
            }
        });

        buttonSBO.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(myForm.buttonDO.isEnabled()) {
                    int index = spinnerIndex.getIndex();

                    if(myForm.tabbedPanelControls.getSelectedIndex() == 0) {
                        ControlRelayOutputBlock crob = createCROB();
                        beginCommand("Select and operate... ");
                        master.getCommandProcessor().selectAndOperate(crob, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    } else if(myForm.tabbedPanelControls.getSelectedIndex() == 1) {
                        AnalogOutputInt16 ao = new AnalogOutputInt16(((Integer) spinnerInt16.getValue()).shortValue(), CommandStatus.SUCCESS);
                        beginCommand("Direct operate... ");
                        master.getCommandProcessor().selectAndOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void start()
    {}

    @Override
    public void update(final BinaryInput meas, final long index)
    {
       final MasterForm form = this;
       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               binaryInputTable.updateBinary(meas, index);
           }
       });
    }

    @Override
    public void update(AnalogInput meas, long index)
    {}

    @Override
    public void update(Counter meas, long index)
    {}

    @Override
    public void update(BinaryOutputStatus bi, long index)
    {}

    @Override
    public void update(AnalogOutputStatus bi, long index)
    {}

    @Override
    public void end()
    {
        final MasterForm form = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               form.binaryInputTable.updateUI();
            }
        });
    }
}
