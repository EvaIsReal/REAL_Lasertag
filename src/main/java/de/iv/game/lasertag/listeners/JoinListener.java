package de.iv.game.lasertag.listeners;

import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.elements.Weapon;
import de.iv.game.lasertag.exceptions.RegisterUserException;
import de.iv.game.lasertag.util.OverlayManager;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

public class JoinListener implements Listener {

    @EventHandler
    public void handleJoin(PlayerJoinEvent e) {
        //Check if DB contains the players data
        if(!API.isRegistered(e.getPlayer().getUniqueId().toString())) {
            try {
                //register the user
                API.registerUser(e.getPlayer().getUniqueId().toString());
                e.getPlayer().sendMessage(Uni.PREFIX + "Welcome to lasertag!");
            } catch (RegisterUserException ex) {
                ex.printStackTrace();
            }
        } else {
            //Player already exists
            e.getPlayer().sendMessage(Uni.PREFIX + "Welcome back!");
            API.checkDB(e.getPlayer().getUniqueId().toString());
            System.out.println(API.getRequiredScore(5));
            System.out.println(API.getLevelFromScore(25.75));
        }
    }

    @EventHandler
    public void handleScoreboard(PlayerJoinEvent e) {
        Player p = e.getPlayer();


    }

}
