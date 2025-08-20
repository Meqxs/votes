package com.meqxsdev.votes.managers;

import com.meqxsdev.votes.VotesPlugin;
import com.meqxsdev.votes.models.Vote;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class VoteManager {
    
    private final VotesPlugin plugin;
    private final Map<String, Vote> activeVotes;
    private final Map<UUID, VoteCreationSession> creationSessions;
    
    public VoteManager(VotesPlugin plugin) {
        this.plugin = plugin;
        this.activeVotes = new HashMap<>();
        this.creationSessions = new HashMap<>();
    }
    
    public void startVoteCreation(Player player) {
        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You don't have permission to create votes!");
            return;
        }
        
        VoteCreationSession session = new VoteCreationSession(player.getUniqueId());
        creationSessions.put(player.getUniqueId(), session);
        
        player.sendMessage(ChatColor.GREEN + "Vote creation started!");
        player.sendMessage(ChatColor.YELLOW + "Please enter the vote title (use & for colors):");
        player.sendMessage(ChatColor.GRAY + "Example: &b&lWhat should we build next?");
    }
    
    public void handleVoteCreationInput(Player player, String input) {
        VoteCreationSession session = creationSessions.get(player.getUniqueId());
        if (session == null) return;
        
        switch (session.getCurrentStep()) {
            case TITLE:
                session.setTitle(input);
                session.setCurrentStep(VoteCreationStep.OPTION_COUNT);
                player.sendMessage(ChatColor.YELLOW + "How many options should this vote have? (1-5):");
                break;
                
            case OPTION_COUNT:
                try {
                    int count = Integer.parseInt(input);
                    if (count < 1 || count > 5) {
                        player.sendMessage(ChatColor.RED + "Please enter a number between 1 and 5!");
                        return;
                    }
                    session.setOptionCount(count);
                    session.setCurrentStep(VoteCreationStep.OPTIONS);
                    session.setCurrentOption(0);
                    player.sendMessage(ChatColor.YELLOW + "Enter option 1:");
                    break;
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Please enter a valid number!");
                    return;
                }
                
            case OPTIONS:
                session.addOption(input);
                session.setCurrentOption(session.getCurrentOption() + 1);
                
                if (session.getCurrentOption() >= session.getOptionCount()) {
                    // Vote creation complete
                    createVote(session);
                    creationSessions.remove(player.getUniqueId());
                    player.sendMessage(ChatColor.GREEN + "Vote created successfully!");
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Enter option " + (session.getCurrentOption() + 1) + ":");
                }
                break;
        }
    }
    
    private void createVote(VoteCreationSession session) {
        Vote vote = new Vote(session.getTitle(), session.getOptions());
        activeVotes.put(vote.getId(), vote);
        
        // Announce the vote to all players
        String announcement = ChatColor.GOLD + "=== NEW VOTE ===" + ChatColor.RESET + "\n" +
                ChatColor.translateAlternateColorCodes('&', vote.getTitle()) + "\n" +
                ChatColor.YELLOW + "Click here to vote: " + ChatColor.GREEN + "/vote";
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(announcement);
        }
    }
    
    public Vote getActiveVote() {
        return activeVotes.values().stream()
                .filter(Vote::isActive)
                .findFirst()
                .orElse(null);
    }
    
    public boolean hasActiveVote() {
        return getActiveVote() != null;
    }
    
    public void vote(Player player, int optionIndex) {
        Vote vote = getActiveVote();
        if (vote == null) {
            player.sendMessage(ChatColor.RED + "There is no active vote!");
            return;
        }
        
        if (vote.hasVoted(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have already voted!");
            return;
        }
        
        if (optionIndex < 0 || optionIndex >= vote.getOptions().size()) {
            player.sendMessage(ChatColor.RED + "Invalid option!");
            return;
        }
        
        vote.addVote(player.getUniqueId(), optionIndex);
        player.sendMessage(ChatColor.GREEN + "Your vote has been recorded!");
    }
    
    public void showVoteResults(Player player) {
        Vote vote = getActiveVote();
        if (vote == null) {
            player.sendMessage(ChatColor.RED + "There is no active vote!");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "=== VOTE RESULTS ===");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', vote.getTitle()));
        player.sendMessage(ChatColor.YELLOW + "Total votes: " + vote.getTotalVotes());
        
        Map<Integer, Integer> counts = vote.getVoteCounts();
        for (int i = 0; i < vote.getOptions().size(); i++) {
            String option = vote.getOptions().get(i);
            int count = counts.get(i);
            double percentage = vote.getTotalVotes() > 0 ? (double) count / vote.getTotalVotes() * 100 : 0;
            
            player.sendMessage(ChatColor.GREEN + String.valueOf(i + 1) + ". " + 
                    ChatColor.translateAlternateColorCodes('&', option) + 
                    ChatColor.GRAY + " - " + count + " votes (" + String.format("%.1f", percentage) + "%)");
        }
    }
    
    public void cancelVoteCreation(Player player) {
        creationSessions.remove(player.getUniqueId());
        player.sendMessage(ChatColor.RED + "Vote creation cancelled.");
    }
    
    public boolean isCreatingVote(Player player) {
        return creationSessions.containsKey(player.getUniqueId());
    }
    
    private enum VoteCreationStep {
        TITLE, OPTION_COUNT, OPTIONS
    }
    
    private static class VoteCreationSession {
        private final UUID playerId;
        private String title;
        private int optionCount;
        private final List<String> options;
        private VoteCreationStep currentStep;
        private int currentOption;
        
        public VoteCreationSession(UUID playerId) {
            this.playerId = playerId;
            this.options = new ArrayList<>();
            this.currentStep = VoteCreationStep.TITLE;
        }
        
        public UUID getPlayerId() { return playerId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public int getOptionCount() { return optionCount; }
        public void setOptionCount(int optionCount) { this.optionCount = optionCount; }
        public List<String> getOptions() { return options; }
        public void addOption(String option) { this.options.add(option); }
        public VoteCreationStep getCurrentStep() { return currentStep; }
        public void setCurrentStep(VoteCreationStep currentStep) { this.currentStep = currentStep; }
        public int getCurrentOption() { return currentOption; }
        public void setCurrentOption(int currentOption) { this.currentOption = currentOption; }
    }
}
