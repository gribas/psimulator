package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.LogLevel;

import javax.swing.*;
import java.awt.event.*;

public class AddTcpDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldLoggerId;
    private JTextField textFieldHost;
    private JLabel labelEndpoint;
    private LogComboBox comboBoxLogLevel;
    private TimeoutSpinner spinnerRetry;
    private TcpPortSpinner spinnerPort;

    private final AddTcpListener listener;

    public AddTcpDialog(String title, String hostFieldName, AddTcpListener listener) {
        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(buttonOK);

        this.labelEndpoint.setText(hostFieldName);
        this.setTitle(title);
        this.listener = listener;

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
        String id = textFieldLoggerId.getText();
        LogLevel level = this.comboBoxLogLevel.getLogLevel();
        int retry = this.spinnerRetry.getRetry();
        String host =  this.textFieldHost.getText();
        int port = this.spinnerPort.getPort();
        this.listener.onAdd(id, level, retry, host, port);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
