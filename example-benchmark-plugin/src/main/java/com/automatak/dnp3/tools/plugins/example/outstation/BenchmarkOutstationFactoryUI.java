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
package com.automatak.dnp3.tools.plugins.example.outstation;

import com.automatak.dnp3.tools.pluginapi.StaticResources;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 3/8/13
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class BenchmarkOutstationFactoryUI extends JFrame {
    private JSlider sliderRateUpdatesPerSecond;
    private JPanel panelMain;
    private JTextField textFieldNumOutstations;
    private JTextField textFieldNUmUpdates;
    final private BenchmarkOutstationPluginFactory factory;
    private int numUpdates = 0;
    private int numOutstations = 0;

    public BenchmarkOutstationFactoryUI(final BenchmarkOutstationPluginFactory factory)
    {
        this.factory = factory;
        this.setTitle("Benchmark OutstationFactory UI");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        updateNumUpdates();
        this.setContentPane(panelMain);
        this.setIconImage(StaticResources.dnpIcon);
        sliderRateUpdatesPerSecond.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = sliderRateUpdatesPerSecond.getValue();
                factory.setUpdateRate(value);
            }
        });
    }

    private void updateNumUpdates()
    {
        this.textFieldNUmUpdates.setText(Integer.toString(numUpdates));
    }

    private void updateNumOutstations()
    {
        this.textFieldNumOutstations.setText(Integer.toString(numOutstations));
    }

    public void addUpdates(final int i)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                numUpdates += i;
                updateNumUpdates();
            }
        });
    }

    public void setNumOutstations(final int i)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                numOutstations = i;
                updateNumOutstations();
            }
        });
    }
}
