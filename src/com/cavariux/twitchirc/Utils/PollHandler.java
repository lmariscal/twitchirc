package com.cavariux.twitchirc.Utils;

import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Core.TwitchBot;
import com.cavariux.twitchirc.TwitchAPIConnections.StreamQueries;
import com.cavariux.twitchirc.TwitchAPIConnections.UserQueries;

import java.util.*;
import java.util.stream.Collectors;

class PollHandler {
	
	private static Map<String, String> latestFollowerForUser = new HashMap<>();
	
	static void handlePolls(Set<Poller.Poll> polls, TwitchBot bot) {
		Map<Poller.PollType, List<Poller.Poll>> dividedPolls = dividePolls(polls);
		
		for (Poller.PollType type : Poller.PollType.values()) {
			type.handlingMethod.accept(dividedPolls.get(type), bot);
		}
	}
	
	private static Map<Poller.PollType, List<Poller.Poll>> dividePolls(Set<Poller.Poll> polls) {
		Map<Poller.PollType, List<Poller.Poll>> result = new HashMap<>();
		for (Poller.PollType type : Poller.PollType.values()) {
			result.put(type, new ArrayList<>());
		}
		
		polls.forEach(p -> result.get(p.type).add(p));
		
		return result;
	}
	
	static void handleLive(List<Poller.Poll> polls, TwitchBot report) {
		List<String> usernames = polls.stream().map(p -> p.username).collect(Collectors.toList());
		Map<String, Boolean> streamIsLiveMap = StreamQueries.getIsLiveMap(usernames, report.getClientID());
		
		List<Poller.Poll> toRemove = new ArrayList<>();
		
		for (String name : streamIsLiveMap.keySet()) {
			if (streamIsLiveMap.get(name)) {
				toRemove.add(getPollForName(name, polls));
				report.onLive(User.getUser(name));
			}
		}
		
		synchronized (report.getPoller()) {
			polls.removeAll(toRemove);
		}
	}
	
	static void handleFollow(List<Poller.Poll> polls, TwitchBot report) {
		String[] usernames = polls.stream().map(p -> p.username).toArray(String[]::new);
		Map<String, List<String>> newFollower = UserQueries.getFollowers(usernames, report.getClientID());
		
		newFollower.forEach((username, newFollowers) -> {
			String previousLatest = latestFollowerForUser.get(username);
			Integer index = newFollowers.indexOf(previousLatest);
			newFollowers.subList(index, newFollowers.size()).clear(); //discard all followers we already know
			if (index > 0)
				latestFollowerForUser.put(username, newFollowers.get(0));
			
			List<User> userList = newFollowers.stream().map(uName -> User.getUser(uName)).collect(Collectors.toList());
			report.onFollow(User.getUser(username), userList);
		});
	}
	
	//TODO prepareFollow
	
	private static Poller.Poll getPollForName(String name, List<Poller.Poll> polls) {
		for (Poller.Poll p : polls) {
			if (p.username.equals(name)) return p;
		}
		return null;
	}
	
}
