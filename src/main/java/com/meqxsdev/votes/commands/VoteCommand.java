package com.meqxsdev.votes.commands;

import com.meqxsdev.votes.VotesPlugin;
import com.meqxsdev.votes.gui.VoteGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor {
    
    private final VotesPlugin plugin;
    
    public VoteCommand(VotesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!plugin.getVoteManager().hasActiveVote()) {
            player.sendMessage(ChatColor.RED + "There is no active vote at the moment!");
            return true;
        }
        
        VoteGUI.openVoteGUI(player, plugin.getVoteManager().getActiveVote());
        return true;
    }
}
