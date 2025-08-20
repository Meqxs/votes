package com.meqxsdev.votes.models;

import java.util.*;

public class Vote {
    private final String id;
    private final String title;
    private final List<String> options;
    private final Map<UUID, Integer> votes;
    private final long createdAt;
    private boolean active;
    
    public Vote(String title, List<String> options) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.title = title;
        this.options = options;
        this.votes = new HashMap<>();
        this.createdAt = System.currentTimeMillis();
        this.active = true;
    }
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public List<String> getOptions() {
        return options;
    }
    
    public Map<UUID, Integer> getVotes() {
        return votes;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean hasVoted(UUID playerId) {
        return votes.containsKey(playerId);
    }
    
    public void addVote(UUID playerId, int optionIndex) {
        votes.put(playerId, optionIndex);
    }
    
    public Map<Integer, Integer> getVoteCounts() {
        Map<Integer, Integer> counts = new HashMap<>();
        for (int i = 0; i < options.size(); i++) {
            counts.put(i, 0);
        }
        
        for (int vote : votes.values()) {
            counts.put(vote, counts.get(vote) + 1);
        }
        
        return counts;
    }
    
    public int getTotalVotes() {
        return votes.size();
    }
}
