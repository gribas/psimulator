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
package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.*;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.LinkedList;

public class CommsTree extends JTree {

    private final static Color darkGreen = new Color(0, 153, 51);

    private abstract class UserNode {

        private final java.util.List<NodeUpdateListener> listeners = new LinkedList<NodeUpdateListener>();

        // hierarchically do cleanup
        public void cleanup()
        {}

        protected void notifyNodeUpdateListeners()
        {
            for(NodeUpdateListener l : listeners) l.onNodeUpdate();
        }

        public void addUpdateListener(NodeUpdateListener listener)
        {
            listeners.add(listener);
        }
    }

    private class Dnp3RootNode extends UserNode {
       @Override
       public String toString()
       {
           return "dnp3";
       }
    }

    private abstract class ChannelNode extends UserNode implements ChannelStateListener {

        public Channel getChannel() {
            return channel;
        }

        private final Channel channel;

        public ChannelState getState() {
            return state;
        }

        private ChannelState state = ChannelState.OPEN;

        public ChannelNode(Channel channel)
        {
            this.channel = channel;
        }

        @Override
        public void onStateChange(final ChannelState state)
        {
            final ChannelNode node = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    node.state = state;
                    node.notifyNodeUpdateListeners();
                }
            });
        }
    }

    private abstract class StackNode extends UserNode implements StackStateListener {

        public Stack getStack() {
            return stack;
        }

        private final Stack stack;

        public StackState getState() {
            return state;
        }

        private StackState state = StackState.COMMS_DOWN;

        public StackNode(Stack stack)
        {
            this.stack = stack;
        }

        @Override
        public void onStateChange(final StackState state)
        {
            final StackNode node = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    node.state = state;
                    node.notifyNodeUpdateListeners();
                }
            });
        }
    }

    private class TcpClientChannelNode extends ChannelNode {

        private final String id;
        private final String host;
        private final int port;

        public TcpClientChannelNode(String id, String host, int port, Channel channel)
        {
            super(channel);
            this.id = id;
            this.host = host;
            this.port = port;
        }

        @Override
        public String toString()
        {
            return host + ":" + port;
        }
    }

    private class SerialChannelNode extends ChannelNode {

        private final SerialSettings settings;

        public SerialChannelNode(SerialSettings settings, Channel channel)
        {
            super(channel);
            this.settings = settings;
        }

        @Override
        public String toString()
        {
            return settings.port;
        }
    }

    private class MasterNode extends StackNode {

        public Master getMaster() {
            return master;
        }

        public MasterForm getForm() {
            return form;
        }

        private final Master master;
        private final MasterForm form;

        @Override
        public void cleanup()
        {
            this.form.setVisible(false);
        }

        public MasterNode(Master master, MasterForm form)
        {
            super(master);
            this.master = master;
            this.form = form;
        }

        @Override
        public String toString()
        {
            return "master";
        }
    }

    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Dnp3RootNode());
    private final DefaultTreeModel model = new DefaultTreeModel(root);
    private final JPopupMenu rootMenu = getRootMenu();


    private class MyCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();

            if(Dnp3RootNode.class.isInstance(userObject)) {

            }
            else if(ChannelNode.class.isInstance(userObject)) {
                ChannelNode cnode = (ChannelNode) userObject;
                switch(cnode.getState())
                {
                    case OPENING:
                        setForeground(Color.ORANGE);
                        break;
                    case OPEN:
                        setForeground(darkGreen);
                        break;
                    default:
                        setForeground(Color.BLACK);
                        break;
                }
            } else if(StackNode.class.isInstance(userObject)) {
                StackNode snode = (StackNode) userObject;
                switch(snode.getState())
                {
                    case COMMS_UP:
                        setForeground(darkGreen);
                        break;
                    default:
                        setForeground(Color.RED);
                        break;
                }
            }

            return this;
        }
    }

    public void setManager(DNP3Manager manager) {
        this.manager = manager;
    }

    private DNP3Manager manager = null;

    private void recursivelyCleanup(DefaultMutableTreeNode node)
    {
        if(UserNode.class.isInstance(node.getUserObject()))
        {
            //depth first traversal
            Enumeration e = node.children();
            while(e.hasMoreElements())
            {
                Object o = e.nextElement();
                if(DefaultMutableTreeNode.class.isInstance(o)) recursivelyCleanup((DefaultMutableTreeNode) o);
            }
            UserNode n = (UserNode) node.getUserObject();
            n.cleanup();
        }
    }

    private JPopupMenu getStackMenu(final DefaultMutableTreeNode node, final StackNode snode)
    {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                recursivelyCleanup(node);
                snode.getStack().shutdown();
                node.removeFromParent();
                model.reload();
            }
        });
        popup.add(removeItem);
        return popup;
    }

    private JPopupMenu getChannelMenu(final DefaultMutableTreeNode node, final ChannelNode cnode)
    {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem addMasterItem = new JMenuItem("Add Master");
        addMasterItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               AddMasterDialog dialog = new AddMasterDialog(new AddMasterListener() {
                   @Override
                   public void onAdd(MasterStackConfig config) {
                       Channel c = cnode.getChannel();
                       MasterForm form = new MasterForm("master");
                       Master m = c.addMaster("master", LogLevel.INTERPRET, form, config);
                       form.configureWithMaster(m);
                       MasterNode mnode = new MasterNode(m, form);
                       m.addStateListener(mnode);
                       final DefaultMutableTreeNode child = new DefaultMutableTreeNode(mnode);
                       node.add(child);
                       mnode.addUpdateListener(new NodeUpdateListener() {
                           @Override
                           public void onNodeUpdate() {
                               model.nodeChanged(child);
                           }
                       });
                       model.reload();
                   }
               });
               dialog.pack();
               dialog.setVisible(true);
            }
        });
        popup.add(addMasterItem);
        popup.add(new JPopupMenu.Separator());
        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                recursivelyCleanup(node);
                Channel c = cnode.getChannel();
                c.shutdown();
                node.removeFromParent();
                model.reload();
            }
        });
        popup.add(removeItem);
        return popup;
    }

    private JPopupMenu getRootMenu()
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
                            TcpClientChannelNode node = new TcpClientChannelNode(loggerId, host, port, c);
                            c.addStateListener(node);
                            final MutableTreeNode channelNode = new DefaultMutableTreeNode(node);
                            DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                            root.add(channelNode);
                            node.addUpdateListener(new NodeUpdateListener() {
                                @Override
                                public void onNodeUpdate() {
                                  model.nodeChanged(channelNode);
                                }
                            });
                            model.reload();
                        }
                    });
                    dialog.pack();
                    dialog.setVisible(true);
                }
            }
        });
        popup.add(addClientItem);
        JMenuItem addSerialItem = new JMenuItem("Add Serial");
        addSerialItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e))
                {
                    AddSerialDialog dialog = new AddSerialDialog(new AddSerial() {
                        @Override
                        public void onAdd(LogLevel level, int retryMs, SerialSettings settings) {
                            Channel c = manager.addSerial(settings.port, level, retryMs, settings);
                            SerialChannelNode node = new SerialChannelNode(settings, c);
                            c.addStateListener(node);
                            final MutableTreeNode channelNode = new DefaultMutableTreeNode(node);
                            DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                            root.add(channelNode);
                            node.addUpdateListener(new NodeUpdateListener() {
                                @Override
                                public void onNodeUpdate() {
                                    model.nodeChanged(channelNode);
                                }
                            });
                            model.reload();
                        }
                    });
                    dialog.pack();
                    dialog.setVisible(true);
                }
            }
        });
        popup.add(addSerialItem);
        return popup;
    }

    public CommsTree()
    {
        this.setModel(model);
        this.setCellRenderer(new MyCellRenderer());

        final JTree tree = this;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int row = tree.getRowForLocation(e.getX(), e.getY());
                TreePath path = tree.getPathForRow(row);
                if(path != null)
                {
                    final DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    if(SwingUtilities.isRightMouseButton(e))
                    {
                        if(Dnp3RootNode.class.isInstance(node.getUserObject()))
                        {
                            rootMenu.show(tree, x, y);
                        } else if(ChannelNode.class.isInstance(node.getUserObject())) {
                            ChannelNode cnode = (ChannelNode) node.getUserObject();
                            getChannelMenu(node, cnode).show(tree, x, y);
                        } else if(StackNode.class.isInstance(node.getUserObject())) {
                            StackNode snode = (StackNode) node.getUserObject();
                            getStackMenu(node, snode).show(tree, x, y);
                        }
                    }
                    else if(SwingUtilities.isLeftMouseButton(e))
                    {
                        if(MasterNode.class.isInstance(node.getUserObject()) && e.getClickCount() == 2)
                        {
                            MasterNode mnode = (MasterNode) node.getUserObject();
                            mnode.getForm().setVisible(true);
                        }
                    }
                }
            }
        });

    }


}
