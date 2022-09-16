package de.iv.game.lasertag.commands.lasertag;

import de.iv.ILib;
import de.iv.game.lasertag.commands.Subcommand;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.fs.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CoinsCommand extends Subcommand {

    @Override
    public String name() {
        return "coins";
    }

    @Override
    public List<String> permissions() {
        return List.of("lt.manage_coins");
    }

    @Override
    public String description() {
        return "Manage your wealth, or the wealth of others.";
    }

    @Override
    public String syntax() {
        return "<set|query> set?[<string: player_name> <number: amount>] | query?[<string: player_name>]";
    }

    @Override
    public List<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public boolean forPlayer() {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        FileConfiguration cfg = FileManager.getConfig("config.yml").toCfg();
        /*/lt coins <operation> <?player> <?amount> */
        if(args.length >= 3) {
            switch (args[1]) {
                case "set" -> {
                    if(sender.hasPermission("lt_manage_coins_other")) {
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ILib.color(Uni.ERROR + "Self money management is not applicable to current command sender!"));
                            return;
                        }
                        try {
                            Player target = Bukkit.getPlayerExact(args[2]);
                            double amount = Double.parseDouble(args[3]);
                            if(target != null) {
                                if(amount <= 0) {
                                    sender.sendMessage(ILib.color(Uni.ERROR + "The amount of coins may not be less than or equal to 0."));
                                    return;
                                }
                                API.setPlayerBalance(target.getUniqueId().toString(), amount);
                                sender.sendMessage(ILib.color(Uni.PREFIX + "&a " + target.getName() + "'s balance was set to &6" + amount));
                                if(sender instanceof Player) ((Player) sender).playSound((Player) sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                            } else sender.sendMessage(ILib.color(Uni.ERROR + cfg.getString("player_not_found_exception").replace("%player_name%", args[2])));

                        } catch (NumberFormatException e) {
                            sender.sendMessage(ILib.color(Uni.ERROR + args[3] + " is not a number!"));
                        }
                    } else sender.sendMessage(ILib.color(Uni.ERROR + "You're not permitted to execute this command."));

                } case "query" -> {
                    if(sender.hasPermission("lt_query_coins_self")) {
                        Player target = Bukkit.getPlayerExact(args[2]);
                        if(target != null) {
                            sender.sendMessage(ILib.color(Uni.PREFIX + ChatColor.GOLD + target.getName() + "&a currently has &6"
                                    + API.getPlayerBalance(target.getUniqueId().toString()) + " &a coins."));
                            if(sender instanceof Player) ((Player) sender).playSound((Player) sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
                        } else sender.sendMessage(ILib.color(Uni.ERROR + cfg.getString("player_not_found_exception").replace("%player_name%", args[2])));
                    }
                } default -> {
                    sender.sendMessage(ILib.color(Uni.ERROR + "No matching alias or command has been found."));
                }

            }

        }

    }
}
