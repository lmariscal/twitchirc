package com.cavariux.twitchirc.TwitchAPIConnections;

import com.cavariux.twitchirc.Json.JsonArray;
import com.cavariux.twitchirc.Json.JsonObject;
import com.cavariux.twitchirc.Json.JsonValue;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamQueries {
	
	private static final String NEW_API_BASE = "https://api.twitch.tv/helix/streams";
	
	public static Map<String, Boolean> getIsLiveMap(List<String> usernames, String clientId) {
		String requestUri = getRequestUri(usernames);
		Map<String, Boolean> result = new HashMap<>();
		
		try {
			URL request = new URL(requestUri);
			URLConnection conn = request.openConnection();
			conn.setRequestProperty("Client-ID", clientId);
			conn.setRequestProperty("Accept", "application/vnd.twitchtv.v5+json");
			
			Reader response = new InputStreamReader(conn.getInputStream());
			
			JsonArray responseObject = JsonObject.readFrom(response).asArray();
			int index = 0;
			for (JsonValue stream : responseObject) {
				JsonObject o = stream.asObject();
				
				String type = o.get("type").asString().toLowerCase();
				Boolean isLive = type.equals("vodcast") || type.equals("live");
				
				result.put(usernames.get(index), isLive);
				
				index++;
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return result;
	}
	
	private static String getRequestUri(List<String> usernames) {
		StringBuilder uriBuilder = new StringBuilder(NEW_API_BASE + "?");
		for (String name : usernames) {
			uriBuilder.append("user_login=").append(name).append("&");
		}
		uriBuilder.setLength(uriBuilder.length() - 1); //trims last &
		
		return uriBuilder.toString();
	}
	
}
