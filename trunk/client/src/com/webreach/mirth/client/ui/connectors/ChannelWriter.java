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


package com.webreach.mirth.client.ui.connectors;

import com.webreach.mirth.client.ui.UIConstants;
import com.webreach.mirth.client.ui.components.MirthFieldConstraints;
import com.webreach.mirth.model.Channel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import javax.swing.JComponent;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import com.webreach.mirth.client.ui.Frame;
import com.webreach.mirth.client.ui.PlatformUI;


/**
 * A form that extends from ConnectorClass.  All methods implemented
 * are described in ConnectorClass.
 */
public class ChannelWriter extends ConnectorClass
{
    Frame parent; 
    
    /** Creates new form FTPReader */
    public final String DATATYPE = "DataType";
    public final String CHANNEL_ID = "host";
    public final String CHANNEL_NAME = "channelName";
    public final String CHANNEL_TEMPLATE= "tempalte";
    private HashMap channelList;
    
    public ChannelWriter()
    {
        this.parent = PlatformUI.MIRTH_FRAME;
        name = "Channel Writer";
        initComponents();
    }
        
    public Properties getProperties()
    {
        Properties properties = new Properties();
        properties.put(DATATYPE, name);
        properties.put(CHANNEL_ID, channelList.get((String)channelNames.getSelectedItem()));
        properties.put(CHANNEL_NAME, (String)channelNames.getSelectedItem());
        properties.put(CHANNEL_TEMPLATE, template.getText());
        return properties;
    }

    public void setProperties(Properties props)
    {   
        ArrayList<String> channelNameArray = new ArrayList<String>();
        channelList = new HashMap();
        channelList.put("None", "sink");
        channelNameArray.add("None");
        for (Channel channel : parent.channels.values())
        {
            if(channel.isEnabled())
            {
                channelList.put(channel.getName(), channel.getId());
                channelNameArray.add(channel.getName());
            }
            
        }
        channelNames.setModel(new javax.swing.DefaultComboBoxModel(channelNameArray.toArray()));
        
        boolean visible = parent.channelEditTasks.getContentPane().getComponent(0).isVisible();
        channelNames.setSelectedItem(((String)props.get(channelList.get(CHANNEL_NAME))));
        parent.channelEditTasks.getContentPane().getComponent(0).setVisible(visible);
        
        template.setText(((String)props.get(CHANNEL_TEMPLATE)));
    }
    
    public Properties getDefaults()
    {
        Properties properties = new Properties();
        properties.put(DATATYPE, name);
        properties.put(CHANNEL_ID, "sink");
        properties.put(CHANNEL_NAME, "None");
        properties.put(CHANNEL_TEMPLATE, "${message.encodedData}");
        return properties;
    }
    
    public boolean checkProperties(Properties props)
    {
        if (((String)props.get(CHANNEL_TEMPLATE)).length() > 0)
        {
                return true;
        }
        return false;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        URL = new javax.swing.JLabel();
        channelNames = new com.webreach.mirth.client.ui.components.MirthComboBox();
        template = new com.webreach.mirth.client.ui.components.MirthSyntaxTextArea();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Channel Writer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0)));
        URL.setText("Channel Name:");

        channelNames.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        template.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Template:");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(35, 35, 35)
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(template, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(URL)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(channelNames, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(URL)
                    .add(channelNames, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(template, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel URL;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private com.webreach.mirth.client.ui.components.MirthComboBox channelNames;
    private javax.swing.JLabel jLabel1;
    private com.webreach.mirth.client.ui.components.MirthSyntaxTextArea template;
    // End of variables declaration//GEN-END:variables

}
