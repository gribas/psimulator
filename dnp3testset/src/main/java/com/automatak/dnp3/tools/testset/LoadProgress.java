package com.automatak.dnp3.tools.testset;

import javax.swing.*;
import java.awt.event.*;

public class LoadProgress extends JDialog {
    private JPanel contentPane;
    private JProgressBar progressBarChannels;
    private JTextField textFieldStatus;


    public LoadProgress(int max) {
        setContentPane(contentPane);
        setModal(true);

        this.progressBarChannels.setMaximum(0);
        this.progressBarChannels.setMaximum(max);

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

    public void setStatus(String text, int step)
    {
        this.textFieldStatus.setText(text);
        this.progressBarChannels.setValue(step);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

}
