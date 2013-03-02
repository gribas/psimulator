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

import com.automatak.dnp3.*;
import com.automatak.dnp3.impl.DNP3ManagerFactory;
import com.automatak.dnp3.mock.PrintingDataObserver;
import com.automatak.dnp3.tools.controls.CommsTree;
import com.automatak.dnp3.tools.controls.LogTable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TestSetForm {

    public static void main(String[] args) {
        DNP3Manager mgr = DNP3ManagerFactory.createDNP3ManagerWithDefaultConcurrency();
        JFrame frame = new JFrame("opendnp3");
        TestSetForm form = new TestSetForm(mgr);
        frame.setJMenuBar(getMenuBar());
        frame.setContentPane(form.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static JMenuBar getMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog dialog = new AboutDialog();
                dialog.pack();
                dialog.configure();
                dialog.setVisible(true);
            }
        });
        menu.add(aboutItem);

       return menuBar;
    }

    public TestSetForm(DNP3Manager manager)
    {
        this.manager = manager;
        this.manager.addLogSubscriber(logTable);
        this.commsTree.setManager(manager);
    }

    private JPanel mainPanel;
    private LogTable logTable;
    private JSplitPane splitPane;
    private CommsTree commsTree;
    private final DNP3Manager manager;


}
