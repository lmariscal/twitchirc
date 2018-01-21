package com.cavariux.twitchirc.Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cavariux.twitchirc.Core.TwitchBot;
import com.cavariux.twitchirc.Json.JsonArray;
import com.cavariux.twitchirc.Json.JsonObject;
import com.cavariux.twitchirc.Json.JsonValue;
import com.cavariux.twitchirc.TwitchAPIConnections.ChannelQueries;

/**
 * The channel object
 *
 * @author Leonardo Mariscal
 * @version 1.0-Beta
 */
public class Channel {
	
	private String urln = "http://tmi.twitch.tv/group/user/$channel$/chatters";
	private String channel;
	private TwitchBot bot;
	private static HashMap<String, Channel> channels = new HashMap<String, Channel>();
	
	public static List<Channel> getChannels() {
		return new ArrayList<Channel>(channels.values());
	}
	
	/**
	 * The constructor of the channel object
	 *
	 * @param channel The name of the Channel
	 * @param bot     The bot that will be linked with
	 */
	private Channel(String channel, TwitchBot bot) {
		this.bot = bot;
		this.channel = channel;
		channels.put(channel, this);
	}
	
	/**
	 * Get a channel from an existing variable
	 *
	 * @param channel The channel name
	 * @param bot     The bot tha use it
	 * @return The channel
	 */
	public static final Channel getChannel(String channel, TwitchBot bot) {
		if (!channel.startsWith("#"))
			channel = "#" + channel;
		if (channels.containsKey(channel))
			return channels.get(channel);
		else
			return new Channel(channel, bot);
	}
	
	/**
	 * Gets the name of the Channel (Automatic if you only put the Channel object)
	 */
	public final String toString() {
		return channel;
	}
	
	/**
	 * Ban a user of the channel (Need Mod)
	 *
	 * @param user The user that will be banned
	 */
	public final void ban(User user) {
		this.bot.sendMessage("/ban " + user, this);
	}
	
	/**
	 * Timeout a player on the chat, useful for clearing spam (Need Mod)
	 *
	 * @param user The user that will be timeout
	 * @param time The time that will get timeout (Sometimes twitch change this)
	 */
	public final void timeOut(User user, int time) {
		this.bot.sendMessage("/timeout " + user + " " + time, this);
	}
	
	/**
	 * Unban a user from the chat (Need Mod)
	 *
	 * @param user The user that will get unban
	 */
	public final void unBan(User user) {
		this.bot.sendMessage("/unban " + user, this);
	}
	
	/**
	 * Slowmode on the chat (Need Mod)
	 *
	 * @param sec seconds of cooldown between messages of a user
	 */
	public final void slowMode(int sec) {
		this.bot.sendMessage("/slow " + sec, this);
	}
	
	/**
	 * Turns off the slowmode (Need Mod)
	 */
	public final void slowOff() {
		this.bot.sendMessage("/slowoff", this);
	}
	
	/**
	 * Turns the chat on subscribers only (Need Mod) (The channel needs partner with twitch)
	 */
	public final void subscribersOnly() {
		this.bot.sendMessage("/subscribers", this);
	}
	
	/**
	 * Turns off the chat only room (Need Mod)
	 */
	public final void suscribersOnlyOff() {
		this.bot.sendMessage("/subscribersoff", this);
	}
	
	/**
	 * Clears the chat useful for clearing spam (Need Mod)
	 */
	public final void clearChat() {
		this.bot.sendMessage("/clear", this);
	}
	
	/**
	 * Turn on the r9kbeta feature on Twitch (Need Mod)
	 */
	public final void r9kbeta() {
		this.bot.sendMessage("/r9kbeta", this);
	}
	
	/**
	 * Turns off the r9kbeta feature on Twitch (Need Mod)
	 */
	public final void r9kbetaOff() {
		this.bot.sendMessage("/r9kbetaoff", this);
	}
	
	/**
	 * Put a comercial on air for 30sec (Need Streamer/Editor)
	 */
	public final void commercial() {
		this.bot.sendMessage("/commercial", this);
	}
	
	/**
	 * Host a channel (Need Streamer/Editor)
	 *
	 * @param channel The target of the host
	 */
	public final void host(Channel channel) {
		this.bot.sendMessage("/host " + channel, this);
	}
	
	/**
	 * Unhost the hosted channel (Need Streamer/Editor)
	 */
	public final void unhost() {
		this.bot.sendMessage("/unhost", this);
	}
	
