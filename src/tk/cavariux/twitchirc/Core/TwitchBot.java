package tk.cavariux.twitchirc.Core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import tk.cavariux.twitchirc.Chat.Channel;
import tk.cavariux.twitchirc.Chat.User;

public class TwitchBot {

	private String user;
	private String oauth_key;
	private BufferedWriter writer;
	private BufferedReader reader;
	private ArrayList<String> channels = new ArrayList<String>();
	private double version = 0.01;
	
	public TwitchBot(){}
	
	public void connect() throws IOException
	{
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
		this.writer.write("USER " + "TwitchIRC v0.01 \r\n");
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
	}
	
	 public final void setUsername(String username)
	{
		this.user = username;
	}
	
	public final void setOauth_Key (String oauth_key)
	{
		this.oauth_key = oauth_key;
	}
	
	protected void onMessage(User user, Channel channel, String message)
	{
		
	}
	
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
	
	public void sendMessage(String message, Channel cav)
	{
		try {
			this.writer.write("PRIVMSG " + cav + " :" + message + "\r\n");
			this.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("> MSG " + cav + " : " + message);
	}
	
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
	
	public final Channel joinChannel (String channel)
	{
		sendRawMessage("JOIN " + channel + "\r\n");
		System.out.println("> JOIN " + channel);
		return new Channel(channel, this);
	}
	
	public final void partChannel (String channel)
	{
		this.sendRawMessage("PART " + channel);
		this.channels.remove(channel);
		System.out.println("> PART " + channel);
	}
	
	public final BufferedWriter getWriter ()
	{
		return this.writer;
	}
	
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
			        /* Send The Messages to The onMessage Method */
			    	//System.out.println(line);
			        String str[];
			        str = line.split("!");
			        final User msg_user = new User(str[0].substring(1, str[0].length()));
			        str = line.split(" ");
			        Channel msg_channel;
			        msg_channel = new Channel(str[2], this);
			        String msg_msg = line.substring((str[0].length() + str[1].length() + str[2].length() + 4), line.length());
			        System.out.println("> " + msg_channel + " | " + msg_user + " >> " +  msg_msg);
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
	
	public final String getVersion()
	{
		return "TwitchIRC v"+version;
	}
}
