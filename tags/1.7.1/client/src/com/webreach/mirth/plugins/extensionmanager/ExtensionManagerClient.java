/*
 * PluginManagerClient.java
 *
 * Created on June 22, 2007, 5:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.webreach.mirth.plugins.extensionmanager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.ImageIcon;

import org.jdesktop.swingworker.SwingWorker;

import sun.misc.BASE64Encoder;

import com.webreach.mirth.client.core.ClientException;
import com.webreach.mirth.client.ui.PlatformUI;
import com.webreach.mirth.plugins.ClientPanelPlugin;

/**
 *
 * @author brendanh
 */
public class ExtensionManagerClient extends ClientPanelPlugin
{
    public ExtensionManagerClient(String name)
    {
        super(name);
        
        getTaskPane().setTitle("Manager Tasks");
        setComponent(new ExtensionManagerPanel(this));
        
        addTask("doRefresh", "Refresh", "Refresh loaded plugins.", "", new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/refresh.png")));
        addTask("doSave", "Save", "Save plugin settings.", "", new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/save.png")));
        addTask("doShowRepository", "Download Extensions", "Browse the public Mirth Extension Repository.", "", new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/database_gear.png")));
        addTask("doCheckAllForUpdates", "Check All for Updates", "Checks all extensions for updates.", "", new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/world_link.png")));
        
        addTask("doCheckForUpdates", "Check for Updates", "Checks for updates.", "", new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/plugin_link.png")));
        addTask("doEnable","Enable Extension","Enable the currently selected extension.","", new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/start.png")));
        addTask("doDisable","Disable Extension","Disable the currently selected extension.","", new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/stop.png")));
        addTask("doShowProperties","Show Properties","Display the currently selected extension properties.","", new ImageIcon(com.webreach.mirth.client.ui.Frame.class.getResource("images/application_view_list.png")));
        setVisibleTasks(0, 3, true);
        setVisibleTasks(4, -1, false);
        
        getComponent().addMouseListener(getPopupMenuMouseAdapter());
    }
    public void doShowProperties()
    {
        ((ExtensionManagerPanel) getComponent()).showExtensionProperties();
    }
    public void doCheckAllForUpdates()
    {
        try
        {
            new UpdateDialog(this);
        }
        catch (ClientException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void doCheckForUpdates()
    {
        try
        {
            new UpdateDialog(this, ((ExtensionManagerPanel) getComponent()).getSelectedExtension());
        }
        catch (ClientException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void doShowRepository()
    {
        try
        {
            new RepositoryDialog(this);
        }
        catch (ClientException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void doRefresh()
    {
        setWorking("Loading plugin settings...", true);
        
        SwingWorker worker = new SwingWorker<Void, Void>()
        {
            public Void doInBackground()
            {
                try
                {
                    refresh();
                }
                catch (ClientException e)
                {
                    alertException(e.getStackTrace(), e.getMessage());
                }
                return null;
            }
            
            public void done()
            {
                setWorking("", false);
            }
        };
        
        worker.execute();
    }
    
    public void doSave()
    {
        setWorking("Saving plugin settings...", true);
        
        SwingWorker worker = new SwingWorker<Void, Void>()
        {
            public Void doInBackground()
            {
                try
                {
                    save();
                    alertInformation("A restart is required before your changes will take effect.");
                }
                catch (ClientException e)
                {
                    alertException(e.getStackTrace(), e.getMessage());
                }
                return null;
            }
            
            public void done()
            {
                setWorking("", false);
            }
        };
        
        worker.execute();
    }
    
    public void doEnable()
    {
        ((ExtensionManagerPanel) getComponent()).enableExtension();
    }
    
    public void doDisable()
    {
        ((ExtensionManagerPanel) getComponent()).disableExtension();
    }
    
    public void refresh() throws ClientException
    {
        ((ExtensionManagerPanel) getComponent()).setPluginData(PlatformUI.MIRTH_FRAME.getPluginMetaData());
        ((ExtensionManagerPanel) getComponent()).setConnectorData(PlatformUI.MIRTH_FRAME.getConnectorMetaData());
    }
    
    public void save() throws ClientException
    {
        PlatformUI.MIRTH_FRAME.mirthClient.setPluginMetaData(((ExtensionManagerPanel) getComponent()).getPluginData());
        PlatformUI.MIRTH_FRAME.mirthClient.setConnectorMetaData(((ExtensionManagerPanel) getComponent()).getConnectorData());
    }
    
    public boolean install(String location, File file)
    {
    	try
        {
    		if (file.exists())
    		{
    			PlatformUI.MIRTH_FRAME.mirthClient.installExtension(location, file);
    		}
    		else
    		{
    			alertError("Invalid extension file.");
                return false;
    		}
        }
        catch(Exception e)
        {
            //alertError("Invalid extension file.");
            return false;
        }
        return true;
    }
    
    public void finishInstall()
    {
    	Properties props = null;
    	try
    	{
    		props = this.getPropertiesFromServer();
    	}
    	catch (ClientException e)
    	{
    		alertException(e.getStackTrace(), e.getMessage());
    	}
    	
		if (props != null && Boolean.parseBoolean(props.getProperty("disableInstall")))
		{
			alertInformation("Your extension(s) have been installed to the 'install_temp' directories in your extensions\n" +
					"location on the server.  To load the new plugins, manually shutdown the Mirth container\n" +
					"(e.g. JBoss), drag the plugins out of 'install_temp', and restart the Mirth container.\n");
		}
		else
		{
			alertInformation("The Mirth server must be restarted for the extension(s) to load.");
		}
    }
    
   
    // Returns the contents of the file in a byte array.
    private byte[] getBytesFromFile(File file) throws IOException
    {
        InputStream is = new FileInputStream(file);
        
        // Get the size of the file
        long length = file.length();
        
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE)
        {
            // File is too large
        }
        
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];
        
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
        {
            offset += numRead;
        }
        
        // Ensure all the bytes have been read in
        if (offset < bytes.length)
        {
            throw new IOException("Could not completely read file " + file.getName());
        }
        
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    
    public void start()
    {
        
    }
    
    public void stop()
    {

    }
    
    public void display()
    {
        try
        {
            refresh();
        }
        catch(ClientException e)
        {
            
        }
    }
}