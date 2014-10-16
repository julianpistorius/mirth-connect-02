/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * 
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL license a copy of which has
 * been included with this distribution in the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;

import com.mirth.connect.client.ui.components.MirthTable;

public class ChannelTagDialog extends MirthDialog {
    private Frame parent;
    private MirthTable tagTable;

    public ChannelTagDialog(MirthTable tagTable) {
        super(PlatformUI.MIRTH_FRAME, true);
        this.parent = PlatformUI.MIRTH_FRAME;
        this.tagTable = tagTable;
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    protected JRootPane createRootPane() {
        JRootPane rootPane = new JRootPane();

        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        rootPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonActionPerformed(null);
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        return rootPane;
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            Dimension dlgSize = getPreferredSize();
            Dimension frmSize = parent.getSize();
            Point loc = parent.getLocation();

            if ((frmSize.width == 0 && frmSize.height == 0) || (loc.x == 0 && loc.y == 0)) {
                setLocationRelativeTo(null);
            } else {
                setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
            }

            Set<String> availableTags = new LinkedHashSet<String>(parent.getChannelTagInfo(false).getTags());
            DefaultTableModel model = (DefaultTableModel) tagTable.getModel();
            int rowCount = model.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                availableTags.remove(model.getValueAt(i, 0));
            }

            tagComboBox.setModel(new DefaultComboBoxModel(availableTags.toArray()));

            if (availableTags.size() > 0) {
                tagComboBox.setSelectedIndex(0);
            }

            tagComboBox.requestFocusInWindow();
        }

        super.setVisible(visible);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cancelButton = new com.mirth.connect.client.ui.components.MirthButton();
        addButton = new com.mirth.connect.client.ui.components.MirthButton();
        tagComboBox = new com.mirth.connect.client.ui.components.MirthComboBox();
        addTagLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Tag");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        tagComboBox.setBackground(new java.awt.Color(255, 255, 255));
        tagComboBox.setEditable(true);
        tagComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tagComboBoxKeyReleased(evt);
            }
        });

        addTagLabel.setText("Tag:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(addTagLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tagComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 251, Short.MAX_VALUE)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addTagLabel)
                    .addComponent(tagComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        String tag = StringUtils.trim((String) tagComboBox.getSelectedItem());

        if (tag == null || tag.isEmpty()) {
            parent.alertError(parent, "Please enter a tag");
        } else {
            DefaultTableModel model = (DefaultTableModel) tagTable.getModel();
            int rowCount = model.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                if (model.getValueAt(i, 0).equals(tag)) {
                    parent.alertError(parent, "\"" + tag + "\" is already assigned to this channel");
                    return;
                }
            }

            model.addRow(new Object[] { tag });
            tagTable.setRowSelectionInterval(rowCount, rowCount);
            dispose();
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void tagComboBoxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tagComboBoxKeyReleased

    }//GEN-LAST:event_tagComboBoxKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.mirth.connect.client.ui.components.MirthButton addButton;
    private javax.swing.JLabel addTagLabel;
    private com.mirth.connect.client.ui.components.MirthButton cancelButton;
    private javax.swing.JPanel jPanel1;
    private com.mirth.connect.client.ui.components.MirthComboBox tagComboBox;
    // End of variables declaration//GEN-END:variables
}
