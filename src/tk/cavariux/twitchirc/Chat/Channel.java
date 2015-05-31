package tk.cavariux.twitchirc.Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tk.cavariux.twitchirc.Core.TwitchBot;
import tk.cavariux.twitchirc.Json.JsonArray;
import tk.cavariux.twitchirc.Json.JsonObject;
import tk.cavariux.twitchirc.Json.JsonValue;

/**
 * The channel object
 * @author Leonardo Mariscal
 * @version 1.3-alpha
 */
public class Channel {
	
	private String urln = "http://tmi.twitch.tv/group/user/$channel$/chatters";
	private String channel;
	private TwitchBot bot;
	private static HashMap<String, Channel> channels = new HashMap<String, Channel>();
	
	/**
	 * The constructor of the channel object
	 * @param channel The name of the Channel
	 * @param bot The bot that will be linked with
	 */
	public Channel (String channel, TwitchBot bot)
	{
		this.bot = bot;
		this.channel = channel;
		channels.put(channel, this);
	}
	
	/**
	 * Get a channel from an existing variable
	 * @param channel The channel name
	 * @param bot The bot tha use it
	 * @return The channel
	 */
	public static final Channel getChannel(String channel, TwitchBot bot)
	{
		if (channels.containsKey(channel))
			return channels.get(channel);
		else
			return new Channel(channel, bot);
	}
	
	/**
	 * Gets the name of the Channel (Automatic if you only put the Channel object)
	 * @deprecated No need of using this, only in small cases
	 */
	@Deprecated
	public final String toString ()
	{
		return channel;
	}
	
	/**
	 * Ban a user of the channel (Need Mod)
	 * @param user The user that will be banned
	 */
	public final void ban(User user)
	{
		this.bot.sendMessage("/ban " + user, this);
	}
	
	/**
	 * Timeout a player on the chat, useful for clearing spam (Need Mod)
	 * @param user The user that will be timeout
	 * @param time The time that will get timeout (Sometimes twitch change this)
	 */
	public final void timeOut(User user, int time)
	{
		this.bot.sendMessage("/timeout " + user + " " + time, this);
	}
	
	/**
	 * Unban a user from the chat (Need Mod)
	 * @param user The user that will get unban
	 */
	public final void unBan (User user)
	{
		this.bot.sendMessage("/unban " + user, this);
	}
	
	/**
	 * Slowmode on the chat (Need Mod)
	 * @param sec seconds of cooldown between messages of a user
	 */
	public final void slowMode (int sec)
	{
		this.bot.sendMessage("/slow " + sec, this);
	}
	
	/**
	 * Turns off the slowmode (Need Mod)
	 */
	public final void slowOff ()
	{
		this.bot.sendMessage("/slowoff", this);
	}
	
	/**
	 * Turns the chat on subscribers only (Need Mod) (The channel needs partner with twitch)
	 */
	public final void subscribersOnly ()
	{
		this.bot.sendMessage("/subscribers", this);
	}
	
	/**
	 * Turns off the chat only room (Need Mod)
	 */
	public final void suscribersOnlyOff ()
	{
		this.bot.sendMessage("/subscribersoff", this);
	}
	
	/**
	 * Clears the chat useful for clearing spam (Need Mod)
	 */
	public final void clearChat ()
	{
		this.bot.sendMessage("/clear", this);
	}
	
	/**
	 * Turn on the r9kbeta feature on Twitch (Need Mod)
	 */
	public final void r9kbeta()
	{
		this.bot.sendMessage("/r9kbeta", this);
	}
	
	/**
	 * Turns off the r9kbeta feature on Twitch (Need Mod)
	 */
	public final void r9kbetaOff()
	{
		this.bot.sendMessage("/r9kbetaoff", this);
	}
	
	/**
	 * Put a comercial on air for 30sec (Need Streamer/Editor) 
	 */
	public final void comercial()
	{
		this.bot.sendMessage("/comercial", this);
	}
	
	/**
	 * Host a channel (Need Streamer/Editor)
	 * @param channel The target of the host
	 */
	public final void host(Channel channel)
	{
		this.bot.sendMessage("/host " + channel, this);
	}
	
	/**
	 * Unhost the hosted channel (Need Streamer/Editor)
	 */
	public final void unhost()
	{
		this.bot.sendMessage("/unhost", this);
	}
	
	/**
	 * Get the currently viewers (This method is on beta so it may not be optimized)
	 * @return A String[] with all the current viewers
	 */
	public final List<User> getViewers()
	{
		URL url;
		try {
			url = new URL(urln.replace("$channel$", channel.toString().substring(1)));
			URLConnection conn = url.openConnection();
	        BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ));
	        String inputLine = "";
	        String str = "";
	        while ((str = br.readLine()) != null)
	        {
	        	inputLine = inputLine + str;
	        }
	        br.close();
	        JsonObject jsonObj = JsonObject.readFrom(inputLine);
	        JsonArray array = jsonObj.get("chatters").asObject().get("viewers").asArray();
	        JsonArray array2 = jsonObj.get("chatters").asObject().get("moderators").asArray();
	        List<User> viewers = new ArrayList<User>();
	        for (JsonValue value : array)
	        	viewers.add(User.getUser(value.toString().substring(1, value.toString().length() - 1)));
	        for (JsonValue value : array2)
	        	viewers.add(User.getUser(value.toString().substring(1, value.toString().length() - 1)));
	        return viewers;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get the currently viewers (This method is on beta so it may not be optimized)
	 * @return A String[] with all the current viewers
	 */
	public final List<User> getMods()
	{
		URL url;
		try {
			url = new URL(urln.replace("$channel$", channel.toString().substring(1)));
			URLConnection conn = url.openConnection();
	        BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ));
	        String inputLine = "";
	        String str = "";
	        while ((str = br.readLine()) != null)
	        {
	        	inputLine = inputLine + str;
	        }
	        br.close();
	        JsonObject jsonObj = JsonObject.readFrom(inputLine);
	        JsonArray array2 = jsonObj.get("chatters").asObject().get("moderators").asArray();
	        List<User> mods = new ArrayList<User>();
	        for (JsonValue value : array2)
	        	mods.add(User.getUser(value.toString().substring(1, value.toString().length() - 1)));
	        return mods;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Check if the user is a mod
	 * @param user The user
	 * @return true : false
	 */
	public final boolean isMod(User user)
	{
		return this.getMods().contains(user);
	}
}
