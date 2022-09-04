package de.iv.game.lasertag.commands;

import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.elements.Weapon;
import de.iv.game.lasertag.elements.WeaponManager;
import de.iv.game.lasertag.exceptions.DBIOException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryItemCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player p) {
            API.getPlayerInventoryItems(p.getUniqueId().toString()).forEach(i -> {
                p.sendMessage(ILib.color("&6" + i.key()));
            });
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            List<String> list = new ArrayList<>();
            list.add("add");
            list.add("remove");
            return list;
        } else return Collections.emptyList();
    }
}
