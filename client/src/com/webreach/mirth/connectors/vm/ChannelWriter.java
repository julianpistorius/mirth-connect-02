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

package com.webreach.mirth.connectors.vm;

import com.webreach.mirth.client.ui.UIConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.webreach.mirth.connectors.ConnectorClass;
import com.webreach.mirth.model.Channel;

/**
 * A form that extends from ConnectorClass. All methods implemented are
 * described in ConnectorClass.
 */
public class ChannelWriter extends ConnectorClass
{
    /** Creates new form FTPReader */
    
    private HashMap channelList;

    public ChannelWriter()
    {
        name = ChannelWriterProperties.name;
        initComponents();
    }

    public Properties getProperties()
    {
        Properties properties = new Properties();
        properties.put(ChannelWriterProperties.DATATYPE, name);
        properties.put(ChannelWriterProperties.CHANNEL_ID, channelList.get((String) channelNames.getSelectedItem()));
        properties.put(ChannelWriterProperties.CHANNEL_NAME, (String) channelNames.getSelectedItem());
        
        if(channelResponseYes.isSelected())
            properties.put(ChannelWriterProperties.CHANNEL_SYNCHRONOUS, UIConstants.YES_OPTION);
        else
            properties.put(ChannelWriterProperties.CHANNEL_SYNCHRONOUS, UIConstants.NO_OPTION);
        
        return properties;
    }

    public void setProperties(Properties props)
    {
        resetInvalidProperties();
        
        ArrayList<String> channelNameArray = new ArrayList<String>();
        channelList = new HashMap();
        channelList.put("None", "sink");
        channelNameArray.add("None");
        for (Channel channel : parent.channels.values())
        {
            channelList.put(channel.getName(), channel.getId());
            channelNameArray.add(channel.getName());
        }
        channelNames.setModel(new javax.swing.DefaultComboBoxModel(channelNameArray.toArray()));

        boolean visible = parent.channelEditTasks.getContentPane().getComponent(0).isVisible();

        if (props.get(ChannelWriterProperties.CHANNEL_NAME) != null)
            channelNames.setSelectedItem((String) props.get(ChannelWriterProperties.CHANNEL_NAME));

        if(props.get(ChannelWriterProperties.CHANNEL_SYNCHRONOUS).equals(UIConstants.YES_OPTION))
            channelResponseYes.setSelected(true);
        else
            channelResponseYes.setSelected(false);
        
        parent.channelEditTasks.getContentPane().getComponent(0).setVisible(visible);
    }

    public Properties getDefaults()
    {
        return new ChannelWriterProperties().getDefaults();
    }

    public boolean checkProperties(Properties props)
    {
        resetInvalidProperties();
        boolean valid = true;
        
        return valid;
    }
    
    private void resetInvalidProperties()
    {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        buttonGroup1 = new javax.swing.ButtonGroup();
        URL = new javax.swing.JLabel();
        channelNames = new com.webreach.mirth.client.ui.components.MirthComboBox();
        jLabel1 = new javax.swing.JLabel();
        channelResponseYes = new com.webreach.mirth.client.ui.components.MirthRadioButton();
        channelResponseNo = new com.webreach.mirth.client.ui.components.MirthRadioButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        URL.setText("Channel Name:");

        channelNames.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Wait for Channel Response:");

        channelResponseYes.setBackground(new java.awt.Color(255, 255, 255));
        channelResponseYes.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonGroup1.add(channelResponseYes);
        channelResponseYes.setText("Yes");
        channelResponseYes.setMargin(new java.awt.Insets(0, 0, 0, 0));

        channelResponseNo.setBackground(new java.awt.Color(255, 255, 255));
        channelResponseNo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buttonGroup1.add(channelResponseNo);
        channelResponseNo.setText("No");
        channelResponseNo.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel1)
                    .add(URL))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(channelNames, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(channelResponseYes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(channelResponseNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(URL)
                    .add(channelNames, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(channelResponseYes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(channelResponseNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel URL;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.webreach.mirth.client.ui.components.MirthComboBox channelNames;
    private com.webreach.mirth.client.ui.components.MirthRadioButton channelResponseNo;
    private com.webreach.mirth.client.ui.components.MirthRadioButton channelResponseYes;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

}
