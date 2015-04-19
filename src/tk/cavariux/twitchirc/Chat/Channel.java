package tk.cavariux.twitchirc.Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import tk.cavariux.twitchirc.Core.TwitchBot;
import tk.cavariux.twitchirc.Json.JsonArray;
import tk.cavariux.twitchirc.Json.JsonObject;
import tk.cavariux.twitchirc.Json.JsonValue;

public class Channel {
	
	private String urln = "http://tmi.twitch.tv/group/user/$channel$/chatters";
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
	
	public final String[] getViewers()
	{
		URL url;
		try {
			url = new URL(urln.replace("$channel$", channel.toString().substring(1)));
			System.out.println(url);
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
	        String[] viewers = new String[array.size() + array2.size()];
	        int i = 0;
	        for (JsonValue value : array)
	        {
	        	viewers[i] = value.toString().substring(1, value.toString().length() - 1);
	        	i++;
	        }
	        for (JsonValue value : array2)
	        {
	        	viewers[i] = value.toString().substring(1, value.toString().length() - 1);
	        	i++;
	        }
	        return viewers;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
