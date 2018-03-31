package com.cavariux.twitchirc.Utils;

import com.cavariux.twitchirc.Core.TwitchBot;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public enum Poller {
	INSTANCE;
	
	private static final int minutesAsMilliSeconds = 60 * 1000;
	
	
	private long pollRate = minutesAsMilliSeconds;
	private Set<Poll> polls = new HashSet<>();
	private ScheduledExecutorService pollingService = Executors.newSingleThreadScheduledExecutor();
	private TwitchBot bot;
	
	Poller() {
	}
	
	public void setBot(TwitchBot bot) {
		this.bot = bot;
	}
	
	public void setPollRate(long milliseconds) {
		this.pollRate = milliseconds;
		restartExecutor();
	}
	
	public void setPollRate(long amount, TimeUnit unit) {
		this.pollRate = unit.convert(amount, TimeUnit.MILLISECONDS);
		restartExecutor();
	}
	
	public long getPollRate() {
		return this.pollRate;
	}
	
	public synchronized void addPoll(Poll poll) {
		this.polls.add(poll);
	}
	
	public synchronized void addPoll(String username, PollType type) {
		this.polls.add(new Poll(username, type));
	}
	
	public synchronized Set<Poll> getUnmodifiablePolls() {
		return Collections.unmodifiableSet(this.polls);
	}
	
	private void restartExecutor() {
		if (!this.pollingService.isShutdown())
			this.pollingService.shutdown();
		
		this.pollingService = Executors.newSingleThreadScheduledExecutor();
		this.pollingService.scheduleAtFixedRate(() -> pollNewEvents(), 0, this.pollRate, TimeUnit.MILLISECONDS);
	}
	
	private synchronized void pollNewEvents() {
		PollHandler.handlePolls(this.polls, bot);
	}
	
	public enum PollType {
		LIVE(PollHandler::handleLive), FOLLOW(PollHandler::handleFollow);
		BiConsumer<List<Poll>, TwitchBot> handlingMethod;
		PollType(BiConsumer<List<Poll>, TwitchBot> consumer) {
			this.handlingMethod = consumer;
		}
		
	}
	
	public class Poll {
		String username;
		PollType type;
		
		public Poll(String username, PollType type) {
			this.username = username;
			this.type = type;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Poll poll = (Poll) o;
			return username.equals(poll.username) &&
					type == poll.type;
		}
		
		@Override
		public int hashCode() {
			
			return Objects.hash(username, type);
		}
	}
	
}
