/*
 * UserDialog.java
 *
 * Created on May 22, 2006, 11:03 AM
 */

package com.webreach.mirth.client;

import com.webreach.mirth.client.core.ClientException;
import com.webreach.mirth.model.User;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author  brendanh
 */
public class UserWizard extends javax.swing.JDialog
{
    Frame parent;
    int index = -1;
    /** Creates new form UserDialog */
    public UserWizard(JFrame parent, int row)
    {
        super(parent);
        this.parent = (Frame)parent;
        initComponents();
        finishButton.setEnabled(false);
        if(row != -1)
        {
            index = this.parent.adminPanel.u.getUserIndex();
            username.setText(this.parent.users.get(index).getUsername());     
            password1.setText(this.parent.users.get(index).getPassword());
            password2.setText(this.parent.users.get(index).getPassword());
        }
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        channelOverview = new javax.swing.JPanel();
        finishButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        username = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        password1 = new javax.swing.JPasswordField();
        password2 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("User Wizard");
        channelOverview.setBackground(new java.awt.Color(255, 255, 255));
        channelOverview.setName("");
        finishButton.setText("Finish");
        finishButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                finishButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelButtonActionPerformed(evt);
            }
        });

        username.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                usernameKeyReleased(evt);
            }
        });

        jLabel1.setText("Username:");

        jLabel2.setBackground(new java.awt.Color(112, 151, 226));
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel2.setText("User Wizard");
        jLabel2.setOpaque(true);

        jLabel3.setText("Password:");

        jLabel4.setText("Retype Password:");

        password1.setFont(new java.awt.Font("Tahoma", 0, 11));
        password1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                password1KeyReleased(evt);
            }
        });

        password2.setFont(new java.awt.Font("Tahoma", 0, 11));
        password2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                password2KeyReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout channelOverviewLayout = new org.jdesktop.layout.GroupLayout(channelOverview);
        channelOverview.setLayout(channelOverviewLayout);
        channelOverviewLayout.setHorizontalGroup(
            channelOverviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(channelOverviewLayout.createSequentialGroup()
                .addContainerGap()
                .add(channelOverviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(channelOverviewLayout.createSequentialGroup()
                        .add(channelOverviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                            .add(channelOverviewLayout.createSequentialGroup()
                                .add(cancelButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(finishButton)))
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, channelOverviewLayout.createSequentialGroup()
                        .add(37, 37, 37)
                        .add(channelOverviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel3)
                            .add(jLabel4))
                        .add(60, 60, 60)
                        .add(channelOverviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(password2)
                            .add(password1)
                            .add(username, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
                        .add(53, 53, 53))))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
        );

        channelOverviewLayout.linkSize(new java.awt.Component[] {cancelButton, finishButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        channelOverviewLayout.setVerticalGroup(
            channelOverviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, channelOverviewLayout.createSequentialGroup()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(29, 29, 29)
                .add(channelOverviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(username, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 30, Short.MAX_VALUE)
                .add(channelOverviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(password1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(31, 31, 31)
                .add(channelOverviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(password2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(69, 69, 69)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(channelOverviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(finishButton)
                    .add(cancelButton))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(channelOverview, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(channelOverview, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void password2KeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_password2KeyReleased
    {//GEN-HEADEREND:event_password2KeyReleased
        if(String.valueOf(password1.getPassword()).equals("") || String.valueOf(password2.getPassword()).trim().equals("") || username.getText().trim().equals(""))
            finishButton.setEnabled(false);
        else
            finishButton.setEnabled(true);
    }//GEN-LAST:event_password2KeyReleased

    private void password1KeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_password1KeyReleased
    {//GEN-HEADEREND:event_password1KeyReleased
        if(String.valueOf(password1.getPassword()).equals("") || String.valueOf(password2.getPassword()).trim().equals("") || username.getText().trim().equals(""))
            finishButton.setEnabled(false);
        else
            finishButton.setEnabled(true);
    }//GEN-LAST:event_password1KeyReleased

    private void finishButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_finishButtonActionPerformed
    {//GEN-HEADEREND:event_finishButtonActionPerformed
        for (int i = 0; i < parent.users.size(); i++)
        {
            if (parent.users.get(i).getUsername().equals(username.getText()))
            {
                JOptionPane.showMessageDialog(this, "Username already exists.");
                return;
            }
        }
        if(!String.valueOf(password1.getPassword()).equals(String.valueOf(password2.getPassword())))
        {
            JOptionPane.showMessageDialog(this, "Passwords must be the same.");
            return;
        }
        else
        {
            User temp = new User();
            try
            {
                if(index != -1)
                    temp.setId(this.parent.users.get(index).getId());
                else
                    temp.setId(parent.mirthClient.getNextId());
                temp.setUsername(username.getText());
                temp.setPassword(String.valueOf(password1.getPassword()));
                parent.updateUser(temp);
            } 
            catch (ClientException ex)
            {
                ex.printStackTrace();
            }
            this.dispose();
        }
    }//GEN-LAST:event_finishButtonActionPerformed

    private void usernameKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_usernameKeyReleased
    {//GEN-HEADEREND:event_usernameKeyReleased
        if(String.valueOf(password1.getPassword()).equals("") || String.valueOf(password2.getPassword()).trim().equals("") || username.getText().trim().equals(""))
            finishButton.setEnabled(false);
        else
            finishButton.setEnabled(true);
    }//GEN-LAST:event_usernameKeyReleased

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
    {//GEN-HEADEREND:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel channelOverview;
    private javax.swing.JButton finishButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPasswordField password1;
    private javax.swing.JPasswordField password2;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
    
}
