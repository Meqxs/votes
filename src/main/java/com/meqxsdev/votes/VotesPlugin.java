package com.meqxsdev.votes;

import com.meqxsdev.votes.commands.VoteCommand;
import com.meqxsdev.votes.commands.VotesCommand;
import com.meqxsdev.votes.listeners.ChatListener;
import com.meqxsdev.votes.listeners.InventoryListener;
import com.meqxsdev.votes.managers.VoteManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VotesPlugin extends JavaPlugin {
    
    private static VotesPlugin instance;
    private VoteManager voteManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.voteManager = new VoteManager(this);
        
        // Register commands
        getCommand("votes").setExecutor(new VotesCommand(this));
        getCommand("vote").setExecutor(new VoteCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        
        getLogger().info("Votes plugin has been enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("Votes plugin has been disabled!");
    }
    
    public static VotesPlugin getInstance() {
        return instance;
    }
    
    public VoteManager getVoteManager() {
        return voteManager;
    }
}
