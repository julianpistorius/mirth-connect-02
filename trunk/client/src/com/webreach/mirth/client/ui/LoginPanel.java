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

import java.awt.Cursor;

import javax.swing.ImageIcon;

import org.jdesktop.swingx.util.SwingWorker;

import com.webreach.mirth.client.core.Client;
import com.webreach.mirth.client.core.ClientException;
import com.webreach.mirth.client.core.VersionMismatchException;

/** Creates the new channel wizard dialog */
public class LoginPanel extends javax.swing.JFrame
{
    private Client client;

    public LoginPanel(String mirthServer, String version, String user, String pass)
    {
        PlatformUI.CLIENT_VERSION = version;
        initComponents();
        setTitle("Mirth Administrator - Login");
        serverName.setText(mirthServer);
        jLabel2.setForeground(UIConstants.HEADER_TITLE_TEXT_COLOR);
        jLabel5.setForeground(UIConstants.HEADER_TITLE_TEXT_COLOR);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/mirthlogo1616.png")).getImage());
        setLocationRelativeTo(null);
        setVisible(true);
        username.grabFocus();
        username.setText(user);
        password.setText(pass);        
        errorPane.setVisible(false);
        
        webreachImage.setIcon(UIConstants.WEBREACH_LOGO);
        webreachImage.setText("");
        webreachImage.setToolTipText(UIConstants.WEBREACH_TOOLTIP);
        webreachImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        webreachImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	BareBonesBrowserLaunch.openURL(UIConstants.WEBREACH_URL);
            }
        });
        
        webreachImage1.setIcon(UIConstants.WEBREACH_LOGO);
        webreachImage1.setText("");
        webreachImage1.setToolTipText(UIConstants.WEBREACH_TOOLTIP);
        webreachImage1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        webreachImage1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	BareBonesBrowserLaunch.openURL(UIConstants.WEBREACH_URL);
            }
        });
        
        if(user.length() > 0 && pass.length() > 0)
            loginButtonActionPerformed(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        loginMain = new javax.swing.JPanel();
        errorPane = new javax.swing.JScrollPane();
        error = new javax.swing.JTextArea();
        closeButton = new javax.swing.JButton();
        loginButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        serverName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        mirthHeadingPanel2 = new com.webreach.mirth.client.ui.MirthHeadingPanel();
        jLabel2 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        webreachImage = new javax.swing.JLabel();
        loggingIn = new javax.swing.JPanel();
        mirthHeadingPanel1 = new com.webreach.mirth.client.ui.MirthHeadingPanel();
        jLabel5 = new javax.swing.JLabel();
        loginProgress = new javax.swing.JProgressBar();
        status = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        webreachImage1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mirth Administrator Login");
        setResizable(false);

        loginMain.setBackground(new java.awt.Color(255, 255, 255));
        loginMain.setName(""); // NOI18N

        errorPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        errorPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        error.setBackground(java.awt.Color.pink);
        error.setColumns(20);
        error.setEditable(false);
        error.setFont(new java.awt.Font("Tahoma", 0, 11));
        error.setRows(3);
        error.setText("There was a problem authenticating the information that\nwas entered.  Please verify that the server is up and \nrunning and that the user information is valid.");
        error.setAutoscrolls(false);
        error.setFocusable(false);
        errorPane.setViewportView(error);

        closeButton.setText("Exit");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        serverName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverNameActionPerformed(evt);
            }
        });

        jLabel1.setText("Server:");

        jLabel3.setText("Username:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Mirth Administrator Login");

        org.jdesktop.layout.GroupLayout mirthHeadingPanel2Layout = new org.jdesktop.layout.GroupLayout(mirthHeadingPanel2);
        mirthHeadingPanel2.setLayout(mirthHeadingPanel2Layout);
        mirthHeadingPanel2Layout.setHorizontalGroup(
            mirthHeadingPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mirthHeadingPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 322, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        mirthHeadingPanel2Layout.setVerticalGroup(
            mirthHeadingPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mirthHeadingPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );

        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });

        jLabel6.setText("Password:");

        password.setFont(new java.awt.Font("Tahoma", 0, 11));
        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });

        webreachImage.setText(" ");

        org.jdesktop.layout.GroupLayout loginMainLayout = new org.jdesktop.layout.GroupLayout(loginMain);
        loginMain.setLayout(loginMainLayout);
        loginMainLayout.setHorizontalGroup(
            loginMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(loginMainLayout.createSequentialGroup()
                .addContainerGap()
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, loginMainLayout.createSequentialGroup()
                .add(49, 49, 49)
                .add(loginMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, errorPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .add(loginMainLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 2, Short.MAX_VALUE)
                        .add(loginMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel6)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel3)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(loginMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(serverName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                            .add(username, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                            .add(password, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))))
                .add(52, 52, 52))
            .add(mirthHeadingPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, loginMainLayout.createSequentialGroup()
                .addContainerGap()
                .add(webreachImage)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 235, Short.MAX_VALUE)
                .add(loginButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(closeButton)
                .addContainerGap())
        );

        loginMainLayout.linkSize(new java.awt.Component[] {closeButton, loginButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        loginMainLayout.setVerticalGroup(
            loginMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, loginMainLayout.createSequentialGroup()
                .add(mirthHeadingPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(22, 22, 22)
                .add(loginMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(serverName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(loginMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(username, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(loginMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(password, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(errorPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(loginMainLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(closeButton)
                    .add(loginButton)
                    .add(webreachImage))
                .addContainerGap())
        );

        loggingIn.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Mirth Administrator Login");

        org.jdesktop.layout.GroupLayout mirthHeadingPanel1Layout = new org.jdesktop.layout.GroupLayout(mirthHeadingPanel1);
        mirthHeadingPanel1.setLayout(mirthHeadingPanel1Layout);
        mirthHeadingPanel1Layout.setHorizontalGroup(
            mirthHeadingPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mirthHeadingPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 326, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        mirthHeadingPanel1Layout.setVerticalGroup(
            mirthHeadingPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mirthHeadingPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addContainerGap())
        );

        loginProgress.setDoubleBuffered(true);

        status.setText("Please wait: Logging in...");

        webreachImage1.setText(" ");

        org.jdesktop.layout.GroupLayout loggingInLayout = new org.jdesktop.layout.GroupLayout(loggingIn);
        loggingIn.setLayout(loggingInLayout);
        loggingInLayout.setHorizontalGroup(
            loggingInLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mirthHeadingPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, loggingInLayout.createSequentialGroup()
                .addContainerGap()
                .add(loginProgress, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
            .add(loggingInLayout.createSequentialGroup()
                .addContainerGap()
                .add(status)
                .addContainerGap(247, Short.MAX_VALUE))
            .add(loggingInLayout.createSequentialGroup()
                .addContainerGap()
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addContainerGap())
            .add(loggingInLayout.createSequentialGroup()
                .addContainerGap()
                .add(webreachImage1)
                .addContainerGap(365, Short.MAX_VALUE))
        );
        loggingInLayout.setVerticalGroup(
            loggingInLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(loggingInLayout.createSequentialGroup()
                .add(mirthHeadingPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(45, 45, 45)
                .add(status)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(loginProgress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(66, 66, 66)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 15, Short.MAX_VALUE)
                .add(webreachImage1)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(loginMain, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(loggingIn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(loginMain, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(loggingIn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void usernameActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_usernameActionPerformed
    {// GEN-HEADEREND:event_usernameActionPerformed
        loginButtonActionPerformed(null);
    }// GEN-LAST:event_usernameActionPerformed

    private void serverNameActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_serverNameActionPerformed
    {// GEN-HEADEREND:event_serverNameActionPerformed
        loginButtonActionPerformed(null);
    }// GEN-LAST:event_serverNameActionPerformed

    private void passwordActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_passwordActionPerformed
    {// GEN-HEADEREND:event_passwordActionPerformed
        loginButtonActionPerformed(null);
    }// GEN-LAST:event_passwordActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_loginButtonActionPerformed
    {// GEN-HEADEREND:event_loginButtonActionPerformed
        errorPane.setVisible(false);
        
        final LoginPanel thisPanel = this;

        SwingWorker worker = new SwingWorker<Void, Void>()
        {
            public Void doInBackground()
            {
                try
                {
                    if (login())
                    {
                        setStatus("Authenticated...");
                        new Mirth(client);
                        thisPanel.dispose();
                    }
                    else
                    {
                        errorPane.setVisible(true);
                        loggingIn.setVisible(false);
                        loginMain.setVisible(true);
                        loginProgress.setIndeterminate(false);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            public void done()
            {

            }
        };
        worker.execute();

        loggingIn.setVisible(true);
        loginMain.setVisible(false);
        loginProgress.setIndeterminate(true);
    }// GEN-LAST:event_loginButtonActionPerformed

    /**
     * If the button is "Next" instead of "Finish" then it moves on to the next
     * options. Otherwise, it creates the new channel.
     */
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_closeButtonActionPerformed
    {// GEN-HEADEREND:event_closeButtonActionPerformed
        this.dispose();
        System.exit(0);
    }// GEN-LAST:event_closeButtonActionPerformed

    private boolean login()
    {
        try
        {
            String server = serverName.getText();
            client = new Client(server);
            PlatformUI.SERVER_NAME = server;
            if (client.login(username.getText(), String.valueOf(password.getPassword()), PlatformUI.CLIENT_VERSION))
            {
                PlatformUI.USER_NAME = username.getText();
                return true;
            }
            else
                error.setText("There was a problem authenticating the information that\nwas entered.  Please verify that the server is up and \nrunning and that the user information is valid.");
        }
        catch (ClientException ex)
        {
            if (ex.getCause() instanceof VersionMismatchException)
                error.setText("The version of this client does not match the version\nof the server.  Please clear your Java cache and\nrelaunch the client from the server webpage.");
            else
                error.setText("There was a problem authenticating the information that\nwas entered.  Please verify that the server is up and \nrunning and that the user information is valid.");	
        }
        return false;
    }

    public void setStatus(String status)
    {
        this.status.setText("Please wait: " + status);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton closeButton;
    private javax.swing.JTextArea error;
    private javax.swing.JScrollPane errorPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel loggingIn;
    private javax.swing.JButton loginButton;
    private javax.swing.JPanel loginMain;
    private javax.swing.JProgressBar loginProgress;
    private com.webreach.mirth.client.ui.MirthHeadingPanel mirthHeadingPanel1;
    private com.webreach.mirth.client.ui.MirthHeadingPanel mirthHeadingPanel2;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField serverName;
    private javax.swing.JLabel status;
    private javax.swing.JTextField username;
    private javax.swing.JLabel webreachImage;
    private javax.swing.JLabel webreachImage1;
    // End of variables declaration//GEN-END:variables

}
