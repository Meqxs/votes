package com.meqxsdev.votes.commands;

import com.meqxsdev.votes.VotesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VotesCommand implements CommandExecutor {
    
    private final VotesPlugin plugin;
    
    public VotesCommand(VotesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You don't have permission to create votes!");
            return true;
        }
        
        plugin.getVoteManager().startVoteCreation(player);
        return true;
    }
}
