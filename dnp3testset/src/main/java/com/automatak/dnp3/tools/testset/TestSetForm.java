package com.automatak.dnp3.tools.testset;

import javax.swing.*;

import com.automatak.dnp3.*;
import com.automatak.dnp3.impl.DNP3ManagerFactory;
import com.automatak.dnp3.mock.PrintingDataObserver;
import com.automatak.dnp3.mock.PrintingLogSubscriber;
import com.automatak.dnp3.tools.controls.LogTable;

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 2/28/13
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSetForm {

    public static void main(String[] args) {
        DNP3Manager mgr = DNP3ManagerFactory.createDNP3ManagerWithDefaultConcurrency();
        JFrame frame = new JFrame("OpenDNP3 testset");
        TestSetForm form = new TestSetForm(mgr);
        frame.setContentPane(form.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Channel client = mgr.addTCPClient("client", LogLevel.DEBUG, 5000, "127.0.0.1", 20001);
        Master master = client.addMaster("master", LogLevel.INTERPRET, PrintingDataObserver.getInstance(), new MasterStackConfig());
    }

    public TestSetForm(DNP3Manager manager)
    {
        this.manager = manager;
        //this.manager.addLogSubscriber(logTable);
        this.manager.addLogSubscriber(PrintingLogSubscriber.getInstance());
    }

    private JPanel panel1;
    private JTree commsTree;
    private LogTable logTable;
    private JSplitPane splitPane;
    private final DNP3Manager manager;


}
