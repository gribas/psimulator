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

import com.automatak.dnp3.MasterStackConfig;

import javax.swing.*;
import java.awt.event.*;

public class AddMasterDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTabbedPane tabbedPane1;
    private JCheckBox checkBoxUseConfirms;
    private UInt16Spinner spinnerLocalAddr;
    private UInt16Spinner spinnerRemoteAddr;
    private IntCountSpinner spinnerRspTimeout;
    private TimeoutSpinner spinnerAppRspTimeout;
    private UInt16Spinner spinnerRetryCount;
    private UInt16Spinner spinnerFragSize;

    //used to set defaults
    private final MasterStackConfig config = new MasterStackConfig();

    private final AddMasterListener listener;

    private void setDefaults()
    {
        // link
        this.checkBoxUseConfirms.setSelected(config.linkConfig.useConfirms);
        this.spinnerLocalAddr.setValue(config.linkConfig.localAddr);
        this.spinnerRemoteAddr.setValue(config.linkConfig.remoteAddr);
        this.spinnerRspTimeout.setValue((int) config.linkConfig.timeoutMs);

        // app
        this.spinnerAppRspTimeout.setValue((int) config.appConfig.rspTimeoutMs);
        this.spinnerRetryCount.setValue(config.appConfig.numRetry);
        this.spinnerFragSize.setValue(config.appConfig.maxFragSize);
    }

    private MasterStackConfig getConfig()
    {
        MasterStackConfig cfg = new MasterStackConfig();

        // link
        cfg.linkConfig.useConfirms = this.checkBoxUseConfirms.isSelected();
        cfg.linkConfig.localAddr = this.spinnerLocalAddr.getUInt16();
        cfg.linkConfig.remoteAddr = this.spinnerRemoteAddr.getUInt16();
        cfg.linkConfig.timeoutMs = this.spinnerRspTimeout.getCount();

        // app
        cfg.appConfig.rspTimeoutMs = this.spinnerAppRspTimeout.getTimeout();
        cfg.appConfig.numRetry = this.spinnerRetryCount.getUInt16();
        cfg.appConfig.maxFragSize = spinnerFragSize.getUInt16();


        return cfg;
    }

    public AddMasterDialog(AddMasterListener listener) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.listener = listener;

        this.setDefaults();

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

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        MasterStackConfig cfg = getConfig();
        cfg.masterConfig.doUnsolOnStartup = true;
        cfg.masterConfig.enableUnsol = true;
        this.listener.onAdd(cfg);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
