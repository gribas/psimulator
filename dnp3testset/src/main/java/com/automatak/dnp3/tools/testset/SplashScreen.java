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
package com.automatak.dnp3.tools.testset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SplashScreen extends JDialog {

    private JPanel contentPane;
    private JButton buttonAccept;
    private JProgressBar progressBarLoad;
    private final SplashScreenListener listener;

    public SplashScreen(SplashScreenListener listener) {
        this.listener = listener;

        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        setUndecorated(true);
        progressBarLoad.setMinimum(0);
        progressBarLoad.setMaximum(100);
        buttonAccept.setVisible(false);
        getRootPane().setDefaultButton(buttonAccept);
        buttonAccept.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    public void showSplash()
    {
        final SplashScreen myDialog = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                myDialog.setVisible(true);
            }
        });
    }

    public void setProcess(final int value)
    {
        final SplashScreen myDialog = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                myDialog.progressBarLoad.setValue(value);
            }
        });
    }

    public void setComplete()
    {
        final SplashScreen myDialog = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                myDialog.buttonAccept.setVisible(true);
            }
        });
    }


    public void configure()
    {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);
    }

    private void onOK() {
        listener.onSplashClose();
        dispose();
    }
}
