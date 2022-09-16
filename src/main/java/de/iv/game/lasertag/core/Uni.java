package de.iv.game.lasertag.core;

import de.iv.ILib;
import de.iv.game.lasertag.elements.Weapon;
import de.iv.game.lasertag.events.GamePlayerDeathEvent;
import de.iv.game.lasertag.fs.FileManager;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class Uni {


    public static final String PREFIX = ILib.color(FileManager.getConfig("config.yml").toCfg().getString("plugin_prefix") + " ");
    public static final String ERROR = ILib.color(FileManager.getConfig("config.yml").toCfg().getString("error_prefix") + " ");

    public static void raycast(PlayerInteractEvent e, Color color, int length, float size, double damage, Sound sound, float vol, float pitch, int soundDistance, Weapon weapon) {
        Location start = e.getPlayer().getEyeLocation();
        Vector direction = start.getDirection();
        getNearbyPlayers(e.getPlayer().getLocation(), soundDistance).forEach(p -> p.playSound(e.getPlayer(), sound, vol, pitch));
        for (double i = 1; i < length; i += 0.15) {
            direction.multiply(i);
            start.add(direction);
            if (start.getBlock().getType().isOccluding()) return;
            for (Entity entity : e.getPlayer().getWorld().getNearbyEntities(start, 0, 0, 0)) {
                if (entity instanceof Player p) {
                    if (p != e.getPlayer()) {
                        p.damage(damage);
                        if(p.isDead()) Main.getInstance().getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(e.getPlayer(), p, weapon));
                    }
                }
                return;
            }
            Particle.DustOptions dustOptions = new Particle.DustOptions(color, size);
            e.getPlayer().getWorld().spawnParticle(Particle.REDSTONE, start, 10, 0, 0, 0, dustOptions);
            start.subtract(direction);
            direction.normalize();
        }
    }

    public static HashMap<Integer, String> compileScoreboardLines() {
        FileConfiguration cfg = FileManager.getConfig("scoreboard.yml").toCfg();
        HashMap<Integer, String> map = new HashMap<>();
        for (String key : cfg.getConfigurationSection("lines").getKeys(false)) {
            try {
                map.put(Integer.parseInt(key), cfg.getString("lines." + key));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static List<Player> getNearbyPlayers(Location loc, int size) {
        List<Player> players = new ArrayList<>();

        for(Player all : Bukkit.getOnlinePlayers()) {
            if(loc.distance(all.getLocation()) <= size) {
                players.add(all);
            }
        }
        return players;
    }

    public static void XPBarProgress(Player player, long current, long max) {
        float progress = Math.floorDiv(current, max);
        player.setExp(progress);
    }



}
