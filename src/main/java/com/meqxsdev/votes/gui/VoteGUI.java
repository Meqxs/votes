package com.meqxsdev.votes.gui;

import com.meqxsdev.votes.VotesPlugin;
import com.meqxsdev.votes.models.Vote;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class VoteGUI {
    
    private static final String GUI_TITLE = ChatColor.DARK_PURPLE + "Vote";
    
    public static void openVoteGUI(Player player, Vote vote) {
        if (vote == null) {
            player.sendMessage(ChatColor.RED + "No active vote found!");
            return;
        }
        
        // Calculate inventory size based on number of options
        int size = Math.max(9, ((vote.getOptions().size() + 1) / 9 + 1) * 9);
        Inventory gui = Bukkit.createInventory(null, size, GUI_TITLE);
        
        // Add title item
        ItemStack titleItem = new ItemStack(Material.BOOK);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName(ChatColor.GOLD + "Vote: " + ChatColor.translateAlternateColorCodes('&', vote.getTitle()));
        List<String> titleLore = new ArrayList<>();
        titleLore.add(ChatColor.GRAY + "Click on an option below to vote!");
        titleLore.add(ChatColor.GRAY + "You can only vote once per round.");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        gui.setItem(4, titleItem);
        
        // Add option items
        int slot = 9;
        for (int i = 0; i < vote.getOptions().size(); i++) {
            String option = vote.getOptions().get(i);
            
            ItemStack optionItem = new ItemStack(Material.PAPER);
            ItemMeta optionMeta = optionItem.getItemMeta();
            optionMeta.setDisplayName(ChatColor.GREEN + "Option " + (i + 1) + ": " + 
                    ChatColor.translateAlternateColorCodes('&', option));
            
            List<String> optionLore = new ArrayList<>();
            optionLore.add(ChatColor.YELLOW + "Click to vote for this option!");
            optionLore.add(ChatColor.GRAY + "Current votes: " + vote.getVoteCounts().get(i));
            optionMeta.setLore(optionLore);
            optionItem.setItemMeta(optionMeta);
            
            gui.setItem(slot, optionItem);
            slot++;
        }
        
        player.openInventory(gui);
    }
    
    public static void handleVoteClick(Player player, int slot, Vote vote) {
        if (slot < 9) return; // Title area
        
        int optionIndex = slot - 9;
        if (optionIndex >= vote.getOptions().size()) return;
        
        if (vote.hasVoted(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have already voted in this round!");
            player.closeInventory();
            return;
        }
        
        // Record the vote
        VotesPlugin.getInstance().getVoteManager().vote(player, optionIndex);
        player.closeInventory();
        
        // Show updated results
        player.sendMessage(ChatColor.GREEN + "Vote recorded! Here are the current results:");
        VotesPlugin.getInstance().getVoteManager().showVoteResults(player);
    }
}
