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

import com.automatak.dnp3.tools.pluginapi.StaticResources;
import com.automatak.dnp3.tools.xml.SimulatorOptions;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class OptionsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTabbedPane tabbedPane1;
    private JTextField textFieldPath;
    private JCheckBox logToFileCheckBox;
    private JCheckBox checkBoxLogToTable;
    private JSpinner spinnerMaxTableSize;
    private JButton buttonChangeFile;
    private boolean approved = false;
    private final SimulatorOptions options;

    public OptionsDialog(SimulatorOptions options) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setTitle("Edit Simulator Options");
        this.setIconImage(StaticResources.dnpIcon);
        this.options = options;

        spinnerMaxTableSize.setModel(new SpinnerNumberModel(options.getMaxLogTableSize(), 100, 10000, 100));
        logToFileCheckBox.setSelected(options.isLogToFile());
        checkBoxLogToTable.setSelected(options.isLogToTable());
        textFieldPath.setText(options.getLogFile().toString());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        final OptionsDialog dialog = this;

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buttonChangeFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(".");
                int ret = chooser.showSaveDialog(dialog);
                if(ret == JFileChooser.APPROVE_OPTION)
                {
                    dialog.textFieldPath.setText(chooser.getSelectedFile().toString());
                }
            }
        });
    }

    public boolean isApproved()
    {
        return approved;
    }

    public SimulatorOptions getOptions()
    {
        return options;
    }

    private void onOK() {
        approved = true;
        options.setLogFile(new File(this.textFieldPath.getText()));
        options.setLogToFile(this.logToFileCheckBox.isSelected());
        options.setLogToTable(this.checkBoxLogToTable.isSelected());
        options.setMaxLogTableSize((Integer) this.spinnerMaxTableSize.getValue());
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
