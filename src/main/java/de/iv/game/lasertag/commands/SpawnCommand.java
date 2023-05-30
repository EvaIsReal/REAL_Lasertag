package de.iv.game.lasertag.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.text.PlainDocument;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender instanceof Player p) {
            p.teleport(p.getWorld().getSpawnLocation());
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            return true;
        }

        return false;
    }
}
