package de.iv.game.lasertag.commands;

import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Uni;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserResetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 1) {
            Player t = Bukkit.getPlayerExact(args[0]);
            if(t != null) {
                API.resetPlayer(t.getUniqueId().toString());
                sender.sendMessage(ILib.color(Uni.PREFIX + "&c" + t.getName() + "'s account has been reset."));
                if(Bukkit.getOnlinePlayers().contains(t)) t.kickPlayer(ILib.color("&c&lYour Account has been reset"));
            }
        }

        return true;
    }
}
