package com.meqxsdev.votes.listeners;

import com.meqxsdev.votes.VotesPlugin;
import com.meqxsdev.votes.gui.VoteGUI;
import com.meqxsdev.votes.models.Vote;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {
    
    private final VotesPlugin plugin;
    
    public InventoryListener(VotesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        
        // Check if this is our vote GUI
        if (event.getView().getTitle().equals(ChatColor.DARK_PURPLE + "Vote")) {
            event.setCancelled(true);
            
            Vote activeVote = plugin.getVoteManager().getActiveVote();
            if (activeVote != null) {
                VoteGUI.handleVoteClick(player, event.getRawSlot(), activeVote);
            }
        }
    }
}
