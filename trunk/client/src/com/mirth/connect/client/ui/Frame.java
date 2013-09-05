/*
 * Copyright (c) Mirth Corporation. All rights reserved.
 * http://www.mirthcorp.com
 * 
 * The software in this package is published under the terms of the MPL
 * license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */

package com.mirth.connect.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.action.ActionFactory;
import org.jdesktop.swingx.action.ActionManager;
import org.jdesktop.swingx.action.BoundAction;
import org.jdesktop.swingx.painter.MattePainter;
import org.syntax.jedit.JEditTextArea;

import com.mirth.connect.client.core.Client;
import com.mirth.connect.client.core.ClientException;
import com.mirth.connect.client.core.RequestAbortedException;
import com.mirth.connect.client.core.TaskConstants;
import com.mirth.connect.client.core.UnauthorizedException;
import com.mirth.connect.client.core.UpdateClient;
import com.mirth.connect.client.ui.alert.AlertEditPanel;
import com.mirth.connect.client.ui.alert.AlertPanel;
import com.mirth.connect.client.ui.alert.DefaultAlertEditPanel;
import com.mirth.connect.client.ui.alert.DefaultAlertPanel;
import com.mirth.connect.client.ui.browsers.event.EventBrowser;
import com.mirth.connect.client.ui.browsers.message.MessageBrowser;
import com.mirth.connect.client.ui.extensionmanager.ExtensionManagerPanel;
import com.mirth.connect.client.ui.extensionmanager.ExtensionUpdateDialog;
import com.mirth.connect.client.ui.panels.reference.ReferenceListFactory;
import com.mirth.connect.donkey.model.channel.DeployedState;
import com.mirth.connect.donkey.model.channel.MetaDataColumn;
import com.mirth.connect.donkey.util.DonkeyElement;
import com.mirth.connect.donkey.util.DonkeyElement.DonkeyElementException;
import com.mirth.connect.model.Channel;
import com.mirth.connect.model.ChannelSummary;
import com.mirth.connect.model.CodeTemplate;
import com.mirth.connect.model.CodeTemplate.CodeSnippetType;
import com.mirth.connect.model.Connector;
import com.mirth.connect.model.Connector.Mode;
import com.mirth.connect.model.ConnectorMetaData;
import com.mirth.connect.model.DashboardStatus;
import com.mirth.connect.model.DashboardStatus.StatusType;
import com.mirth.connect.model.EncryptionSettings;
import com.mirth.connect.model.InvalidChannel;
import com.mirth.connect.model.PluginMetaData;
import com.mirth.connect.model.ServerSettings;
import com.mirth.connect.model.UpdateInfo;
import com.mirth.connect.model.UpdateSettings;
import com.mirth.connect.model.User;
import com.mirth.connect.model.alert.AlertModel;
import com.mirth.connect.model.alert.AlertStatus;
import com.mirth.connect.model.converters.ObjectXMLSerializer;
import com.mirth.connect.model.filters.MessageFilter;
import com.mirth.connect.model.util.ImportConverter3_0_0;
import com.mirth.connect.plugins.DashboardColumnPlugin;
import com.mirth.connect.plugins.DataTypeClientPlugin;
import com.mirth.connect.util.MigrationUtil;

/**
 * The main content frame for the Mirth Client Application. Extends JXFrame and
 * sets up all content.
 */
public class Frame extends JXFrame {

    private Logger logger = Logger.getLogger(this.getClass());
    public Client mirthClient;
    public DashboardPanel dashboardPanel = null;
    public ChannelPanel channelPanel = null;
    public SettingsPane settingsPane = null;
    public UserPanel userPanel = null;
    public ChannelSetup channelEditPanel = null;
    public EventBrowser eventBrowser = null;
    public MessageBrowser messageBrowser = null;
    public AlertPanel alertPanel = null;
    public AlertEditPanel alertEditPanel = null;
    public CodeTemplatePanel codeTemplatePanel = null;
    public GlobalScriptsPanel globalScriptsPanel = null;
    public ExtensionManagerPanel extensionsPanel = null;
    public JXTaskPaneContainer taskPaneContainer;
    public ChannelFilter channelFilter = null;
    public List<DashboardStatus> status = null;
    public Map<String, Channel> channels = null;
    public List<User> users = null;
    public List<CodeTemplate> codeTemplates = null;
    public ActionManager manager = ActionManager.getInstance();
    public JPanel contentPanel;
    public BorderLayout borderLayout1 = new BorderLayout();
    public StatusBar statusBar;
    public JSplitPane splitPane = new JSplitPane();
    public JScrollPane taskPane = new JScrollPane();
    public JScrollPane contentPane = new JScrollPane();
    public Component currentContentPage = null;
    public JXTaskPaneContainer currentTaskPaneContainer = null;
    public JScrollPane container;

    // Task panes and popup menus
    public JXTaskPane viewPane;
    public JXTaskPane otherPane;
    public JXTaskPane channelTasks;
    public JPopupMenu channelPopupMenu;
    public JXTaskPane dashboardTasks;
    public JPopupMenu dashboardPopupMenu;
    public JXTaskPane eventTasks;
    public JPopupMenu eventPopupMenu;
    public JXTaskPane messageTasks;
    public JPopupMenu messagePopupMenu;
    public JXTaskPane details;
    public JXTaskPane channelEditTasks;
    public JPopupMenu channelEditPopupMenu;
    public JXTaskPane userTasks;
    public JPopupMenu userPopupMenu;
    public JXTaskPane alertTasks;
    public JPopupMenu alertPopupMenu;
    public JXTaskPane alertEditTasks;
    public JPopupMenu alertEditPopupMenu;
    public JXTaskPane codeTemplateTasks;
    public JPopupMenu codeTemplatePopupMenu;
    public JXTaskPane globalScriptsTasks;
    public JPopupMenu globalScriptsPopupMenu;
    public JXTaskPane extensionsTasks;
    public JPopupMenu extensionsPopupMenu;

    public JXTitledPanel rightContainer;
    private ExecutorService statusUpdaterExecutor = Executors.newSingleThreadExecutor();
    private Future<?> statusUpdaterJob = null;
    public static Preferences userPreferences;
    private boolean connectionError;
    private ArrayList<CharsetEncodingInformation> availableCharsetEncodings = null;
    private List<String> charsetEncodings = null;
    private boolean isEditingChannel = false;
    private boolean isEditingAlert = false;
    private LinkedHashMap<String, String> workingStatuses = new LinkedHashMap<String, String>();
    public LinkedHashMap<String, String> dataTypeToDisplayName;
    public LinkedHashMap<String, String> displayNameToDataType;
    private Map<String, PluginMetaData> loadedPlugins;
    private Map<String, ConnectorMetaData> loadedConnectors;
    private UpdateClient updateClient = null;
    private boolean refreshingStatuses = false;
    private boolean queueRefreshStatus = false;
    private boolean refreshingAlerts = false;
    private boolean queueRefreshAlert = false;
    private Map<String, Integer> safeErrorFailCountMap = new HashMap<String, Integer>();
    private Map<Component, String> componentTaskMap = new HashMap<Component, String>();
    private boolean acceleratorKeyPressed = false;
    private Set<String> allChannelTags;
    private RemoveMessagesDialog removeMessagesDialog;
    private MessageExportDialog messageExportDialog;
    private MessageImportDialog messageImportDialog;

    public Frame() {
        rightContainer = new JXTitledPanel();
        channels = new HashMap<String, Channel>();

        taskPaneContainer = new JXTaskPaneContainer();

        setTitle(UIConstants.TITLE_TEXT + " - " + PlatformUI.SERVER_NAME);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/mirth_32_ico.png")).getImage());
        makePaneContainer();

        connectionError = false;

        this.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                if (channelEditPanel != null && channelEditPanel.filterPane != null) {
                    channelEditPanel.filterPane.resizePanes();
                }
                if (channelEditPanel != null && channelEditPanel.transformerPane != null) {
                    channelEditPanel.transformerPane.resizePanes();
                }
            }

            public void componentHidden(ComponentEvent e) {}

            public void componentShown(ComponentEvent e) {}

            public void componentMoved(ComponentEvent e) {}
        });

        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                if (logout(true)) {
                    System.exit(0);
                }
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                // Update the state of the accelerator key (CTRL on Windows)
                updateAcceleratorKeyPressed(e);
                return false;
            }
        });
    }

    /**
     * Prepares the list of the encodings. This method is called from the Frame
     * class.
     * 
     */
    public void setCharsetEncodings() {
        if (this.availableCharsetEncodings != null) {
            return;
        }
        try {
            this.charsetEncodings = this.mirthClient.getAvailableCharsetEncodings();
            this.availableCharsetEncodings = new ArrayList<CharsetEncodingInformation>();
            this.availableCharsetEncodings.add(new CharsetEncodingInformation(UIConstants.DEFAULT_ENCODING_OPTION, "Default"));
            for (int i = 0; i < charsetEncodings.size(); i++) {
                String canonical = charsetEncodings.get(i);
                this.availableCharsetEncodings.add(new CharsetEncodingInformation(canonical, canonical));
            }
        } catch (Exception e) {
            alertError(this, "Error getting the charset list:\n " + e);
        }
    }

    /**
     * Creates all the items in the combo box for the connectors.
     * 
     * This method is called from each connector.
     */
    public void setupCharsetEncodingForConnector(javax.swing.JComboBox charsetEncodingCombobox) {
        if (this.availableCharsetEncodings == null) {
            this.setCharsetEncodings();
        }
        if (this.availableCharsetEncodings == null) {
            logger.error("Error, the are no encodings detected.");
            return;
        }
        charsetEncodingCombobox.removeAllItems();
        for (int i = 0; i < this.availableCharsetEncodings.size(); i++) {
            charsetEncodingCombobox.addItem(this.availableCharsetEncodings.get(i));
        }
    }

    /**
     * Sets the combobox for the string previously selected. If the server can't
     * support the encoding, the default one is selected. This method is called
     * from each connector.
     */
    public void setPreviousSelectedEncodingForConnector(javax.swing.JComboBox charsetEncodingCombobox, String selectedCharset) {
        if (this.availableCharsetEncodings == null) {
            this.setCharsetEncodings();
        }
        if (this.availableCharsetEncodings == null) {
            logger.error("Error, there are no encodings detected.");
            return;
        }
        if ((selectedCharset == null) || (selectedCharset.equalsIgnoreCase(UIConstants.DEFAULT_ENCODING_OPTION))) {
            charsetEncodingCombobox.setSelectedIndex(0);
        } else if (this.charsetEncodings.contains(selectedCharset)) {
            int index = this.availableCharsetEncodings.indexOf(new CharsetEncodingInformation(selectedCharset, selectedCharset));
            if (index < 0) {
                logger.error("Synchronization lost in the list of the encoding characters.");
                index = 0;
            }
            charsetEncodingCombobox.setSelectedIndex(index);
        } else {
            alertInformation(this, "Sorry, the JVM of the server can't support the previously selected " + selectedCharset + " encoding. Please choose another one or install more encodings in the server.");
            charsetEncodingCombobox.setSelectedIndex(0);
        }
    }

    /**
     * Get the strings which identifies the encoding selected by the user.
     * 
     * This method is called from each connector.
     */
    public String getSelectedEncodingForConnector(javax.swing.JComboBox charsetEncodingCombobox) {
        try {
            return ((CharsetEncodingInformation) charsetEncodingCombobox.getSelectedItem()).getCanonicalName();
        } catch (Throwable t) {
            alertInformation(this, "Error " + t);
            return UIConstants.DEFAULT_ENCODING_OPTION;
        }
    }

    /**
     * Called to set up this main window frame.
     */
    public void setupFrame(Client mirthClient) {

        LoginPanel login = LoginPanel.getInstance();

        this.mirthClient = mirthClient;
        login.setStatus("Loading extensions...");
        loadExtensionMetaData();
        // Re-initialize the controller every time the frame is setup
        AuthorizationControllerFactory.getAuthorizationController().initialize();
        refreshCodeTemplates();
        initializeExtensions();

        // Load the data type/display name maps now that the extensions have been loaded.
        dataTypeToDisplayName = new LinkedHashMap<String, String>();
        displayNameToDataType = new LinkedHashMap<String, String>();
        for (Entry<String, DataTypeClientPlugin> entry : LoadedExtensions.getInstance().getDataTypePlugins().entrySet()) {
            dataTypeToDisplayName.put(entry.getKey(), entry.getValue().getDisplayName());
            displayNameToDataType.put(entry.getValue().getDisplayName(), entry.getKey());
        }

        setInitialVisibleTasks();
        login.setStatus("Loading preferences...");
        userPreferences = Preferences.userNodeForPackage(Mirth.class);
        userPreferences.put("defaultServer", PlatformUI.SERVER_NAME);
        login.setStatus("Loading GUI components...");
        splitPane.setDividerSize(0);
        splitPane.setBorder(BorderFactory.createEmptyBorder());

        contentPanel = (JPanel) getContentPane();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder());
        taskPane.setBorder(BorderFactory.createEmptyBorder());

        statusBar = new StatusBar();
        statusBar.setBorder(BorderFactory.createEmptyBorder());
        contentPane.setBorder(BorderFactory.createEmptyBorder());

        buildContentPanel(rightContainer, contentPane, false);

        // Set task pane container background painter
        MattePainter taskPanePainter = new MattePainter(new GradientPaint(0f, 0f, UIConstants.JX_CONTAINER_BACKGROUND_COLOR, 0f, 1f, UIConstants.JX_CONTAINER_BACKGROUND_COLOR));
        taskPanePainter.setPaintStretched(true);
        taskPaneContainer.setBackgroundPainter(taskPanePainter);

        // Set main content container title painter
        MattePainter contentTitlePainter = new MattePainter(new GradientPaint(0f, 0f, UIConstants.JX_CONTAINER_BACKGROUND_COLOR, 0f, 1f, UIConstants.JX_CONTAINER_BACKGROUND_COLOR));
        contentTitlePainter.setPaintStretched(true);
        rightContainer.setTitlePainter(contentTitlePainter);

        splitPane.add(rightContainer, JSplitPane.RIGHT);
        splitPane.add(taskPane, JSplitPane.LEFT);
        taskPane.setMinimumSize(new Dimension(UIConstants.TASK_PANE_WIDTH, 0));
        splitPane.setDividerLocation(UIConstants.TASK_PANE_WIDTH);

        contentPanel.add(statusBar, BorderLayout.SOUTH);
        contentPanel.add(splitPane, java.awt.BorderLayout.CENTER);

        try {
            PlatformUI.SERVER_ID = mirthClient.getServerId();
            PlatformUI.SERVER_VERSION = mirthClient.getVersion();
            PlatformUI.SERVER_TIMEZONE = mirthClient.getServerTimezone();

            setTitle(getTitle() + " - (" + PlatformUI.SERVER_VERSION + ")");

            PlatformUI.BUILD_DATE = mirthClient.getBuildDate();

            // Initialize ObjectXMLSerializer once we know the server version
            try {
                ObjectXMLSerializer.getInstance().init(PlatformUI.SERVER_VERSION);
            } catch (Exception e) {
            }
        } catch (ClientException e) {
            alertError(this, "Could not get server information.");
        }

        // Display the server timezone information
        statusBar.setTimezoneText(PlatformUI.SERVER_TIMEZONE);

        setCurrentTaskPaneContainer(taskPaneContainer);
        login.setStatus("Loading dashboard...");
        doShowDashboard();
        login.setStatus("Loading channel editor...");
        channelEditPanel = new ChannelSetup();
        login.setStatus("Loading alert editor...");
        if (alertEditPanel == null) {
            alertEditPanel = new DefaultAlertEditPanel();
        }
        login.setStatus("Loading message browser...");
        messageBrowser = new MessageBrowser();

        // DEBUGGING THE UIDefaults:

