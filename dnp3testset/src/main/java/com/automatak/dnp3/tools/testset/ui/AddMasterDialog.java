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

import com.automatak.dnp3.LogLevel;
import com.automatak.dnp3.MasterStackConfig;
import com.automatak.dnp3.PointClass;
import com.automatak.dnp3.tools.pluginapi.MasterPluginFactory;
import com.automatak.dnp3.tools.pluginapi.StaticResources;

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
    private UInt16Spinner spinnerMaxReceiveFragSize;
    private JCheckBox allowTimeSyncCheckBox;
    private JCheckBox doUnsolEnableDisableCheckBox;
    private JCheckBox enableUnsolCheckBox;
    private JCheckBox class3CheckBox;
    private JCheckBox class2CheckBox;
    private JCheckBox class1CheckBox;
    private TimeoutSpinner spinnerIntegrityRate;
    private JCheckBox periodicIntegrityPollCheckBox;
    private TimeoutSpinner spinnerTaskRetryRate;
    private JTextArea textAreaLoggerId;
    private LogComboBox comboxBoxLogLevel;
    private IntCountSpinner spinnerLinkRetry;

    //used to set defaults
    private final MasterStackConfig config;
    private final AddMasterListener listener;

    private void setDefaults()
    {
        // link
        this.checkBoxUseConfirms.setSelected(config.linkConfig.useConfirms);
        this.spinnerLocalAddr.setValue(config.linkConfig.localAddr);
        this.spinnerRemoteAddr.setValue(config.linkConfig.remoteAddr);
        this.spinnerRspTimeout.setValue((int) config.linkConfig.timeoutMs);
        this.spinnerLinkRetry.setValue(config.linkConfig.numRetry);

        // app
        this.spinnerAppRspTimeout.setValue((int) config.appConfig.rspTimeoutMs);
        this.spinnerRetryCount.setValue(config.appConfig.numRetry);
        this.spinnerMaxReceiveFragSize.setValue(config.appConfig.maxFragSize);

        // master
        this.allowTimeSyncCheckBox.setSelected(config.masterConfig.allowTimeSync);
        this.doUnsolEnableDisableCheckBox.setSelected(config.masterConfig.doUnsolOnStartup);
        this.enableUnsolCheckBox.setSelected(config.masterConfig.enableUnsol);

        int mask = config.masterConfig.unsolClassMask;
        class1CheckBox.setSelected((mask & PointClass.CLASS_1.toInt()) != 0);
        class2CheckBox.setSelected((mask & PointClass.CLASS_2.toInt()) != 0);
        class3CheckBox.setSelected((mask & PointClass.CLASS_3.toInt()) != 0);

        long rate = config.masterConfig.integrityRateMs;

        periodicIntegrityPollCheckBox.setSelected(rate >= 0);
        spinnerIntegrityRate.setValue((int) rate);
        spinnerTaskRetryRate.setValue((int) config.masterConfig.taskRetryRateMs);
    }

    private MasterStackConfig getConfig()
    {
        MasterStackConfig cfg = new MasterStackConfig();

        // link
        cfg.linkConfig.useConfirms = this.checkBoxUseConfirms.isSelected();
        cfg.linkConfig.localAddr = this.spinnerLocalAddr.getUInt16();
        cfg.linkConfig.remoteAddr = this.spinnerRemoteAddr.getUInt16();
        cfg.linkConfig.timeoutMs = this.spinnerRspTimeout.getCount();
        cfg.linkConfig.numRetry = this.spinnerLinkRetry.getCount();

        // app
        cfg.appConfig.rspTimeoutMs = this.spinnerAppRspTimeout.getTimeout();
        cfg.appConfig.numRetry = this.spinnerRetryCount.getUInt16();
        cfg.appConfig.maxFragSize = spinnerMaxReceiveFragSize.getUInt16();

        //master
        cfg.masterConfig.allowTimeSync = allowTimeSyncCheckBox.isSelected();
        cfg.masterConfig.doUnsolOnStartup = doUnsolEnableDisableCheckBox.isSelected();
        cfg.masterConfig.enableUnsol = enableUnsolCheckBox.isSelected();

        int mask = 0;
        if(class1CheckBox.isSelected()) mask |= PointClass.CLASS_1.toInt();
        if(class2CheckBox.isSelected()) mask |= PointClass.CLASS_2.toInt();
        if(class3CheckBox.isSelected()) mask |= PointClass.CLASS_3.toInt();

        cfg.masterConfig.unsolClassMask = mask;
        cfg.masterConfig.integrityRateMs = periodicIntegrityPollCheckBox.isSelected() ? spinnerIntegrityRate.getTimeout() : -1;
        cfg.masterConfig.taskRetryRateMs = spinnerTaskRetryRate.getTimeout();

        return cfg;
    }

    public AddMasterDialog(MasterPluginFactory factory, AddMasterListener listener) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.setResizable(false);
        this.setIconImage(StaticResources.dnpIcon);
        this.setTitle("Add Master using plugin: " + factory.getPluginName());
        this.setIconImage(StaticResources.dnpIcon);
        this.textAreaLoggerId.setText(factory.getPluginName());

        this.listener = listener;
        this.config = factory.getDefaultConfig();

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
        LogLevel level = comboxBoxLogLevel.getLogLevel();
        String id = textAreaLoggerId.getText();
        this.listener.onAdd(id, level, cfg);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
