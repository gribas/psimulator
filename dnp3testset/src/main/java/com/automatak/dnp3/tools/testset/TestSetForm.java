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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.automatak.dnp3.*;
import com.automatak.dnp3.impl.DNP3ManagerFactory;
import com.automatak.dnp3.tools.pluginapi.MasterPluginFactory;
import com.automatak.dnp3.tools.pluginapi.OutstationPluginFactory;
import com.automatak.dnp3.tools.testset.ui.CommsTree;
import com.automatak.dnp3.tools.testset.ui.LogTable;
import com.automatak.dnp3.tools.pluginapi.StaticResources;
import com.automatak.dnp3.tools.testset.ui.OptionsDialog;
import com.automatak.dnp3.tools.xml.SimulatorOptions;
import com.automatak.dnp3.tools.xml.XSimulatorConfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class TestSetForm {

    private static void startApplication(final PluginConfiguration config)
    {
        final DNP3Manager mgr = DNP3ManagerFactory.createDNP3ManagerWithDefaultConcurrency();
        JFrame frame = new JFrame("Automatak Protocol Simulator");
        frame.setIconImage(StaticResources.dnpIcon);
        final TestSetForm form = new TestSetForm(mgr, config);
        frame.setJMenuBar(form.getMenuBar(config));
        frame.setContentPane(form.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        // explicitly shutdown the dnp3 interface, detaching native threads from the JVM
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for(OutstationPluginFactory fac: config.getOutstations()) fac.shutdown();
                for(MasterPluginFactory fac: config.getMasters()) fac.shutdown();
                mgr.shutdown();
                form.cleanup();
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
                    JOptionPane.showMessageDialog(splash, ex.getMessage(), "Did you install the Visual Stuido 2012 C++ Redistributable?", JOptionPane.ERROR_MESSAGE);
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

    private JMenuBar getMenuBar(PluginConfiguration config)
    {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveFile = new JMenuItem("Save");
        final TestSetForm form = this;
        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int ret = chooser.showSaveDialog(form.commsTree);
                if(ret == JFileChooser.APPROVE_OPTION)
                {
                    File file = chooser.getSelectedFile();
                    try {
                        JAXBContext context = JAXBContext.newInstance(XSimulatorConfig.class);
                        Marshaller m = context.createMarshaller();
                        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                        XSimulatorConfig cfg = form.commsTree.getConfig();
                        cfg.setXSimulatorOptions(options.getOptions());
                        m.marshal(cfg, file);
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(commsTree, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        fileMenu.add(saveFile);
        JMenuItem loadFile = new JMenuItem("Load");
        loadFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(commsTree.isActive())
                {
                    int ret = JOptionPane.showConfirmDialog(commsTree, "Your current config will be overwritten, continue?", "Warning", JOptionPane.WARNING_MESSAGE);
                    showLoadDialog();
                }
                else showLoadDialog();
            }
        });
        fileMenu.add(loadFile);
        bar.add(fileMenu);
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OptionsDialog dialog = new OptionsDialog(options);
                dialog.pack();
                dialog.setVisible(true);
                if(dialog.isApproved())
                {
                    form.setOptions(dialog.getOptions());
                }
            }
        });
        optionsMenu.add(editItem);
        bar.add(optionsMenu);
        JMenu pluginsMenu = new JMenu("Plugin UI");
        for(final OutstationPluginFactory fac: config.getOutstations())
        {
            if(fac.hasUi())
            {
                JMenuItem item = new JMenuItem(fac.getPluginName());
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fac.showUi();
                    }
                });
                pluginsMenu.add(item);
            }
        }
        for(final MasterPluginFactory fac: config.getMasters())
        {
            if(fac.hasUi())
            {
                JMenuItem item = new JMenuItem(fac.getPluginName());
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fac.showUi();
                    }
                });
                pluginsMenu.add(item);
            }
        }
        bar.add(pluginsMenu);
        return bar;
    }

    private void setOptions(SimulatorOptions options)
    {
        logToFile.setEnabled(options.isLogToFile());
        logToFile.changeFiles(options.getLogFile());
        logTable.setMaxTableSize(options.getMaxLogTableSize());
        logTable.setEnabled(options.isLogToTable());
    }

    public void showLoadDialog()
    {
        JFileChooser chooser = new JFileChooser("./config");
        int ret = chooser.showOpenDialog(this.commsTree);
        if(ret == JFileChooser.APPROVE_OPTION)
        {
            File file = chooser.getSelectedFile();
            try {
                JAXBContext context = JAXBContext.newInstance(XSimulatorConfig.class);
                Unmarshaller m = context.createUnmarshaller();
                XSimulatorConfig cfg = (XSimulatorConfig) m.unmarshal(file);
                this.options.configure(cfg.getXSimulatorOptions());
                this.setOptions(options);
                progressBarStatus.setMinimum(0);
                progressBarStatus.setMaximum(cfg.getXChannel().size());
                final TestSetForm form = this;
                commsTree.loadConfig(cfg, new XmlLoadListener() {
                    @Override
                    public void update(String status, int step) {
                       textFieldStatus.setText(status);
                       progressBarStatus.setValue(step + 1);
                    }
                    @Override
                    public void complete()
                    {
                       SwingUtilities.invokeLater(new Runnable() {
                           @Override
                           public void run() {
                               textFieldStatus.setText("complete");
                           }
                       });
                    }
                });
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(commsTree, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public TestSetForm(DNP3Manager manager, PluginConfiguration config)
    {
        options.setLogToFile(true);
        manager.addLogSubscriber(logTable);
        manager.addLogSubscriber(logToFile);
        this.commsTree.configure(manager, config);
        setOptions(options);
    }

    public void cleanup()
    {
        logToFile.stop();
    }

    private JPanel mainPanel;
    private LogTable logTable;
    private JSplitPane splitPane;
    private CommsTree commsTree;
    private JProgressBar progressBarStatus;
    private JTextField textFieldStatus;
    private SimulatorOptions options = new SimulatorOptions();
    private final LogToFile logToFile = new LogToFile();


}
