package com.cavariux.twitchirc.Core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The main object to start making your bot
 * @author Leonardo Mariscal
 * @version 1.0-Beta
 */
public class TwitchBot {
	
	@SuppressWarnings("unused")
	private String whispers_ip = "";
	@SuppressWarnings("unused")
	private int whispers_port = 443;
	private boolean wen = true;
	private String user;
	private String oauth_key;
	private BufferedWriter writer;
	private BufferedReader reader;
	private ArrayList<String> channels = new ArrayList<>();
	private String version = "v1.2";
	private boolean stopped = true;
	private String commandTrigger = "!";
	private String clientID = "";
	
	public final List<String> getChannels(){
		return channels;
	}
	
	public TwitchBot(){}
	
	/**
	 * Here you can connect without having to connect to a chat group
	 */
	public void connect() {
		wen = false;
		connect("irc.twitch.tv", 6667);
	}
	
	/**
	 * The connect method alouds you to connect to the IRC servers on twitch
	 * @param ip The ip of the Chat group
	 * @param port The port of the Chat group
	 */
	public void connect(String ip, int port)
	{
		if (isRunning()) return;
		try{
			if (user == null || user == "")
			{
				System.err.println("Please select a valid Username");
				System.exit(1);
				return;
			}
			if (oauth_key == null || oauth_key == "")
			{
				System.err.println("Please select a valid Oauth_Key");
				System.exit(2);
				return;
			}
			
			@SuppressWarnings("resource")
			Socket socket = new Socket(ip, port);
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			this.writer.write("PASS " + oauth_key + "\r\n");
			this.writer.write("NICK " + user + "\r\n");
			this.writer.write("USER " + this.getVersion() + " \r\n");
			this.writer.write("CAP REQ :twitch.tv/commands twitch.tv/membership \r\n");
			this.writer.flush();
			
			String line = "";
			while ((line = this.reader.readLine()) != null)
			{
				 if (line.indexOf("004") >= 0) {
		                System.out.println("Connected >> " + user + " ~ irc.twitch.tv");
		                break;
		            }else {
		                System.out.println(line);
		            }	
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Set the username that the connect method will use
	 * @param username Needs your <a href="http://www.twitch.tv">Twitch</a> Username to connect
	 */
	public final void setUsername(String username) {
		this.user = username;
	}
	 
	/**
	 * Sets the required ClientID for the use of the api.
	 * @param clientID
	 */
	public final void setClientID(String clientID) {
		this.clientID = clientID;
	}
	
	/**
	 * Method to return the clientid.
	 * @return The clientid
	 */
	public final String getClientID() {
		if (this.clientID != null)
			return this.clientID;
		else {
			System.out.println("You need to give a clientID to use the TwitchAPI");
			return "";
		}
	}
	
	 /**
	  * Set the "password" that the <a href="connect">connect</a> method will use.
	  * @param oauth_key To get this key go to the <a href="http://twitchapps.com/tmi/">TwitchApps</a> and get it on the TMI section
	  */
	public final void setOauth_Key (String oauth_key)
	{
		this.oauth_key = oauth_key;
	}
	
	/**
	 * This method is called when a message is sent on the Twitch Chat.
	 * @param user The user is sent, if you put it on a String it will give you the user's nick
	 * @param channel The channel where the message was sent
	 * @param message The message
	 */
	protected void onMessage(String user, String channel, String message) {
		
	}
	
	/**
	 * This method is called when a command is sent on the Twitch Chat.
	 * @param user The user is sent, if you put it on a String it will give you the user's nick
	 * @param channel The channel where the command was sent
	 * @param command The command
	 */
	protected void onCommand(String user, String channel, String command) {
		
	}
	
//	/**
//	 * This method is used if you want to send a command to the IRC server, not commontly used
//	 * @param message the command you will sent
//	 */
//	public void sendRawMessage(String message)
//	{
//		try {
//			this.writer.write(message + " \r\n");
//			this.writer.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(message);
//	}
	
	/**
	 * An int variation of the sendRawMessage(String)
	 * @param message the command you will sent
	 */
	public void sendRawMessage(Object message)
	{
		try {
			this.writer.write(message + " \r\n");
			this.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(message);
	}
	
	/**
	 * Send a message to a channel on Twitch (Don't need to be on that channel)
	 * @param message The message that will be sent
	 * @param channel The channel where the message will be sent
	 */
	public void sendMessage(Object message, String channel)
	{
		try {
			this.writer.write("PRIVMSG " + channel + " :" + message.toString() + "\r\n");
			this.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("> MSG " + channel + " : " + message.toString());
	}
	
//	/**
//	 * Send a message to a channel on Twitch (Don't need to be on that channel)
//	 * @param message The message that will be sent
//	 * @param channel The channel where the message will be sent
//	 */
//	public void sendMessage(String message, Channel channel)
//	{
//		try {
//			this.writer.write("PRIVMSG " + channel + " :" + message + "\r\n");
//			this.writer.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("> MSG " + channel + " : " + message);
//	}
//	
//	/**
//	 * A sendMessage variation with an int
//	 * @param message The message that will be sent
//	 * @param channel channel The channel where the message will be sent
//	 */
//	public void sendMessage(int message, Channel channel)
//	{
//		try {
//			this.writer.write("PRIVMSG " + channel + " :" + message + "\r\n");
//			this.writer.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("> MSG " + channel + " : " + message);
//	}
	
	/**
	 * The method to join an IRC channel on Twitch
	 * @param channel The channel name
	 * @return You can get the channel you just created
	 */
	@SuppressWarnings( "deprecation" )
	public final String joinChannel (String channel) {
		sendRawMessage("JOIN " + channel + "\r\n");
		this.channels.add(channel);
		System.out.println("> JOIN " + channel);
		return channel;
	}
	
	/**
	 * Leaves the channel you want
	 * @param channel The channel you want to leave
	 */
	public final void partChannel (String channel)
	{
		this.sendRawMessage("PART " + channel);
		this.channels.remove(channel);
		System.out.println("> PART " + channel);
	}
	
	/**
	 * No need to use this dev things
	 * @return a BufferedWrtier
	 */
	public final BufferedWriter getWriter ()
	{
		return this.writer;
	}
	
	/**
	 * Starts the full mechanism of the bot, this is the last method to be called
	 */
	public final void start()
	{
		if (isRunning()) return;
		String line = "";
		stopped = false;
		try {
			while ((line = this.reader.readLine( )) != null && !stopped) {
			    if (line.toLowerCase( ).startsWith("ping")) {
			    	System.out.println("> PING");
			        System.out.println("< PONG " + line.substring(5));
			        this.writer.write("PONG " + line.substring(5) + "\r\n");
			        this.writer.flush();
			    } else if (line.contains("PRIVMSG"))
			    {
			        String str[];
			        str = line.split("!");
			        final String msg_user = str[0].substring(1, str[0].length());
			        str = line.split(" ");
			        String msg_channel;
			        msg_channel = str[2];
			        String msg_msg = line.substring((str[0].length() + str[1].length() + str[2].length() + 4), line.length());
			        System.out.println("> " + msg_channel + " | " + msg_user + " >> " +  msg_msg);
			        if (msg_msg.startsWith(commandTrigger))
			        	onCommand(msg_user, msg_channel, msg_msg.substring(1));
			        
			        onMessage(msg_user, msg_channel, msg_msg);
			    } else if (line.contains(" JOIN ")) {
			    	String[] p = line.split(" ");
			    	String[] pd = line.split("!");
			    	if (p[1].equals("JOIN")) 
			    		userJoins(pd[0].substring(1), p[2]);
				} else if (line.contains(" PART ")) {
			    	String[] p = line.split(" ");
			    	String[] pd = line.split("!");
			    	if (p[1].equals("PART")) 
			    		userParts(pd[0].substring(1), p[2]);
				} else if (line.contains(" WHISPER ")) {
					String[] parts = line.split(":");
					final String wsp_user = parts[1].split("!")[0];
					String message = parts[2];
					onWhisper(wsp_user, message);
				} else if (line.startsWith(":tmi.twitch.tv ROOMSTATE")) {
			    	
				} else if (line.startsWith(":tmi.twitch.tv NOTICE"))
			    {
			    	String[] parts = line.split(" ");
			    	if (line.contains("This room is now in slow mode. You may send messages every"))
			    	{
			    		System.out.println("> Chat is now in slow mode. You can send messages every " + parts[15] + " sec(s)!");
			    	} else if (line.contains("subscribers-only mode")) {
			    		if (line.contains("This room is no longer"))
			    			System.out.println("> The room is no longer Subscribers Only!");
			    		else
			    			System.out.println("> The room has been set to Subscribers Only!");
			    	} else {
			    		System.out.println(line);
			    	}
			    } else if (line.startsWith(":jtv MODE "))
			    {
			    	String[] p = line.split(" ");
			    	if (p[3].equals("+o")) {
			    		System.out.println("> +o " + p[4]);
			    	} else {
			    		System.out.println("> -o " + p[4]);
			    	}
			    }else if (line.toLowerCase().contains("disconnected"))
			    {
			    	System.out.println(line);
			    	this.connect();
			    } else
			    {
			        System.out.println("> " + line);
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Simply stops the bot.
	 */
	public void stop() {
		this.stopped = true;
		this.sendRawMessage("Stopping"); //so that the bot has one message to process to actually stop
	}
	
	/**
	 * Stops the bot and sends a message to all channels the bot has joined
	 * @param message Message to send to all channels
	 */
	public void stopWithMessage(String message) {
		this.stopped = true;
		for (String cnl : channels) {
			this.sendMessage(message, cnl);
		}
	}

	public boolean isRunning() {
		return !stopped;
	}
	
	/**
	 * A user joins the channel
	 * @param user The user that has join
	 * @param channel The channel he joined
	 */
	protected void userJoins(String user, String channel) {
		
	}
	
	/**
	 * A user leaves/parts the channel
	 * @param user The user that has left
	 * @param channel The channel he left
	 */
	protected void userParts(String user, String channel)
	{
		
	}
	
	/**
	 * This will let you whisper to the specified user
	 * @param user The user to send the message
	 * @param message The messsage to send
	 */
	public void whisper(String user, String message)
	{
		if (!channels.isEmpty()) {
			this.sendMessage("/w " + user + " " + message, channels.get(0));
			
		} else if (!wen) {
			System.out.println("You have to be either connected to at least one channel or join another Server to be able to whisper!");
		} else {
			sendRawMessage("PRIVMSG #jtv :/w " + user + " " + message);
		}
	}
	
	/**
	 * When overrided this method lets you check for whispers.
	 * @param user The user that sent it
	 * @param message The message he sent
	 */
	protected void onWhisper(String user, String message) {
	}
	
	/**
	 * Sets the whispers ip (000.000.000.000:0000)
	 * @param ip The whole ip
	 */
	protected final void setWhispersIp(String ip)
	{
		if (!ip.contains(":")) {
			System.out.println("Invaid ip!");
			return;
		}
		String[] args = ip.split(":");
		whispers_ip = args[0];
		whispers_port = Integer.parseInt(args[1]);
	}
	
	/**
	 * Sets the whispers ip
	 * @param ip The ip to connect
	 * @param port The port to connect with
	 */
	protected final void setWhispersIp(String ip, int port)
	{
		whispers_ip = ip;
		whispers_port = port;
	}
	
	/**
	 * Sets the string that will mark a message as command if it begins with it
	 * @param trigger string to mark a message as command
	 */
	public void setCommandTrigger(String trigger) {
		this.commandTrigger = trigger;
	}
	
	/**
	 * Get the version of the TwitchIRC lib
	 * @return the version of the TwitchIRC lib
	 */
	public final String getVersion()
	{
		return "TwitchIRC "+version;
	}
	
}
