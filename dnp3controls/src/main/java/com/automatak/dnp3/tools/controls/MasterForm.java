package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MasterForm extends JFrame implements DataObserver {
    private JPanel panelMain;
    private JTabbedPane tabbedPane1;
    private MeasTable binaryInputTable;

    public void configureWithMaster(Master master) {
        this.setJMenuBar(createMenuBar(master));
    }

    private JMenuBar createMenuBar(final Master master)
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Control");
        menuBar.add(menu);
        JMenuItem crobItem = new JMenuItem("Conrol Relay Output Block");
        crobItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               CrobDialog dialog = new CrobDialog(master.getCommandProcessor());
               dialog.pack();
               dialog.setVisible(true);
            }
        });
        menu.add(crobItem);
        return menuBar;
    }

    public MasterForm(String name)
    {
        super(name);
        this.setContentPane(panelMain);
        this.pack();
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    @Override
    public void start()
    {}

    @Override
    public void update(final BinaryInput meas, final long index)
    {
       final MasterForm form = this;
       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               binaryInputTable.updateBinary(meas, index);
           }
       });
    }

    @Override
    public void update(AnalogInput meas, long index)
    {}

    @Override
    public void update(Counter meas, long index)
    {}

    @Override
    public void update(BinaryOutputStatus bi, long index)
    {}

    @Override
    public void update(AnalogOutputStatus bi, long index)
    {}

    @Override
    public void end()
    {
        final MasterForm form = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               form.binaryInputTable.updateUI();
            }
        });
    }
}
