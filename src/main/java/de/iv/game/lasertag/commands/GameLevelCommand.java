package de.iv.game.lasertag.commands;

import de.iv.game.lasertag.core.API;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameLevelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            int level = Integer.parseInt(args[1]);
            API.setPlayerLevel(level, target.getUniqueId().toString());
            API.setPlayerGameScore(target.getUniqueId().toString(), API.getRequiredScore(level));
        }

        return true;
    }
}
