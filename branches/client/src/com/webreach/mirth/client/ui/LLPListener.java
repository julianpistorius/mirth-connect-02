package com.webreach.mirth.client.ui;

import java.util.Properties;
import java.util.StringTokenizer;

public class LLPListener extends ConnectorClass
{
    Frame parent;
    /** Creates new form LLPListener */
    public LLPListener()
    {
        this.parent = PlatformUI.MIRTH_FRAME;
        name = "LLP Listener";
        initComponents();
    }

    public Properties getProperties()
    {
        Properties properties = new Properties();
        properties.put("DataType", name);
        String listenerIPAddress = listenerIPAddressField.getText() + "." + listenerIPAddressField1.getText() + "." + listenerIPAddressField2.getText() + "." + listenerIPAddressField3.getText();
        properties.put("address", listenerIPAddress);
        properties.put("port", listenerPortField.getText());
        properties.put("ReceiveTimeout", receiveTimeoutField.getText());
        properties.put("BufferSize", bufferSizeField.getText());

        if (keepConnectionOpenYesRadio.isSelected())
            properties.put("KeepConnectionOpen", "YES");
        else
            properties.put("KeepConnectionOpen", "NO");

        properties.put("StartOfMessageCharacter", startOfMessageCharacterField.getText());
        properties.put("EndOfMessageCharacter", endOfMessageCharacterField.getText());
        properties.put("FieldSeparator", fieldSeparatorField.getText());
        properties.put("RecordSeparator", recordSeparatorField.getText());
        properties.put("SendACK", sendACKCombobox.getSelectedItem());
        return properties;
    }

    public void setProperties(Properties props)
    {
        String listenerIPAddress = (String)props.get("address");
        StringTokenizer IP = new StringTokenizer(listenerIPAddress, ".");
        if (IP.hasMoreTokens())
            listenerIPAddressField.setText(IP.nextToken());
        else
            listenerIPAddressField.setText("");
        if (IP.hasMoreTokens())
            listenerIPAddressField1.setText(IP.nextToken());
        else
            listenerIPAddressField1.setText("");
        if (IP.hasMoreTokens())
            listenerIPAddressField2.setText(IP.nextToken());
        else
            listenerIPAddressField2.setText("");
        if (IP.hasMoreTokens())
            listenerIPAddressField3.setText(IP.nextToken());
        else
            listenerIPAddressField3.setText("");

        listenerPortField.setText((String)props.get("port"));
        receiveTimeoutField.setText((String)props.get("ReceiveTimeout"));
        bufferSizeField.setText((String)props.get("BufferSize"));

        if(((String)props.get("KeepConnectionOpen")).equals("YES"))
            keepConnectionOpenYesRadio.setSelected(true);
        else
            keepConnectionOpenNoRadio.setSelected(true);

        startOfMessageCharacterField.setText((String)props.get("StartOfMessageCharacter"));
        endOfMessageCharacterField.setText((String)props.get("EndOfMessageCharacter"));
        fieldSeparatorField.setText((String)props.get("FieldSeparator"));
        recordSeparatorField.setText((String)props.get("RecordSeparator"));
        boolean visible = parent.channelEditTasks.getContentPane().getComponent(0).isVisible();
        sendACKCombobox.setSelectedItem(props.get("SendACK"));
        parent.channelEditTasks.getContentPane().getComponent(0).setVisible(visible);
    }

    public void setDefaults()
    {
        listenerIPAddressField.setText("127");
        listenerIPAddressField1.setText("0");
        listenerIPAddressField2.setText("0");
        listenerIPAddressField3.setText("1");
        listenerPortField.setText("3600");
        receiveTimeoutField.setText("");
        bufferSizeField.setText("");
        keepConnectionOpenYesRadio.setSelected(true);
        startOfMessageCharacterField.setText("");
        endOfMessageCharacterField.setText("");
        fieldSeparatorField.setText("");
        recordSeparatorField.setText("");
        sendACKCombobox.setSelectedIndex(0);
    }
    
