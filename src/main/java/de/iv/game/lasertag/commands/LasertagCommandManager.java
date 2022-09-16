package de.iv.game.lasertag.commands;

import de.iv.ILib;
import de.iv.game.lasertag.commands.lasertag.CoinsCommand;
import de.iv.game.lasertag.core.Uni;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LasertagCommandManager implements CommandExecutor, TabCompleter {

    private ArrayList<Subcommand> subcommands = new ArrayList<>();

    public LasertagCommandManager() {
        subcommands.add(new CoinsCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length > 0) {
            for (int i = 0; i < subcommands.size(); i++) {
                Subcommand cmd = getSubcommands().get(i);
                if(args[0].equalsIgnoreCase(cmd.name()) | cmd.aliases().contains(args[0])) {
                    if(cmd.forPlayer() && sender instanceof Player p) {
                        cmd.execute(p, args);
                    } else if(cmd.forPlayer() && !(sender instanceof Player p)) {
                        sender.sendMessage(ILib.color(Uni.ERROR + "This command cannot be executed by the console."));
                        return false;
                    } else {
                        cmd.execute(sender, args);
                    }
                } else {
                    sender.sendMessage(ILib.color(Uni.ERROR + "No matching alias or command has been found."));
                    return false;
                }
            }
        } else {
            sender.sendMessage(ILib.color(Uni.ERROR + "Argument size is not valid."));
        }

        return true;
    }

    public ArrayList<Subcommand> getSubcommands() {
        return subcommands;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1 -> {
                return getSubcommands().stream()
                        .map(Subcommand::name)
                        .collect(Collectors.toList());
            }
            case 2 ->{
                return getSubcommands().stream()
                        .map(Subcommand::syntax)
                        .collect(Collectors.toList());
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }
}
