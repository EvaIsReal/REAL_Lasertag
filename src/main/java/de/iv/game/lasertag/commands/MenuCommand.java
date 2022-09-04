package de.iv.game.lasertag.commands;

import de.iv.game.lasertag.menus.BaseMenu;
import de.iv.game.lasertag.menus.ShopMenu;
import de.iv.iutils.exceptions.ManagerSetupException;
import de.iv.iutils.exceptions.MenuManagerException;
import de.iv.iutils.menus.MenuManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player p) {
            try {
                MenuManager.openMenu(BaseMenu.class, p);
                p.playSound(p, Sound.BLOCK_CHEST_OPEN, 1, 2);
            } catch (MenuManagerException | ManagerSetupException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
