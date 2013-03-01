package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.DNP3Manager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;

public class CommsTree extends JTree {

    private class RootNode {

    }

    private final DefaultMutableTreeNode root = new DefaultMutableTreeNode(new RootNode());
    private final DefaultTreeModel model = new DefaultTreeModel(root);
    //private final ImageIcon rootIcon;

    private class MyCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();

            if(RootNode.class.isInstance(userObject)) {
                setText("<html><font color=\"black\">open</font><font color=\"red\">dnp</font><font color=\"black\">3</font></html>");
                setToolTipText("right click to add a communication channel");
            }
            else {

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
    }


}
