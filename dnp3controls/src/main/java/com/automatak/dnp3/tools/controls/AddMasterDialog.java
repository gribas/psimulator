package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.MasterStackConfig;

import javax.swing.*;
import java.awt.event.*;

public class AddMasterDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTabbedPane tabbedPane1;

    private final AddMasterListener listener;

    public AddMasterDialog(AddMasterListener listener) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
        MasterStackConfig cfg = new MasterStackConfig();
        cfg.masterConfig.doUnsolOnStartup = true;
        cfg.masterConfig.enableUnsol = true;
        this.listener.onAdd(cfg);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
