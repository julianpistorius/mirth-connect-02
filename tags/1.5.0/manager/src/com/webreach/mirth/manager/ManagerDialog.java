/*
 * ManagerDialog.java
 *
 * Created on April 13, 2007, 2:51 PM
 */

package com.webreach.mirth.manager;

import com.webreach.mirth.manager.util.ObjectCloner;
import com.webreach.mirth.manager.util.ObjectClonerException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author  brendanh
 */
public class ManagerDialog extends javax.swing.JDialog
{
    private Properties serverProperties;
    private Properties databaseProperties;
    private Properties log4jProperties;
        
    private static final String SERVER_WEBSTART_PORT = "http.port";
    private static final String SERVER_ADMINISTRATOR_PORT = "https.port";
    private static final String LOG4J_MIRTH_LOG_LEVEL = "log4j.rootCategory";
    private static final String LOG4J_DATABASE_LOG_LEVEL = "log4j.logger.java.sql";
    private static final String DATABASE_TYPE = "database";
    private static final String DATABASE_URL = "url";
    private static final String DATABASE_USERNAME = "username";
    private static final String DATABASE_PASSWORD = "password";
    
    private static final String DATABASE_DERBY = "derby";
    private static final String DATABASE_POSTGRES = "postgres";
    private static final String DATABASE_MYSQL = "mysql";
    private static final String DATABASE_SQLSERVER = "sqlserver";
    private static final String DATABASE_SQLSERVER2005 = "sqlserver2005";
    private static final String DATABASE_ORACLE = "oracle";
    
    private static final String log4jPropertiesPath = "conf\\log4j.properties";
    private static final String serverPropertiesPath = "conf\\mirth.properties";
    private static final String serverLogsPath = "logs\\";
    private static final String derbyPropertiesPath = "conf\\derby-SqlMapConfig.properties";
    private static final String postgresPropertiesPath = "conf\\postgres-SqlMapConfig.properties";
    private static final String mysqlPropertiesPath = "conf\\mysql-SqlMapConfig.properties";
    private static final String sqlserverPropertiesPath = "conf\\sqlserver-SqlMapConfig.properties";
    private static final String sqlserver2005PropertiesPath = "conf\\sqlserver2005-SqlMapConfig.properties";
    private static final String oraclePropertiesPath = "conf\\oracle-SqlMapConfig.properties";
    
    private static final String[] log4jErrorCodes = new String[]{"ERROR", "WARN", "DEBUG", "INFO"};
    
