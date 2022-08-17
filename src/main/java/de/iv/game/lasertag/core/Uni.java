package de.iv.game.lasertag.core;

import de.iv.game.lasertag.fs.Config;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Uni {



    public static void raycast(PlayerInteractEvent e, Color color, float size, double damage, Sound sound, float vol, float pitch, int soundDistance) {
        Location start = e.getPlayer().getEyeLocation();
        Vector direction = start.getDirection();
        e.getPlayer().playSound(e.getPlayer(), sound, vol, pitch);
        for(double i = 1; i < 30; i += 0.15) {
            direction.multiply(i);
            start.add(direction);
            if(start.getBlock().getType().isOccluding()) return;
            for (Entity entity : e.getPlayer().getWorld().getNearbyEntities(start, 0, 0, 0)) {
                if(entity instanceof Player p) {
                    if(p != e.getPlayer()) {
                        Main.getInstance().getServer().getPluginManager().callEvent(new EntityDamageByEntityEvent(e.getPlayer(), p, EntityDamageEvent.DamageCause.CUSTOM, damage));
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


}
