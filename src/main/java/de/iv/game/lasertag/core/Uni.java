package de.iv.game.lasertag.core;

import de.iv.ILib;
import de.iv.game.lasertag.game.Weapon;
import de.iv.game.lasertag.events.GamePlayerDeathEvent;
import de.iv.game.lasertag.fs.FileManager;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;

public class Uni {


    public static final String PREFIX = ILib.color(FileManager.getConfig("config.yml").getCfg().getString("plugin_prefix") + " ");
    public static final String ERROR = ILib.color("&4&lERROR &c");

    public static void sendErrorMessage(Player p, String message) {
        p.sendMessage(ILib.color(ERROR + message));
    }

    public static void runPlayerTaskLater(Player p, long delay, Consumer<Player> consumer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                consumer.accept(p);
            }
        }.runTaskLater(Main.getInstance(), delay);
    }

    public static BukkitTask runPlayerOnTick(Player player, long delay, Consumer<Player> consumer) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if(player == null) this.cancel();
                consumer.accept(player);
            }
        }.runTaskTimer(Main.getInstance(), delay, 1);
    }

    public static BukkitTask runPlayerOnTick(Player player, long delay, Consumer<Player> consumer, Predicate<Player> stoppingPredicate) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if(stoppingPredicate.test(player) || player == null) this.cancel();
                consumer.accept(player);
            }
        }.runTaskTimer(Main.getInstance(), delay, 1);
    }

    public static double round(double x, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(x));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static Collection<Player> getNearbyPlayers(Location loc, int radius) {
        ArrayList<Player> r = new ArrayList<>();
        loc.getWorld().getNearbyEntities(loc, radius, radius, radius).forEach(e -> {
            if(e instanceof Player p) {
                r.add(p);
            }
        });
        return r;
    }

    public static String convertWithDelimiter(int number, char delimiter) {
        String numberString = String.valueOf(number);
        StringBuilder result = new StringBuilder();

        int digitCount = 0;
        for (int i = numberString.length() - 1; i >= 0; i--) {
            result.insert(0, numberString.charAt(i));
            digitCount++;

            if (digitCount % 3 == 0 && i != 0) {
                result.insert(0, delimiter);
            }
        }

        return result.toString();
    }


    /**
     * Spawn a Sphere at a given {@link Location}. The radius and height of the sphere is by default a value between 1 <br>
     * and {@code Math#Pi}. Use the {@code radiusAmplifier} to change the sphere's radius. <br>
     *
     * @param loc - The {@link Location} the particles are spawned
     * @param radiusAmplifier - A value that manipulates the initial radius of the sphere
     * @param particle - The particle the sphere is made of
     * @param count - The amount of particles spawned on each place
     * @param speed - the speed of the particles
     */

    public static void particleSphere(Location loc, int circleCount, int radiusAmplifier, Particle particle, int count, double speed) {
        for (double i = 0; i <= Math.PI; i += Math.PI / circleCount) {
            //let y = height
            double y = Math.cos(i)*radiusAmplifier;
            //let r = radius
            double r = Math.sin(i)*radiusAmplifier;
            /*
            We can use the cosine of i to get a value that represents our sphere's height, since the cosine of a real
            value x is just the sine of x+pi/2. That means that the highest point of the sphere has the lowest radius, in fact zero.
             */
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / circleCount) {
                double x = Math.cos(a) * r;
                double z = Math.sin(a) * r;
                loc.add(x, y, z);
                loc.getWorld().spawnParticle(particle, loc, 1, 0, 0, 0, speed);
                loc.subtract(x, y, z);
            }
        }
    }

    /**
     * Creates a cylinder made out of particles. Every length is calculated in blocks.
     *
     * @param loc - location of the cylinders base
     * @param radius - the cylinders radius
     * @param ppc - amount of particles displayed in each circle [particles per circle]
     * @param height - the height of the cylinders head
     * @param circleAmount - amount of circles between head and base
     * @param particle - the displayed particle
     * @param count - the amount of particles spawned at once
     */
    public static void particleCylinder(Location loc, double radius, int ppc, double height, int circleAmount, Particle particle, int count, double speed) {
        // 2, 0.2 => 10/1
        for(double k = 0; k <= height; k += height/circleAmount) {
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / ppc) {
                double x = (Math.cos(a) + 0.05) *radius;
                double z = Math.sin(a) *radius;
                loc.add(x, k, z);
                loc.getWorld().spawnParticle(particle, loc, count, 0, 0, 0, speed);
                loc.subtract(x, k, z);
            }

        }
    }

    /**
     * Creates a cylinder made out of particles. Every length is calculated in blocks.
     *
     * @param loc - location of the cylinders base
     * @param radius - the cylinders radius
     * @param ppc - amount of particles displayed in each circle [particles per circle]
     * @param height - the height of the cylinders head
     * @param circleAmount - amount of circles between head and base
     * @param dustOptions - options for dust particle
     * @param count - the amount of particles spawned at once
     */
    public static void particleCylinder(Location loc, double radius, int ppc, double height, int circleAmount, int count, Particle.DustOptions dustOptions) {
        // 2, 0.2 => 10/1
        for(double k = 0; k <= height; k += height/circleAmount) {
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / ppc) {
                double x = (Math.cos(a) + 0.05) *radius;
                double z = Math.sin(a) *radius;
                loc.add(x, k, z);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, count, 0, 0, 0, 1, dustOptions);
                loc.subtract(x, k, z);
            }

        }
    }


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
