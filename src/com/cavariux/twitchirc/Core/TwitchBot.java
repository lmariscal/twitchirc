package com.cavariux.twitchirc.Core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;

/**
 * The main object to start making your bot
 * @author Leonardo Mariscal
 * @version 1.0-Beta
 */
public class TwitchBot {
	
	private String user;
	private String oauth_key;
	private BufferedWriter writer;
	private BufferedReader reader;
	private ArrayList<String> channels = new ArrayList<String>();
	private String version = "v1.0-Beta";
	
	public TwitchBot(){}
	
	/**
	 * The connect method alouds you to connect to the IRC servers on twitch
	 */
	public void connect()
	{
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
			Socket socket = new Socket("irc.twitch.tv", 6667);
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			this.writer.write("PASS " + oauth_key + "\r\n");
			this.writer.write("NICK " + user + "\r\n");
			this.writer.write("USER " + this.getVersion() + " \r\n");
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
	 public final void setUsername(String username)
	{
		this.user = username;
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
	protected void onMessage(User user, Channel channel, String message)
	{
		
	}
	
	/**
	 * This method is called when a command is sent on the Twitch Chat.
	 * @param user The user is sent, if you put it on a String it will give you the user's nick
	 * @param channel The channel where the command was sent
	 * @param message The command
	 */
	protected void onCommand(User user, Channel channel, String command)
	{
		
	}
	
	/**
	 * This method is used if you want to send a command to the IRC server, not commontly used
	 * @param message the command you will sent
	 */
	public void sendRawMessage(String message)
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
	 * An int variation of the sendRawMessage(String)
	 * @param message the command you will sent
	 */
	public void sendRawMessage(int message)
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
	public void sendMessage(Object message, Channel channel)
	{
		try {
			this.writer.write("PRIVMSG " + channel + " :" + message.toString() + "\r\n");
			this.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("> MSG " + channel + " : " + message.toString());
	}
	
	/**
	 * Send a message to a channel on Twitch (Don't need to be on that channel)
	 * @param message The message that will be sent
	 * @param channel The channel where the message will be sent
	 */
	public void sendMessage(String message, Channel channel)
	{
		try {
			this.writer.write("PRIVMSG " + channel + " :" + message + "\r\n");
			this.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("> MSG " + channel + " : " + message);
	}
	
	/**
	 * A sendMessage variation with an int
	 * @param message The message that will be sent
	 * @param channel channel The channel where the message will be sent
	 */
	public void sendMessage(int message, Channel channel)
	{
		try {
			this.writer.write("PRIVMSG " + channel + " :" + message + "\r\n");
			this.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("> MSG " + channel + " : " + message);
	}
	
	/**
	 * The method to join an IRC channel on Twitch
	 * @param channel The channel name
	 * @return You can get the channel you just created
	 */
	public final Channel joinChannel (String channel)
	{
		sendRawMessage("JOIN " + channel + "\r\n");
		System.out.println("> JOIN " + channel);
		return Channel.getChannel(channel, this);
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
		String line = "";
		try {
			while ((line = this.reader.readLine( )) != null) {
			    if (line.toLowerCase( ).startsWith("ping")) {
			    	System.out.println("> PING");
			        System.out.println("< PONG " + line.substring(5));
			        this.writer.write("PONG " + line.substring(5) + "\r\n");
			        this.writer.flush();
			    } else if (line.contains("PRIVMSG"))
			    {
			        String str[];
			        str = line.split("!");
			        final User msg_user = User.getUser(str[0].substring(1, str[0].length()));
			        str = line.split(" ");
			        Channel msg_channel;
			        msg_channel = Channel.getChannel(str[2], this);
			        String msg_msg = line.substring((str[0].length() + str[1].length() + str[2].length() + 4), line.length());
			        System.out.println("> " + msg_channel + " | " + msg_user + " >> " +  msg_msg);
			        if (msg_msg.startsWith("!"))
			        	onCommand(msg_user, msg_channel, msg_msg.substring(1));
			        
			        onMessage(msg_user, msg_channel, msg_msg);
			    } else if (line.toLowerCase().contains("disconnected"))
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
	 * Get the version of the TwitchIRC lib
	 * @return the version of the TwitchIRC lib
	 */
	public final String getVersion()
	{
		return "TwitchIRC "+version;
	}
}
