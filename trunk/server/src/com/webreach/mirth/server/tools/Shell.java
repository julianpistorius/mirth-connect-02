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

package com.webreach.mirth.server.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.webreach.mirth.client.core.Client;
import com.webreach.mirth.client.core.ClientException;
import com.webreach.mirth.model.Channel;
import com.webreach.mirth.model.ChannelStatistics;
import com.webreach.mirth.model.ChannelStatus;
import com.webreach.mirth.model.ChannelSummary;
import com.webreach.mirth.model.SystemEvent;
import com.webreach.mirth.model.ChannelStatus.State;
import com.webreach.mirth.model.converters.ObjectXMLSerializer;
import com.webreach.mirth.model.filters.SystemEventFilter;
import com.webreach.mirth.model.util.ImportConverter;
import com.webreach.mirth.server.controllers.ChannelController;

public class Shell {
	private Client client;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy_HH-mm-ss.SS");

	public static void main(String[] args) {
		Shell shell = new Shell();
		shell.run(args);
	}

	private void run(String[] args) {
		Option serverOption = OptionBuilder.withArgName("address").hasArg().withDescription("server address").create("a");
		Option userOption = OptionBuilder.withArgName("user").hasArg().withDescription("user login").create("u");
		Option passwordOption = OptionBuilder.withArgName("password").hasArg().withDescription("user password").create("p");
		Option scriptOption = OptionBuilder.withArgName("script").hasArg().withDescription("script file").create("s");
		Option helpOption = new Option("h", "help");

		Options options = new Options();
		options.addOption(serverOption);
		options.addOption(userOption);
		options.addOption(passwordOption);
		options.addOption(helpOption);
		options.addOption(scriptOption);

		CommandLineParser parser = new GnuParser();

		try {
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("a") && line.hasOption("u") && line.hasOption("p")) {
				String server = line.getOptionValue("a");
				String user = line.getOptionValue("u");
				String password = line.getOptionValue("p");
				

				try {
					client = new Client(server);
					if (line.hasOption("s")){
						runScript(line, server, user, password);
					}else{
						if (client.login(user, password)) {
							System.out.println("Connected to Mirth server @ " + server + " (" + client.getVersion() + ")");
							BufferedReader reader = new BufferedReader(new InputStreamReader( System.in ));
							
							String statement = null;

							try {
								while (!(statement = reader.readLine()).equalsIgnoreCase("quit")) {
									try{
										executeStatement(statement);
									}catch (Exception e){
										System.out.println("Invalid statment. Use \"help\" for list of valid statements");
									}
								}
							} finally {
								reader.close();
							}
						} else {
							System.out.println("Error: Could not login to server.");
						}
					}
					client.logout();
					System.out.println("Disconnected from server.");
				} catch (ClientException ce) {
					ce.printStackTrace();
				} catch (IOException ioe) {
					System.out.println("Error: Could not load script file.");
				}
			} else if (line.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("Shell", options);
			}
		} catch (ParseException e) {
			System.err.println("Error: Could not parse input arguments.");
		}
	}

	private void runScript(CommandLine line, String server, String user, String password) throws ClientException, FileNotFoundException, IOException {
		String script = line.getOptionValue("s");
		if (client.login(user, password)) {
			System.out.println("Connected to Mirth server @ " + server + " (" + client.getVersion() + ")");

			BufferedReader reader = new BufferedReader(new FileReader(script));
			String statement = null;

			try {
				while ((statement = reader.readLine()) != null) {
					System.out.println("Executing statement: " + statement);
					executeStatement(statement);
				}
			} finally {
				reader.close();
			}
		} else {
			System.out.println("Error: Could not login to server.");
		}
	}

	private void executeStatement(String command) {
		try {
			if (command.startsWith("#")){
				//Comments
				return;
			}
			String[] arguments = command.split(" ");
			ArrayList<String> newArgs = new ArrayList<String>();
			StringBuilder quotedString = new StringBuilder();
			boolean sawQuote = false;
			for (int i = 0; i < arguments.length; i++) {
				if (arguments[i].indexOf('\"') > -1){
					if (sawQuote){
						//close the quotes
						sawQuote = false;
						quotedString.append(" ");
						quotedString.append(arguments[i].replace("\"", ""));
						newArgs.add(quotedString.toString());
					}else{
						quotedString = new StringBuilder();
						String arg = arguments[i].replaceFirst("\"", "");
						if (arg.indexOf('\"') > -1){
							newArgs.add(arg.replaceAll("\"", ""));
							sawQuote = false;
						}else{
							sawQuote = true;
							quotedString.append(arg);
						}
						
						
					}
				}else{
					if (sawQuote){
						quotedString.append(" ");
						quotedString.append(arguments[i]);
					}else{
						newArgs.add(arguments[i]);
					}
				}
			}
			
			arguments = new String[newArgs.size()];
			newArgs.toArray(arguments);
			if (arguments.length >= 1) {
				String arg1 = arguments[0];
				if (arg1.equals("help")){
					System.out.println("Available Commands:");
					System.out.println("status\t\t\t\t\t\tReturns status of deployed channels");
					System.out.println("start\t\t\t\t\t\tStarts all Channels");
					System.out.println("stop\t\t\t\t\t\tStops all Channels");
					System.out.println("pause\t\t\t\t\t\tPauses all Channels");
					System.out.println("resume\t\t\t\t\t\tResumes all Channels");
					System.out.println("deploy\t\t\t\t\t\tDeploys all Channels");
					System.out.println("import \"path\"\t\t\t\t\tImports channel specified by <path>");
					System.out.println("export id|\"name\"|* \"path\"\t\t\tExports the specified channel to <path>");
					System.out.println("channel start|stop|pause|resume|* id|\"name\"\tPerforms specified channel action");
					System.out.println("channel stats|export|* id|\"name\"\t\t\tPerforms specified channel action");
					System.out.println("channel remove|enable|disable|* id|\"name\"\t\tRemove, enable or disable specified channel");
					System.out.println("channel list\t\t\t\t\tLists all Channels");
					System.out.println("clear\t\t\t\t\t\tRemoves all messages from all Channels");
					System.out.println("dump stats|events \"path\"\t\t\tDumps stats or events to specified file");
					System.out.println("quit\t\t\t\t\t\tQuits Mirth Shell");
					return;
				}
				else if (arg1.equalsIgnoreCase("start") || arg1.equalsIgnoreCase("stop")|| arg1.equalsIgnoreCase("pause") || arg1.equalsIgnoreCase("resume")) {
					List<ChannelStatus> channels = client.getChannelStatusList();

					for (Iterator<ChannelStatus> iter = channels.iterator(); iter.hasNext();) {
						ChannelStatus channel = iter.next();

						if (arg1.equals("start") && (channel.getState().equals(State.STOPPED) || channel.getState().equals(State.PAUSED))) {
							if (channel.getState().equals(State.PAUSED)){
								client.resumeChannel(channel.getChannelId());
								System.out.println("Channel " + channel.getName() + " Resumed");
							}else{
								client.startChannel(channel.getChannelId());
								System.out.println("Channel " + channel.getName() + " Started");
							}
							
						} else if (arg1.equals("stop") && (channel.getState().equals(State.STARTED) || channel.getState().equals(State.PAUSED))) {
							client.stopChannel(channel.getChannelId());
							System.out.println("Channel " + channel.getName() + " Stopped");
						}else if (arg1.equals("pause") && channel.getState().equals(State.STARTED)) {
							client.pauseChannel(channel.getChannelId());
							System.out.println("Channel " + channel.getName() + " Paused");
						}else if (arg1.equals("resume") && channel.getState().equals(State.PAUSED)) {
							client.resumeChannel(channel.getChannelId());
							System.out.println("Channel " + channel.getName() + " Resumed");
						}
					}
				} else if(arg1.equalsIgnoreCase("deploy")){
					System.out.println("Deploying Channels");
					List<Channel> channels = client.getChannel(null);
					
					boolean hasChannels = false;
					for (Iterator iter = channels.iterator(); iter.hasNext();) {
						Channel channel = (Channel) iter.next();
						if (channel.isEnabled()){
							hasChannels = true;
							break;
						}
					}
					client.deployChannels();
					if (hasChannels){
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						List<ChannelStatus> channelStatus = client.getChannelStatusList();
						while (channelStatus.size() == 0){
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							channelStatus = client.getChannelStatusList();
						}
					}
					System.out.println("Channels Deployed");
				}else if (arg1.equalsIgnoreCase("import")) {
					String path = arguments[1];
					File fXml = new File(path);
					if (!fXml.exists()){
						System.out.println(path + " not found");
						return;
					}else if (!fXml.canRead()){
						System.out.println("Can not read " + path);
						return;
					}else{
						doImportChannel(fXml);
					}
					
				} else if (arg1.equalsIgnoreCase("status")){
					
					System.out.println("ID\t\t\t\t\tStatus\t\tName");
					List<ChannelStatus> channels = client.getChannelStatusList();
					for (Iterator<ChannelStatus> iter = channels.iterator(); iter.hasNext();) {
						ChannelStatus channel = iter.next();
						
						System.out.println(channel.getChannelId() + "\t"  + channel.getState().toString() + "\t\t" + channel.getName());
					}
					return;
				}else if (arg1.equalsIgnoreCase("export")) {
					if (arguments.length < 3){
						System.out.println("Invalid number of arguments. Syntax is: export id|name|all \"path\"");
						return;
					}
					
					String key = arguments[1];
					String path = arguments[2];
					ObjectXMLSerializer serializer = new ObjectXMLSerializer();
					List<Channel> channels = client.getChannel(null);
					if (key.equalsIgnoreCase("*")){
						for (Iterator iter = channels.iterator(); iter.hasNext();) {
							try {
								Channel channel = (Channel) iter.next();
								File fXml = new File(path + channel.getName() + ".xml");
								System.out.println("Exporting " + channel.getName());
								String channelXML = serializer.toXML(channel);
								writeFile(fXml, channelXML);
							} catch (IOException e) {
								System.out.println("Error Writing File " + path);
							}
						}
						System.out.println("Export Complete.");
						return;
					}else{
						File fXml = new File(path);
						
						for (Iterator iter = channels.iterator(); iter.hasNext();) {
							Channel channel = (Channel) iter.next();
							if (channel.getName().equalsIgnoreCase(key) != channel.getId().equalsIgnoreCase(key)){
								System.out.println("Exporting " + channel.getName());
								String channelXML = serializer.toXML(channel);
								try {
									writeFile(fXml, channelXML);
								} catch (IOException e) {
									System.out.println("Error Writing File " + path);
								}
								System.out.println("Export Complete.");
								return;
							}
						}
					}
					
					
			        
					
				}  else if (arg1.equalsIgnoreCase("channel")) {
					if (arguments.length < 2){
						System.out.println("Invalid number of arguments. Syntax is: channel start|stop|pause|resume|stats|remove|enable|disable|export|list id|\"name\"");
						return;
					}else if (arguments.length < 3 && !arguments[1].equalsIgnoreCase("list") && !arguments[1].equalsIgnoreCase("stats")){
						System.out.println("Invalid number of arguments. Syntax is: channel start|stop|pause|resume|stats|remove|enable|disable|export|list id|\"name\"");
						return;
					}
					
					String comm = arguments[1];
					

					if (comm.equalsIgnoreCase("stats") && arguments.length < 3){
						//System.out.println("Mirth Channel Statistics Dump: " + (new Date()).toString() + "\n");
						System.out.println("Received\tFiltered\tSent\t\tError\t\tName");

						List<Channel> channels = client.getChannel(null);
					
						for (Iterator iter = channels.iterator(); iter.hasNext();) {
							Channel channel = (Channel) iter.next();
							ChannelStatistics stats = client.getStatistics(channel.getId());
							System.out.println(stats.getReceivedCount() + "\t\t" + stats.getRejectedCount() + "\t\t"+ stats.getSentCount() + "\t\t" + stats.getErrorCount() + "\t\t" + channel.getName());
						}
						return;
					}
					if (comm.equalsIgnoreCase("list")){
						List<Channel> allChannels = client.getChannel(null);
						System.out.println("ID\t\t\t\t\tEnabled\t\tName");
						String enable = "";
						for (Iterator<Channel> iter = allChannels.iterator(); iter.hasNext();) {
							Channel channel = iter.next();
							if (channel.isEnabled()){
								enable = "ENABLED";
							}else{
								enable = "DISABLED";
							}
							System.out.println(channel.getId() + "\t" + enable + "\t\t" + channel.getName() );
						}
						return;
					}
					if (comm.equalsIgnoreCase("disable") || comm.equalsIgnoreCase("enable") || comm.equalsIgnoreCase("remove")){
						List<Channel> channels = client.getChannel(null);
						String key = arguments[2];
						
						for (Iterator<Channel> iter = channels.iterator(); iter.hasNext();) {
							Channel channel = iter.next();
							if (key.equalsIgnoreCase("*") || channel.getName().equalsIgnoreCase(key) || channel.getId().equalsIgnoreCase(key)){
								if (comm.equalsIgnoreCase("disable") && channel.isEnabled()){
									channel.setEnabled(false);
									client.updateChannel(channel, true);
									System.out.println("Channel '" + channel.getName() + "' Disabled");
								}else if (comm.equalsIgnoreCase("enable") && !channel.isEnabled()){
									channel.setEnabled(true);
									client.updateChannel(channel, true);
									System.out.println("Channel '" + channel.getName() + "' Enabled");
								}else if (comm.equalsIgnoreCase("remove")){
									if (channel.isEnabled()){
										channel.setEnabled(false);
									}
									client.removeChannel(channel);
									System.out.println("Channel '" + channel.getName() + "' Removed");
								}
								if (!key.equalsIgnoreCase("*"))
									return;
							}
						}
						
					}
					
					List<ChannelStatus> channels = client.getChannelStatusList();
					String key = arguments[2];
					
							
					for (Iterator<ChannelStatus> iter = channels.iterator(); iter.hasNext();) {
						ChannelStatus channel = iter.next();
					
						if (key.equalsIgnoreCase("*") || channel.getName().equalsIgnoreCase(key) || channel.getChannelId().equalsIgnoreCase(key)){
							if (comm.equalsIgnoreCase("start") && (channel.getState().equals(State.PAUSED) || channel.getState().equals(State.STOPPED))){
								if (channel.getState().equals(State.PAUSED)){
									client.resumeChannel(channel.getChannelId());
									System.out.println("Channel '" + channel.getName() + "' Resumed");
								}else{
									client.startChannel(channel.getChannelId());
									System.out.println("Channel '" + channel.getName() + "' Started");
								}
							} else if (comm.equalsIgnoreCase("stop") && (channel.getState().equals(State.PAUSED) || channel.getState().equals(State.STARTED))){
								client.stopChannel(channel.getChannelId());
								System.out.println("Channel '" + channel.getName() + "' Stopped");
							}else if (comm.equalsIgnoreCase("pause") && channel.getState().equals(State.STARTED)){
								client.pauseChannel(channel.getChannelId());
								System.out.println("Channel '" + channel.getName() + "' Paused");
							}else if (comm.equalsIgnoreCase("resume") && channel.getState().equals(State.PAUSED)){
								client.resumeChannel(channel.getChannelId());
								System.out.println("Channel '" + channel.getName() + "' Resumed");
							}else if (comm.equalsIgnoreCase("stats")){
								ChannelStatistics stats = client.getStatistics(channel.getChannelId());
								System.out.println("Channel Stats for " + channel.getName());
								System.out.println("Received: " + stats.getReceivedCount());
								System.out.println("Filtered: " + stats.getRejectedCount());
								System.out.println("Sent: " + stats.getSentCount());
								System.out.println("Error: " + stats.getErrorCount());
							}
							if (!key.equalsIgnoreCase("*"))
								return;
						}
					}
				} 
				
				else if (arg1.equalsIgnoreCase("clear")) {
				
					List<Channel> channels = client.getChannel(null);

					for (Iterator iter = channels.iterator(); iter.hasNext();) {
						Channel channel = (Channel) iter.next();
						client.clearMessages(channel.getId());
					}
				} else if (arg1.equalsIgnoreCase("dump")) {
					if (arguments.length >= 2) {
						String arg2 = arguments[1];	

						if (arg2.equalsIgnoreCase("stats")) {
							String dumpFilename = arguments[2];
							dumpFilename = replaceValues(dumpFilename);

							StringBuilder builder = new StringBuilder();
							builder.append("Mirth Channel Statistics Dump: " + (new Date()).toString() + "\n");
							builder.append("Name, Received, Filtered, Sent, Error\n");

							List<Channel> channels = client.getChannel(null);
							
							for (Iterator iter = channels.iterator(); iter.hasNext();) {
								Channel channel = (Channel) iter.next();
								ChannelStatistics stats = client.getStatistics(channel.getId());
								builder.append(channel.getName() + ", " + stats.getReceivedCount() + ", " + stats.getRejectedCount() + ", "+ stats.getSentCount() + ", " + stats.getErrorCount() +"\n");
							}

							File dumpFile = new File(dumpFilename);

							try {
								writeFile(dumpFile, builder.toString());	
								System.out.println("Stats written to " + dumpFilename);
							} catch (IOException e) {
								System.out.println("Error: Could not write file: " + dumpFile.getAbsolutePath());
							}
						} else if (arg2.equals("events")) {
							String dumpFilename = arguments[2];
							dumpFilename = replaceValues(dumpFilename);

							StringBuilder builder = new StringBuilder();
							builder.append("Mirth Event Log Dump: " + (new Date()).toString() + "\n");
							builder.append("Id, Event, Date, Description, Level\n");

							List<SystemEvent> events = client.getSystemEvents(new SystemEventFilter());
							
							for (Iterator iter = events.iterator(); iter.hasNext();) {
								SystemEvent event = (SystemEvent) iter.next();
								builder.append(event.getId() +", " + event.getEvent() + ", " + formatDate(event.getDate()) + ", " + event.getDescription() + ", " + event.getLevel()  + "\n");
							}
							
							File dumpFile = new File(dumpFilename);

							try {
								writeFile(dumpFile, builder.toString());	
								System.out.println("Events written to " + dumpFilename);
							} catch (IOException e) {
								System.out.println("Error: Could not write file: " + dumpFile.getAbsolutePath());
							}
						} else {
							System.out.println("Error: Unknown dump command: " + arg2);
						}
					} else {
						System.out.println("Error: Missing dump commands.");
					}
				} else {
					System.out.println("Error: Unknown command: " + command);
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	 public static String readFile(File file) throws IOException
	    {
	        BufferedReader reader = new BufferedReader(new FileReader(file));
	        StringBuilder contents = new StringBuilder();
	        String line = null;

	        try
	        {
	            while ((line = reader.readLine()) != null)
	            {
	                contents.append(line + "\n");
	            }
	        }
	        finally
	        {
	            reader.close();
	        }

	        return contents.toString();
	    }
	private void doImportChannel(File importFile) throws ClientException{
		String channelXML = "";

        try
        {
            channelXML = readFile(importFile);
        }
        catch (IOException e)
        {
           System.out.println("File could not be read.");
           return;
        }

        ObjectXMLSerializer serializer = new ObjectXMLSerializer();
        Channel importChannel;

        try
        {
            importChannel = (Channel) serializer.fromXML(channelXML.replaceAll("\\&\\#x0D;\\n", "\n").replaceAll("\\&\\#x0D;", "\n"));
        }
        catch (Exception e)
        {
        	System.out.println("Invalid channel file.");
            return;
        }

       

        if (importChannel.getVersion() != null && !importChannel.getVersion().equals(client.getVersion()))
        {
            ImportConverter converter = new ImportConverter();
            importChannel = converter.convertChannel(importChannel);
        }
        String channelName = importChannel.getName();
        if (!checkChannelName(channelName, importChannel.getId()))
        {
            channelName = client.getGuid();
        }
        importChannel.setName(channelName);

        importChannel.setRevision(0);
        importChannel.setId(client.getGuid());
        client.updateChannel(importChannel, true);
        System.out.println("Channel '" + channelName + "' imported successfully." );
        
	}

    /**
     * Checks to see if the passed in channel name already exists
     * @throws ClientException 
     */
    public boolean checkChannelName(String name, String id) throws ClientException
    {
        if (name.equals(""))
        {
            
            return false;
        }

        for (Channel channel : client.getChannel(null))
        {
            if (channel.getName().equalsIgnoreCase(name) && !channel.getId().equals(id))
            {
                return false;
            }
        }
        return true;
    }

	private String replaceValues(String source) {
		source = source.replaceAll("\\$\\{date\\}", getTimeStamp());
		return source;
	}
	
	private void writeFile(File file, String data) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		try {
			writer.write(data);
			writer.flush();
		} finally {
			writer.close();
		}
	}
	
    private String getTimeStamp() {
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }
    
    private String formatDate(Calendar date) {
    	return String.format("%1$tY-%1$tm-%1$td 00:00:00", date);
    }
}

