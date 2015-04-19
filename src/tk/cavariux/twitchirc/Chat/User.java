package tk.cavariux.twitchirc.Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import tk.cavariux.twitchirc.Json.JsonArray;
import tk.cavariux.twitchirc.Json.JsonObject;
import tk.cavariux.twitchirc.Json.JsonValue;

/**
 * The user object
 * @author Leonardo Mariscal
 * @version 1.2-alpha
 */
public class User
{
	private String urln = "http://tmi.twitch.tv/group/user/$channel$/chatters";
	private String user;
	
	/**
	 * The constructor for a user.
	 * @param user The name of the User
	 */
	public User(String user)
	{
		this.user = user;
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
	public final boolean isOp(Channel channel)
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
	        JsonArray array = jsonObj.get("chatters").asObject().get("moderators").asArray();
	        ArrayList<String> mods = new ArrayList<String>();
	        for (JsonValue value : array)
	        {
	        	mods.add(value.toString().substring(1, value.toString().length() - 1));
	        }
	        if (mods.contains(this.toString()))
	        	return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
