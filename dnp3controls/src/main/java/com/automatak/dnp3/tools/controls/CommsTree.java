package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.*;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CommsTree extends JTree {

    private class RootNode {
       @Override
       public String toString()
       {
           return "root";
       }
    }

    private abstract class ChannelNode implements ChannelStateListener {

        private final Channel channel;
        private final DefaultTreeModel model;

        public ChannelState getState() {
            return state;
        }

        private ChannelState state = ChannelState.OPEN;

        public ChannelNode(DefaultTreeModel model, Channel channel)
        {
            this.channel = channel;
            this.model = model;
        }

        @Override
        public void onStateChange(final ChannelState state)
        {
            final ChannelNode node = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    node.state = state;
                    node.model.reload();
                }
            });
        }


    }

    private class TcpClientChannelNode extends ChannelNode {

        private final String id;

        public TcpClientChannelNode(DefaultTreeModel model, String id, Channel channel)
        {
            super(model, channel);
            this.id = id;
        }

        @Override
        public String toString()
        {
            return id;
        }
    }

    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode(new RootNode());
    private final DefaultTreeModel model = new DefaultTreeModel(root);


    private class MyCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();

            if(RootNode.class.isInstance(userObject)) {

            }
            else if(ChannelNode.class.isInstance(userObject)) {
                ChannelNode cnode = (ChannelNode) userObject;
                switch(cnode.getState())
                {
                    case CLOSED:
                        setForeground(Color.black);
                        break;
                    default:
                        setForeground(Color.RED);
                }
            }
            return this;
        }
    }

    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            throw new RuntimeException("Couldn't find file: " + path);
        }
    }

    public void setManager(DNP3Manager manager) {
        this.manager = manager;
    }

    private DNP3Manager manager = null;

    public CommsTree()
    {
        this.setModel(model);
        //rootIcon = createImageIcon("/images/glyphicons_371_global.png", "The root icon");
        this.setCellRenderer(new MyCellRenderer());

        final JTree tree = this;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if(SwingUtilities.isRightMouseButton(e))
                {
                    int row = tree.getRowForLocation(e.getX(), e.getY());
                    TreePath path = tree.getPathForRow(row);
                    if(path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        if(RootNode.class.isInstance(node.getUserObject()))
                        {
                            JPopupMenu popup = new JPopupMenu();
                            JMenuItem addClientItem = new JMenuItem("Add Tcp Client");
                            addClientItem.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mousePressed(MouseEvent e) {
                                    if(SwingUtilities.isLeftMouseButton(e))
                                    {
                                        AddTcpDialog dialog = new AddTcpDialog("Add Tcp Client", "IP", new AddTcpListener() {
                                            @Override
                                            public void onAdd(String loggerId, LogLevel level, int retryMs, String host, int port)
                                            {
                                               Channel c = manager.addTCPClient(loggerId, level, retryMs, host, port);
                                               TcpClientChannelNode node = new TcpClientChannelNode(model, loggerId, c);
                                               c.addStateListener(node);
                                               MutableTreeNode channelNode = new DefaultMutableTreeNode(node);
                                               DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                                               root.add(channelNode);
                                               model.reload();
                                            }
                                        });
                                        dialog.pack();
                                        dialog.setVisible(true);
                                    }
                                }
                            });
                            popup.add(addClientItem);
                            popup.show(tree, x, y);
                        }
                    }
                }
                else if(SwingUtilities.isLeftMouseButton(e))
                {


                }
            }
        });

    }


}
