package test;

import tk.cavariux.twitchirc.Chat.Channel;
import tk.cavariux.twitchirc.Chat.User;
import tk.cavariux.twitchirc.Core.TwitchBot;

public class CavsBot extends TwitchBot
{

	public CavsBot ()
	{
		this.setUsername("CavsBot");
		this.setOauth_Key("oauth:whnrm49fyojrosf1rru3rxfty4jici");
	}
	
	@Override
	public void onMessage(User user, Channel channel, String message)
	{
		if (message.equalsIgnoreCase("!hi"))
			this.sendMessage("Hi there " + user, channel);
		if (message.equalsIgnoreCase("!mods"))
			channel.getMods();
		if (message.equalsIgnoreCase("!mod"))
			this.sendMessage(channel.isMod(user) + " eres mod!", channel);
	}
}
