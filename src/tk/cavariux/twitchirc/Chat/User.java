package tk.cavariux.twitchirc.Chat;

import java.util.HashMap;

import tk.cavariux.twitchirc.Core.TwitchBot;

/**
 * The user object
 * @author Leonardo Mariscal
 * @version 1.3-alpha
 */
public class User
{
	private String user;
	private static HashMap<String, User> users = new HashMap<String, User>();
	
	/**
	 * The constructor for a user.
	 * @param user The name of the User
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
	 * @deprecated No need on using it, if you put the User on a string it will change automatically
	 */
	@Deprecated
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
	 */
	public final void timeout(Channel channel, int time)
	{
		channel.timeOut(this, time);
	}
	
	/**
	 * Timeout a player from a channel (Requires Streamer/Editor)
	 * @param channel The channel
	 */
	public final void hostthisUser(Channel channel, TwitchBot bot)
	{
		channel.host(Channel.getChannel(user, bot));;
	}
}
