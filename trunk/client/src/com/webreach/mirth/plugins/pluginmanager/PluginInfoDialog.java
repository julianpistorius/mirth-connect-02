/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mirth.
 *
 * The Initial Developer of the Original Code is
 * WebReach, Inc.
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Gerald Bortis <geraldb@webreachinc.com>
 *
 * ***** END LICENSE BLOCK ***** */

package com.webreach.mirth.plugins.pluginmanager;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.webreach.mirth.client.ui.BareBonesBrowserLaunch;
import com.webreach.mirth.client.ui.PlatformUI;

/** Creates the About Mirth dialog. The content is loaded from about.txt. */
public class PluginInfoDialog extends javax.swing.JDialog
{
    /**
     * Creates new form ViewContentDialog
     */
    public PluginInfoDialog(String name,
            String type,
            String author,
            String mirthVersion,
            String pluginVersion,
            String url,
            String description)
    {
        super(PlatformUI.MIRTH_FRAME);
        
        initComponents();
        nameLabel.setText(name);
        typeLabel.setText(type);
        authorLabel.setText(author);
        mirthVersionLabel.setText(mirthVersion);
        pluginVersionLabel.setText(pluginVersion);
        descriptionLabel.setText(description);
        urlLabel.setText(url);
        urlLabel.setToolTipText(url);
        urlLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Dimension dlgSize = getPreferredSize();
        Dimension frmSize = PlatformUI.MIRTH_FRAME.getSize();
        Point loc = PlatformUI.MIRTH_FRAME.getLocation();
        setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        setVisible(true);
        
    }  
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        authorLabel = new javax.swing.JLabel();
        mirthVersionLabel = new javax.swing.JLabel();
        pluginVersionLabel = new javax.swing.JLabel();
        urlLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionLabel = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Extension Information");
        setResizable(false);
        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jPanel1.setMaximumSize(null);
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setLabelFor(nameLabel);
        jLabel2.setText("Name:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setLabelFor(nameLabel);
        jLabel3.setText("Author:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setLabelFor(nameLabel);
        jLabel4.setText("Mirth Version:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setLabelFor(nameLabel);
        jLabel5.setText("Plugin Version:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setLabelFor(nameLabel);
        jLabel6.setText("Description:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel7.setLabelFor(nameLabel);
        jLabel7.setText("Plugin URL:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel8.setLabelFor(nameLabel);
        jLabel8.setText("Type:");

        urlLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        urlLabel.setForeground(new java.awt.Color(0, 51, 204));
        urlLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/webreach/mirth/client/ui/images/world_go.png")));
        urlLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                urlLabelMouseClicked(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(204, 204, 204)));
        jPanel2.setForeground(new java.awt.Color(153, 153, 153));
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(295, Short.MAX_VALUE)
                .add(closeButton))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(closeButton)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(null);
        descriptionLabel.setBorder(null);
        descriptionLabel.setEditable(false);
        descriptionLabel.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        descriptionLabel.setEnabled(false);
        jScrollPane1.setViewportView(descriptionLabel);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel7)
                            .add(jLabel5)
                            .add(jLabel4)
                            .add(jLabel3)
                            .add(jLabel2)
                            .add(jLabel8)
                            .add(jLabel6))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(nameLabel)
                            .add(typeLabel)
                            .add(authorLabel)
                            .add(mirthVersionLabel)
                            .add(pluginVersionLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                                    .add(urlLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 266, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(nameLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(typeLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(authorLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(mirthVersionLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(pluginVersionLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(urlLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel6)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void urlLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_urlLabelMouseClicked
        // TODO add your handling code here:
        BareBonesBrowserLaunch.openURL(urlLabel.getText());
    }//GEN-LAST:event_urlLabelMouseClicked
    
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeButtonActionPerformed
    {//GEN-HEADEREND:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel authorLabel;
    private javax.swing.JButton closeButton;
    private javax.swing.JTextPane descriptionLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel mirthVersionLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel pluginVersionLabel;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JLabel urlLabel;
    // End of variables declaration//GEN-END:variables
    
}
