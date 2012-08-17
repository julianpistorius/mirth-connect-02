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

package com.webreach.mirth.client.ui;

import com.webreach.mirth.client.ui.components.MirthSyntaxTextArea;
import com.webreach.mirth.model.MessageObject;
import com.webreach.mirth.model.MessageObject.Protocol;
import com.webreach.mirth.model.converters.DocumentSerializer;
import java.awt.Dimension;
import java.awt.Point;
import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.EDITokenMarker;
import org.syntax.jedit.tokenmarker.HL7TokenMarker;
import org.syntax.jedit.tokenmarker.X12TokenMarker;
import org.syntax.jedit.tokenmarker.XMLTokenMarker;
import org.w3c.dom.Document;

/** Creates the About Mirth dialog. The content is loaded from about.txt. */
public class EditMessageDialog extends javax.swing.JDialog
{
    private Frame parent;
    private MessageObject message;
    /**
     * Creates new form ViewContentDialog
     */
    public EditMessageDialog(MessageObject message)
    {
        super(PlatformUI.MIRTH_FRAME);
        this.parent = PlatformUI.MIRTH_FRAME;
        this.message = message;
        initComponents();
        setCorrectDocument(messageContent, message.getRawData(), message.getRawDataProtocol());
        messageContent.setCaretPosition(0);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        Dimension dlgSize = getPreferredSize();
        Dimension frmSize = parent.getSize();
        Point loc = parent.getLocation();
        setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        setVisible(true);
    }
    
    private void setCorrectDocument(MirthSyntaxTextArea textPane, String message, MessageObject.Protocol protocol)
    {
        SyntaxDocument newDoc = new SyntaxDocument();
        
        if (message != null)
        {
            if (protocol != null)
            {
                if (protocol.equals(MessageObject.Protocol.HL7V2))
                {
                    newDoc.setTokenMarker(new HL7TokenMarker());
                    message = message.replace('\r', '\n');
                    // HL7 (ER7) encoded messages have \r as end of line
                    // segments
                    // The syntax editor box only recognizes \n
                    // Add \n to make things look normal
                }
                else if (protocol.equals(MessageObject.Protocol.XML) || protocol.equals(Protocol.HL7V3))
                {
                    newDoc.setTokenMarker(new XMLTokenMarker());
                    DocumentSerializer serializer = new DocumentSerializer();
                    serializer.setPreserveSpace(false);
                   
                    try
                    {
                        Document doc = serializer.fromXML(message);
                        message = serializer.toXML(doc);
                    }
                    catch(Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
                else if (protocol.equals(MessageObject.Protocol.X12))
                {
                    newDoc.setTokenMarker(new X12TokenMarker());
                }
                else if (protocol.equals(MessageObject.Protocol.EDI))
                {
                    newDoc.setTokenMarker(new EDITokenMarker());
                }
            }
            
            textPane.setDocument(newDoc);
            textPane.setText(message);
        }
        else
        {
            textPane.setDocument(newDoc);
            textPane.setText("");
        }
        
        textPane.setCaretPosition(0);
    }   
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        jPanel1 = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        processMessageButton = new javax.swing.JButton();
        messageContent = new com.webreach.mirth.client.ui.components.MirthSyntaxTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Raw Message Data");
        getAccessibleContext().setAccessibleName("Raw Message Data");
        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeButtonActionPerformed(evt);
            }
        });

        processMessageButton.setText("Process Message");
        processMessageButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                processMessageButtonActionPerformed(evt);
            }
        });

        messageContent.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(processMessageButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(closeButton))
                    .add(messageContent, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(messageContent, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(closeButton)
                    .add(processMessageButton))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeButtonActionPerformed
    {//GEN-HEADEREND:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void processMessageButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_processMessageButtonActionPerformed
    {//GEN-HEADEREND:event_processMessageButtonActionPerformed
        message.setRawData(messageContent.getText());
        parent.processMessage(message);
        this.dispose();
    }//GEN-LAST:event_processMessageButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel jPanel1;
    private com.webreach.mirth.client.ui.components.MirthSyntaxTextArea messageContent;
    private javax.swing.JButton processMessageButton;
    // End of variables declaration//GEN-END:variables

}