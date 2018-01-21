package com.cavariux.twitchirc.TwitchAPIConnections;

import com.cavariux.twitchirc.Json.JsonArray;
import com.cavariux.twitchirc.Json.JsonObject;
import com.cavariux.twitchirc.Json.JsonValue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public class UserQueries {
	
	private static final String V5_API_BASE = "https://api.twitch.tv/kraken/users";
	
	public static int[] getUserId(String clientId, String... usernames) {
		String names = Arrays.toString(usernames).replace(" ", "");
		
		String requestUri = V5_API_BASE + "?login=" + names.substring(1, names.length()-1);
		try {
			URL request = new URL(requestUri);
			HttpURLConnection conn = (HttpURLConnection) request.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Client-ID", clientId);
			conn.setRequestProperty("Accept", "application/vnd.twitchtv.v5+json");
			Reader response = new InputStreamReader(conn.getInputStream());
			JsonObject obj = JsonObject.readFrom(response);
			JsonArray users = obj.get("users").asArray();
			
			int[] result = new int[users.size()];
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
		return new int[]{0, 0};
	}
	
}
