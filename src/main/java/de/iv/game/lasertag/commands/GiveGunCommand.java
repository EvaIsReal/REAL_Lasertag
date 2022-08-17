package de.iv.game.lasertag.commands;

import de.iv.game.lasertag.elements.Weapon;
import de.iv.game.lasertag.elements.WeaponManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GiveGunCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player p) {
            if(args.length == 1) {
                String gunKey = args[0];
                Weapon w = WeaponManager.getWeapon(gunKey);
                p.getInventory().addItem(w.toItem());
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            List<String> keys = new ArrayList<>();
            for (Weapon activeWeapon : WeaponManager.getActiveWeapons()) {
                keys.add(activeWeapon.getKey());
            }
            return keys;
        }
        return Collections.emptyList();
    }
}
