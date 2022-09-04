package de.iv.game.lasertag.listeners;

import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.events.GamePlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(GamePlayerDeathEvent e) {
        Bukkit.broadcastMessage(
                ILib.color(Uni.PREFIX + ChatColor.GOLD + e.getTarget().getName() + " &7was killed by &6" + e.getKiller().getName() + " &7using " + e.getWeapon().getName()));
        API.setPlayerGameScore(e.getKiller().getUniqueId().toString(), e.getWeapon().getXp() + API.getPlayerGameScore(e.getKiller().getUniqueId().toString()));
    }

    @EventHandler
    public void cancelDeathMessage(PlayerDeathEvent e) {
        e.setDeathMessage("");
    }

}
