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
package com.automatak.dnp3.tools.plugins.example.master;

import com.automatak.dnp3.tools.pluginapi.StaticResources;

import javax.swing.*;
import java.awt.event.*;

public class BenchmarkMasterFactoryUI extends JFrame {
    private JPanel contentPane;
    private JTextField textFieldNumOutstations;
    private JTextField textFieldNumMeasurements;
    private JTextField textFieldMeasPerSec;
    private JTextField textFieldNumTx;

    private int numOutstations = 0;
    private int numMeasurements = 0;
    private int numTx = 0;

    private long lastSample = 0;
    private int lastCount = 0;

    public void addOutstations()
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ++numOutstations;
                textFieldNumOutstations.setText(Integer.toString(numOutstations));
                updateTextFields();
            }
        });
    }

    public void removeOutstations()
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                --numOutstations;
                textFieldNumOutstations.setText(Integer.toString(numOutstations));
                updateTextFields();
            }
        });
    }

    public void doTx(final int updates)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                long ms = System.currentTimeMillis();
                numTx += 1;
                numMeasurements += updates;
                long deltaT = ms - lastSample;
                if(deltaT > 1000)
                {
                    lastSample = ms;
                    int deltaM = numMeasurements - lastCount;
                    lastCount = numMeasurements;
                    long rate = (deltaM / deltaT) * 1000;
                    textFieldMeasPerSec.setText(Long.toString(rate));
                }

                updateTextFields();
            }
        });
    }

    private void updateTextFields()
    {
        textFieldNumMeasurements.setText(Integer.toString(numMeasurements));
        textFieldNumOutstations.setText(Integer.toString(numOutstations));
        textFieldNumTx.setText(Integer.toString(numTx));
    }

    public BenchmarkMasterFactoryUI() {
        setContentPane(contentPane);
        setResizable(false);
        setIconImage(StaticResources.dnpIcon);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        updateTextFields();
    }

}
