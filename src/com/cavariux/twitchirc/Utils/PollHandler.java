package com.cavariux.twitchirc.Utils;

import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Core.TwitchBot;
import com.cavariux.twitchirc.TwitchAPIConnections.StreamQueries;
import com.cavariux.twitchirc.TwitchAPIConnections.UserQueries;

import java.util.*;
import java.util.stream.Collectors;

class PollHandler {
	
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
		long[] ids = UserQueries.getUserId(report.getClientID(), usernames);
		List<String> newFollower = UserQueries.getNewFollowers(ids); //TODO add that method in the UserQueries
	}
	
	private static Poller.Poll getPollForName(String name, List<Poller.Poll> polls) {
		for (Poller.Poll p : polls) {
			if (p.username.equals(name)) return p;
		}
		return null;
	}
	
}
