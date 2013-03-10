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
package com.automatak.dnp3.tools.plugins.example.mastergui;

import com.automatak.dnp3.*;
import com.automatak.dnp3.tools.pluginapi.*;
import com.automatak.dnp3.tools.pluginapi.ui.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ExampleMasterUI extends JFrame implements DataObserver {

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
    private MeasTable analogInputTable;
    private MeasTable counterTable;
    private MeasTable analogOutputStatusTable;
    private MeasTable binaryOutputStatusTable;
    private JSpinner spinnerInt32;
    private JSpinner spinnerFloat32;
    private JSpinner spinnerDouble64;

    private CommandProcessor processor = null;

    private void beginCommand(String text)
    {
        this.buttonDO.setEnabled(false);
        this.buttonSBO.setEnabled(false);
        this.textFieldStatus.setText(text);
    }

    private void endCommand(final CommandStatus status)
    {
        final ExampleMasterUI myForm = this;
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

    public void configure(CommandProcessor processor) {
       this.processor = processor;
    }

    public ExampleMasterUI(String name)
    {
        super(name);
        this.setContentPane(panelMain);
        this.setIconImage(StaticResources.dnpIcon);
        this.pack();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        spinnerInt16.setModel(new SpinnerNumberModel(0, Short.MIN_VALUE, Short.MAX_VALUE, 1));
        spinnerInt32.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        //spinnerFloat32.setModel(new SpinnerNumberModel(0.0, -Float.MAX_VALUE, Float.MAX_VALUE, 0.1));
        //spinnerDouble64.setModel(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1));

        final ExampleMasterUI myForm = this;

        buttonDO.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(myForm.buttonDO.isEnabled()) {
                    int index = spinnerIndex.getIndex();
                    if(myForm.tabbedPanelControls.getSelectedIndex() == 0) {
                        ControlRelayOutputBlock crob = createCROB();
                        beginCommand("Direct operate Group12Var1... ");
                        myForm.processor.directOperate(crob, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    } else if(myForm.tabbedPanelControls.getSelectedIndex() == 1) {
                        AnalogOutputInt16 ao = new AnalogOutputInt16(((Integer) spinnerInt16.getValue()).shortValue(), CommandStatus.SUCCESS);
                        beginCommand("Direct operate Group41Var2... ");
                        myForm.processor.directOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    } if(myForm.tabbedPanelControls.getSelectedIndex() == 2) {
                        AnalogOutputInt32 ao = new AnalogOutputInt32(((Integer) spinnerInt32.getValue()).intValue(), CommandStatus.SUCCESS);
                        beginCommand("Direct operate Group41Var1... ");
                        myForm.processor.directOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    } else if(myForm.tabbedPanelControls.getSelectedIndex() == 3) {
                        AnalogOutputFloat32 ao = new AnalogOutputFloat32(((Double) spinnerFloat32.getValue()).floatValue(), CommandStatus.SUCCESS);
                        beginCommand("Direct operate Group41Var3... ");
                        myForm.processor.directOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    } else if(myForm.tabbedPanelControls.getSelectedIndex() == 4) {
                        AnalogOutputDouble64 ao = new AnalogOutputDouble64(((Double) spinnerDouble64.getValue()), CommandStatus.SUCCESS);
                        beginCommand("Direct operate Group41Var4... ");
                        myForm.processor.directOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
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
                        beginCommand("Select and operate Group12Var1... ");
                        processor.selectAndOperate(crob, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    } else if(myForm.tabbedPanelControls.getSelectedIndex() == 1) {
                        AnalogOutputInt16 ao = new AnalogOutputInt16(((Integer) spinnerInt16.getValue()).shortValue(), CommandStatus.SUCCESS);
                        beginCommand("Select and operate Group41Var2... ");
                        processor.selectAndOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    } if(myForm.tabbedPanelControls.getSelectedIndex() == 2) {
                        AnalogOutputInt32 ao = new AnalogOutputInt32(((Integer) spinnerInt32.getValue()).intValue(), CommandStatus.SUCCESS);
                        beginCommand("Select and operate Group41Var1... ");
                        myForm.processor.selectAndOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    } else if(myForm.tabbedPanelControls.getSelectedIndex() == 3) {
                        AnalogOutputFloat32 ao = new AnalogOutputFloat32(((Double) spinnerFloat32.getValue()).floatValue(), CommandStatus.SUCCESS);
                        beginCommand("Select and operate Group41Var3... ");
                        myForm.processor.selectAndOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
                            @Override
                            public void onComplete(CommandStatus value) {
                                myForm.endCommand(value);
                            }
                        });
                    } else if(myForm.tabbedPanelControls.getSelectedIndex() == 4) {
                        AnalogOutputDouble64 ao = new AnalogOutputDouble64(((Double) spinnerDouble64.getValue()), CommandStatus.SUCCESS);
                        beginCommand("Select and operate Group41Var4... ");
                        myForm.processor.selectAndOperate(ao, index).addListener(new ListenableFuture.CompletionListener<CommandStatus>() {
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
       final ExampleMasterUI form = this;
       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               String value = Boolean.toString(meas.getValue());
               binaryInputTable.update(value, meas, BinaryQualityConverter.getInstance(), index);
           }
       });
    }

    @Override
    public void update(final AnalogInput meas, final long index)
    {
        final ExampleMasterUI form = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String value = Double.toString(meas.getValue());
                analogInputTable.update(value, meas, AnalogQualityConverter.getInstance(), index);
            }
        });
    }

    @Override
    public void update(final Counter meas, final long index)
    {
        final ExampleMasterUI form = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String value = Long.toString(meas.getValue());
                counterTable.update(value, meas, CounterQualityConverter.getInstance(), index);
            }
        });
    }

    @Override
    public void update(final BinaryOutputStatus meas, final long index)
    {
        final ExampleMasterUI form = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String value = Boolean.toString(meas.getValue());
                binaryOutputStatusTable.update(value, meas, BinaryOutputStatusQualityConverter.getInstance(), index);
            }
        });
    }

    @Override
    public void update(final AnalogOutputStatus meas, final long index)
    {
        final ExampleMasterUI form = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String value = Double.toString(meas.getValue());
                analogOutputStatusTable.update(value, meas, AnalogOutputStatusQualityConverter.getInstance(), index);
            }
        });
    }

    @Override
    public void end()
    {
        final ExampleMasterUI form = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               form.binaryInputTable.updateUI();
            }
        });
    }
}
