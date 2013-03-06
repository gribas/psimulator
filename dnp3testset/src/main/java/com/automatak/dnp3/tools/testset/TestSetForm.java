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
import com.automatak.dnp3.tools.plugins.example.master.ExampleMasterPluginFactory;
import com.automatak.dnp3.tools.plugins.example.outstation.ExampleOutstationPluginFactory;
import com.automatak.dnp3.tools.testset.ui.CommsTree;
import com.automatak.dnp3.tools.testset.ui.LogTable;
import com.automatak.dnp3.tools.pluginapi.StaticResources;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestSetForm {

    private static void startApplication(PluginConfiguration config)
    {
        final DNP3Manager mgr = DNP3ManagerFactory.createDNP3ManagerWithDefaultConcurrency();
        JFrame frame = new JFrame("Automatak Protocol Simulator");
        frame.setIconImage(StaticResources.dnpIcon);
        TestSetForm form = new TestSetForm(mgr, config);
        frame.setContentPane(form.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        // explicitly shutdown the dnp3 interface, detaching native threads from the JVM
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mgr.shutdown();
            }
        });
    }

    private static void startSplash()  throws InterruptedException
    {
        final SplashScreen splash = new SplashScreen();
        splash.pack();
        splash.configure();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                splash.setVisible(true);
            }
        });
        PluginLoaderListener listener = new PluginLoaderListener() {
            @Override
            public void onProgressUpdate(int step, int max) {
                splash.setProgress(step, max);
            }

            @Override
            public void onException(Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        };

        final PluginConfiguration plugins = attemptLoad(listener);
        splash.addSplashCloseListener(new SplashScreenListener() {
            @Override
            public void onSplashClose() {
                try {
                    startApplication(plugins);
                }
                catch(UnsatisfiedLinkError ex)
                {
                    JOptionPane.showMessageDialog(splash, ex.getMessage(), "Is opendnpjava.dll on your library path?", JOptionPane.ERROR_MESSAGE);
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(splash, ex.getMessage(), "Unknown exception occurred while loading application", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        splash.setComplete();
    }

    static PluginConfiguration attemptLoad(PluginLoaderListener listener)
    {
        PluginLoader loader = new PluginLoader();
        PluginConfiguration config = loader.loadPlugins(listener);
        config.getOutstations().add(new ExampleOutstationPluginFactory());
        config.getMasters().add(new ExampleMasterPluginFactory());
        return config;
    }

    public static void main(String[] args) throws InterruptedException
    {
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }
        startSplash();
    }

    public TestSetForm(DNP3Manager manager, PluginConfiguration config)
    {
        manager.addLogSubscriber(logTable);
        this.commsTree.configure(manager, config);
    }

    private JPanel mainPanel;
    private LogTable logTable;
    private JSplitPane splitPane;
    private CommsTree commsTree;


}