    /**
     * Creates new form ManagerDialog
     */
    public ManagerDialog()
    {
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                close();
            }
        });       
    }
    
    public void open()
    {
        ManagerController.getInstance().updateMirthServiceStatus();
        loadServerProperties();
        setVisible(true);
    }
    
    public void close()
    {
        setVisible(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        jPanel3 = new javax.swing.JPanel();
        heading = new com.webreach.mirth.manager.MirthHeadingPanel();
        jLabel2 = new javax.swing.JLabel();
        tabPanel = new javax.swing.JTabbedPane();
        servicePanel = new javax.swing.JPanel();
        serviceButtonContainer = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        restartButton = new javax.swing.JButton();
        serverPanel = new javax.swing.JPanel();
        serverWebstartPort = new javax.swing.JTextField();
        serverAdministratorPort = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        serverLogFiles = new javax.swing.JList();
        jLabel4 = new javax.swing.JLabel();
        viewFileButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        mirthLogLevel = new javax.swing.JComboBox();
        databaseLogLevel = new javax.swing.JComboBox();
        databasePanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        databaseUrl = new javax.swing.JTextField();
        databaseType = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        databaseUsername = new javax.swing.JTextField();
        databasePassword = new javax.swing.JPasswordField();
        closeButton = new javax.swing.JButton();
        launchButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setTitle("Mirth Server Manager");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Mirth Server Manager");

        org.jdesktop.layout.GroupLayout headingLayout = new org.jdesktop.layout.GroupLayout(heading);
        heading.setLayout(headingLayout);
        headingLayout.setHorizontalGroup(
            headingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(headingLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 207, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(172, Short.MAX_VALUE))
        );
        headingLayout.setVerticalGroup(
            headingLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, headingLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tabPanel.setFocusable(false);
        servicePanel.setBackground(new java.awt.Color(255, 255, 255));
        servicePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        servicePanel.setFocusable(false);
        serviceButtonContainer.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Starts the windows Mirth service.");

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                startButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                stopButtonActionPerformed(evt);
            }
        });

        jLabel12.setText("Stops the windows Mirth service.");

        jLabel13.setText("Restarts the windows Mirth service.");

        restartButton.setText("Restart");
        restartButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                restartButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout serviceButtonContainerLayout = new org.jdesktop.layout.GroupLayout(serviceButtonContainer);
        serviceButtonContainer.setLayout(serviceButtonContainerLayout);
        serviceButtonContainerLayout.setHorizontalGroup(
            serviceButtonContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(serviceButtonContainerLayout.createSequentialGroup()
                .addContainerGap()
                .add(serviceButtonContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(serviceButtonContainerLayout.createSequentialGroup()
                        .add(stopButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(jLabel12))
                    .add(serviceButtonContainerLayout.createSequentialGroup()
                        .add(startButton)
                        .add(10, 10, 10)
                        .add(jLabel11))
                    .add(serviceButtonContainerLayout.createSequentialGroup()
                        .add(restartButton)
                        .add(10, 10, 10)
                        .add(jLabel13)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        serviceButtonContainerLayout.linkSize(new java.awt.Component[] {restartButton, startButton, stopButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        serviceButtonContainerLayout.setVerticalGroup(
            serviceButtonContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(serviceButtonContainerLayout.createSequentialGroup()
                .addContainerGap()
                .add(serviceButtonContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(startButton)
                    .add(jLabel11))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(serviceButtonContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(restartButton)
                    .add(jLabel13))
                .add(7, 7, 7)
                .add(serviceButtonContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(stopButton)
                    .add(jLabel12))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout servicePanelLayout = new org.jdesktop.layout.GroupLayout(servicePanel);
        servicePanel.setLayout(servicePanelLayout);
        servicePanelLayout.setHorizontalGroup(
            servicePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(servicePanelLayout.createSequentialGroup()
                .add(11, 11, 11)
                .add(serviceButtonContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );
        servicePanelLayout.setVerticalGroup(
            servicePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(servicePanelLayout.createSequentialGroup()
                .add(11, 11, 11)
                .add(serviceButtonContainer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        tabPanel.addTab("Service", servicePanel);

        serverPanel.setBackground(new java.awt.Color(255, 255, 255));
        serverPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        serverPanel.setFocusable(false);

        jLabel3.setText("Administator Port:");

        jLabel1.setText("WebStart Port:");

        serverLogFiles.addListSelectionListener(new javax.swing.event.ListSelectionListener()
        {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt)
            {
                serverLogFilesValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(serverLogFiles);

        jLabel4.setText("Log Files:");

        viewFileButton.setText("View File");
        viewFileButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                viewFileButtonActionPerformed(evt);
            }
        });

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                refreshButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Main Log Level:");

        jLabel6.setText("Database Log Level:");

        mirthLogLevel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        databaseLogLevel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        org.jdesktop.layout.GroupLayout serverPanelLayout = new org.jdesktop.layout.GroupLayout(serverPanel);
        serverPanel.setLayout(serverPanelLayout);
        serverPanelLayout.setHorizontalGroup(
            serverPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(serverPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(serverPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel3)
                    .add(jLabel1)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(serverPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, serverPanelLayout.createSequentialGroup()
                        .add(serverPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(serverWebstartPort)
                            .add(serverAdministratorPort, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 16, Short.MAX_VALUE)
                        .add(serverPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel5)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel6))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(serverPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, databaseLogLevel, 0, 73, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, mirthLogLevel, 0, 73, Short.MAX_VALUE)
                    .add(refreshButton)
                    .add(viewFileButton))
                .addContainerGap())
        );

        serverPanelLayout.linkSize(new java.awt.Component[] {refreshButton, viewFileButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        serverPanelLayout.setVerticalGroup(
            serverPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, serverPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(serverPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jLabel5)
                    .add(mirthLogLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(serverWebstartPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(serverPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jLabel6)
                    .add(databaseLogLevel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(serverAdministratorPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(serverPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, 0, 0, Short.MAX_VALUE)
                    .add(jLabel4)
                    .add(serverPanelLayout.createSequentialGroup()
                        .add(refreshButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(viewFileButton)))
                .addContainerGap())
        );
        tabPanel.addTab("Server", serverPanel);

        databasePanel.setBackground(new java.awt.Color(255, 255, 255));
        databasePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        databasePanel.setFocusable(false);
        jLabel7.setText("Type:");

        jLabel8.setText(" URL:");

        databaseType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "derby", "postgres", "mysql", "oracle", "sqlserver", "sqlserver2005" }));
        databaseType.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                databaseTypeActionPerformed(evt);
            }
        });

        jLabel9.setText("Username:");

        jLabel10.setText("Password:");

        databasePassword.setFont(new java.awt.Font("Tahoma", 0, 11));

        org.jdesktop.layout.GroupLayout databasePanelLayout = new org.jdesktop.layout.GroupLayout(databasePanel);
        databasePanel.setLayout(databasePanelLayout);
        databasePanelLayout.setHorizontalGroup(
            databasePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(databasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(databasePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel10)
                    .add(jLabel9)
                    .add(jLabel8)
                    .add(jLabel7))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(databasePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(databaseType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(databaseUrl, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                    .add(databaseUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(databasePassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        databasePanelLayout.setVerticalGroup(
            databasePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(databasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(databasePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(databaseType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(databasePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(databaseUrl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(databasePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(databaseUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(databasePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel10)
                    .add(databasePassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        tabPanel.addTab("Database", databasePanel);

        closeButton.setText("Cancel");
        closeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeButtonActionPerformed(evt);
            }
        });

        launchButton.setText("Administrator");
        launchButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                launchButtonActionPerformed(evt);
            }
        });

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                okButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(launchButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 122, Short.MAX_VALUE)
                .add(okButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(tabPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addContainerGap())
            .add(heading, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
        );

        jPanel3Layout.linkSize(new java.awt.Component[] {closeButton, okButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(heading, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tabPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 155, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(closeButton)
                    .add(okButton)
                    .add(launchButton))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okButtonActionPerformed
    {//GEN-HEADEREND:event_okButtonActionPerformed
        saveProperties();
        close();
    }//GEN-LAST:event_okButtonActionPerformed

    private void serverLogFilesValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_serverLogFilesValueChanged
    {//GEN-HEADEREND:event_serverLogFilesValueChanged
        if(serverLogFiles.getSelectedIndex() != -1)
            viewFileButton.setEnabled(true);
        else
            viewFileButton.setEnabled(false);
    }//GEN-LAST:event_serverLogFilesValueChanged

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_refreshButtonActionPerformed
    {//GEN-HEADEREND:event_refreshButtonActionPerformed
        refreshLogs();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void databaseTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_databaseTypeActionPerformed
    {//GEN-HEADEREND:event_databaseTypeActionPerformed
        loadDatabaseProperties();
    }//GEN-LAST:event_databaseTypeActionPerformed

    private void launchButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_launchButtonActionPerformed
    {//GEN-HEADEREND:event_launchButtonActionPerformed
        ManagerController.getInstance().launchAdministrator();
    }//GEN-LAST:event_launchButtonActionPerformed

    private void viewFileButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_viewFileButtonActionPerformed
    {//GEN-HEADEREND:event_viewFileButtonActionPerformed
        ManagerController.getInstance().openLogFile(PlatformUI.MIRTH_PATH + serverLogsPath + (String)serverLogFiles.getSelectedValue());
    }//GEN-LAST:event_viewFileButtonActionPerformed

    private void restartButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_restartButtonActionPerformed
    {//GEN-HEADEREND:event_restartButtonActionPerformed
        if(checkPropertiesForChanges())
            saveProperties();
        ManagerController.getInstance().restartMirth();
    }//GEN-LAST:event_restartButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_stopButtonActionPerformed
    {//GEN-HEADEREND:event_stopButtonActionPerformed
        ManagerController.getInstance().stopMirth(true);
    }//GEN-LAST:event_stopButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_startButtonActionPerformed
    {//GEN-HEADEREND:event_startButtonActionPerformed
        if(checkPropertiesForChanges())
            saveProperties();
        ManagerController.getInstance().startMirth(true);
    }//GEN-LAST:event_startButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeButtonActionPerformed
    {//GEN-HEADEREND:event_closeButtonActionPerformed
        close();
    }//GEN-LAST:event_closeButtonActionPerformed
    
    private void loadServerProperties()
    {
        serverProperties = ManagerController.getInstance().getProperties(PlatformUI.MIRTH_PATH + serverPropertiesPath);
        log4jProperties = ManagerController.getInstance().getProperties(PlatformUI.MIRTH_PATH + log4jPropertiesPath);
        
        if(serverProperties.getProperty(SERVER_WEBSTART_PORT) != null)
            serverWebstartPort.setText(serverProperties.getProperty(SERVER_WEBSTART_PORT));
        else
            serverWebstartPort.setText("");
        
        if(serverProperties.getProperty(SERVER_ADMINISTRATOR_PORT) != null)
            serverAdministratorPort.setText(serverProperties.getProperty(SERVER_ADMINISTRATOR_PORT));
        else
            serverAdministratorPort.setText("");
        
        if(serverProperties.getProperty(DATABASE_TYPE) != null)
        {
            databaseType.setSelectedItem(serverProperties.getProperty(DATABASE_TYPE));
        }
        else
            databaseType.setSelectedIndex(0);
        
        mirthLogLevel.setModel(new DefaultComboBoxModel(log4jErrorCodes));
        databaseLogLevel.setModel(new DefaultComboBoxModel(log4jErrorCodes));
        
        if(log4jProperties.getProperty(LOG4J_MIRTH_LOG_LEVEL) != null)
        {
            for(int i = 0; i < log4jErrorCodes.length; i++)
                if(log4jProperties.getProperty(LOG4J_MIRTH_LOG_LEVEL).indexOf(log4jErrorCodes[i]) != -1)
                    mirthLogLevel.setSelectedItem(log4jErrorCodes[i]);
        }
        
        if(log4jProperties.getProperty(LOG4J_DATABASE_LOG_LEVEL) != null)
            databaseLogLevel.setSelectedItem(log4jProperties.getProperty(LOG4J_DATABASE_LOG_LEVEL));
        
        refreshLogs();
        serverLogFilesValueChanged(null);
    }
    
    private void loadDatabaseProperties()
    {
        if(((String)databaseType.getSelectedItem()).equals(DATABASE_DERBY))
            databaseProperties = ManagerController.getInstance().getProperties(PlatformUI.MIRTH_PATH + derbyPropertiesPath);
        else if(((String)databaseType.getSelectedItem()).equals(DATABASE_POSTGRES))
            databaseProperties = ManagerController.getInstance().getProperties(PlatformUI.MIRTH_PATH + postgresPropertiesPath);
        else if(((String)databaseType.getSelectedItem()).equals(DATABASE_MYSQL))
            databaseProperties = ManagerController.getInstance().getProperties(PlatformUI.MIRTH_PATH + mysqlPropertiesPath);
        else if(((String)databaseType.getSelectedItem()).equals(DATABASE_SQLSERVER))
            databaseProperties = ManagerController.getInstance().getProperties(PlatformUI.MIRTH_PATH + sqlserverPropertiesPath);
        else if(((String)databaseType.getSelectedItem()).equals(DATABASE_SQLSERVER2005))
            databaseProperties = ManagerController.getInstance().getProperties(PlatformUI.MIRTH_PATH + sqlserver2005PropertiesPath);
        else if(((String)databaseType.getSelectedItem()).equals(DATABASE_ORACLE))
            databaseProperties = ManagerController.getInstance().getProperties(PlatformUI.MIRTH_PATH + oraclePropertiesPath);
        
        if(databaseProperties.getProperty(DATABASE_URL) != null)
            databaseUrl.setText(databaseProperties.getProperty(DATABASE_URL));
        else
            databaseUrl.setText("");
        
        if(databaseProperties.getProperty(DATABASE_USERNAME) != null)
            databaseUsername.setText(databaseProperties.getProperty(DATABASE_USERNAME));
        else
            databaseUsername.setText("");
        
        if(databaseProperties.getProperty(DATABASE_PASSWORD) != null)
            databasePassword.setText(databaseProperties.getProperty(DATABASE_PASSWORD));
        else
            databasePassword.setText("");
    }
    
    private void saveProperties()
    {
        updateAllProperties(serverProperties, log4jProperties, databaseProperties);
        
        ManagerController.getInstance().setProperties(serverProperties, PlatformUI.MIRTH_PATH + serverPropertiesPath);
        ManagerController.getInstance().setProperties(log4jProperties, PlatformUI.MIRTH_PATH + log4jPropertiesPath);
        
        if(((String)databaseType.getSelectedItem()).equals(DATABASE_DERBY))
            ManagerController.getInstance().setProperties(databaseProperties, PlatformUI.MIRTH_PATH + derbyPropertiesPath);
        else if(((String)databaseType.getSelectedItem()).equals(DATABASE_POSTGRES))
            ManagerController.getInstance().setProperties(databaseProperties, PlatformUI.MIRTH_PATH + postgresPropertiesPath);
        else if(((String)databaseType.getSelectedItem()).equals(DATABASE_MYSQL))
            ManagerController.getInstance().setProperties(databaseProperties, PlatformUI.MIRTH_PATH + mysqlPropertiesPath);
        else if(((String)databaseType.getSelectedItem()).equals(DATABASE_SQLSERVER))
            ManagerController.getInstance().setProperties(databaseProperties, PlatformUI.MIRTH_PATH + sqlserverPropertiesPath);
        else if(((String)databaseType.getSelectedItem()).equals(DATABASE_SQLSERVER2005))
            ManagerController.getInstance().setProperties(databaseProperties, PlatformUI.MIRTH_PATH + sqlserver2005PropertiesPath);
        else if(((String)databaseType.getSelectedItem()).equals(DATABASE_ORACLE))
            ManagerController.getInstance().setProperties(databaseProperties, PlatformUI.MIRTH_PATH + oraclePropertiesPath);
    }
    
    private void updateAllProperties(Properties serverProperties, Properties log4jProperties, Properties databaseProperties)
    {
        updateServerProperties(serverProperties, log4jProperties);
        updateDatabaseProperties(databaseProperties);
    }
    
    private void updateServerProperties(Properties serverProperties, Properties log4jProperties)
    {
        serverProperties.setProperty(SERVER_WEBSTART_PORT, serverWebstartPort.getText());
        serverProperties.setProperty(SERVER_ADMINISTRATOR_PORT, serverAdministratorPort.getText());
        serverProperties.setProperty(DATABASE_TYPE, ((String)databaseType.getSelectedItem()));
        
        StringTokenizer st = new StringTokenizer((String)log4jProperties.getProperty(LOG4J_MIRTH_LOG_LEVEL), ",");
        String mirthLogLevelString = (String) mirthLogLevel.getSelectedItem();
        
        if(st.hasMoreTokens())
            st.nextToken();
        
        while (st.hasMoreTokens())
        {
            mirthLogLevelString += "," + st.nextToken();
        }        
        
        log4jProperties.setProperty(LOG4J_MIRTH_LOG_LEVEL, mirthLogLevelString);
        log4jProperties.setProperty(LOG4J_DATABASE_LOG_LEVEL, (String)databaseLogLevel.getSelectedItem());         
    }
    
    private void updateDatabaseProperties(Properties databaseProperties)
    {
        databaseProperties.setProperty(DATABASE_URL, databaseUrl.getText());
        databaseProperties.setProperty(DATABASE_USERNAME, databaseUsername.getText());
        databaseProperties.setProperty(DATABASE_PASSWORD,  new String(databasePassword.getPassword()));
    }
    
    private boolean checkPropertiesForChanges()
    {
        try
        {
            Properties tempServerProperties = (Properties) ObjectCloner.deepCopy(serverProperties);
            Properties tempLog4jProperties = (Properties) ObjectCloner.deepCopy(log4jProperties);
            Properties tempDatabaseProperties = (Properties) ObjectCloner.deepCopy(databaseProperties);
            
            updateAllProperties(tempServerProperties, tempLog4jProperties, tempDatabaseProperties);
        
            boolean identical = ManagerController.getInstance().compareProps(serverProperties,tempServerProperties) && ManagerController.getInstance().compareProps(log4jProperties,tempLog4jProperties) && ManagerController.getInstance().compareProps(databaseProperties,tempDatabaseProperties);

            if(!identical)
                if(!ManagerController.getInstance().alertOption("Would you like to save the changes before you continue?"))
                    return false;
        }
        catch(ObjectClonerException e)
        {
            e.printStackTrace();
        }
        
        return true;
    }
    
    private void refreshLogs()
    {
        serverLogFiles.setListData(ManagerController.getInstance().getLogFiles(PlatformUI.MIRTH_PATH + serverLogsPath).toArray());
    }
    
    public void setStartButtonActive(boolean active)
    {
        startButton.setEnabled(active);
    }
    
    public void setStopButtonActive(boolean active)
    {
        stopButton.setEnabled(active);
    }
    
    public void setRestartButtonActive(boolean active)
    {
        restartButton.setEnabled(active);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JComboBox databaseLogLevel;
    private javax.swing.JPanel databasePanel;
    private javax.swing.JPasswordField databasePassword;
    private javax.swing.JComboBox databaseType;
    private javax.swing.JTextField databaseUrl;
    private javax.swing.JTextField databaseUsername;
    private com.webreach.mirth.manager.MirthHeadingPanel heading;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton launchButton;
    private javax.swing.JComboBox mirthLogLevel;
    private javax.swing.JButton okButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton restartButton;
    private javax.swing.JTextField serverAdministratorPort;
    private javax.swing.JList serverLogFiles;
    private javax.swing.JPanel serverPanel;
    private javax.swing.JTextField serverWebstartPort;
    private javax.swing.JPanel serviceButtonContainer;
    private javax.swing.JPanel servicePanel;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JButton viewFileButton;
    // End of variables declaration//GEN-END:variables
    
}