//         UIDefaults uiDefaults = UIManager.getDefaults(); Enumeration enum1 =
//         uiDefaults.keys(); while (enum1.hasMoreElements()) { Object key =
//         enum1.nextElement(); Object val = uiDefaults.get(key);
////         if(key.toString().indexOf("ComboBox") != -1)
//         System.out.println("UIManager.put(\"" + key.toString() + "\",\"" +
//         (null != val ? val.toString() : "(null)") + "\");"); }

    }

    private void loadExtensionMetaData() {
        try {
            loadedPlugins = mirthClient.getPluginMetaData();
            loadedConnectors = mirthClient.getConnectorMetaData();
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), "Unable to load extensions");
        }
    }

    private void initializeExtensions() {
        // Initialize all of the extensions now that the metadata has been retrieved
        LoadedExtensions.getInstance().initialize();
        LoadedExtensions.getInstance().startPlugins();
    }

    /**
     * Builds the content panel with a title bar and settings.
     */
    private void buildContentPanel(JXTitledPanel container, JScrollPane component, boolean opaque) {
        container.getContentContainer().setLayout(new BorderLayout());
        container.setBorder(null);
        container.setTitleFont(new Font("Tahoma", Font.BOLD, 18));
        container.setTitleForeground(UIConstants.HEADER_TITLE_TEXT_COLOR);
        JLabel mirthConnectImage = new JLabel();
        mirthConnectImage.setIcon(UIConstants.MIRTHCONNECT_LOGO_GRAY);
        mirthConnectImage.setText(" ");
        mirthConnectImage.setToolTipText(UIConstants.MIRTHCONNECT_TOOLTIP);
        mirthConnectImage.setCursor(new Cursor(Cursor.HAND_CURSOR));

        mirthConnectImage.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BareBonesBrowserLaunch.openURL(UIConstants.MIRTHCONNECT_URL);
            }
        });

        ((JPanel) container.getComponent(0)).add(mirthConnectImage);

        component.setBorder(new LineBorder(Color.GRAY, 1));
        component.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        component.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        container.getContentContainer().add(component);
    }

    /**
     * Set the main content panel title to a String
     */
    public void setPanelName(String name) {
        rightContainer.setTitle(name);
        statusBar.setStatusText("");
    }

    public String startWorking(final String displayText) {
        String id = null;

        synchronized (workingStatuses) {
            if (statusBar != null) {
                id = UUID.randomUUID().toString();
                workingStatuses.put(id, displayText);
                statusBar.setWorking(true);
                statusBar.setText(displayText);
            }
        }

        return id;
    }

    public void stopWorking(final String workingId) {
        synchronized (workingStatuses) {
            if ((statusBar != null) && (workingId != null)) {
                workingStatuses.remove(workingId);

                if (workingStatuses.size() > 0) {
                    statusBar.setWorking(true);
                    statusBar.setText(new LinkedList<String>(workingStatuses.values()).getLast());
                } else {
                    statusBar.setWorking(false);
                    statusBar.setText("");
                }
            }
        }
    }

    /**
     * Changes the current content page to the Channel Editor with the new
     * channel specified as the loaded one.
     */
    public void setupChannel(Channel channel) {
        setBold(viewPane, UIConstants.ERROR_CONSTANT);
        setCurrentContentPage(channelEditPanel);
        setFocus(channelEditTasks);
        setVisibleTasks(channelEditTasks, channelEditPopupMenu, 0, 0, false);
        channelEditPanel.addChannel(channel);
    }

    /**
     * Edits a channel at a specified index, setting that channel as the current
     * channel in the editor.
     */
    public void editChannel(Channel channel) {
        setBold(viewPane, UIConstants.ERROR_CONSTANT);
        setCurrentContentPage(channelEditPanel);
        setFocus(channelEditTasks);
        setVisibleTasks(channelEditTasks, channelEditPopupMenu, 0, 4, false);
        channelEditPanel.editChannel(channel);
    }

    /**
     * Changes the current content page to the Alert Editor with the new
     * alert specified as the loaded one.
     */
    public void setupAlert() {
        setBold(viewPane, UIConstants.ERROR_CONSTANT);
        setCurrentContentPage(alertEditPanel);
        setFocus(alertEditTasks);
        setVisibleTasks(alertEditTasks, alertEditPopupMenu, 0, 0, false);
        alertEditPanel.addAlert();
    }

    /**
     * Edits an alert at a specified index, setting that alert as the current
     * alert in the editor.
     */
    public void editAlert(AlertModel alertModel) {
        if (alertEditPanel.editAlert(alertModel)) {
            setBold(viewPane, UIConstants.ERROR_CONSTANT);
            setCurrentContentPage(alertEditPanel);
            setFocus(alertEditTasks);
            setVisibleTasks(alertEditTasks, alertEditPopupMenu, 0, 0, false);
        }
    }

    /**
     * Edit global scripts
     */
    public void editGlobalScripts() {
        setBold(viewPane, UIConstants.ERROR_CONSTANT);
        setCurrentContentPage(globalScriptsPanel);
        setFocus(globalScriptsTasks);
        setVisibleTasks(globalScriptsTasks, globalScriptsPopupMenu, 0, 0, false);
        setPanelName("Global Scripts");
    }

    /**
     * Sets the current content page to the passed in page.
     */
    public void setCurrentContentPage(Component contentPageObject) {
        if (contentPageObject == currentContentPage) {
            return;
        }

        if (currentContentPage != null) {
            contentPane.getViewport().remove(currentContentPage);
        }

        contentPane.getViewport().add(contentPageObject);
        currentContentPage = contentPageObject;

        // Always cancel the current job if it is still running.
        if (statusUpdaterJob != null && !statusUpdaterJob.isDone()) {
            statusUpdaterJob.cancel(true);
        }

        // Start a new status updater job if the current content page is the dashboard
        if (currentContentPage == dashboardPanel || currentContentPage == alertPanel) {
            statusUpdaterJob = statusUpdaterExecutor.submit(new StatusUpdater());
        }
    }

    /**
     * Sets the current task pane container
     */
    private void setCurrentTaskPaneContainer(JXTaskPaneContainer container) {
        if (container == currentTaskPaneContainer) {
            return;
        }

        if (currentTaskPaneContainer != null) {
            taskPane.getViewport().remove(currentTaskPaneContainer);
        }

        taskPane.getViewport().add(container);
        currentTaskPaneContainer = container;
    }

    /**
     * Makes all of the task panes and shows the dashboard panel.
     */
    private void makePaneContainer() {
        createViewPane();
        createChannelPane();
        createChannelEditPane();
        createDashboardPane();
        createEventPane();
        createMessagePane();
        createUserPane();
        createAlertPane();
        createAlertEditPane();
        createGlobalScriptsPane();
        createCodeTemplatePane();
        createExtensionsPane();
        createOtherPane();
    }

    private void setInitialVisibleTasks() {
        // View Pane
        setVisibleTasks(viewPane, null, 0, -1, true);

        // Alert Pane
        setVisibleTasks(alertTasks, alertPopupMenu, 0, -1, true);
        setVisibleTasks(alertTasks, alertPopupMenu, 4, -1, false);

        // Alert Edit Pane
        setVisibleTasks(alertEditTasks, alertEditPopupMenu, 0, 0, false);
        setVisibleTasks(alertEditTasks, alertEditPopupMenu, 1, 1, true);

        // Channel Pane
        setVisibleTasks(channelTasks, channelPopupMenu, 0, -1, true);
        setVisibleTasks(channelTasks, channelPopupMenu, 1, 2, false);
        setVisibleTasks(channelTasks, channelPopupMenu, 7, -1, false);

        // Channel Edit Pane
        setVisibleTasks(channelEditTasks, channelEditPopupMenu, 0, 15, false);
        setVisibleTasks(channelEditTasks, channelEditPopupMenu, 14, 14, true);

        // Dashboard Pane
        setVisibleTasks(dashboardTasks, dashboardPopupMenu, 0, 0, true);
        setVisibleTasks(dashboardTasks, dashboardPopupMenu, 1, -1, false);

        // Event Pane
        setVisibleTasks(eventTasks, eventPopupMenu, 0, 2, true);

        // Message Pane
        setVisibleTasks(messageTasks, messagePopupMenu, 0, -1, true);
        setVisibleTasks(messageTasks, messagePopupMenu, 6, -1, false);
        setVisibleTasks(messageTasks, messagePopupMenu, 7, 7, true);

        // User Pane
        setVisibleTasks(userTasks, userPopupMenu, 0, 1, true);
        setVisibleTasks(userTasks, userPopupMenu, 2, -1, false);

        // Code Template Pane
        setVisibleTasks(codeTemplateTasks, codeTemplatePopupMenu, 0, 1, false);
        setVisibleTasks(codeTemplateTasks, codeTemplatePopupMenu, 2, 4, true);
        setVisibleTasks(codeTemplateTasks, codeTemplatePopupMenu, 5, 6, false);

        // Global Scripts Pane
        setVisibleTasks(globalScriptsTasks, globalScriptsPopupMenu, 0, 0, false);
        setVisibleTasks(globalScriptsTasks, globalScriptsPopupMenu, 1, -1, true);

        // Extensions Pane
        setVisibleTasks(extensionsTasks, extensionsPopupMenu, 0, 0, true);
        setVisibleTasks(extensionsTasks, extensionsPopupMenu, 1, 1, true);
        setVisibleTasks(extensionsTasks, extensionsPopupMenu, 2, -1, false);

        // Other Pane
        setVisibleTasks(otherPane, null, 0, -1, true);
    }

    /**
     * Creates the view task pane.
     */
    private void createViewPane() {
        // Create View pane
        viewPane = new JXTaskPane();
        viewPane.setTitle("Mirth Connect");
        viewPane.setName(TaskConstants.VIEW_KEY);
        viewPane.setFocusable(false);

        addTask(TaskConstants.VIEW_DASHBOARD, "Dashboard", "Contains information about your currently deployed channels.", "D", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/application_view_detail.png")), viewPane, null);
        addTask(TaskConstants.VIEW_CHANNEL, "Channels", "Contains various operations to perform on your channels.", "C", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/application_form.png")), viewPane, null);
        addTask(TaskConstants.VIEW_USERS, "Users", "Contains information on users.", "U", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/group.png")), viewPane, null);
        addTask(TaskConstants.VIEW_SETTINGS, "Settings", "Contains local and system settings.", "S", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/wrench.png")), viewPane, null);
        addTask(TaskConstants.VIEW_ALERTS, "Alerts", "Contains alert settings.", "A", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/error.png")), viewPane, null);
        addTask(TaskConstants.VIEW_EVENTS, "Events", "Show the event logs for the system.", "E", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/table.png")), viewPane, null);
        addTask(TaskConstants.VIEW_EXTENSIONS, "Extensions", "View and manage Mirth Connect extensions", "X", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/plugin.png")), viewPane, null);

        setNonFocusable(viewPane);
        taskPaneContainer.add(viewPane);
        viewPane.setVisible(true);
    }

    /**
     * Creates the template task pane.
     */
    private void createAlertPane() {
        // Create Alert Edit Tasks Pane
        alertTasks = new JXTaskPane();
        alertPopupMenu = new JPopupMenu();
        alertTasks.setTitle("Alert Tasks");
        alertTasks.setName(TaskConstants.ALERT_KEY);
        alertTasks.setFocusable(false);

        addTask(TaskConstants.ALERT_REFRESH, "Refresh", "Refresh the list of alerts.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_refresh.png")), alertTasks, alertPopupMenu);
        addTask(TaskConstants.ALERT_NEW, "New Alert", "Create a new alert.", "N", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/error_add.png")), alertTasks, alertPopupMenu);
        addTask(TaskConstants.ALERT_IMPORT, "Import Alert", "Import an alert from an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_go.png")), alertTasks, alertPopupMenu);
        addTask(TaskConstants.ALERT_EXPORT_ALL, "Export All Alerts", "Export all of the alerts to an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_disk.png")), alertTasks, alertPopupMenu);
        addTask(TaskConstants.ALERT_EXPORT, "Export Alert", "Export the currently selected alert to an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_disk.png")), alertTasks, alertPopupMenu);
        addTask(TaskConstants.ALERT_DELETE, "Delete Alert", "Delete the currently selected alert.", "L", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/error_delete.png")), alertTasks, alertPopupMenu);
        addTask(TaskConstants.ALERT_EDIT, "Edit Alert", "Edit the currently selected alert.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/application_form_edit.png")), alertTasks, alertPopupMenu);
        addTask(TaskConstants.ALERT_ENABLE, "Enable Alert", "Enable the currently selected alert.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_play_blue.png")), alertTasks, alertPopupMenu);
        addTask(TaskConstants.ALERT_DISABLE, "Disable Alert", "Disable the currently selected alert.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_stop_blue.png")), alertTasks, alertPopupMenu);

        setNonFocusable(alertTasks);
        taskPaneContainer.add(alertTasks);
    }

    /**
     * Creates the template task pane.
     */
    private void createAlertEditPane() {
        // Create Alert Edit Tasks Pane
        alertEditTasks = new JXTaskPane();
        alertEditPopupMenu = new JPopupMenu();
        alertEditTasks.setTitle("Alert Edit Tasks");
        alertEditTasks.setName(TaskConstants.ALERT_EDIT_KEY);
        alertEditTasks.setFocusable(false);

        addTask(TaskConstants.ALERT_EDIT_SAVE, "Save Alert", "Save all changes made to this alert.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/disk.png")), alertEditTasks, alertEditPopupMenu);
        addTask(TaskConstants.ALERT_EDIT_EXPORT, "Export Alert", "Export the currently selected alert to an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_disk.png")), alertEditTasks, alertEditPopupMenu);

        setNonFocusable(alertEditTasks);
        taskPaneContainer.add(alertEditTasks);
    }

    /**
     * Creates the channel task pane.
     */
    private void createChannelPane() {
        // Create Channel Tasks Pane
        channelTasks = new JXTaskPane();
        channelPopupMenu = new JPopupMenu();
        channelTasks.setTitle("Channel Tasks");
        channelTasks.setName(TaskConstants.CHANNEL_KEY);
        channelTasks.setFocusable(false);

        addTask(TaskConstants.CHANNEL_REFRESH, "Refresh", "Refresh the list of channels.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_refresh.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_REDEPLOY_ALL, "Redeploy All", "Undeploy all channels and deploy all currently enabled channels.", "A", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_rotate_clockwise.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_DEPLOY, "Deploy Channel", "Deploys the currently selected channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_redo.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_GLOBAL_SCRIPTS, "Edit Global Scripts", "Edit scripts that are not channel specific.", "G", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/script_edit.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_CODE_TEMPLATES, "Edit Code Templates", "Create and manage templates to be used in JavaScript throughout Mirth.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/page_edit.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_NEW, "New Channel", "Create a new channel.", "N", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/application_form_add.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_IMPORT, "Import Channel", "Import a channel from an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_go.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_EXPORT_ALL, "Export All Channels", "Export all of the channels to XML files.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_disk.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_EXPORT, "Export Channel", "Export the currently selected channel to an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_disk.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_DELETE, "Delete Channel", "Delete the currently selected channel.", "L", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/application_form_delete.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_CLONE, "Clone Channel", "Clone the currently selected channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/page_copy.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT, "Edit Channel", "Edit the currently selected channel.", "I", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/application_form_edit.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_ENABLE, "Enable Channel", "Enable the currently selected channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_play_blue.png")), channelTasks, channelPopupMenu);
        addTask(TaskConstants.CHANNEL_DISABLE, "Disable Channel", "Disable the currently selected channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_stop_blue.png")), channelTasks, channelPopupMenu);

        setNonFocusable(channelTasks);
        taskPaneContainer.add(channelTasks);
    }

    /**
     * Creates the channel edit task pane.
     */
    private void createChannelEditPane() {
        // Create Channel Edit Tasks Pane
        channelEditTasks = new JXTaskPane();
        channelEditPopupMenu = new JPopupMenu();
        channelEditTasks.setTitle("Channel Tasks");
        channelEditTasks.setName(TaskConstants.CHANNEL_EDIT_KEY);
        channelEditTasks.setFocusable(false);

        addTask(TaskConstants.CHANNEL_EDIT_SAVE, "Save Changes", "Save all changes made to this channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/disk.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_VALIDATE, "Validate Connector", "Validate the currently visible connector.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/accept.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_NEW_DESTINATION, "New Destination", "Create a new destination.", "N", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/add.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_DELETE_DESTINATION, "Delete Destination", "Delete the currently selected destination.", "L", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/delete.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_CLONE_DESTINATION, "Clone Destination", "Clones the currently selected destination.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/page_copy.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_ENABLE_DESTINATION, "Enable Destination", "Enable the currently selected destination.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_play_blue.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_DISABLE_DESTINATION, "Disable Destination", "Disable the currently selected destination.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_stop_blue.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_MOVE_DESTINATION_UP, "Move Dest. Up", "Move the currently selected destination up.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_up.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_MOVE_DESTINATION_DOWN, "Move Dest. Down", "Move the currently selected destination down.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_down.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_FILTER, UIConstants.EDIT_FILTER, "Edit the filter for the currently selected destination.", "F", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/table_edit.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_TRANSFORMER, UIConstants.EDIT_TRANSFORMER, "Edit the transformer for the currently selected destination.", "T", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/table_edit.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_RESPONSE_TRANSFORMER, UIConstants.EDIT_RESPONSE_TRANSFORMER, "Edit the response transformer for the currently selected destination.", "R", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/table_edit.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_IMPORT_CONNECTOR, "Import Connector", "Import the currently displayed connector from an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_go.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_EXPORT_CONNECTOR, "Export Connector", "Export the currently displayed connector to an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_disk.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_EXPORT, "Export Channel", "Export the currently selected channel to an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_disk.png")), channelEditTasks, channelEditPopupMenu);
        addTask(TaskConstants.CHANNEL_EDIT_VALIDATE_SCRIPT, "Validate Script", "Validate the currently viewed script.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/accept.png")), channelEditTasks, channelEditPopupMenu);

        setNonFocusable(channelEditTasks);
        taskPaneContainer.add(channelEditTasks);
    }

    /**
     * Creates the status task pane.
     */
    private void createDashboardPane() {
        // Create Status Tasks Pane
        dashboardTasks = new JXTaskPane();
        dashboardPopupMenu = new JPopupMenu();
        dashboardTasks.setTitle("Dashboard Tasks");
        dashboardTasks.setName(TaskConstants.DASHBOARD_KEY);
        dashboardTasks.setFocusable(false);

        addTask(TaskConstants.DASHBOARD_REFRESH, "Refresh", "Refresh the list of statuses.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_refresh.png")), dashboardTasks, dashboardPopupMenu);

        addTask(TaskConstants.DASHBOARD_SEND_MESSAGE, "Send Message", "Send messages to the currently selected channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/email_go.png")), dashboardTasks, dashboardPopupMenu);
        addTask(TaskConstants.DASHBOARD_SHOW_MESSAGES, "View Messages", "Show the messages for the currently selected channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/page_white_stack.png")), dashboardTasks, dashboardPopupMenu);
        addTask(TaskConstants.DASHBOARD_REMOVE_ALL_MESSAGES, "Remove All Messages", "Remove all Messages in this channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/email_delete.png")), dashboardTasks, dashboardPopupMenu);
        addTask(TaskConstants.DASHBOARD_CLEAR_STATS, "Clear Statistics", "Reset the statistics for this channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/chart_bar_delete.png")), dashboardTasks, dashboardPopupMenu);

        addTask(TaskConstants.DASHBOARD_START, "Start", "Start the currently selected channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_play_blue.png")), dashboardTasks, dashboardPopupMenu);
        addTask(TaskConstants.DASHBOARD_PAUSE, "Pause", "Pause the currently selected channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_pause_blue.png")), dashboardTasks, dashboardPopupMenu);
        addTask(TaskConstants.DASHBOARD_STOP, "Stop", "Stop the currently selected channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_stop_blue.png")), dashboardTasks, dashboardPopupMenu);
        addTask(TaskConstants.DASHBOARD_HALT, "Halt", "Halt the currently selected channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/stop.png")), dashboardTasks, dashboardPopupMenu);

        addTask(TaskConstants.DASHBOARD_UNDEPLOY, "Undeploy Channel", "Undeploys the currently selected channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_undo.png")), dashboardTasks, dashboardPopupMenu);

        addTask(TaskConstants.DASHBOARD_START_CONNECTOR, "Start", "Start the currently selected connector.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_play_blue.png")), dashboardTasks, dashboardPopupMenu);
        addTask(TaskConstants.DASHBOARD_STOP_CONNECTOR, "Stop", "Stop the currently selected connector.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_stop_blue.png")), dashboardTasks, dashboardPopupMenu);

        setNonFocusable(dashboardTasks);
        taskPaneContainer.add(dashboardTasks);
    }

    /**
     * Creates the event task pane.
     */
    private void createEventPane() {
        // Create Event Tasks Pane
        eventTasks = new JXTaskPane();
        eventPopupMenu = new JPopupMenu();
        eventTasks.setTitle("Event Tasks");
        eventTasks.setName(TaskConstants.EVENT_KEY);
        eventTasks.setFocusable(false);

        addTask(TaskConstants.EVENT_REFRESH, "Refresh", "Refresh the list of events with the given filter.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_refresh.png")), eventTasks, eventPopupMenu);
        addTask(TaskConstants.EVENT_EXPORT_ALL, "Export All Events", "Export all events to a file on the server.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_disk.png")), eventTasks, eventPopupMenu);
        addTask(TaskConstants.EVENT_REMOVE_ALL, "Remove All Events", "Remove all events and optionally export them to a file on the server.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/table_delete.png")), eventTasks, eventPopupMenu);

        setNonFocusable(eventTasks);
        taskPaneContainer.add(eventTasks);
    }

    /**
     * Creates the message task pane.
     */
    private void createMessagePane() {
        // Create Message Tasks Pane
        messageTasks = new JXTaskPane();
        messagePopupMenu = new JPopupMenu();
        messageTasks.setTitle("Message Tasks");
        messageTasks.setName(TaskConstants.MESSAGE_KEY);
        messageTasks.setFocusable(false);

        addTask(TaskConstants.MESSAGE_REFRESH, "Refresh", "Refresh the list of messages with the current search criteria.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_refresh.png")), messageTasks, messagePopupMenu);
        addTask(TaskConstants.MESSAGE_SEND, "Send Message", "Send a message to the channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/email_go.png")), messageTasks, messagePopupMenu);
        addTask(TaskConstants.MESSAGE_IMPORT, "Import Messages", "Import messages from a file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_go.png")), messageTasks, messagePopupMenu);
        addTask(TaskConstants.MESSAGE_EXPORT, "Export Results", "Export all messages in the current search.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_disk.png")), messageTasks, messagePopupMenu);
        addTask(TaskConstants.MESSAGE_REMOVE_ALL, "Remove All Messages", "Remove all messages in this channel.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/email_delete.png")), messageTasks, messagePopupMenu);
        addTask(TaskConstants.MESSAGE_REMOVE_FILTERED, "Remove Results", "Remove all messages in the current search.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/email_delete.png")), messageTasks, messagePopupMenu);
        addTask(TaskConstants.MESSAGE_REMOVE, "Remove Message", "Remove the selected Message.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/delete.png")), messageTasks, messagePopupMenu);
        addTask(TaskConstants.MESSAGE_REPROCESS_FILTERED, "Reprocess Results", "Reprocess all messages in the current search.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_rotate_clockwise.png")), messageTasks, messagePopupMenu);
        addTask(TaskConstants.MESSAGE_REPROCESS, "Reprocess Message", "Reprocess the selected message.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_redo.png")), messageTasks, messagePopupMenu);
        addTask(TaskConstants.MESSAGE_VIEW_IMAGE, "View Attachment", "View Attachment", "View the attachment for the selected message.", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/attach.png")), messageTasks, messagePopupMenu);

        setNonFocusable(messageTasks);
        taskPaneContainer.add(messageTasks);
    }

    /**
     * Creates the users task pane.
     */
    private void createUserPane() {
        // Create User Tasks Pane
        userTasks = new JXTaskPane();
        userPopupMenu = new JPopupMenu();
        userTasks.setTitle("User Tasks");
        userTasks.setName(TaskConstants.USER_KEY);
        userTasks.setFocusable(false);

        addTask(TaskConstants.USER_REFRESH, "Refresh", "Refresh the list of users.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_refresh.png")), userTasks, userPopupMenu);
        addTask(TaskConstants.USER_NEW, "New User", "Create a new user.", "N", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/user_add.png")), userTasks, userPopupMenu);
        addTask(TaskConstants.USER_EDIT, "Edit User", "Edit the currently selected user.", "I", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/user_edit.png")), userTasks, userPopupMenu);
        addTask(TaskConstants.USER_DELETE, "Delete User", "Delete the currently selected user.", "L", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/user_delete.png")), userTasks, userPopupMenu);

        setNonFocusable(userTasks);
        taskPaneContainer.add(userTasks);
    }

    /**
     * Creates the codeTemplate task pane.
     */
    private void createCodeTemplatePane() {
        // Create CodeTemplate Edit Tasks Pane
        codeTemplateTasks = new JXTaskPane();
        codeTemplatePopupMenu = new JPopupMenu();
        codeTemplateTasks.setTitle("Code Template Tasks");
        codeTemplateTasks.setName(TaskConstants.CODE_TEMPLATE_KEY);
        codeTemplateTasks.setFocusable(false);

        addTask(TaskConstants.CODE_TEMPLATE_REFRESH, "Refresh", "Refresh the list of code templates.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_refresh.png")), codeTemplateTasks, codeTemplatePopupMenu);
        addTask(TaskConstants.CODE_TEMPLATE_SAVE, "Save CodeTemplates", "Save all changes made to all code templates.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/disk.png")), codeTemplateTasks, codeTemplatePopupMenu);
        addTask(TaskConstants.CODE_TEMPLATE_NEW, "New CodeTemplate", "Create a new code template.", "N", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/add.png")), codeTemplateTasks, codeTemplatePopupMenu);
        addTask(TaskConstants.CODE_TEMPLATE_IMPORT, "Import Code Templates", "Import list of code templates from an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_go.png")), codeTemplateTasks, codeTemplatePopupMenu);
        addTask(TaskConstants.CODE_TEMPLATE_EXPORT, "Export Code Templates", "Export the list of code templates to an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_disk.png")), codeTemplateTasks, codeTemplatePopupMenu);
        addTask(TaskConstants.CODE_TEMPLATE_DELETE, "Delete CodeTemplate", "Delete the currently selected code template.", "L", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/delete.png")), codeTemplateTasks, codeTemplatePopupMenu);
        addTask(TaskConstants.CODE_TEMPLATE_VALIDATE, "Validate Script", "Validate the currently viewed script.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/accept.png")), codeTemplateTasks, codeTemplatePopupMenu);

        setNonFocusable(codeTemplateTasks);
        taskPaneContainer.add(codeTemplateTasks);
    }

    /**
     * Creates the global scripts edit task pane.
     */
    private void createGlobalScriptsPane() {
        globalScriptsTasks = new JXTaskPane();
        globalScriptsPopupMenu = new JPopupMenu();
        globalScriptsTasks.setTitle("Script Tasks");
        globalScriptsTasks.setName(TaskConstants.GLOBAL_SCRIPT_KEY);
        globalScriptsTasks.setFocusable(false);

        addTask(TaskConstants.GLOBAL_SCRIPT_SAVE, "Save Scripts", "Save all changes made to all scripts.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/disk.png")), globalScriptsTasks, globalScriptsPopupMenu);
        addTask(TaskConstants.GLOBAL_SCRIPT_VALIDATE, "Validate Script", "Validate the currently viewed script.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/accept.png")), globalScriptsTasks, globalScriptsPopupMenu);
        addTask(TaskConstants.GLOBAL_SCRIPT_IMPORT, "Import Scripts", "Import all global scripts from an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_go.png")), globalScriptsTasks, globalScriptsPopupMenu);
        addTask(TaskConstants.GLOBAL_SCRIPT_EXPORT, "Export Scripts", "Export all global scripts to an XML file.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/report_disk.png")), globalScriptsTasks, globalScriptsPopupMenu);

        setNonFocusable(globalScriptsTasks);
        taskPaneContainer.add(globalScriptsTasks);
    }

    /**
     * Creates the extensions task pane.
     */
    private void createExtensionsPane() {
        extensionsTasks = new JXTaskPane();
        extensionsPopupMenu = new JPopupMenu();
        extensionsTasks.setTitle("Extension Tasks");
        extensionsTasks.setName(TaskConstants.EXTENSIONS_KEY);
        extensionsTasks.setFocusable(false);

        addTask(TaskConstants.EXTENSIONS_REFRESH, "Refresh", "Refresh loaded plugins.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/arrow_refresh.png")), extensionsTasks, extensionsPopupMenu);
        addTask(TaskConstants.EXTENSIONS_CHECK_FOR_UPDATES, "Check for Updates", "Checks all extensions for updates.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/world_link.png")), extensionsTasks, extensionsPopupMenu);
        addTask(TaskConstants.EXTENSIONS_ENABLE, "Enable Extension", "Enable the currently selected extension.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_play_blue.png")), extensionsTasks, extensionsPopupMenu);
        addTask(TaskConstants.EXTENSIONS_DISABLE, "Disable Extension", "Disable the currently selected extension.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/control_stop_blue.png")), extensionsTasks, extensionsPopupMenu);
        addTask(TaskConstants.EXTENSIONS_SHOW_PROPERTIES, "Show Properties", "Display the currently selected extension properties.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/application_view_list.png")), extensionsTasks, extensionsPopupMenu);
        addTask(TaskConstants.EXTENSIONS_UNINSTALL, "Uninstall Extension", "Uninstall the currently selected extension", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/plugin_delete.png")), extensionsTasks, extensionsPopupMenu);

        setNonFocusable(extensionsTasks);
        taskPaneContainer.add(extensionsTasks);
    }

    /**
     * Creates the other task pane.
     */
    private void createOtherPane() {
        // Create Other Pane
        otherPane = new JXTaskPane();
        otherPane.setTitle("Other");
        otherPane.setName(TaskConstants.OTHER_KEY);
        otherPane.setFocusable(false);
        addTask(TaskConstants.OTHER_HELP, "Help", "View the Mirth Connect wiki.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/help.png")), otherPane, null);
        addTask(TaskConstants.OTHER_ABOUT, "About Mirth Connect", "View the about page for Mirth Connect.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/information.png")), otherPane, null);
        addTask(TaskConstants.OTHER_VISIT_MIRTH, "Visit mirthcorp.com", "View Mirth's homepage.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/house.png")), otherPane, null);
        addTask(TaskConstants.OTHER_REPORT_ISSUE, "Report Issue", "Visit Mirth's issue tracker.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/bug.png")), otherPane, null);
        addTask(TaskConstants.OTHER_LOGOUT, "Logout", "Logout and return to the login screen.", "", new ImageIcon(com.mirth.connect.client.ui.Frame.class.getResource("images/disconnect.png")), otherPane, null);
        setNonFocusable(otherPane);
        taskPaneContainer.add(otherPane);
        otherPane.setVisible(true);
    }

    public JXTaskPane getOtherPane() {
        return otherPane;
    }

    /**
     * Initializes the bound method call for the task pane actions and adds them
     * to the taskpane/popupmenu.
     */
    public void addTask(String callbackMethod, String displayName, String toolTip, String shortcutKey, ImageIcon icon, JXTaskPane pane, JPopupMenu menu) {
        BoundAction boundAction = ActionFactory.createBoundAction(callbackMethod, displayName, shortcutKey);

        if (icon != null) {
            boundAction.putValue(Action.SMALL_ICON, icon);
        }
        boundAction.putValue(Action.SHORT_DESCRIPTION, toolTip);
        boundAction.registerCallback(this, callbackMethod);

        Component component = pane.add(boundAction);
        getComponentTaskMap().put(component, callbackMethod);

        if (menu != null) {
            menu.add(boundAction);
        }
    }

    public Map<Component, String> getComponentTaskMap() {
        return componentTaskMap;
    }

    /**
     * Alerts the user with a yes/no option with the passed in 'message'
     */
    public boolean alertOption(Component parentComponent, String message) {
        int option = JOptionPane.showConfirmDialog(getVisibleComponent(parentComponent), message, "Select an Option", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Alerts the user with a Ok/cancel option with the passed in 'message'
     */
    public boolean alertOkCancel(Component parentComponent, String message) {
        int option = JOptionPane.showConfirmDialog(getVisibleComponent(parentComponent), message, "Select an Option", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Alerts the user with an information dialog with the passed in 'message'
     */
    public void alertInformation(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(getVisibleComponent(parentComponent), message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Alerts the user with a warning dialog with the passed in 'message'
     */
    public void alertWarning(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(getVisibleComponent(parentComponent), message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Alerts the user with an error dialog with the passed in 'message'
     */
    public void alertError(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(getVisibleComponent(parentComponent), message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Alerts the user with an error dialog with the passed in 'message' and a
     * 'question'.
     */
    public void alertCustomError(Component parentComponent, String message, String question) {
        parentComponent = getVisibleComponent(parentComponent);

        Window owner = getWindowForComponent(parentComponent);

        if (owner instanceof java.awt.Frame) {
            new CustomErrorDialog((java.awt.Frame) owner, message, question);
        } else { // window instanceof Dialog
            new CustomErrorDialog((java.awt.Dialog) owner, message, question);
        }
    }

    /**
     * Alerts the user with an exception dialog with the passed in stack trace.
     */
    public void alertException(Component parentComponent, StackTraceElement[] strace, String message) {
        alertException(parentComponent, strace, message, null);
    }

    /**
     * Alerts the user with an exception dialog with the passed in stack trace.
     */
    public void alertException(Component parentComponent, StackTraceElement[] strace, String message, String safeErrorKey) {
        if (connectionError) {
            return;
        }

        if (safeErrorKey != null) {
            increaseSafeErrorFailCount(safeErrorKey);

            if (getSafeErrorFailCount(safeErrorKey) < 3) {
                return;
            }
        }

        parentComponent = getVisibleComponent(parentComponent);

        if (message != null) {
            if (message.indexOf("Received close_notify during handshake") != -1) {
                return;
            }

            if (message.indexOf("Forbidden") != -1 || message.indexOf("reset") != -1) {
                connectionError = true;
                statusUpdaterExecutor.shutdownNow();

                alertWarning(parentComponent, "Sorry your connection to Mirth has either timed out or there was an error in the connection.  Please login again.");
                if (!exportChannelOnError()) {
                    return;
                }
                mirthClient.cleanup();
                this.dispose();
                LoginPanel.getInstance().initialize(PlatformUI.SERVER_NAME, PlatformUI.CLIENT_VERSION, "", "");
                return;
            } else if (message.startsWith("java.net.ConnectException: Connection refused")) {
                connectionError = true;
                statusUpdaterExecutor.shutdownNow();

                alertWarning(parentComponent, "The Mirth server " + PlatformUI.SERVER_NAME + " is no longer running.  Please start it and login again.");
                if (!exportChannelOnError()) {
                    return;
                }
                mirthClient.cleanup();
                this.dispose();
                LoginPanel.getInstance().initialize(PlatformUI.SERVER_NAME, PlatformUI.CLIENT_VERSION, "", "");
                return;
            } else if (message.startsWith("com.mirth.connect.client.core.UnauthorizedException")) {
                message = "You are not authorized to peform this action.\n\n" + message;
            }
        }

        String stackTrace = (message == null ? "" : message + "\n");

        for (int i = 0; i < strace.length; i++) {
            stackTrace += strace[i].toString() + "\n";
        }

        logger.error(stackTrace);

        Window owner = getWindowForComponent(parentComponent);

        if (owner instanceof java.awt.Frame) {
            new ErrorDialog((java.awt.Frame) owner, stackTrace);
        } else { // window instanceof Dialog
            new ErrorDialog((java.awt.Dialog) owner, stackTrace);
        }
    }

    private Component getVisibleComponent(Component component) {
        if (component != null && component.isVisible()) {
            return component;
        } else if (this.isVisible()) {
            return this;
        } else {
            return null;
        }
    }

    private Window getWindowForComponent(Component parentComponent) {
        Window owner = null;

        if (parentComponent == null) {
            owner = this;
        } else if (parentComponent instanceof java.awt.Frame || parentComponent instanceof java.awt.Dialog) {
            owner = (Window) parentComponent;
        } else {
            owner = SwingUtilities.windowForComponent(parentComponent);

            if (owner == null) {
                owner = this;
            }
        }

        return owner;
    }

    /**
     * Sets the 'index' in 'pane' to be bold
     */
    public void setBold(JXTaskPane pane, int index) {
        for (int i = 0; i < pane.getContentPane().getComponentCount(); i++) {
            pane.getContentPane().getComponent(i).setFont(UIConstants.TEXTFIELD_PLAIN_FONT);
        }

        if (index != UIConstants.ERROR_CONSTANT) {
            pane.getContentPane().getComponent(index).setFont(UIConstants.TEXTFIELD_BOLD_FONT);
        }
    }

    /**
     * Sets the visible task pane to the specified 'pane'
     */
    public void setFocus(JXTaskPane pane) {
        setFocus(new JXTaskPane[] { pane }, true, true);
    }

    /**
     * Sets the visible task panes to the specified 'panes'.
     * Also allows setting the 'Mirth' and 'Other' panes.
     */
    public void setFocus(JXTaskPane[] panes, boolean mirthPane, boolean otherPane) {
        taskPaneContainer.getComponent(0).setVisible(mirthPane);

        // ignore the first and last components
        for (int i = 1; i < taskPaneContainer.getComponentCount() - 1; i++) {
            taskPaneContainer.getComponent(i).setVisible(false);
        }

        taskPaneContainer.getComponent(taskPaneContainer.getComponentCount() - 1).setVisible(otherPane);

        if (panes != null) {
            for (JXTaskPane pane : panes) {
                if (pane != null) {
                    pane.setVisible(true);
                }
            }
        }
    }

    /**
     * Sets all components in pane to be non-focusable.
     */
    public void setNonFocusable(JXTaskPane pane) {
        for (int i = 0; i < pane.getContentPane().getComponentCount(); i++) {
            pane.getContentPane().getComponent(i).setFocusable(false);
        }
    }

    /**
     * Sets the visible tasks in the given 'pane' and 'menu'. The method takes
     * an interval of indices (end index should be -1 to go to the end), as
     * well as a whether they should be set to visible or not-visible.
     */
    public void setVisibleTasks(JXTaskPane pane, JPopupMenu menu, int startIndex, int endIndex, boolean visible) {
        // If the endIndex is -1, disregard it, otherwise stop there.
        for (int i = startIndex; (endIndex == -1 ? true : i <= endIndex) && (i < pane.getContentPane().getComponentCount()); i++) {
            // If the component being set visible is in the security list, don't allow it.

            boolean componentVisible = visible;
            String componentTask = getComponentTaskMap().get(pane.getContentPane().getComponent(i));
            if (componentTask != null) {
                if (!AuthorizationControllerFactory.getAuthorizationController().checkTask(pane.getName(), componentTask)) {
                    componentVisible = false;
                }
            }

            pane.getContentPane().getComponent(i).setVisible(componentVisible);

            if (menu != null) {
                menu.getComponent(i).setVisible(componentVisible);
            }
        }
    }

    /**
     * A prompt to ask the user if he would like to save the changes made before
     * leaving the page.
     */
    public boolean confirmLeave() {
        if ((currentContentPage == channelEditPanel || currentContentPage == channelEditPanel.transformerPane || currentContentPage == channelEditPanel.filterPane) && isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, "Would you like to save the channel changes?");
            if (option == JOptionPane.YES_OPTION) {
                if (!channelEditPanel.saveChanges()) {
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        } else if (currentContentPage == settingsPane && isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, "Would you like to save the " + settingsPane.getCurrentSettingsPanel().getTabName() + " settings changes?");

            if (option == JOptionPane.YES_OPTION) {
                if (!settingsPane.getCurrentSettingsPanel().doSave()) {
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        } else if (currentContentPage == alertEditPanel && isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, "Would you like to save the alerts?");

            if (option == JOptionPane.YES_OPTION) {
                if (!alertEditPanel.saveAlert()) {
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        } else if (currentContentPage == globalScriptsPanel && isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, "Would you like to save the scripts?");

            if (option == JOptionPane.YES_OPTION) {
                if (!doSaveGlobalScripts()) {
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        } else if (currentContentPage == codeTemplatePanel && isSaveEnabled()) {
            codeTemplatePanel.stopCodeTemplateEditing();

            int option = JOptionPane.showConfirmDialog(this, "Would you like to save the code templates?");

            if (option == JOptionPane.YES_OPTION) {
                if (!saveCodeTemplates()) {
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        }

        setSaveEnabled(false);
        return true;
    }

    /**
     * Sends the channel passed in to the server, updating it or adding it.
     * 
     * @throws ClientException
     */
    public boolean updateChannel(Channel curr, boolean override) throws ClientException {
        if (!mirthClient.updateChannel(curr, override)) {
            if (alertOption(this, "This channel has been modified since you first opened it.\nWould you like to overwrite it?")) {
                mirthClient.updateChannel(curr, true);
            } else {
                return false;
            }
        }
        retrieveChannels();

        return true;
    }

    /**
     * Sends the passed in user to the server, updating it or adding it.
     */
    public boolean updateUser(final Component parentComponent, final User updateUser, final String newPassword) {
        final String workingId = startWorking("Saving user...");

        if (StringUtils.isNotEmpty(newPassword)) {
            /*
             * If a new user is being passed in (null user id), the password
             * will only be checked right now.
             */
            if (!checkOrUpdateUserPassword(parentComponent, updateUser, newPassword)) {
                stopWorking(workingId);
                return false;
            }
        }

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    mirthClient.updateUser(updateUser);
                    retrieveUsers();

                    /*
                     * If the user id was null, a new user was being created and
                     * the password was only checked. Get the created user with
                     * the id and then update the password.
                     */
                    if (updateUser.getId() == null) {
                        User newUser = null;
                        for (User user : users) {
                            if (user.getUsername().equals(updateUser.getUsername())) {
                                newUser = user;
                            }
                        }
                        checkOrUpdateUserPassword(parentComponent, newUser, newPassword);
                    }
                } catch (ClientException e) {
                    alertException(parentComponent, e.getStackTrace(), e.getMessage());
                }
                return null;
            }

            public void done() {
                // The userPanel will be null if the user panel has not been viewed (i.e. registration).
                if (userPanel != null) {
                    userPanel.updateUserTable();
                }

                stopWorking(workingId);
            }
        };

        worker.execute();

        return true;
    }

    /**
     * If the current user is being updated, it needs to be done in the main
     * thread so that the username can be changed, re-logged in, and the current
     * user information can be updated.
     * 
     * @param parentComponent
     * @param currentUser
     * @param newPassword
     * @return
     */
    public boolean updateCurrentUser(Component parentComponent, final User currentUser, String newPassword) {
        final String workingId = startWorking("Saving user...");

        // Find out if the username is being changed so that we can login again.
        boolean changingUsername = !currentUser.getUsername().equals(PlatformUI.USER_NAME);

        /*
         * If there is a new password, update it. If not, make sure that the
         * username is not being changed, since the password must be updated
         * when the username is changed.
         */
        if (StringUtils.isNotEmpty(newPassword)) {
            if (!checkOrUpdateUserPassword(parentComponent, currentUser, newPassword)) {
                stopWorking(workingId);
                return false;
            }
        } else if (changingUsername) {
            alertWarning(parentComponent, "If you are changing your username, you must also update your password.");
            stopWorking(workingId);
            return false;
        }

        try {
            mirthClient.updateUser(currentUser);
            retrieveUsers();
        } catch (ClientException e) {
            alertException(parentComponent, e.getStackTrace(), e.getMessage());
        } finally {
            // The userPanel will be null if the user panel has not been viewed (i.e. registration).
            if (userPanel != null) {
                userPanel.updateUserTable();
            }

            stopWorking(workingId);
        }

        // If the username is being changed, login again.
        if (changingUsername) {
            final String workingId2 = startWorking("Switching User...");

            try {
                LoadedExtensions.getInstance().resetPlugins();
                mirthClient.logout();
                mirthClient.login(currentUser.getUsername(), newPassword, PlatformUI.CLIENT_VERSION);
                PlatformUI.USER_NAME = currentUser.getUsername();
                updateClient = null; // Reset the update client so it uses the new user next time it is called.
            } catch (ClientException e) {
                alertException(parentComponent, e.getStackTrace(), e.getMessage());
            } finally {
                stopWorking(workingId2);
            }
        }

        return true;
    }

    public boolean checkOrUpdateUserPassword(Component parentComponent, final User currentUser, String newPassword) {
        try {
            List<String> responses = mirthClient.checkOrUpdateUserPassword(currentUser, newPassword);
            if (CollectionUtils.isNotEmpty(responses)) {
                String responseString = "Your password is not valid. Please fix the following:\n";
                for (String response : responses) {
                    responseString += (" - " + response + "\n");
                }
                alertError(this, responseString);
                return false;
            }
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
            return false;
        }

        return true;
    }

    public User getCurrentUser(Component parentComponent) {
        User currentUser = null;

        try {
            retrieveUsers();
            for (User user : users) {
                if (user.getUsername().equals(PlatformUI.USER_NAME)) {
                    currentUser = user;
                }
            }
        } catch (ClientException e) {
            alertException(parentComponent, e.getStackTrace(), e.getMessage());
        }

        return currentUser;
    }

    public UpdateClient getUpdateClient(Component parentComponent) {
        if (updateClient == null) {
            User currentUser = PlatformUI.MIRTH_FRAME.getCurrentUser(parentComponent);
            updateClient = PlatformUI.MIRTH_FRAME.mirthClient.getUpdateClient(currentUser);
        }

        return updateClient;
    }

    public void registerUser(final User user) {
        final String workingId = startWorking("Registering user...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    getUpdateClient(PlatformUI.MIRTH_FRAME).registerUser(user);
                } catch (ClientException e) {
                    // ignore errors connecting to update/stats server
                }

                return null;
            }

            public void done() {
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void checkForUpdates() {
        final String workingId = startWorking("Checking for updates...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                UpdateSettings updateSettings = null;
                try {
                    updateSettings = mirthClient.getUpdateSettings();
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }

                // Only check if updates are enabled
                if ((updateSettings != null) && updateSettings.getUpdatesEnabled()) {
                    try {
                        List<UpdateInfo> updateInfoList = getUpdateClient(PlatformUI.MIRTH_FRAME).getUpdates();

                        boolean newUpdates = false;

                        for (UpdateInfo updateInfo : updateInfoList) {
                            // Set to true as long as the update is not ignored and not optional.
                            if (!updateInfo.isIgnored() && !updateInfo.isOptional()) {
                                newUpdates = true;
                            }
                        }

                        if (newUpdates) {
                            new ExtensionUpdateDialog(updateInfoList);
                        }
                    } catch (ClientException e) {
                        // ignore errors connecting to update/stats server
                    }
                }

                return null;
            }

            public void done() {
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void sendUsageStatistics() {
        final String workingId = startWorking("Sending usage statistics...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                UpdateSettings updateSettings = null;
                try {
                    updateSettings = mirthClient.getUpdateSettings();
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }

                if ((updateSettings != null) && updateSettings.getStatsEnabled()) {
                    try {
                        getUpdateClient(PlatformUI.MIRTH_FRAME).sendUsageStatistics();
                    } catch (ClientException e) {
                        // ignore errors connecting to update/stats server
                    }
                }

                return null;
            }

            public void done() {
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    /**
     * Checks to see if the passed in channel id already exists
     */
    public boolean checkChannelId(String id) {
        for (Channel channel : channels.values()) {
            if (channel.getId().equalsIgnoreCase(id)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks to see if the passed in channel name already exists
     */
    public boolean checkChannelName(String name, String id) {
        if (name.equals("")) {
            alertWarning(this, "Channel name cannot be empty.");
            return false;
        }

        if (name.length() > 40) {
            alertWarning(this, "Channel name cannot be longer than 40 characters.");
            return false;
        }

        Pattern alphaNumericPattern = Pattern.compile("^[a-zA-Z_0-9\\-\\s]*$");
        Matcher matcher = alphaNumericPattern.matcher(name);

        if (!matcher.find()) {
            alertWarning(this, "Channel name cannot have special characters besides hyphen, underscore, and space.");
            return false;
        }

        for (Channel channel : channels.values()) {
            if (channel.getName().equalsIgnoreCase(name) && !channel.getId().equals(id)) {
                alertWarning(this, "Channel \"" + name + "\" already exists.");
                return false;
            }
        }
        return true;
    }

    /**
     * Enables the save button for needed page.
     */
    public void setSaveEnabled(boolean enabled) {
        if (currentContentPage == channelEditPanel) {
            setVisibleTasks(channelEditTasks, channelEditPopupMenu, 0, 0, enabled);
        } else if (channelEditPanel != null && currentContentPage == channelEditPanel.transformerPane) {
            channelEditPanel.transformerPane.modified = enabled;
        } else if (channelEditPanel != null && currentContentPage == channelEditPanel.filterPane) {
            channelEditPanel.filterPane.modified = enabled;
        } else if (currentContentPage == settingsPane) {
            settingsPane.getCurrentSettingsPanel().setSaveEnabled(enabled);
        } else if (alertEditPanel != null && currentContentPage == alertEditPanel) {
            setVisibleTasks(alertEditTasks, alertEditPopupMenu, 0, 0, enabled);
        } else if (globalScriptsPanel != null && currentContentPage == globalScriptsPanel) {
            setVisibleTasks(globalScriptsTasks, globalScriptsPopupMenu, 0, 0, enabled);
        } else if (codeTemplatePanel != null && currentContentPage == codeTemplatePanel) {
            setVisibleTasks(codeTemplateTasks, codeTemplatePopupMenu, 1, 1, enabled);
        }
    }

    /**
     * Enables the save button for needed page.
     */
    public boolean isSaveEnabled() {
        boolean enabled = false;

        if (currentContentPage == channelEditPanel) {
            enabled = channelEditTasks.getContentPane().getComponent(0).isVisible();
        } else if (channelEditPanel != null && currentContentPage == channelEditPanel.transformerPane) {
            enabled = channelEditTasks.getContentPane().getComponent(0).isVisible() || channelEditPanel.transformerPane.modified;
        } else if (channelEditPanel != null && currentContentPage == channelEditPanel.filterPane) {
            enabled = channelEditTasks.getContentPane().getComponent(0).isVisible() || channelEditPanel.filterPane.modified;
        } else if (currentContentPage == settingsPane) {
            enabled = settingsPane.getCurrentSettingsPanel().isSaveEnabled();
        } else if (alertEditPanel != null && currentContentPage == alertEditPanel) {
            enabled = alertEditTasks.getContentPane().getComponent(0).isVisible();
        } else if (globalScriptsPanel != null && currentContentPage == globalScriptsPanel) {
            enabled = globalScriptsTasks.getContentPane().getComponent(0).isVisible();
        } else if (codeTemplatePanel != null && currentContentPage == codeTemplatePanel) {
            enabled = codeTemplateTasks.getContentPane().getComponent(1).isVisible();
        }

        return enabled;
    }

    // ////////////////////////////////////////////////////////////
    // --- All bound actions are beneath this point --- //
    // ////////////////////////////////////////////////////////////
    public void goToMirth() {
        BareBonesBrowserLaunch.openURL("http://www.mirthcorp.com/");
    }

    public void goToAbout() {
        new AboutMirth();
    }

    public void doReportIssue() {
        BareBonesBrowserLaunch.openURL(UIConstants.ISSUE_TRACKER_LOCATION);
    }

    public void doShowDashboard() {
        if (dashboardPanel == null) {
            dashboardPanel = new DashboardPanel();
        }

        if (!confirmLeave()) {
            return;
        }

        if (channelFilter == null) {
            channelFilter = new ChannelFilter();
        }

        channelFilter.setOnSave(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doRefreshStatuses(true);
            }
        });

        setBold(viewPane, 0);
        setPanelName("Dashboard");
        setCurrentContentPage(dashboardPanel);
        setFocus(dashboardTasks);
        retrieveAllChannelTags();

        doRefreshStatuses(true);
    }

    public void doShowChannel() {
        if (channelPanel == null) {
            channelPanel = new ChannelPanel();
        }

        if (!confirmLeave()) {
            return;
        }

        if (channelFilter == null) {
            channelFilter = new ChannelFilter();
        }

        channelFilter.setOnSave(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doRefreshChannels();
            }
        });

        setBold(viewPane, 1);
        setPanelName("Channels");
        setCurrentContentPage(channelPanel);
        setFocus(channelTasks);
        retrieveAllChannelTags();

        doRefreshChannels();
    }

    public void doShowUsers() {
        if (userPanel == null) {
            userPanel = new UserPanel();
        }

        if (!confirmLeave()) {
            return;
        }

        final String workingId = startWorking("Loading users...");

        setBold(viewPane, 2);
        setPanelName("Users");
        setCurrentContentPage(userPanel);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                refreshUser();
                return null;
            }

            public void done() {
                setFocus(userTasks);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doShowSettings() {
        if (settingsPane == null) {
            settingsPane = new SettingsPane();
        }

        if (!confirmLeave()) {
            return;
        }

        final String workingId = startWorking("Loading settings...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                settingsPane.setSelectedSettingsPanel(0);
                return null;
            }

            public void done() {
                setBold(viewPane, 3);
                setPanelName("Settings");
                setCurrentContentPage(settingsPane);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doShowAlerts() {
        if (alertPanel == null) {
            alertPanel = new DefaultAlertPanel();
        }

        if (!confirmLeave()) {
            return;
        }

        setBold(viewPane, 4);
        setPanelName("Alerts");
        setCurrentContentPage(alertPanel);
        setFocus(alertTasks);
        setSaveEnabled(false);
        doRefreshAlerts(true);
    }

    public void doShowExtensions() {
        if (extensionsPanel == null) {
            extensionsPanel = new ExtensionManagerPanel();
        }

        final String workingId = startWorking("Loading extensions...");
        if (confirmLeave()) {
            setBold(viewPane, 6);
            setPanelName("Extensions");
            setCurrentContentPage(extensionsPanel);
            setFocus(extensionsTasks);
            refreshExtensions();
            stopWorking(workingId);
        }
    }

    public void doLogout() {
        logout(false);
    }

    public boolean logout(boolean quit) {
        if (!confirmLeave()) {
            return false;
        }

        statusUpdaterExecutor.shutdownNow();

        if (currentContentPage == messageBrowser) {
            mirthClient.getServerConnection().abort(messageBrowser.getAbortOperations());
        }

        userPreferences = Preferences.userNodeForPackage(Mirth.class);
        userPreferences.putInt("maximizedState", getExtendedState());
        userPreferences.putInt("width", getWidth());
        userPreferences.putInt("height", getHeight());

        LoadedExtensions.getInstance().stopPlugins();

        try {
            mirthClient.cleanup();
            mirthClient.logout();
            this.dispose();

            if (!quit) {
                LoginPanel.getInstance().initialize(PlatformUI.SERVER_NAME, PlatformUI.CLIENT_VERSION, "", "");
            }
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
        }

        return true;
    }

    public void doMoveDestinationDown() {
        channelEditPanel.moveDestinationDown();
    }

    public void doMoveDestinationUp() {
        channelEditPanel.moveDestinationUp();
    }

    public void doNewChannel() {
        if (LoadedExtensions.getInstance().getSourceConnectors().size() == 0 || LoadedExtensions.getInstance().getDestinationConnectors().size() == 0) {
            alertError(this, "You must have at least one source connector and one destination connector installed.");
            return;
        }

        // The channel wizard will call createNewChannel() or create a channel
        // from a wizard.
        new ChannelWizard();
    }

    public void createNewChannel() {
        Channel channel = new Channel();

        try {
            channel.setId(mirthClient.getGuid());
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
        }

        channel.setName("");
        setupChannel(channel);
    }

    public void doEditChannel() {
        if (isEditingChannel) {
            return;
        } else {
            isEditingChannel = true;
        }

        List<Channel> selectedChannels = channelPanel.getSelectedChannels();
        if (selectedChannels.size() > 1) {
            JOptionPane.showMessageDialog(Frame.this, "This operation can only be performed on a single channel.");
        } else if (selectedChannels.size() == 0) {
            JOptionPane.showMessageDialog(Frame.this, "Channel no longer exists.");
        } else {
            try {
                Channel channel = selectedChannels.get(0);

                if (channel instanceof InvalidChannel) {
                    InvalidChannel invalidChannel = (InvalidChannel) channel;
                    Throwable cause = invalidChannel.getCause();
                    alertException(this, cause.getStackTrace(), "Channel \"" + channel.getName() + "\" is invalid and cannot be edited. " + getMissingExtensions(invalidChannel) + "Original cause:\n" + cause.getMessage());
                } else {
                    editChannel((Channel) SerializationUtils.clone(channel));
                }
            } catch (SerializationException e) {
                alertException(this, e.getStackTrace(), e.getMessage());
            }
        }
        isEditingChannel = false;
    }

    private String getMissingExtensions(InvalidChannel channel) {
        Set<String> missingConnectors = new HashSet<String>();
        Set<String> missingDataTypes = new HashSet<String>();

        try {
            DonkeyElement channelElement = new DonkeyElement(((InvalidChannel) channel).getChannelXml());

            checkConnectorForMissingExtensions(channelElement.getChildElement("sourceConnector"), true, missingConnectors, missingDataTypes);

            DonkeyElement destinationConnectors = channelElement.getChildElement("destinationConnectors");
            if (destinationConnectors != null) {
                for (DonkeyElement destinationConnector : destinationConnectors.getChildElements()) {
                    checkConnectorForMissingExtensions(destinationConnector, false, missingConnectors, missingDataTypes);
                }
            }
        } catch (DonkeyElementException e) {
        }

        StringBuilder builder = new StringBuilder();

        if (!missingConnectors.isEmpty()) {
            builder.append("\n\nYour Mirth Connect installation is missing required connectors for this channel:\n     ");
            builder.append(StringUtils.join(missingConnectors.toArray(), "\n     "));
            builder.append("\n\n");
        }

        if (!missingDataTypes.isEmpty()) {
            if (missingConnectors.isEmpty()) {
                builder.append("\n\n");
            }
            builder.append("Your Mirth Connect installation is missing required data types for this channel:\n     ");
            builder.append(StringUtils.join(missingDataTypes.toArray(), "\n     "));
            builder.append("\n\n");
        }

        return builder.toString();
    }

    private void checkConnectorForMissingExtensions(DonkeyElement connector, boolean source, Set<String> missingConnectors, Set<String> missingDataTypes) {
        if (connector != null) {
            DonkeyElement transportName = connector.getChildElement("transportName");
            // Check for 2.x-specific connectors
            transportName.setTextContent(ImportConverter3_0_0.convertTransportName(transportName.getTextContent()));

            if (transportName != null) {
                if (source && !LoadedExtensions.getInstance().getSourceConnectors().containsKey(transportName.getTextContent())) {
                    missingConnectors.add(transportName.getTextContent());
                } else if (!source && !LoadedExtensions.getInstance().getDestinationConnectors().containsKey(transportName.getTextContent())) {
                    missingConnectors.add(transportName.getTextContent());
                }
            }

            checkTransformerForMissingExtensions(connector.getChildElement("transformer"), missingDataTypes);
            if (!source) {
                checkTransformerForMissingExtensions(connector.getChildElement("responseTransformer"), missingDataTypes);
            }
        }
    }

    private void checkTransformerForMissingExtensions(DonkeyElement transformer, Set<String> missingDataTypes) {
        if (transformer != null) {
            // Check for 2.x-specific data types
            missingDataTypes.addAll(ImportConverter3_0_0.getMissingDataTypes(transformer, LoadedExtensions.getInstance().getDataTypePlugins().keySet()));

            DonkeyElement inboundDataType = transformer.getChildElement("inboundDataType");

            if (inboundDataType != null && !LoadedExtensions.getInstance().getDataTypePlugins().containsKey(inboundDataType.getTextContent())) {
                missingDataTypes.add(inboundDataType.getTextContent());
            }

            DonkeyElement outboundDataType = transformer.getChildElement("outboundDataType");

            if (outboundDataType != null && !LoadedExtensions.getInstance().getDataTypePlugins().containsKey(outboundDataType.getTextContent())) {
                missingDataTypes.add(outboundDataType.getTextContent());
            }
        }
    }

    public void doEditGlobalScripts() {
        if (globalScriptsPanel == null) {
            globalScriptsPanel = new GlobalScriptsPanel();
        }

        final String workingId = startWorking("Loading global scripts...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                globalScriptsPanel.edit();
                return null;
            }

            public void done() {
                editGlobalScripts();
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doEditCodeTemplates() {

        if (codeTemplatePanel == null) {
            codeTemplatePanel = new CodeTemplatePanel();
        }

        final String workingId = startWorking("Loading code templates...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                refreshCodeTemplates();
                return null;
            }

            public void done() {
                codeTemplatePanel.updateCodeTemplateTable();
                setBold(viewPane, UIConstants.ERROR_CONSTANT);
                setPanelName("Code Templates");
                setCurrentContentPage(codeTemplatePanel);
                codeTemplatePanel.setDefaultCodeTemplate();
                setFocus(codeTemplateTasks);
                setSaveEnabled(false);
                stopWorking(workingId);
            }
        };

        worker.execute();

    }

    public void doValidateCurrentGlobalScript() {
        globalScriptsPanel.validateCurrentScript();
    }

    public void doImportGlobalScripts() {
        String content = browseForFileString("XML");

        if (content != null) {
            try {
                ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
                @SuppressWarnings("unchecked")
                Map<String, String> importScripts = serializer.deserialize(content, Map.class);

                for (Entry<String, String> globalScriptEntry : importScripts.entrySet()) {
                    importScripts.put(globalScriptEntry.getKey(), globalScriptEntry.getValue().replaceAll("com.webreach.mirth", "com.mirth.connect"));
                }

                globalScriptsPanel.importAllScripts(importScripts);
            } catch (Exception e) {
                alertException(this, e.getStackTrace(), "Invalid scripts file. " + e.getMessage());
            }
        }
    }

    public void doExportGlobalScripts() {
        if (changesHaveBeenMade()) {
            if (alertOption(this, "You must save your global scripts before exporting.  Would you like to save them now?")) {
                String validationMessage = globalScriptsPanel.validateAllScripts();
                if (validationMessage != null) {
                    alertCustomError(this, validationMessage, CustomErrorDialog.ERROR_VALIDATING_GLOBAL_SCRIPTS);
                    return;
                }

                globalScriptsPanel.save();
                setSaveEnabled(false);
            } else {
                return;
            }
        }

        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        String globalScriptsXML = serializer.serialize(globalScriptsPanel.exportAllScripts());

        exportFile(globalScriptsXML, null, "XML", "Global Scripts export");
    }

    public void doValidateChannelScripts() {
        channelEditPanel.validateScripts();
    }

    public boolean doSaveGlobalScripts() {
        String validationMessage = globalScriptsPanel.validateAllScripts();
        if (validationMessage != null) {
            alertCustomError(this, validationMessage, CustomErrorDialog.ERROR_VALIDATING_GLOBAL_SCRIPTS);
            return false;
        }

        final String workingId = startWorking("Saving global scripts...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                globalScriptsPanel.save();
                return null;
            }

            public void done() {
                setSaveEnabled(false);
                stopWorking(workingId);
            }
        };

        worker.execute();

        return true;
    }

    public void doDeleteChannel() {
        if (!alertOption(this, "Are you sure you want to delete the selected channel(s)?\nAny selected deployed channel(s) will first be undeployed.")) {
            return;
        }

        final String workingId = startWorking("Deleting channel...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    status = mirthClient.getChannelStatusList();
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                    return null;
                }
                List<Channel> selectedChannels = channelPanel.getSelectedChannels();
                if (selectedChannels.size() == 0) {
                    return null;
                }

                Set<String> undeployChannelIds = new LinkedHashSet<String>();

                for (Channel channel : selectedChannels) {

                    String channelId = channel.getId();
                    for (int i = 0; i < status.size(); i++) {
                        if (status.get(i).getChannelId().equals(channelId)) {
                            undeployChannelIds.add(channelId);
                        }
                    }
                }

                if (undeployChannelIds.size() > 0) {
                    try {
                        mirthClient.undeployChannels(undeployChannelIds);
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                        return null;
                    }
                }

                for (Channel channel : selectedChannels) {
                    try {
                        mirthClient.removeChannel(channel);
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                        return null;
                    }
                }

                return null;
            }

            public void done() {
                doRefreshChannels();
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doRefreshChannels() {
        final String workingId = startWorking("Loading channels...");

        final List<String> selectedChannelIds = new ArrayList<String>();

        for (Channel channel : channelPanel.getSelectedChannels()) {
            selectedChannelIds.add(channel.getId());
        }

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    status = mirthClient.getChannelStatusList();
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }

                retrieveChannels();
                return null;
            }

            public void done() {
                channelPanel.updateChannelTable(new ArrayList<Channel>(channels.values()));

                setVisibleTasks(channelTasks, channelPopupMenu, 1, 2, false);
                setVisibleTasks(channelTasks, channelPopupMenu, 7, -1, false);

                if (channels.size() > 0) {
                    if (!channelFilter.isTagFilterEnabled()) {
                        setVisibleTasks(channelTasks, channelPopupMenu, 1, 1, true);
                    }

                    setVisibleTasks(channelTasks, channelPopupMenu, 7, 7, true);
                }

                channelPanel.setSelectedChannels(selectedChannelIds);

                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void retrieveChannels() {
        try {
            Map<String, Integer> channelHeaders = new HashMap<String, Integer>();

            for (Channel channel : channels.values()) {
                channelHeaders.put(channel.getId(), channel.getRevision());
            }

            List<ChannelSummary> changedChannels = mirthClient.getChannelSummary(channelHeaders);

            if (changedChannels.size() == 0) {
                return;
            } else {
                Set<String> addedOrUpdatedChannelIds = new LinkedHashSet<String>();

                for (ChannelSummary channelSummary : changedChannels) {
                    if (channelSummary.isDeleted()) {
                        channels.remove(channelSummary.getId());
                    } else {
                        addedOrUpdatedChannelIds.add(channelSummary.getId());
                    }
                }

                List<Channel> channelsToAddOrUpdate = mirthClient.getChannels(addedOrUpdatedChannelIds);
                for (Channel channel : channelsToAddOrUpdate) {
                    channels.put(channel.getId(), channel);
                }
            }
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
        }
    }

    public void clearChannelCache() {
        channels = new HashMap<String, Channel>();
    }

    public synchronized void setRefreshingStatuses(boolean refreshingStatuses) {
        this.refreshingStatuses = refreshingStatuses;
    }

    public synchronized boolean isRefreshingStatuses() {
        return refreshingStatuses;
    }

    public synchronized void setRefreshingAlerts(boolean refreshingAlerts) {
        this.refreshingAlerts = refreshingAlerts;
    }

    public synchronized boolean isRefreshingAlerts() {
        return refreshingAlerts;
    }

    public synchronized void increaseSafeErrorFailCount(String safeErrorKey) {
        int safeErrorFailCount = getSafeErrorFailCount(safeErrorKey) + 1;
        this.safeErrorFailCountMap.put(safeErrorKey, safeErrorFailCount);
    }

    public synchronized void resetSafeErrorFailCount(String safeErrorKey) {
        this.safeErrorFailCountMap.put(safeErrorKey, 0);
    }

    public synchronized int getSafeErrorFailCount(String safeErrorKey) {
        if (safeErrorFailCountMap.containsKey(safeErrorKey)) {
            return safeErrorFailCountMap.get(safeErrorKey);
        } else {
            return 0;
        }
    }

    public void doRefreshStatuses() {
        doRefreshStatuses(true);
    }

    public void doRefreshStatuses(boolean queue) {
        // Don't allow anything to be getting or setting refreshingStatuses
        // while this block is being executed.
        synchronized (this) {
            if (isRefreshingStatuses()) {
                if (queue) {
                    queueRefreshStatus = true;
                }
                return;
            }

            setRefreshingStatuses(true);
        }
        final String workingId = startWorking("Loading statistics...");

        // moving SwingWorker into the refreshStatuses() method...
        // ArrayIndexOutOfBound exception occurs due to updateTable method on
        // the UI executed concurrently on multiple threads in the background.
        // and they share a global 'parent.status' variable that changes its
        // state between threads.
        // updateTable() method should be called in done(), not in the
        // background only when the 'status' object is done assessed in the
        // background.

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                try {
                    status = mirthClient.getChannelStatusList();
                    resetSafeErrorFailCount(TaskConstants.DASHBOARD_REFRESH);

                    if (status != null) {
                        for (DashboardColumnPlugin plugin : LoadedExtensions.getInstance().getDashboardColumnPlugins().values()) {
                            plugin.tableUpdate(status);
                        }
                    }
                } catch (ClientException e) {
                    status = null;
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage(), TaskConstants.DASHBOARD_REFRESH);
                }

                return null;
            }

            public void done() {
                stopWorking(workingId);

                if (status != null) {
                    dashboardPanel.updateTable(status);
                    dashboardPanel.updateCurrentPluginPanel();
                }

                setRefreshingStatuses(false);

                // Perform another refresh if any were queued up to ensure the dashboard is up to date.
                if (queueRefreshStatus) {
                    queueRefreshStatus = false;
                    doRefreshStatuses(false);
                }
            }
        };

        worker.execute();
    }

    public void doStart() {
        Set<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedChannelStatuses();

        if (selectedStatuses.size() == 0) {
            return;
        }

        for (final DashboardStatus status : selectedStatuses) {
            final String workingId = startWorking("Starting channel...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                public Void doInBackground() {
                    try {
                        if (status.getState() == DeployedState.PAUSED) {
                            mirthClient.resumeChannel(status.getChannelId());
                        } else {
                            mirthClient.startChannel(status.getChannelId());
                        }
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                    }

                    return null;
                }

                public void done() {
                    doRefreshStatuses(true);
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doStop() {
        Set<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedChannelStatuses();

        if (selectedStatuses.size() == 0) {
            return;
        }

        for (final DashboardStatus status : selectedStatuses) {
            final String workingId = startWorking("Stopping channel...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                public Void doInBackground() {
                    try {
                        mirthClient.stopChannel(status.getChannelId());
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                    }

                    return null;
                }

                public void done() {
                    doRefreshStatuses(true);
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doHalt() {
        Set<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedChannelStatuses();

        if (selectedStatuses.size() == 0 || !alertOption(this, "Are you sure you want to halt this channel?")) {
            return;
        }

        for (final DashboardStatus status : selectedStatuses) {
            final String workingId = startWorking("Halting channel...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                public Void doInBackground() {
                    try {
                        mirthClient.haltChannel(status.getChannelId());
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                    }

                    return null;
                }

                public void done() {
                    doRefreshStatuses(true);
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doPause() {
        Set<DashboardStatus> selectedChannelStatuses = dashboardPanel.getSelectedChannelStatuses();

        if (selectedChannelStatuses.size() == 0) {
            return;
        }

        for (final DashboardStatus channelStatus : selectedChannelStatuses) {
            final String workingId = startWorking("Pausing channel...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {
                    try {
                        mirthClient.pauseChannel(channelStatus.getChannelId());
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                    }
                    return null;
                }

                public void done() {
                    doRefreshStatuses(true);
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doStartConnector() {
        List<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedStatuses();

        if (selectedStatuses.size() == 0) {
            return;
        }

        for (final DashboardStatus status : selectedStatuses) {
            final String workingId = startWorking("Starting connector...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                public Void doInBackground() {
                    try {
                        mirthClient.startConnector(status.getChannelId(), status.getMetaDataId());
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                    }

                    return null;
                }

                public void done() {
                    doRefreshStatuses(true);
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doStopConnector() {
        List<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedStatuses();

        if (selectedStatuses.size() == 0) {
            return;
        }

        boolean warnQueueDisabled = false;

        for (final DashboardStatus status : selectedStatuses) {
            if (status.getMetaDataId() != 0 && !status.isQueueEnabled()) {
                warnQueueDisabled = true;
            } else {
                final String workingId = startWorking("Stopping connector...");

                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    public Void doInBackground() {
                        try {
                            mirthClient.stopConnector(status.getChannelId(), status.getMetaDataId());
                        } catch (ClientException e) {
                            alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                        }

                        return null;
                    }

                    public void done() {
                        doRefreshStatuses(true);
                        stopWorking(workingId);
                    }
                };

                worker.execute();
            }
        }

        if (warnQueueDisabled) {
            alertWarning(this, "<html>One or more destination connectors were not stopped because queueing was not enabled.<br>Queueing must be enabled for a destination connector to be stopped individually.</html>");
        }
    }

    public void doNewDestination() {
        channelEditPanel.addNewDestination();
    }

    public void doDeleteDestination() {
        if (!alertOption(this, "Are you sure you want to delete this destination?")) {
            return;
        }

        channelEditPanel.deleteDestination();
    }

    public void doCloneDestination() {
        channelEditPanel.cloneDestination();
    }

    public void doEnableDestination() {
        channelEditPanel.enableDestination();
    }

    public void doDisableDestination() {
        channelEditPanel.disableDestination();
    }

    public void doEnableChannel() {
        List<Channel> selectedChannels = channelPanel.getSelectedChannels();
        if (selectedChannels.size() == 0) {
            alertWarning(this, "Channel no longer exists.");
            return;
        }

        for (final Channel channel : selectedChannels) {
            if (channel instanceof InvalidChannel) {
                InvalidChannel invalidChannel = (InvalidChannel) channel;
                Throwable cause = invalidChannel.getCause();
                alertException(this, cause.getStackTrace(), "Channel \"" + channel.getName() + "\" is invalid and cannot be enabled. " + getMissingExtensions(invalidChannel) + "Original cause:\n" + cause.getMessage());
                return;
            }

            String validationMessage = channelEditPanel.checkAllForms(channel);
            if (validationMessage != null) {
                alertCustomError(this, validationMessage, CustomErrorDialog.ERROR_ENABLING_CHANNEL);
                return;
            }

            final String workingId = startWorking("Enabling channel...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {

                    channel.setEnabled(true);
                    try {
                        updateChannel(channel, false);
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                    }
                    return null;
                }

                public void done() {
                    doRefreshChannels();
                    stopWorking(workingId);
                }
            };

            worker.execute();

        }
    }

    public void doDisableChannel() {
        List<Channel> selectedChannels = channelPanel.getSelectedChannels();
        if (selectedChannels.size() == 0) {
            alertWarning(this, "Channel no longer exists.");
            return;
        }

        for (final Channel channel : selectedChannels) {
            if (!(channel instanceof InvalidChannel)) {
                final String workingId = startWorking("Disabling channel...");

                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    public Void doInBackground() {
                        channel.setEnabled(false);
                        try {
                            updateChannel(channel, false);
                        } catch (ClientException e) {
                            alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                        }
                        return null;
                    }

                    public void done() {
                        doRefreshChannels();
                        stopWorking(workingId);
                    }
                };

                worker.execute();
            }
        }
    }

    public void doNewUser() {
        new UserDialog(null);
    }

    public void doEditUser() {
        int index = userPanel.getUserIndex();

        if (index == UIConstants.ERROR_CONSTANT) {
            alertWarning(this, "User no longer exists.");
        } else {
            new UserDialog(users.get(index));
        }
    }

    public void doDeleteUser() {
        if (!alertOption(this, "Are you sure you want to delete this user?")) {
            return;
        }

        final String workingId = startWorking("Deleting user...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                if (users.size() == 1) {
                    alertWarning(PlatformUI.MIRTH_FRAME, "You must have at least one user account.");
                    return null;
                }

                int userToDelete = userPanel.getUserIndex();

                try {
                    if (userToDelete != UIConstants.ERROR_CONSTANT) {
                        mirthClient.removeUser(users.get(userToDelete));
                        retrieveUsers();
                    }
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }
                return null;
            }

            public void done() {
                userPanel.updateUserTable();
                userPanel.deselectRows();
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doRefreshUser() {
        final String workingId = startWorking("Loading users...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                refreshUser();
                return null;
            }

            public void done() {
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void refreshUser() {
        User user = null;
        String userName = null;
        int index = userPanel.getUserIndex();

        if (index != UIConstants.ERROR_CONSTANT) {
            user = users.get(index);
        }

        try {
            retrieveUsers();
            userPanel.updateUserTable();

            if (user != null) {
                for (int i = 0; i < users.size(); i++) {
                    if (user.equals(users.get(i))) {
                        userName = users.get(i).getUsername();
                    }
                }
            }
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
        }

        // as long as the channel was not deleted
        if (userName != null) {
            userPanel.setSelectedUser(userName);
        }
    }

    public void doRedeployAll() {
        final String workingId = startWorking("Deploying channels...");
        dashboardPanel.deselectRows();
        doShowDashboard();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    mirthClient.redeployAllChannels();
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }
                return null;
            }

            public void done() {
                stopWorking(workingId);
                doRefreshStatuses(true);
            }
        };

        worker.execute();
    }

    public void doDeployChannel() {
        List<Channel> selectedChannels = channelPanel.getSelectedChannels();
        if (selectedChannels.size() == 0) {
            alertWarning(this, "Channel no longer exists.");
            return;
        }

        // Only deploy enabled channels
        final Set<String> selectedEnabledChannelIds = new LinkedHashSet<String>();
        boolean channelDisabled = false;
        for (Channel channel : selectedChannels) {
            if (channel.isEnabled()) {
                selectedEnabledChannelIds.add(channel.getId());
            } else {
                channelDisabled = true;
            }
        }

        if (channelDisabled) {
            alertWarning(this, "Disabled channels will not be deployed.");
        }

        String plural = (selectedChannels.size() > 1) ? "s" : "";
        final String workingId = startWorking("Deploying channel" + plural + "...");

        dashboardPanel.deselectRows();
        doShowDashboard();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    mirthClient.deployChannels(selectedEnabledChannelIds);
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }
                return null;
            }

            public void done() {
                stopWorking(workingId);
                doRefreshStatuses(true);
            }
        };

        worker.execute();
    }

    public void doUndeployChannel() {
        final Set<DashboardStatus> selectedChannelStatuses = dashboardPanel.getSelectedChannelStatuses();

        if (selectedChannelStatuses.size() == 0) {
            return;
        }

        dashboardPanel.deselectRows();

        String plural = (selectedChannelStatuses.size() > 1) ? "s" : "";
        final String workingId = startWorking("Undeploying channel" + plural + "...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    Set<String> channelIds = new LinkedHashSet<String>();

                    for (DashboardStatus channelStatus : selectedChannelStatuses) {
                        channelIds.add(channelStatus.getChannelId());
                    }

                    mirthClient.undeployChannels(channelIds);
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }
                return null;
            }

            public void done() {
                stopWorking(workingId);
                doRefreshStatuses(true);
            }
        };

        worker.execute();
    }

    public void doSaveChannel() {
        final String workingId = startWorking("Saving channel...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                if (changesHaveBeenMade() || currentContentPage == channelEditPanel.transformerPane || currentContentPage == channelEditPanel.filterPane) {
                    if (channelEditPanel.saveChanges()) {
                        setSaveEnabled(false);
                    }
                    return null;
                } else {
                    return null;
                }
            }

            public void done() {
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public boolean changesHaveBeenMade() {
        if (channelEditPanel != null && currentContentPage == channelEditPanel) {
            return channelEditTasks.getContentPane().getComponent(0).isVisible();
        } else if (channelEditPanel != null && currentContentPage == channelEditPanel.transformerPane) {
            return channelEditPanel.transformerPane.modified;
        } else if (channelEditPanel != null && currentContentPage == channelEditPanel.filterPane) {
            return channelEditPanel.filterPane.modified;
        } else if (settingsPane != null && currentContentPage == settingsPane) {
            return settingsPane.getCurrentSettingsPanel().isSaveEnabled();
        } else if (alertEditPanel != null && currentContentPage == alertEditPanel) {
            return alertEditTasks.getContentPane().getComponent(0).isVisible();
        } else if (globalScriptsPanel != null && currentContentPage == globalScriptsPanel) {
            return globalScriptsTasks.getContentPane().getComponent(0).isVisible();
        } else if (codeTemplatePanel != null && currentContentPage == codeTemplatePanel) {
            return codeTemplateTasks.getContentPane().getComponent(1).isVisible();
        } else {
            return false;
        }
    }

    public void doShowMessages() {
        if (messageBrowser == null) {
            messageBrowser = new MessageBrowser();
        }

        List<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedStatuses();
        Set<DashboardStatus> selectedChannelStatuses = dashboardPanel.getSelectedChannelStatuses();

        if (selectedStatuses.size() == 0) {
            return;
        }

        if (selectedChannelStatuses.size() > 1) {
            JOptionPane.showMessageDialog(Frame.this, "This operation can only be performed on a single channel.");
            return;
        }

        final String channelId = selectedStatuses.get(0).getChannelId();
        final List<Integer> metaDataIds = new ArrayList<Integer>();

        for (DashboardStatus status : selectedStatuses) {
            metaDataIds.add(status.getMetaDataId());
        }

        retrieveChannels();

        setBold(viewPane, -1);
        setPanelName("Channel Messages - " + selectedChannelStatuses.iterator().next().getName());
        setCurrentContentPage(messageBrowser);
        setFocus(messageTasks);

        final String workingId = startWorking("Retrieving channel metadata...");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            Map<Integer, String> connectors;
            List<MetaDataColumn> metaDataColumns;

            public Void doInBackground() {
                try {
                    connectors = mirthClient.getConnectorNames(channelId);
                    metaDataColumns = mirthClient.getMetaDataColumns(channelId);
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }
                return null;
            }

            public void done() {
                stopWorking(workingId);

                if (connectors == null || metaDataColumns == null) {
                    alertError(PlatformUI.MIRTH_FRAME, "Could not retrieve metadata for channel.");
                } else {
                    messageBrowser.loadChannel(channelId, connectors, metaDataColumns, metaDataIds);
                }
            }
        };

        worker.execute();
    }

    public void doShowEvents() {
        doShowEvents(null);
    }

    public void doShowEvents(String eventNameFilter) {
        if (!confirmLeave()) {
            return;
        }

        if (eventBrowser == null) {
            eventBrowser = new EventBrowser();
        }

        setBold(viewPane, 5);
        setPanelName("Events");
        setCurrentContentPage(eventBrowser);
        setFocus(eventTasks);

        eventBrowser.loadNew(eventNameFilter);
    }

    public void doEditTransformer() {
        channelEditPanel.transformerPane.resizePanes();
        String name = channelEditPanel.editTransformer();
        setPanelName("Edit Channel - " + channelEditPanel.currentChannel.getName() + " - " + name + " Transformer");
    }

    public void doEditResponseTransformer() {
        channelEditPanel.transformerPane.resizePanes();
        String name = channelEditPanel.editResponseTransformer();
        setPanelName("Edit Channel - " + channelEditPanel.currentChannel.getName() + " - " + name + " Response Transformer");
    }

    public void doEditFilter() {
        channelEditPanel.filterPane.resizePanes();
        String name = channelEditPanel.editFilter();
        setPanelName("Edit Channel - " + channelEditPanel.currentChannel.getName() + " - " + name + " Filter");
    }

    public void updateFilterTaskName(int rules) {
        updateFilterOrTransformerTaskName(UIConstants.EDIT_FILTER, UIConstants.EDIT_FILTER_TASK_NUMBER, rules, false);
    }

    public void updateTransformerTaskName(int steps, boolean outboundTemplate) {
        updateFilterOrTransformerTaskName(UIConstants.EDIT_TRANSFORMER, UIConstants.EDIT_TRANSFORMER_TASK_NUMBER, steps, outboundTemplate);
    }

    public void updateResponseTransformerTaskName(int steps, boolean outboundTemplate) {
        updateFilterOrTransformerTaskName(UIConstants.EDIT_RESPONSE_TRANSFORMER, UIConstants.EDIT_RESPONSE_TRANSFORMER_TASK_NUMBER, steps, outboundTemplate);
    }

    private void updateFilterOrTransformerTaskName(String taskName, int componentIndex, int rulesOrSteps, boolean outboundTemplate) {
        if (rulesOrSteps > 0) {
            taskName += " (" + rulesOrSteps + ")";
        } else if (outboundTemplate) {
            taskName += " (0)";
        }

        ((JXHyperlink) channelEditTasks.getContentPane().getComponent(componentIndex)).setText(taskName);
        ((JMenuItem) channelEditPopupMenu.getComponent(componentIndex)).setText(taskName);
    }

    public void doValidate() {
        channelEditPanel.doValidate();
    }

    public void doImport() {
        String content = browseForFileString("XML");

        if (content != null) {
            importChannel(content, true);
        }
    }

    public void importChannel(String content, boolean showAlerts) {
        if (showAlerts && !promptObjectMigration(content, "channel")) {
            return;
        }

        boolean overwrite = false;
        Channel importChannel = null;

        try {
            importChannel = ObjectXMLSerializer.getInstance().deserialize(content, Channel.class);
        } catch (Exception e) {
            if (showAlerts) {
                alertException(this, e.getStackTrace(), "Invalid channel file:\n" + e.getMessage());
            }

            return;
        }

        try {
            String channelName = importChannel.getName();
            String tempId = mirthClient.getGuid();

            // Check to see that the channel name doesn't already exist.
            if (!checkChannelName(channelName, tempId)) {
                if (!alertOption(this, "Would you like to overwrite the existing channel?  Choose 'No' to create a new channel.")) {
                    importChannel.setRevision(0);

                    do {
                        channelName = JOptionPane.showInputDialog(this, "Please enter a new name for the channel.", channelName);
                        if (channelName == null) {
                            return;
                        }
                    } while (!checkChannelName(channelName, tempId));

                    importChannel.setName(channelName);
                    importChannel.setId(tempId);
                } else {
                    overwrite = true;

                    for (Channel channel : channels.values()) {
                        if (channel.getName().equalsIgnoreCase(channelName)) {
                            // If overwriting, use the old revision number and id
                            importChannel.setRevision(channel.getRevision());
                            importChannel.setId(channel.getId());
                        }
                    }
                }
            } else {
                // Start the revision number over for a new channel
                importChannel.setRevision(0);

                // If the channel name didn't already exist, make sure
                // the id doesn't exist either.
                if (!checkChannelId(importChannel.getId())) {
                    importChannel.setId(tempId);
                }

            }

            channels.put(importChannel.getId(), importChannel);
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
        }

        if (importChannel instanceof InvalidChannel) {
            // Update the channel, but don't try to edit it
            try {
                updateChannel(importChannel, overwrite);

                if (importChannel instanceof InvalidChannel && showAlerts) {
                    InvalidChannel invalidChannel = (InvalidChannel) importChannel;
                    Throwable cause = invalidChannel.getCause();
                    alertException(this, cause.getStackTrace(), "Channel \"" + importChannel.getName() + "\" is invalid. " + getMissingExtensions(invalidChannel) + " Original cause:\n" + cause.getMessage());
                }

                doRefreshChannels();
            } catch (Exception e) {
                channels.remove(importChannel.getId());
            }
        } else if (showAlerts) {
            final Channel importChannelFinal = importChannel;

            /*
             * MIRTH-2048 - This is a hack to fix the memory access error
             * that only occurs on OS X. The block of code that edits the
             * channel needs to be invoked later so that the screen does not
             * change before the drag/drop action of a channel finishes.
             */
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        editChannel(importChannelFinal);
                        setSaveEnabled(true);
                    } catch (Exception e) {
                        channels.remove(importChannelFinal.getId());
                        alertError(PlatformUI.MIRTH_FRAME, "Channel had an unknown problem. Channel import aborted.");
                        channelEditPanel = new ChannelSetup();
                        doShowChannel();
                    }
                }

            });
        } else {
            try {
                updateChannel(importChannel, overwrite);
                doShowChannel();
            } catch (Exception e) {
                channels.remove(importChannel.getId());
                doShowChannel();
            }
        }
    }

    public boolean doExportChannel() {
        if (changesHaveBeenMade()) {
            if (alertOption(this, "This channel has been modified. You must save the channel changes before you can export. Would you like to save them now?")) {
                if (!channelEditPanel.saveChanges()) {
                    return false;
                }
            } else {
                return false;
            }

            setSaveEnabled(false);
        }

        Channel channel;
        if (currentContentPage == channelEditPanel || currentContentPage == channelEditPanel.filterPane || currentContentPage == channelEditPanel.transformerPane) {
            channel = channelEditPanel.currentChannel;
        } else {
            List<Channel> selectedChannels = channelPanel.getSelectedChannels();
            if (selectedChannels.size() > 1) {
                JOptionPane.showMessageDialog(Frame.this, "This operation can only be performed on a single channel.");
                return false;
            }
            channel = selectedChannels.get(0);
        }

        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        String channelXML = serializer.serialize(channel);

        return exportFile(channelXML, channel.getName(), "XML", "Channel");
    }

    public void doExportAll() {
        JFileChooser exportFileChooser = new JFileChooser();
        exportFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        File currentDir = new File(userPreferences.get("currentDirectory", ""));
        if (currentDir.exists()) {
            exportFileChooser.setCurrentDirectory(currentDir);
        }

        int returnVal = exportFileChooser.showSaveDialog(this);
        File exportFile = null;
        File exportDirectory = null;

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            boolean tagFilteredEnabled = channelFilter.isTagFilterEnabled();
            Set<String> visibleTags = channelFilter.getVisibleTags();

            userPreferences.put("currentDirectory", exportFileChooser.getCurrentDirectory().getPath());
            try {
                exportDirectory = exportFileChooser.getSelectedFile();

                for (Channel channel : channels.values()) {
                    if (!tagFilteredEnabled || CollectionUtils.containsAny(visibleTags, channel.getProperties().getTags())) {
                        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
                        String channelXML = serializer.serialize(channel);

                        exportFile = new File(exportDirectory.getAbsolutePath() + "/" + channel.getName() + ".xml");

                        if (exportFile.exists()) {
                            if (!alertOption(this, "The file " + channel.getName() + ".xml already exists.  Would you like to overwrite it?")) {
                                continue;
                            }
                        }

                        FileUtils.writeStringToFile(exportFile, channelXML, UIConstants.CHARSET);
                    }
                }
                alertInformation(this, "All files were written successfully to " + exportDirectory.getPath() + ".");
            } catch (IOException ex) {
                alertError(this, "File could not be written.");
            }
        }
    }

    /**
     * Import a file with the default defined file filter type.
     * 
     * @return
     */
    public String browseForFileString(String fileExtension) {
        File file = browseForFile(fileExtension);

        if (file != null) {
            return readFileToString(file);
        }

        return null;
    }

    /**
     * Read the bytes from a file with the default defined file filter type.
     * 
     * @return
     */
    public byte[] browseForFileBytes(String fileExtension) {
        File file = browseForFile(fileExtension);

        if (file != null) {
            try {
                return FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                alertError(this, "Unable to read file.");
            }
        }

        return null;
    }

    public String readFileToString(File file) {
        try {
            String content = FileUtils.readFileToString(file, UIConstants.CHARSET);

            if (StringUtils.startsWith(content, EncryptionSettings.ENCRYPTION_PREFIX)) {
                return mirthClient.getEncryptor().decrypt(StringUtils.removeStart(content, EncryptionSettings.ENCRYPTION_PREFIX));
            } else {
                return content;
            }
        } catch (IOException e) {
            alertError(this, "Unable to read file.");
        }

        return null;
    }

    public File browseForFile(String fileExtension) {
        JFileChooser importFileChooser = new JFileChooser();

        if (fileExtension != null) {
            importFileChooser.setFileFilter(new MirthFileFilter(fileExtension));
        }

        File currentDir = new File(userPreferences.get("currentDirectory", ""));

        if (currentDir.exists()) {
            importFileChooser.setCurrentDirectory(currentDir);
        }

        if (importFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            userPreferences.put("currentDirectory", importFileChooser.getCurrentDirectory().getPath());
            return importFileChooser.getSelectedFile();
        }

        return null;
    }

    /**
     * Creates a File with the default defined file filter type, but does not
     * yet write to it.
     * 
     * @param defaultFileName
     * @param fileExtension
     * @return
     */
    public File createFileForExport(String defaultFileName, String fileExtension) {
        JFileChooser exportFileChooser = new JFileChooser();

        if (defaultFileName != null) {
            exportFileChooser.setSelectedFile(new File(defaultFileName));
        }

        if (fileExtension != null) {
            exportFileChooser.setFileFilter(new MirthFileFilter(fileExtension));
        }

        File currentDir = new File(userPreferences.get("currentDirectory", ""));

        if (currentDir.exists()) {
            exportFileChooser.setCurrentDirectory(currentDir);
        }

        if (exportFileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            userPreferences.put("currentDirectory", exportFileChooser.getCurrentDirectory().getPath());
            File exportFile = exportFileChooser.getSelectedFile();

            if ((exportFile.getName().length() < 4) || !FilenameUtils.getExtension(exportFile.getName()).equalsIgnoreCase(fileExtension)) {
                exportFile = new File(exportFile.getAbsolutePath() + "." + fileExtension.toLowerCase());
            }

            if (exportFile.exists()) {
                if (!alertOption(this, "This file already exists.  Would you like to overwrite it?")) {
                    return null;
                }
            }

            return exportFile;
        } else {
            return null;
        }
    }

    /**
     * Export a file with the default defined file filter type.
     * 
     * @param fileContents
     * @param fileName
     * @return
     */
    public boolean exportFile(String fileContents, String defaultFileName, String fileExtension, String name) {
        return exportFile(fileContents, createFileForExport(defaultFileName, fileExtension), name);
    }

    public boolean exportFile(String fileContents, File exportFile, String name) {
        if (exportFile != null) {
            try {
                String contentToWrite = null;

                if (mirthClient.isEncryptExport()) {
                    contentToWrite = EncryptionSettings.ENCRYPTION_PREFIX + mirthClient.getEncryptor().encrypt(fileContents);
                } else {
                    contentToWrite = fileContents;
                }

                FileUtils.writeStringToFile(exportFile, contentToWrite, UIConstants.CHARSET);
                alertInformation(this, name + " was written to " + exportFile.getPath() + ".");
            } catch (IOException ex) {
                alertError(this, "File could not be written.");
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    public void doImportConnector() {
        String content = browseForFileString("XML");

        if (content != null) {
            try {
                channelEditPanel.importConnector(ObjectXMLSerializer.getInstance().deserialize(content, Connector.class));
            } catch (Exception e) {
                alertException(this, e.getStackTrace(), e.getMessage());
            }
        }
    }

    public void doExportConnector() {
        if (changesHaveBeenMade()) {
            if (alertOption(this, "This channel has been modified. You must save the channel changes before you can export. Would you like to save them now?")) {
                if (!channelEditPanel.saveChanges()) {
                    return;
                }
            } else {
                return;
            }

            setSaveEnabled(false);
        }

        Connector connector = channelEditPanel.exportSelectedConnector();

        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        String connectorXML = serializer.serialize(connector);

        String fileName = channelEditPanel.currentChannel.getName();
        if (connector.getMode().equals(Mode.SOURCE)) {
            fileName += " Source";
        } else {
            fileName += " " + connector.getName();
        }

        exportFile(connectorXML, fileName, "XML", "Connector");
    }

    public void doCloneChannel() {
        List<Channel> selectedChannels = channelPanel.getSelectedChannels();
        if (selectedChannels.size() > 1) {
            JOptionPane.showMessageDialog(Frame.this, "This operation can only be performed on a single channel.");
            return;
        }

        Channel channel = selectedChannels.get(0);

        if (channel instanceof InvalidChannel) {
            InvalidChannel invalidChannel = (InvalidChannel) channel;
            Throwable cause = invalidChannel.getCause();
            alertException(this, cause.getStackTrace(), "Channel \"" + channel.getName() + "\" is invalid and cannot be cloned. " + getMissingExtensions(invalidChannel) + "Original cause:\n" + cause.getMessage());
            return;
        }

        try {
            channel = (Channel) SerializationUtils.clone(channel);
        } catch (SerializationException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
            return;
        }

        try {
            channel.setRevision(0);
            channel.setId(mirthClient.getGuid());
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
        }

        String channelName = channel.getName();
        do {
            channelName = JOptionPane.showInputDialog(this, "Please enter a new name for the channel.", channelName);
            if (channelName == null) {
                return;
            }
        } while (!checkChannelName(channelName, channel.getId()));

        channel.setName(channelName);
        channels.put(channel.getId(), channel);

        editChannel(channel);
        setSaveEnabled(true);
    }

    public void doRefreshMessages() {
        messageBrowser.refresh(null, true);
    }

    public void doSendMessage() {
        retrieveChannels();

        List<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedStatuses();
        String channelId = selectedStatuses.get(0).getChannelId();
        List<Integer> selectedMetaDataIds = new ArrayList<Integer>();

        for (DashboardStatus status : selectedStatuses) {
            if (status.getChannelId() != channelId) {
                JOptionPane.showMessageDialog(Frame.this, "This operation can only be performed on a single channel.");
                return;
            }

            if (status.getStatusType() == StatusType.CHANNEL) {
                selectedMetaDataIds = null;
            } else if (selectedMetaDataIds != null) {
                Integer metaDataId = status.getMetaDataId();

                if (metaDataId != null) {
                    selectedMetaDataIds.add(metaDataId);
                }
            }
        }

        Channel channel = channels.get(channelId);

        if (channel == null) {
            alertError(this, "Channel no longer exists!");
            return;
        }

        new EditMessageDialog("", channel.getSourceConnector().getTransformer().getInboundDataType(), channel.getId(), dashboardPanel.getDestinationConnectorNames(channelId), selectedMetaDataIds);
    }

    public void doExportMessages() {
        if (messageExportDialog == null) {
            messageExportDialog = new MessageExportDialog();
        }

        messageExportDialog.setEncryptor(mirthClient.getEncryptor());
        messageExportDialog.setMessageFilter(messageBrowser.getMessageFilter());
        messageExportDialog.setPageSize(messageBrowser.getPageSize());
        messageExportDialog.setChannelId(messageBrowser.getChannelId());
        messageExportDialog.setLocationRelativeTo(this);
        messageExportDialog.setVisible(true);
    }

    public void doImportMessages() {
        if (messageImportDialog == null) {
            messageImportDialog = new MessageImportDialog();
        }

        messageImportDialog.setChannelId(messageBrowser.getChannelId());
        messageImportDialog.setMessageBrowser(messageBrowser);
        messageImportDialog.setLocationRelativeTo(this);
        messageImportDialog.setVisible(true);
    }

    public void doRemoveAllMessages() {
        if (removeMessagesDialog == null) {
            removeMessagesDialog = new RemoveMessagesDialog(this, true);
        }

        removeMessagesDialog.init(dashboardPanel.getSelectedChannelStatuses());
        removeMessagesDialog.setLocationRelativeTo(this);
        removeMessagesDialog.setVisible(true);
    }

    public void doClearStats() {
        List<DashboardStatus> channelStatuses = dashboardPanel.getSelectedStatusesRecursive();

        if (channelStatuses.size() != 0) {
            new DeleteStatisticsDialog(channelStatuses);
        } else {
            dashboardPanel.deselectRows();
        }
    }

    public void clearStats(List<DashboardStatus> statusesToClear, final boolean deleteReceived, final boolean deleteFiltered, final boolean deleteSent, final boolean deleteErrored) {
        final String workingId = startWorking("Clearing statistics...");
        Map<String, List<Integer>> channelConnectorMap = new HashMap<String, List<Integer>>();

        for (DashboardStatus status : statusesToClear) {
            String channelId = status.getChannelId();
            Integer metaDataId = status.getMetaDataId();

            List<Integer> metaDataIds = channelConnectorMap.get(channelId);

            if (metaDataIds == null) {
                metaDataIds = new ArrayList<Integer>();
                channelConnectorMap.put(channelId, metaDataIds);
            }

            metaDataIds.add(metaDataId);
        }

        final Map<String, List<Integer>> channelConnectorMapFinal = channelConnectorMap;

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    mirthClient.clearStatistics(channelConnectorMapFinal, deleteReceived, deleteFiltered, deleteSent, deleteErrored);
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }
                return null;
            }

            public void done() {
                doRefreshStatuses(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doRemoveFilteredMessages() {
        if (alertOption(this, "<html>Are you sure you would like to remove all currently filtered messages (including QUEUED) in this channel?<br>Channel must be stopped for unfinished messages to be removed.<br><font size='1'><br></font>WARNING: Removing a Source message will remove all of its destinations.</html>")) {
            final String workingId = startWorking("Removing messages...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {
                    try {
                        mirthClient.removeMessages(messageBrowser.getChannelId(), messageBrowser.getMessageFilter());
                    } catch (ClientException e) {
                        if (e.getCause() instanceof RequestAbortedException) {
                            // The client is no longer waiting for the delete request
                        } else {
                            alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                        }
                    }
                    return null;
                }

                public void done() {
                    if (currentContentPage == dashboardPanel) {
                        doRefreshStatuses(true);
                    } else if (currentContentPage == messageBrowser) {
                        messageBrowser.refresh(1, true);
                    }
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doRemoveMessage() {
        final Integer metaDataId = messageBrowser.getSelectedMetaDataId();
        final Long messageId = messageBrowser.getSelectedMessageId();
        final String channelId = messageBrowser.getChannelId();

        if (alertOption(this, "<html>Are you sure you would like to remove the selected message?<br>Channel must be stopped for an unfinished message to be removed.<br><font size='1'><br></font>WARNING: Removing a Source message will remove all of its destinations.</html>")) {
            final String workingId = startWorking("Removing message...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {
                    try {
                        MessageFilter filter = new MessageFilter();
                        filter.setMessageIdLower(messageId);
                        filter.setMessageIdUpper(messageId);
                        List<Integer> metaDataIds = new ArrayList<Integer>();
                        metaDataIds.add(metaDataId);
                        filter.setIncludedMetaDataIds(metaDataIds);
                        mirthClient.removeMessages(channelId, filter);
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                    }
                    return null;
                }

                public void done() {
                    if (currentContentPage == dashboardPanel) {
                        doRefreshStatuses(true);
                    } else if (currentContentPage == messageBrowser) {
                        messageBrowser.refresh(null, false);
                    }
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doReprocessFilteredMessages() {
        doReprocess(messageBrowser.getMessageFilter(), null);
    }

    public void doReprocessMessage() {
        Long messageId = messageBrowser.getSelectedMessageId();

        if (messageBrowser.canReprocessMessage(messageId)) {
            MessageFilter filter = new MessageFilter();
            filter.setMessageIdLower(messageId);
            filter.setMessageIdUpper(messageId);
            doReprocess(filter, messageBrowser.getSelectedMetaDataId());
        } else {
            alertError(this, "Message " + messageId + " cannot be reprocessed because no source raw content was found.");
        }
    }

    private void doReprocess(final MessageFilter filter, final Integer selectedMetaDataId) {
        final String workingId = startWorking("Retrieving Channels...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                if (channels == null || channels.values().size() == 0) {
                    retrieveChannels();
                }

                return null;
            }

            public void done() {
                stopWorking(workingId);
                Map<Integer, String> destinationConnectors = new LinkedHashMap<Integer, String>();
                destinationConnectors.putAll(dashboardPanel.getDestinationConnectorNames(messageBrowser.getChannelId()));
                new ReprocessMessagesDialog(messageBrowser.getChannelId(), filter, destinationConnectors, selectedMetaDataId);
            }
        };

        worker.execute();
    }

    public void reprocessMessage(final String channelId, final MessageFilter filter, final boolean replace, final List<Integer> reprocessMetaDataIds) {
        final String workingId = startWorking("Reprocessing messages...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                try {
                    mirthClient.reprocessMessages(channelId, filter, replace, reprocessMetaDataIds);
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }
                return null;
            }

            public void done() {
                messageBrowser.updateFilterButtonFont(Font.BOLD);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void viewImage() {
        final String workingId = startWorking("Opening attachment...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                messageBrowser.viewAttachment();
                stopWorking(workingId);
                return null;
            }

            public void done() {
                stopWorking(workingId);
            }
        };
        worker.execute();
    }

    public void processMessage(final String channelId, final String message, final List<Integer> metaDataIds) {
        final String workingId = startWorking("Processing message...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                try {
                    mirthClient.processMessage(channelId, message, metaDataIds);
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }
                return null;
            }

            public void done() {
                if (currentContentPage == messageBrowser) {
                    messageBrowser.updateFilterButtonFont(Font.BOLD);
                }

                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doRefreshEvents() {
        eventBrowser.refresh(null);
    }

    public void doRemoveAllEvents() {
        int option = JOptionPane.showConfirmDialog(this, "All events will be removed. Would you also like them to be\n" + "exported to a file on the server?");
        if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
            return;
        }

        final boolean export = (option == JOptionPane.YES_OPTION);

        final String workingId = startWorking("Clearing events...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            private String exportPath = null;

            public Void doInBackground() {
                try {
                    if (export) {
                        exportPath = mirthClient.exportAndRemoveAllEvents();
                    } else {
                        mirthClient.removeAllEvents();
                    }
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }
                return null;
            }

            public void done() {
                eventBrowser.runSearch();

                if (exportPath != null) {
                    alertInformation(PlatformUI.MIRTH_FRAME, "Events have been exported to the following server path:\n" + exportPath);
                }

                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doExportAllEvents() {
        if (alertOption(this, "Are you sure you would like to export all events? An export\n" + "file will be placed in the exports directory on the server.")) {
            final String workingId = startWorking("Exporting events...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                private String exportPath = null;

                public Void doInBackground() {
                    try {
                        exportPath = mirthClient.exportAllEvents();
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                    }
                    return null;
                }

                public void done() {
                    if (exportPath != null) {
                        alertInformation(PlatformUI.MIRTH_FRAME, "Events have been exported to the following server path:\n" + exportPath);
                    }

                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doRefreshAlerts() {
        doRefreshAlerts(true);
    }

    public void doRefreshAlerts(boolean queue) {
        // Don't allow anything to be getting or setting refreshingAlerts
        // while this block is being executed.
        synchronized (this) {
            if (isRefreshingAlerts()) {
                if (queue) {
                    queueRefreshAlert = true;
                }
                return;
            }

            setRefreshingAlerts(true);
        }

        final String workingId = startWorking("Loading alerts...");

        final List<String> selectedAlertIds = alertPanel.getSelectedAlertIds();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            private List<AlertStatus> alertStatusList;

            public Void doInBackground() {
                try {
                    alertStatusList = mirthClient.getAlertStatusList();
                } catch (ClientException e) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }
                return null;
            }

            public void done() {
                alertPanel.updateAlertTable(alertStatusList);
                alertPanel.setSelectedAlertIds(selectedAlertIds);
                stopWorking(workingId);

                setRefreshingAlerts(false);

                // Perform another refresh if any were queued up to ensure the alert dashboard is up to date.
                if (queueRefreshAlert) {
                    queueRefreshAlert = false;
                    doRefreshAlerts(false);
                }
            }
        };

        worker.execute();
    }

    public void doSaveAlerts() {
        if (changesHaveBeenMade()) {
            try {
                ServerSettings serverSettings = mirthClient.getServerSettings();
                if (StringUtils.isBlank(serverSettings.getSmtpHost()) || StringUtils.isBlank(serverSettings.getSmtpPort())) {
                    alertWarning(PlatformUI.MIRTH_FRAME, "The SMTP server on the settings page is not specified or is incomplete.  An SMTP server is required to send alerts.");
                }
            } catch (ClientException e) {
                if (!(e.getCause() instanceof UnauthorizedException)) {
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }
            }

            final String workingId = startWorking("Saving alerts...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                public Void doInBackground() {
                    if (alertEditPanel.saveAlert()) {
                        setSaveEnabled(false);
                    }

                    return null;
                }

                public void done() {
                    stopWorking(workingId);
                }
            };

            worker.execute();
        }
    }

    public void doDeleteAlert() {
        if (!alertOption(this, "Are you sure you want to delete the selected alert(s)?")) {
            return;
        }

        final String workingId = startWorking("Deleting alert...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                List<String> selectedAlertIds = alertPanel.getSelectedAlertIds();

                for (String alertId : selectedAlertIds) {
                    try {
                        mirthClient.removeAlert(alertId);
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                        return null;
                    }
                }

                return null;
            }

            public void done() {
                doRefreshAlerts(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doNewAlert() {
        retrieveChannels();
        setupAlert();
    }

    public void doEditAlert() {
        if (isEditingAlert) {
            return;
        } else {
            isEditingAlert = true;
        }

        List<String> selectedAlertIds = alertPanel.getSelectedAlertIds();
        if (selectedAlertIds.size() > 1) {
            JOptionPane.showMessageDialog(Frame.this, "This operation can only be performed on a single alert.");
        } else if (selectedAlertIds.size() == 0) {
            JOptionPane.showMessageDialog(Frame.this, "Alert no longer exists.");
        } else {
            try {
                List<AlertModel> alerts = mirthClient.getAlert(selectedAlertIds.get(0));

                if (alerts == null || alerts.isEmpty()) {
                    JOptionPane.showMessageDialog(Frame.this, "Alert no longer exists.");
                    doRefreshAlerts(true);
                } else {
                    retrieveChannels();
                    editAlert(alerts.get(0));
                }
            } catch (ClientException e) {
                alertException(this, e.getStackTrace(), e.getMessage());
            }
        }
        isEditingAlert = false;
    }

    public void doEnableAlert() {
        final String workingId = startWorking("Enabling alert...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                List<String> selectedAlertIds = alertPanel.getSelectedAlertIds();

                for (String alertId : selectedAlertIds) {
                    try {
                        mirthClient.enableAlert(alertId);
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                        return null;
                    }
                }

                return null;
            }

            public void done() {
                doRefreshAlerts(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doDisableAlert() {
        final String workingId = startWorking("Enabling alert...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                List<String> selectedAlertIds = alertPanel.getSelectedAlertIds();

                for (String alertId : selectedAlertIds) {
                    try {
                        mirthClient.disableAlert(alertId);
                    } catch (ClientException e) {
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                        return null;
                    }
                }

                return null;
            }

            public void done() {
                doRefreshAlerts(true);
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doExportAlert() {
        if (changesHaveBeenMade()) {
            if (alertOption(this, "This alert has been modified. You must save the alert changes before you can export. Would you like to save them now?")) {
                if (!alertEditPanel.saveAlert()) {
                    return;
                }
            } else {
                return;
            }

            setSaveEnabled(false);
        }

        List<String> selectedAlertIds;

        if (currentContentPage == alertEditPanel) {
            selectedAlertIds = new ArrayList<String>();

            String alertId = alertEditPanel.getAlertId();
            if (alertId != null) {
                selectedAlertIds.add(alertId);
            }
        } else {
            selectedAlertIds = alertPanel.getSelectedAlertIds();
        }

        if (CollectionUtils.isEmpty(selectedAlertIds)) {
            return;
        }

        List<AlertModel> alerts;
        try {
            alerts = mirthClient.getAlert(selectedAlertIds.get(0));
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
            return;
        }

        AlertModel alert;
        if (CollectionUtils.isEmpty(alerts)) {
            JOptionPane.showMessageDialog(Frame.this, "Alert no longer exists.");
            doRefreshAlerts(true);
        } else {
            alert = alerts.get(0);
            ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
            String alertXML = serializer.serialize(alert);

            exportFile(alertXML, alert.getName(), "XML", "Alert");
        }
    }

    public void doExportAlerts() {
        JFileChooser exportFileChooser = new JFileChooser();
        exportFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        File currentDir = new File(userPreferences.get("currentDirectory", ""));
        if (currentDir.exists()) {
            exportFileChooser.setCurrentDirectory(currentDir);
        }

        int returnVal = exportFileChooser.showSaveDialog(this);
        File exportFile = null;
        File exportDirectory = null;

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            userPreferences.put("currentDirectory", exportFileChooser.getCurrentDirectory().getPath());
            try {
                exportDirectory = exportFileChooser.getSelectedFile();

                List<AlertModel> alerts;
                try {
                    alerts = mirthClient.getAlert(null);
                } catch (ClientException e) {
                    alertException(this, e.getStackTrace(), e.getMessage());
                    return;
                }

                for (AlertModel alert : alerts) {
                    ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
                    String channelXML = serializer.serialize(alert);

                    exportFile = new File(exportDirectory.getAbsolutePath() + "/" + alert.getName() + ".xml");

                    if (exportFile.exists()) {
                        if (!alertOption(this, "The file " + alert.getName() + ".xml already exists.  Would you like to overwrite it?")) {
                            continue;
                        }
                    }

                    FileUtils.writeStringToFile(exportFile, channelXML, UIConstants.CHARSET);
                }
                alertInformation(this, "All files were written successfully to " + exportDirectory.getPath() + ".");
            } catch (IOException ex) {
                alertError(this, "File could not be written.");
            }
        }
    }

    public void doImportAlert() {
        String content = browseForFileString("XML");

        if (content != null) {
            importAlert(content, true);
        }
    }

    public void importAlert(String alertXML, boolean showAlerts) {
        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        List<AlertModel> alertList;

        try {
            alertList = (List<AlertModel>) serializer.deserializeList(alertXML.replaceAll("\\&\\#x0D;\\n", "\n").replaceAll("\\&\\#x0D;", "\n"), AlertModel.class);
        } catch (Exception e) {
            if (showAlerts) {
                alertException(this, e.getStackTrace(), "Invalid alert file:\n" + e.getMessage());
            }
            return;
        }

        removeInvalidItems(alertList, AlertModel.class);

        for (AlertModel importAlert : alertList) {
            try {
                String alertName = importAlert.getName();
                String tempId = mirthClient.getGuid();

                // Check to see that the alert name doesn't already exist.
                if (!checkAlertName(alertName)) {
                    if (!alertOption(this, "Would you like to overwrite the existing alert?  Choose 'No' to create a new alert.")) {
                        do {
                            alertName = JOptionPane.showInputDialog(this, "Please enter a new name for the channel.", alertName);
                            if (alertName == null) {
                                return;
                            }
                        } while (!checkAlertName(alertName));

                        importAlert.setName(alertName);
                        importAlert.setId(tempId);
                    } else {
                        for (Entry<String, String> entry : alertPanel.getAlertNames().entrySet()) {
                            String id = entry.getKey();
                            String name = entry.getValue();
                            if (name.equalsIgnoreCase(alertName)) {
                                // If overwriting, use the old id
                                importAlert.setId(id);
                            }
                        }
                    }
                }

                mirthClient.updateAlert(importAlert);
            } catch (Exception e) {
                alertException(this, e.getStackTrace(), "Error importing alert:\n" + e.getMessage());
            }
        }

        doRefreshAlerts(true);
    }

    /**
     * Checks to see if the passed in channel name already exists
     */
    public boolean checkAlertName(String name) {
        if (name.equals("")) {
            alertWarning(this, "Channel name cannot be empty.");
            return false;
        }

        Pattern alphaNumericPattern = Pattern.compile("^[a-zA-Z_0-9\\-\\s]*$");
        Matcher matcher = alphaNumericPattern.matcher(name);

        if (!matcher.find()) {
            alertWarning(this, "Channel name cannot have special characters besides hyphen, underscore, and space.");
            return false;
        }

        for (String alertName : alertPanel.getAlertNames().values()) {
            if (alertName.equalsIgnoreCase(name)) {
                alertWarning(this, "Alert \"" + name + "\" already exists.");
                return false;
            }
        }
        return true;
    }

    public void doRefreshCodeTemplates() {
        final String workingId = startWorking("Loading code templates...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                refreshCodeTemplates();
                return null;
            }

            public void done() {
                codeTemplatePanel.updateCodeTemplateTable();
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void refreshCodeTemplates() {
        try {
            codeTemplates = mirthClient.getCodeTemplate(null);
        } catch (ClientException e) {
            // If the user is unauthorized and it's the first time (startup, when
            // codeTemplates is null), then initialize the code templates.
            if (e.getCause() instanceof UnauthorizedException && codeTemplates == null) {
                codeTemplates = new ArrayList<CodeTemplate>();
            } else {
                alertException(this, e.getStackTrace(), e.getMessage());
            }
        }
    }

    public void doSaveCodeTemplates() {
        final String workingId = startWorking("Saving codeTemplates...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            public Void doInBackground() {
                saveCodeTemplates();
                return null;
            }

            public void done() {
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public boolean saveCodeTemplates() {
        try {
            codeTemplatePanel.saveCodeTemplate();

            for (CodeTemplate template : codeTemplates) {
                if (template.getType() == CodeSnippetType.FUNCTION) {
                    if (!codeTemplatePanel.validateCodeTemplate(template.getCode(), false, template.getName())) {
                        return false;
                    }
                }
            }
            mirthClient.updateCodeTemplates(codeTemplates);
            ReferenceListFactory.getInstance().updateUserTemplates();
            setSaveEnabled(false);
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
            return false;
        }
        return true;
    }

    public void doDeleteCodeTemplate() {
        codeTemplatePanel.deleteCodeTemplate();
    }

    public void doValidateCodeTemplate() {
        codeTemplatePanel.validateCodeTemplate();
    }

    public void doNewCodeTemplate() {
        codeTemplatePanel.addCodeTemplate();
    }

    public void doExportCodeTemplates() {
        if (changesHaveBeenMade()) {
            if (alertOption(this, "Would you like to save the changes made to the code templates?")) {
                saveCodeTemplates();
            } else {
                return;
            }
        }

        ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
        String codeTemplateXML = serializer.serialize(codeTemplates);

        exportFile(codeTemplateXML, null, "XML", "Code templates export");
    }

    public void doImportCodeTemplates() {
        String content = browseForFileString("XML");

        if (content != null) {
            try {
                ObjectXMLSerializer serializer = ObjectXMLSerializer.getInstance();
                boolean append = false;

                List<CodeTemplate> newCodeTemplates = serializer.deserializeList(content, CodeTemplate.class);

                removeInvalidItems(newCodeTemplates, CodeTemplate.class);

                if (newCodeTemplates.size() > 0) {
                    if (codeTemplates != null && codeTemplates.size() > 0) {
                        if (alertOption(this, "Would you like to append these code templates to the existing code templates?")) {
                            append = true;
                        }
                    }

                    if (append) {
                        for (CodeTemplate newCodeTemplate : newCodeTemplates) {
                            newCodeTemplate.setId(UUID.randomUUID().toString());

                            // make sure the name doesn't already exist
                            for (CodeTemplate codeTemplate : codeTemplates) {
                                // If the name already exists, generate a new unique name
                                if (codeTemplate.getName().equalsIgnoreCase(newCodeTemplate.getName())) {
                                    String newCodeTemplateName = "Template ";

                                    boolean uniqueName = false;
                                    int i = 0;
                                    while (!uniqueName) {
                                        i++;
                                        uniqueName = true;
                                        for (CodeTemplate codeTemplateLookup : codeTemplates) {
                                            if (codeTemplateLookup.getName().equalsIgnoreCase(newCodeTemplateName + i)) {
                                                uniqueName = false;
                                            }
                                        }
                                    }

                                    newCodeTemplate.setName(newCodeTemplateName + i);
                                }
                            }

                            codeTemplates.add(newCodeTemplate);
                        }
                    } else {
                        codeTemplates = newCodeTemplates;
                    }
    
                    alertInformation(this, "All code templates imported successfully.");
    
                    setSaveEnabled(true);

                    // If appending, just deselect the rows, which saves 
                    // the state of the last selected row.
                    // If replacing, set isDeletingAlert so the state is 
                    // not saved while the alert is being removed.
                    if (append) {
                        codeTemplatePanel.deselectCodeTemplateRows();
                    } else {
                        codeTemplatePanel.isDeleting = true;
                        codeTemplatePanel.deselectCodeTemplateRows();
                        codeTemplatePanel.isDeleting = false;
                    }
    
                    codeTemplatePanel.updateCodeTemplateTable();
                }
            } catch (Exception e) {
                alertException(this, e.getStackTrace(), "Invalid code template file: " + e.getMessage());
            }
        }
    }

    ///// Start Extension Tasks /////
    public void doRefreshExtensions() {
        final String workingId = startWorking("Loading extension settings...");

        if (confirmLeave()) {
            refreshExtensions();
        }

        stopWorking(workingId);
    }

    public void refreshExtensions() {
        extensionsPanel.setPluginData(getPluginMetaData());
        extensionsPanel.setConnectorData(getConnectorMetaData());
    }

    public void doCheckForUpdates() {
        try {
            new ExtensionUpdateDialog();
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
        }
    }

    public void doEnableExtension() {
        final String workingId = startWorking("Enabling extension...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private boolean success = true;

            public Void doInBackground() {
                try {
                    mirthClient.setExtensionEnabled(extensionsPanel.getSelectedExtension().getName(), true);
                } catch (ClientException e) {
                    success = false;
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }

                return null;
            }

            public void done() {
                if (success) {
                    extensionsPanel.setSelectedExtensionEnabled(true);
                    extensionsPanel.setRestartRequired(true);
                }
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doDisableExtension() {
        final String workingId = startWorking("Disabling extension...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private boolean success = true;

            public Void doInBackground() {
                try {
                    mirthClient.setExtensionEnabled(extensionsPanel.getSelectedExtension().getName(), false);
                } catch (ClientException e) {
                    success = false;
                    alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                }

                return null;
            }

            public void done() {
                if (success) {
                    extensionsPanel.setSelectedExtensionEnabled(false);
                    extensionsPanel.setRestartRequired(true);
                }
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public void doShowExtensionProperties() {
        extensionsPanel.showExtensionProperties();
    }

    public void doUninstallExtension() {
        final String workingId = startWorking("Uninstalling extension...");

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private boolean success = true;

            public Void doInBackground() {
                String packageName = extensionsPanel.getSelectedExtension().getPath();

                if (alertOkCancel(PlatformUI.MIRTH_FRAME, "Uninstalling this extension will remove all plugins and/or connectors\nin the following extension folder: " + packageName)) {
                    try {
                        mirthClient.uninstallExtension(packageName);
                    } catch (ClientException e) {
                        success = false;
                        alertException(PlatformUI.MIRTH_FRAME, e.getStackTrace(), e.getMessage());
                    }
                }

                return null;
            }

            public void done() {
                if (success) {
                    extensionsPanel.setRestartRequired(true);
                }
                stopWorking(workingId);
            }
        };

        worker.execute();
    }

    public boolean installExtension(File file) {
        try {
            if (file.exists()) {
                mirthClient.installExtension(file);
            } else {
                alertError(this, "Invalid extension file.");
                return false;
            }
        } catch (Exception e) {
            String errorMessage = "Unable to install extension.";
            try {
                String tempErrorMessage = java.net.URLDecoder.decode(e.getMessage(), "UTF-8");
                String versionError = "VersionMismatchException: ";
                int messageIndex = tempErrorMessage.indexOf(versionError);

                if (messageIndex != -1) {
                    errorMessage = tempErrorMessage.substring(messageIndex + versionError.length());
                }

            } catch (UnsupportedEncodingException e1) {
                alertException(this, e1.getStackTrace(), e1.getMessage());
            }

            alertError(this, errorMessage);

            return false;
        }
        return true;
    }

    ///// End Extension Tasks /////

    public boolean exportChannelOnError() {
        if (isSaveEnabled()) {
            int option = JOptionPane.showConfirmDialog(this, "Would you like to save the channel changes locally to your computer?");
            if (option == JOptionPane.YES_OPTION) {
                if (!channelEditPanel.saveChanges()) {
                    return false;
                }

                boolean enabled = isSaveEnabled();
                setSaveEnabled(false);
                if (!doExportChannel()) {
                    setSaveEnabled(enabled);
                    return false;
                }
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return false;
            } else {
                setSaveEnabled(false);
            }
        }
        return true;
    }

    public void doContextSensitiveSave() {
        if (currentContentPage == channelEditPanel) {
            doSaveChannel();
        } else if (currentContentPage == channelEditPanel.filterPane) {
            doSaveChannel();
        } else if (currentContentPage == channelEditPanel.transformerPane) {
            doSaveChannel();
        } else if (currentContentPage == globalScriptsPanel) {
            doSaveGlobalScripts();
        } else if (currentContentPage == codeTemplatePanel) {
            doSaveCodeTemplates();
        } else if (currentContentPage == settingsPane) {
            settingsPane.getCurrentSettingsPanel().doSave();
        } else if (currentContentPage == alertEditPanel) {
            doSaveAlerts();
        }
    }

    public void doFind(JEditTextArea text) {
        FindRplDialog find;
        Window owner = getWindowForComponent(text);

        if (owner instanceof java.awt.Frame) {
            find = new FindRplDialog((java.awt.Frame) owner, true, text);
        } else { // window instanceof Dialog
            find = new FindRplDialog((java.awt.Dialog) owner, true, text);
        }

        find.setVisible(true);
    }

    public void doHelp() {
        BareBonesBrowserLaunch.openURL(UIConstants.HELP_LOCATION);
    }

    public Map<String, PluginMetaData> getPluginMetaData() {
        return this.loadedPlugins;
    }

    public Map<String, ConnectorMetaData> getConnectorMetaData() {
        return this.loadedConnectors;
    }

    public String getSelectedChannelIdFromDashboard() {
        return dashboardPanel.getSelectedStatuses().get(0).getChannelId();
    }

    public Channel getSelectedChannelFromDashboard() {
        retrieveChannels();
        return channels.get(getSelectedChannelIdFromDashboard());
    }

    public List<Integer> getSelectedMetaDataIdsFromDashboard(String channelId) {
        List<DashboardStatus> selectedStatuses = dashboardPanel.getSelectedStatuses();
        List<Integer> metaDataIds = new ArrayList<Integer>();

        if (selectedStatuses.size() == 0) {
            return metaDataIds;
        }

        for (DashboardStatus status : selectedStatuses) {
            if (status.getChannelId() == channelId) {
                Integer metaDataId = status.getMetaDataId();

                if (metaDataId != null) {
                    metaDataIds.add(metaDataId);
                }
            }
        }

        return metaDataIds;
    }

    public void retrieveUsers() throws ClientException {
        users = mirthClient.getUser(null);
    }

    public synchronized void updateAcceleratorKeyPressed(InputEvent e) {
        this.acceleratorKeyPressed = (((e.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) > 0) || ((e.getModifiers() & InputEvent.CTRL_MASK) > 0) || ((e.getModifiers() & InputEvent.ALT_MASK) > 0));
    }

    public synchronized boolean isAcceleratorKeyPressed() {
        return acceleratorKeyPressed;
    }

    public void retrieveAllChannelTags() {
        try {
            allChannelTags = mirthClient.getChannelTags();
        } catch (ClientException e) {
            alertException(this, e.getStackTrace(), e.getMessage());
        }
    }

    public Set<String> getAllChannelTags() {
        return allChannelTags;
    }

    /**
     * Checks to see if the serialized object version is current, and prompts
     * the user if it is not.
     */
    private boolean promptObjectMigration(String content, String objectName) {
        String version = null;

        try {
            version = MigrationUtil.normalizeVersion(MigrationUtil.getSerializedObjectVersion(content), 3);
        } catch (Exception e) {
            logger.error("Failed to read version information", e);
        }

        StringBuilder message = new StringBuilder();

        if (version == null) {
            message.append("The " + objectName + " being imported is from an unknown version of Mirth Connect.\n");
        } else {
            int comparison = MigrationUtil.compareVersions(version, PlatformUI.SERVER_VERSION);

            if (comparison == 0) {
                return true;
            }

            if (comparison > 0) {
                alertInformation(this, "The " + objectName + " being imported originated from Mirth version " + version + ".\nYou are using Mirth Connect version " + PlatformUI.SERVER_VERSION + ".\nThe " + objectName + " cannot be imported, because it originated from a newer version of Mirth Connect.");
                return false;
            }

            if (comparison < 0) {
                message.append("The " + objectName + " being imported originated from Mirth version " + version + ".\n");
            }
        }

        message.append("You are using Mirth Connect version " + PlatformUI.SERVER_VERSION + ".\nWould you like to automatically convert the " + objectName + " to the " + PlatformUI.SERVER_VERSION + " format?");
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, message.toString(), "Select an Option", JOptionPane.YES_NO_OPTION);
    }

    /**
     * Removes items from the list that are not of the expected class.
     */
    private void removeInvalidItems(List<?> list, Class<?> expectedClass) {
        int originalSize = list.size();

        for (int i = 0; i < list.size(); i++) {
            if (!expectedClass.isInstance(list.get(i))) {
                list.remove(i--);
            }
        }

        if (list.size() < originalSize) {
            if (list.size() == 0) {
                alertError(this, "The imported object(s) are not of the expected class: " + expectedClass.getSimpleName());
            } else {
                alertError(this, "One or more imported objects were skipped, because they are not of the expected class: " + expectedClass.getSimpleName());
            }
        }
    }
}