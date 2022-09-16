package de.iv.game.lasertag.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Subcommand {

    public abstract String name();
    public abstract List<String> permissions();
    public abstract String description();
    public abstract String syntax();
    public abstract List<String> aliases();
    public abstract boolean forPlayer();
    public abstract void execute(CommandSender sender, String[] args);

}
