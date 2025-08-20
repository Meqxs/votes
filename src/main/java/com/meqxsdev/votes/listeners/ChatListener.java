package com.meqxsdev.votes.listeners;

import com.meqxsdev.votes.VotesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    
    private final VotesPlugin plugin;
    
    public ChatListener(VotesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        
        if (plugin.getVoteManager().isCreatingVote(player)) {
            event.setCancelled(true);
            
            // Handle vote creation input on main thread
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                plugin.getVoteManager().handleVoteCreationInput(player, event.getMessage());
            });
        }
    }
}
