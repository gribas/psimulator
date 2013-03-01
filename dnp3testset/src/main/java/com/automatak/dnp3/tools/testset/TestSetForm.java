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

        //Channel client = mgr.addTCPClient("client", LogLevel.INTERPRET, 5000, "127.0.0.1", 20000);
        //Master master = client.addMaster("master", LogLevel.INTERPRET, PrintingDataObserver.getInstance(), new MasterStackConfig());
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
