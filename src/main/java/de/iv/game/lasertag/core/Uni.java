package de.iv.game.lasertag.core;

import com.google.gson.Gson;
import de.iv.ILib;
import de.iv.game.lasertag.elements.Weapon;
import de.iv.game.lasertag.elements.WeaponManager;
import de.iv.game.lasertag.events.GamePlayerDeathEvent;
import de.iv.game.lasertag.fs.FileManager;
import de.iv.iutils.sqlite.SQLite;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Uni {


    public static final String PREFIX = ILib.color(FileManager.getConfig("config.yml").getCfg().getString("plugin_prefix") + " ");

    public static void raycast(PlayerInteractEvent e, Color color, int length, float size, double damage, Sound sound, float vol, float pitch, int soundDistance, Weapon weapon) {
        Location start = e.getPlayer().getEyeLocation();
        Vector direction = start.getDirection();
        e.getPlayer().playSound(e.getPlayer(), sound, vol, pitch);
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
        FileConfiguration cfg = FileManager.getConfig("scoreboard.yml").getCfg();
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



    public static void XPBarProgress(Player player, long current, long max) {
        float progress = Math.floorDiv(current, max);
        player.setExp(progress);
    }



}
