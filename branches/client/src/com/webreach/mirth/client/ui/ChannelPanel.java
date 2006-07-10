package com.webreach.mirth.client.ui;

import com.webreach.mirth.model.Channel;
import java.awt.Point;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Highlighter;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;

public class ChannelPanel extends javax.swing.JPanel {
    
    private JScrollPane channelPane;
    private JXTable channelTable;
    private Frame parent;
    
    /** Creates new form ChannelPanel */
    public ChannelPanel() {
        this.parent = PlatformUI.MIRTH_FRAME;
        initComponents();
    }
    
    public void initComponents() {
        channelPane = new JScrollPane();
        
        makeChannelTable();
        
        channelPane.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                showChannelPopupMenu(evt, false);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                showChannelPopupMenu(evt, false);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                deselectRows();
            }
        });
        
        channelPane.setComponentPopupMenu(parent.channelPopupMenu);
        
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(channelPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, channelTable.getWidth(), Short.MAX_VALUE)
                );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(channelPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, channelTable.getHeight(), Short.MAX_VALUE)
                );
    }
    
    public void makeChannelTable() {
        channelTable = new JXTable();
        
        Object[][] tableData = new Object[parent.channels.size()][3];
        
        for (int i=0; i < parent.channels.size(); i++)
        {
            Channel temp = parent.channels.get(i);
            
            if (temp.isEnabled())
                tableData[i][0] = new CellData(new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/bullet_blue.png")),"Enabled");
            else
                tableData[i][0] = new CellData(new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/bullet_black.png")),"Disabled");
            
            if (temp.getDirection().equals(Channel.Direction.INBOUND))
                tableData[i][1] = "Inbound";
            else
                tableData[i][1] = "Outbound";

            tableData[i][2] = temp.getName();
        }
                
        
        channelTable.setModel(new javax.swing.table.DefaultTableModel(
                tableData,
                new String []
        {
            "Status", "Direction", "Name"
        }
        ) {
            boolean[] canEdit = new boolean []
            {
                false, false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        channelTable.setSelectionMode(0);        
        
        channelTable.getColumnExt("Status").setMaxWidth(UIConstants.MAX_WIDTH);
        channelTable.getColumnExt("Direction").setMaxWidth(UIConstants.MAX_WIDTH);
        
        channelTable.getColumnExt("Status").setCellRenderer(new ImageCellRenderer());
        channelTable.packTable(UIConstants.COL_MARGIN);

        channelTable.setRowHeight(20);
        channelTable.setOpaque(true);
        channelTable.setRowSelectionAllowed(true);
        
        if(Preferences.systemNodeForPackage(Mirth.class).getBoolean("highlightRows", true))
        {
            HighlighterPipeline highlighter = new HighlighterPipeline();
            highlighter.addHighlighter(new AlternateRowHighlighter(UIConstants.HIGHLIGHTER_COLOR, UIConstants.BACKGROUND_COLOR, UIConstants.TITLE_TEXT_COLOR));
            channelTable.setHighlighters(highlighter);
        }
        
        channelPane.setViewportView(channelTable);
        
        channelTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent evt)
            {
                ChannelListSelected(evt);
            }
        });
        channelTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                showChannelPopupMenu(evt, true);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                showChannelPopupMenu(evt, true);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                if (evt.getClickCount() >= 2)
                    parent.doEditChannel();
            }
        });
    }
    
    private void showChannelPopupMenu(java.awt.event.MouseEvent evt, boolean onTable)
    {
        if (evt.isPopupTrigger())
        {
            if (onTable)
            {
                int row = channelTable.rowAtPoint(new Point(evt.getX(), evt.getY()));
                channelTable.setRowSelectionInterval(row, row);
            }
            else
                deselectRows();
            parent.channelPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }
    
    private void ChannelListSelected(ListSelectionEvent evt)
    {
        int row = channelTable.getSelectedRow();
        
        if(row >= 0)
        {
            parent.setVisibleTasks(parent.channelTasks, parent.channelPopupMenu, 4, -1, true);

            int columnNumber = getColumnNumber("Status");
            if (((CellData)channelTable.getValueAt(row, columnNumber)).getText().equals("Enabled"))
                parent.setVisibleTasks(parent.channelTasks, parent.channelPopupMenu, 7, 7, false);
            else
                parent.setVisibleTasks(parent.channelTasks, parent.channelPopupMenu, 8, 8, false);
        }
    }
    
    public void deselectRows()
    {
        channelTable.clearSelection();
        parent.setVisibleTasks(parent.channelTasks, parent.channelPopupMenu, 4, -1, false);
    }
    
    public int getSelectedChannel()
    {
        int columnNumber = getColumnNumber("Name");
        
        if (channelTable.getSelectedRow() != -1)
        {
            String channelName = (String) channelTable.getValueAt(channelTable.getSelectedRow(), columnNumber);
            for (int i=0; i < parent.channels.size(); i++)
            {
                if (parent.channels.get(i).getName().equals(channelName))
                    return i;
            }
        }
        return -1;
    }
    
    public boolean setSelectedChannel(String channelName)
    {
        int columnNumber = getColumnNumber("Name");
        for (int i = 0; i < parent.channels.size(); i++)
        {
            if (channelName.equals(channelTable.getValueAt(i, columnNumber)))
            {
                channelTable.setRowSelectionInterval(i,i);
                return true;
            }
        }
        return false;
    }
    
    private int getColumnNumber(String name)
    {
        for (int i = 0; i < channelTable.getColumnCount(); i++)
        {
            if (channelTable.getColumnName(i).equalsIgnoreCase(name))
                return i;
        }
        return -1;
    }
}