    public Properties getDefaults()
    {
        Properties properties = new Properties();
        properties.put("DataType", name);
        properties.put("address", "127.0.0.1");
        properties.put("port", "3600");
        properties.put("ReceiveTimeout", "");
        properties.put("BufferSize", "");
        properties.put("KeepConnectionOpen", "YES");
        properties.put("StartOfMessageCharacter", "");
        properties.put("EndOfMessageCharacter", "");
        properties.put("FieldSeparator", "");
        properties.put("RecordSeparator", "");
        properties.put("SendACK", sendACKCombobox.getItemAt(0));
        return properties;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        keepConnectionOpenGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        sendACKCombobox = new com.webreach.mirth.client.ui.MirthComboBox();
        bufferSizeField = new com.webreach.mirth.client.ui.MirthTextField();
        receiveTimeoutField = new com.webreach.mirth.client.ui.MirthTextField();
        listenerPortField = new com.webreach.mirth.client.ui.MirthTextField();
        recordSeparatorField = new com.webreach.mirth.client.ui.MirthTextField();
        startOfMessageCharacterField = new com.webreach.mirth.client.ui.MirthTextField();
        endOfMessageCharacterField = new com.webreach.mirth.client.ui.MirthTextField();
        fieldSeparatorField = new com.webreach.mirth.client.ui.MirthTextField();
        keepConnectionOpenYesRadio = new com.webreach.mirth.client.ui.MirthRadioButton();
        keepConnectionOpenNoRadio = new com.webreach.mirth.client.ui.MirthRadioButton();
        listenerIPAddressField3 = new com.webreach.mirth.client.ui.MirthTextField();
        jLabel25 = new javax.swing.JLabel();
        listenerIPAddressField2 = new com.webreach.mirth.client.ui.MirthTextField();
        jLabel26 = new javax.swing.JLabel();
        listenerIPAddressField1 = new com.webreach.mirth.client.ui.MirthTextField();
        jLabel9 = new javax.swing.JLabel();
        listenerIPAddressField = new com.webreach.mirth.client.ui.MirthTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "LLP Listener", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0)));
        jLabel1.setText("Listener IP Address:");

        jLabel2.setText("Listener Port:");

        jLabel3.setText("Receive Timeout (ms):");

        jLabel4.setText("Buffer Size:");

        jLabel5.setText("Keep Connection Open:");

        jLabel34.setText("Start of Message Character:");

        jLabel35.setText("End of Message Character:");

        jLabel36.setText("Record Sparator:");

        jLabel37.setText("Field Separator:");

        jLabel38.setText("Send ACK:");

        sendACKCombobox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Auto", "Yes", "No" }));

        keepConnectionOpenYesRadio.setBackground(new java.awt.Color(255, 255, 255));
        keepConnectionOpenYesRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        keepConnectionOpenGroup.add(keepConnectionOpenYesRadio);
        keepConnectionOpenYesRadio.setText("Yes");
        keepConnectionOpenYesRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

        keepConnectionOpenNoRadio.setBackground(new java.awt.Color(255, 255, 255));
        keepConnectionOpenNoRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        keepConnectionOpenGroup.add(keepConnectionOpenNoRadio);
        keepConnectionOpenNoRadio.setText("No");
        keepConnectionOpenNoRadio.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel25.setText(".");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel26.setText(".");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel9.setText(".");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel2)
                    .add(jLabel3)
                    .add(jLabel5)
                    .add(jLabel34)
                    .add(jLabel35)
                    .add(jLabel37)
                    .add(jLabel36)
                    .add(jLabel38)
                    .add(jLabel4)
                    .add(jLabel1))
                .add(15, 15, 15)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(keepConnectionOpenYesRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(keepConnectionOpenNoRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, receiveTimeoutField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, listenerPortField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, sendACKCombobox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, startOfMessageCharacterField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, endOfMessageCharacterField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, fieldSeparatorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, recordSeparatorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, bufferSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 131, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(listenerIPAddressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel9)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(listenerIPAddressField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel26)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(listenerIPAddressField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel25)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(listenerIPAddressField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel1)
                        .add(listenerIPAddressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(listenerIPAddressField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(listenerIPAddressField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(listenerIPAddressField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel9)
                    .add(jLabel26)
                    .add(jLabel25))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(listenerPortField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(receiveTimeoutField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .add(8, 8, 8)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(bufferSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(keepConnectionOpenYesRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(keepConnectionOpenNoRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(startOfMessageCharacterField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel34))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel35)
                    .add(endOfMessageCharacterField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel37)
                    .add(fieldSeparatorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel36)
                    .add(recordSeparatorField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(sendACKCombobox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel38))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.webreach.mirth.client.ui.MirthTextField bufferSizeField;
    private com.webreach.mirth.client.ui.MirthTextField endOfMessageCharacterField;
    private com.webreach.mirth.client.ui.MirthTextField fieldSeparatorField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.ButtonGroup keepConnectionOpenGroup;
    private com.webreach.mirth.client.ui.MirthRadioButton keepConnectionOpenNoRadio;
    private com.webreach.mirth.client.ui.MirthRadioButton keepConnectionOpenYesRadio;
    private com.webreach.mirth.client.ui.MirthTextField listenerIPAddressField;
    private com.webreach.mirth.client.ui.MirthTextField listenerIPAddressField1;
    private com.webreach.mirth.client.ui.MirthTextField listenerIPAddressField2;
    private com.webreach.mirth.client.ui.MirthTextField listenerIPAddressField3;
    private com.webreach.mirth.client.ui.MirthTextField listenerPortField;
    private com.webreach.mirth.client.ui.MirthTextField receiveTimeoutField;
    private com.webreach.mirth.client.ui.MirthTextField recordSeparatorField;
    private com.webreach.mirth.client.ui.MirthComboBox sendACKCombobox;
    private com.webreach.mirth.client.ui.MirthTextField startOfMessageCharacterField;
    // End of variables declaration//GEN-END:variables

}
