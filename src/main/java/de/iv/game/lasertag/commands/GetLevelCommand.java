package de.iv.game.lasertag.commands;


import com.google.common.collect.Lists;
import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GetLevelCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 1) {
            try {
                double score = Double.parseDouble(args[0]);
                sender.sendMessage(ILib.color("&8Corresponding level is &6" + API.getLevelFromScore(score)));

            } catch (NumberFormatException e) {
                sender.sendMessage(ILib.color(args[0] + " &4is not a number."));
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            return Lists.newArrayList("<scoreQuery>");
        }
        return Collections.emptyList();
    }
}
