package de.iv.game.lasertag.listeners;

import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.exceptions.RegisterUserException;
import de.iv.game.lasertag.game.AbilityPerformer;
import de.iv.game.lasertag.game.LTGameProfile;
import de.iv.game.lasertag.game.Weapon;
import de.iv.game.lasertag.util.OverlayManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

public class BukkitPlayerListener implements Listener {

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
        }
        API.GAME_PROFILES.add(new LTGameProfile(e.getPlayer()));


    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if(e.getEntity() instanceof Player p) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        API.GAME_PROFILES.remove(API.getGameProfile(p));

        API.playerTasks.values().forEach(BukkitTask::cancel);
        API.playerTasks.remove(e.getPlayer());
        OverlayManager.playerListMap.remove(p);
        if(p.isGlowing()) p.setGlowing(false);
    }

}
