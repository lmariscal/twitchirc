package com.cavariux.twitchirc.Chat;

import java.util.HashMap;

import com.cavariux.twitchirc.Core.TwitchBot;

/**
 * The user object
 * @author Leonardo Mariscal
 * @version 1.0-Beta
 */
public class User
{
	private String user;
	private static HashMap<String, User> users = new HashMap<String, User>();
	
	/**
	 * The constructor for a user.
	 * @param user The name of the User
	 * @deprecated Please use the getUser method to get the user
	 */
	public User(String user)
	{
		this.user = user;
		users.put(user, this);
	}
	
	public static final User getUser(String ign)
	{
		if (users.containsKey(ign))
			return users.get(ign);
		else
			return new User(ign);
	}
	
	/**
	 * Change the User into a String
	 */
	public String toString()
	{
		return user;
	}
	
	/**
	 * Check if the User is op (May bug out some times) (Method in beta may not be optimized)
	 * @param channel The channel to check
	 * @return A boolean if its op returns true
	 */
	public final boolean isMod(Channel channel)
	{
		return channel.isMod(this);
	}
	
	/**
	 * Ban a player from a channel (Requires Mod)
	 * @param channel The channel
	 */
	public final void ban(Channel channel)
	{
		channel.ban(this);
	}	
	
	/**
	 * UnBan a player from a channel (Requires Mod)
	 * @param channel The channel
	 */
	public final void unBan(Channel channel)
	{
		channel.unBan(this);
	}
	
	/**
	 * Timeout a player from a channel (Requires Mod)
	 * @param channel The channel
	 * @param time The time to timeout
	 */
	public final void timeout(Channel channel, int time)
	{
		channel.timeOut(this, time);
	}
	
	/**
	 * Timeout a player from a channel (Requires Streamer/Editor)
	 * @param channel The channel
	 * @param bot The bot that have the power
	 */
	public final void hostthisUser(Channel channel, TwitchBot bot)
	{
		channel.host(Channel.getChannel(user, bot));;
	}
	
	/**
	 * Check if the selected user is following the channel
	 * @param channel The channel to check
	 * @return If the user is following
	 */
	public final boolean isFollowing(Channel channel) 
	{
		return channel.isFollowing(this);
	}

	/**
	 * Experimental Idk if it works I cant test it, please test it and send feedback
	 * @param channel The user to check
	 * @param oauth_token The token to acces the information
	 * @return If the user is a sub
	 */
	public final boolean isSubscribed(Channel channel, String oauth_token) 
	{
		return channel.isSubscribed(this, oauth_token);
	}
}