	/**
	 * Get the currently viewers (This method is on beta so it may not be optimized)
	 *
	 * @return A String[] with all the current viewers
	 */
	public final List<User> getViewers() {
		URL url;
		try {
			url = new URL(urln.replace("$channel$", channel.toString().substring(1)));
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine = "";
			String str = "";
			while ((str = br.readLine()) != null) {
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
	 *
	 * @return A String[] with all the current viewers
	 */
	public final List<User> getMods() {
		URL url;
		try {
			url = new URL(urln.replace("$channel$", channel.toString().substring(1)));
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine = "";
			String str = "";
			while ((str = br.readLine()) != null) {
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
	 *
	 * @param user The user
	 * @return true : false
	 */
	public final boolean isMod(User user) {
		return this.getMods().contains(user);
	}
	
	/**
	 * Check if the selected user is following the channel
	 *
	 * @param user The user to check
	 * @return If the user is following
	 */
	@SuppressWarnings("deprecation")
	public final boolean isFollowing(User user) {
		try {
			URL url = new URL("https://api.twitch.tv/kraken/users/" + user.toString().toLowerCase() + "/follows/channels/" + channel.substring(1).toLowerCase());
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Client-ID", bot.getClientID());
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			JsonObject jsonObj = JsonObject.readFrom(br.readLine());
			String str = jsonObj.get("channel").asObject().get("status").toString();
			return str.equals("null");
		} catch (IOException ex) {
			return false;
		}
	}
	
	/**
	 * Experimental Idk if it works I cant test it, please test it and send feedback
	 * <p>
	 * Still experimental after patch!
	 *
	 * @param user        The user to check
	 * @param oauth_token The token to access the information.
	 *                    This method requires the token to be generated with requested scope "channel_check_subscription"
	 * @return If the user is a sub
	 */
	@SuppressWarnings("deprecation")
	public final boolean isSubscribed(User user, String oauth_token) {
//		try {
//			URL url = new URL("https://api.twitch.tv/kraken/channels/" + channel.substring(1).toLowerCase() + "/subscriptions/" + user.toString().toLowerCase() + "?oauth_token=" + oauth_token);
//			URLConnection conn = url.openConnection();
//			conn.setRequestProperty("Client-ID", bot.getClientID());
//	        BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ));
//	        JsonObject jsonObj = JsonObject.readFrom(br.readLine());
//	        String str = jsonObj.get("_id").asString();
//	        return str.equals("404");
//		} catch (IOException ex) {
//			ex.printStackTrace();
//			return false;
//		}
		return ChannelQueries.isSubscriber(this, user, oauth_token, bot.getClientID());
	}
	
	
	/**
	 * Check if the channel is in Stream
	 *
	 * @return Returns the state
	 */
	public final boolean isLive() {
		return Channel.isLive(this, bot);
	}
	
	/**
	 * Get the game the streamer is streaming
	 *
	 * @return The game name
	 */
	public final String getGame() {
		if (!this.isLive()) return "";
		try {
			URL url = new URL("https://api.twitch.tv/kraken/streams/" + channel.substring(1));
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Client-ID", bot.getClientID());
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String strs = br.readLine();
			JsonObject jsonObj = JsonObject.readFrom(strs);
			String str = jsonObj.get("stream").asObject().get("game").asString();
			return str;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get the title that the Streamer put
	 *
	 * @return the title
	 */
	public final String getTitle() {
		if (!this.isLive()) return "";
		try {
			URL url = new URL("https://api.twitch.tv/kraken/streams/" + channel.substring(1));
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Client-ID", bot.getClientID());
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			JsonObject jsonObj = JsonObject.readFrom(br.readLine());
			String str = jsonObj.get("stream").asObject().get("channel").asObject().get("status").asString();
			return str;
		} catch (IOException ex) {
			return null;
		}
	}
	
	/**
	 * Get the viewers on that stream
	 *
	 * @return the number of viewers seeing the stream
	 */
	public final int getViewersNum() {
		if (!this.isLive()) return 0;
		try {
			URL url = new URL("https://api.twitch.tv/kraken/streams/" + channel.substring(1));
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Client-ID", bot.getClientID());
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			JsonObject jsonObj = JsonObject.readFrom(br.readLine());
			int i = jsonObj.get("stream").asObject().get("viewers").asInt();
			return i;
		} catch (IOException ex) {
			return 0;
		}
	}
	
	/**
	 * Get the language the streamer is streaming
	 *
	 * @return the language in the streamer is streaming
	 */
	public final String getLanguange() {
		try {
			URL url = new URL("https://api.twitch.tv/kraken/streams/" + channel.substring(1));
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Client-ID", bot.getClientID());
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			JsonObject jsonObj = JsonObject.readFrom(br.readLine());
			String str = jsonObj.get("stream").asObject().get("channel").asObject().get("language").asString();
			return str;
		} catch (IOException ex) {
			return null;
		}
	}
	
	/**
	 * Ge the total followers of that streamer
	 *
	 * @return the number of followers
	 */
	public final int getFollowersNum() {
		try {
			URL url = new URL("https://api.twitch.tv/kraken/channels/" + channel.substring(1) + "/follows");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Client-ID", bot.getClientID());
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine = "";
			String str = "";
			while ((str = br.readLine()) != null) {
				inputLine = inputLine + str;
			}
			br.close();
			JsonObject jsonObj = JsonObject.readFrom(inputLine);
			int stri = jsonObj.get("_total").asInt();
			return stri;
		} catch (IOException ex) {
			System.out.println("Errur:");
			System.out.println(ex.getMessage());
			return 0;
		}
	}
	
	/**
	 * Get the total views of the streamer
	 *
	 * @return the total views of the streamer
	 */
	public final int getTotalViews() {
		try {
			URL url = new URL("https://api.twitch.tv/kraken/streams/" + channel.substring(1));
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Client-ID", bot.getClientID());
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			JsonObject jsonObj = JsonObject.readFrom(br.readLine());
			int str = jsonObj.get("stream").asObject().get("channel").asObject().get("views").asInt();
			return str;
		} catch (IOException ex) {
			return 0;
		}
	}
	
	/**
	 * Check if a channel is in Stream
	 *
	 * @param channel the channel to check
	 * @return Returns the state
	 */
	public static final boolean isLive(Channel channel, TwitchBot bot) {
		try {
			URL url = new URL("https://api.twitch.tv/kraken/streams/" + channel.toString().substring(1));
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Client-ID", bot.getClientID());
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			JsonObject jsonObj = JsonObject.readFrom(br.readLine());
			String str = jsonObj.get("stream").toString();
			return !str.equals("null");
		} catch (IOException ex) {
			return false;
		}
	}
}
