package tk.cavariux.twitchirc.Chat;

import tk.cavariux.twitchirc.Core.TwitchBot;

public class Channel {
	
	private String channel;
	private TwitchBot bot;
	
	public Channel (String channel, TwitchBot bot)
	{
		this.bot = bot;
		this.channel = channel;
	}
	
	public final String toString ()
	{
		return channel;
	}
	
	public final void ban(User user)
	{
		this.bot.sendMessage("/ban " + user, this);
	}
	
	public final void timeOut(User user, int time)
	{
		this.bot.sendMessage("/timeout " + user + " " + time, this);
	}
	
	public final void unBan (User user)
	{
		this.bot.sendMessage("/unban " + user, this);
	}
	
	public final void slowMode (int sec)
	{
		this.bot.sendMessage("/slow " + sec, this);
	}
	
	public final void slowOff ()
	{
		this.bot.sendMessage("/slowoff", this);
	}
	
	public final void subscribersOnly ()
	{
		this.bot.sendMessage("/subscribers", this);
	}
	
	public final void suscribersOnlyOff ()
	{
		this.bot.sendMessage("/subscribersoff", this);
	}
	
	public final void clearChat ()
	{
		this.bot.sendMessage("/clear", this);
	}
	
	public final void r9kbeta()
	{
		this.bot.sendMessage("/r9kbeta", this);
	}
	
	public final void r9kbetaOff()
	{
		this.bot.sendMessage("/r9kbetaoff", this);
	}
	
	public final void comercial()
	{
		this.bot.sendMessage("/comercial", this);
	}
	
	public final void host(Channel channel)
	{
		this.bot.sendMessage("/host " + channel, this);
	}
	
	public final void unhost()
	{
		this.bot.sendMessage("/unhost", this);
	}
}
