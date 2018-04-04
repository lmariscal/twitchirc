package com.cavariux.twitchirc.TwitchAPIConnections;

import com.cavariux.twitchirc.Json.JsonArray;
import com.cavariux.twitchirc.Json.JsonObject;
import com.cavariux.twitchirc.Json.JsonValue;
import org.w3c.dom.xpath.XPathResult;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class UserQueries {
	
	private static final String V5_API_BASE = "https://api.twitch.tv/kraken/users";
	private static final String NEW_API_BASE = "https://api.twitch.tv/helix/users";
	
	public static long[] getUserId(String clientId, String... usernames) {
		String names = Arrays.toString(usernames).replace(" ", "");
		
		String requestUri = V5_API_BASE + "?login=" + names.substring(1, names.length() - 1);
		try {
			JsonObject obj = sendRequest(requestUri, clientId);
			JsonArray users = obj.get("users").asArray();
			
			long[] result = new long[users.size()];
			int index = 0;
			
			for (JsonValue user : users) {
				int id = Integer.parseInt(user.asObject().get("_id").asString());
				result[index] = id;
				index++;
			}
			return result;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return new long[]{0, 0};
	}
	
	private static List<String> getUsernames(String clientId, List<Long> ids) {
		StringBuilder uriBuilder = new StringBuilder(NEW_API_BASE + "?");
		ids.forEach(id -> uriBuilder.append("id=").append(id).append("&"));
		uriBuilder.setLength(uriBuilder.length() - 1);
		
		String requestUri = uriBuilder.toString();
		List<String> result = new ArrayList<>();
		
		try {
			JsonObject obj = sendRequest(requestUri, clientId);
			JsonArray users = obj.get("data").asArray();
			
			for (JsonValue user : users) {
				result.add(user.asObject().get("display_name").asString());
			}
			
			return result;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return new ArrayList<>();
	}
	
	public static Map<String, List<String>> getFollowers(String[] usernames, String clientId) {
		long[] ids = UserQueries.getUserId(clientId, usernames);
		
		String requestUri = NEW_API_BASE + "/follows?to_id=";
		
		Map<String, List<String>> result = new HashMap<>();
		
		int index = 0;
		for (long id : ids) {
			try {
				JsonObject obj = sendRequest(requestUri + id, clientId);
				JsonArray data = obj.get("data").asArray();
				List<Long> followingIds = new ArrayList<>();
				
				for (JsonValue follow : data) {
					followingIds.add(follow.asObject().get("from_id").asLong());
				}
				
				result.put(usernames[index], getUsernames(clientId, followingIds));
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			index++;
		}
		return result;
	}
	
	
	private static JsonObject sendRequest(String requestUri, String clientId) throws IOException {
		URL request = new URL(requestUri);
		HttpURLConnection conn = (HttpURLConnection) request.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Client-ID", clientId);
		conn.setRequestProperty("Accept", "application/vnd.twitchtv.v5+json");
		Reader response = new InputStreamReader(conn.getInputStream());
		return JsonObject.readFrom(response);
	}
	
}
