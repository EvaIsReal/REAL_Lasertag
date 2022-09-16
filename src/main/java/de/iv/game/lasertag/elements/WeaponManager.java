package de.iv.game.lasertag.elements;

import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.exceptions.ConfigException;
import de.iv.game.lasertag.fs.FileManager;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class WeaponManager implements Listener {

    private static ArrayList<Weapon> activeWeapons = new ArrayList<>();
    private HashMap<UUID, Long> cooldownMap = new HashMap<>();


    @EventHandler
    public void handleFire(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (e.hasItem()) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) | e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && item != null) {
                if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING)) {
                    String gunKey = e.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING);
                    Weapon w = getWeapon(gunKey.substring(4));
                    if (w == null) return;
                    if (!cooldownMap.containsKey(e.getPlayer().getUniqueId()) || System.currentTimeMillis() - cooldownMap.get(e.getPlayer().getUniqueId()) >= w.getCooldownMillis()) {
                        cooldownMap.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
                        w.fire(e);
                    } else {
                        e.getPlayer().playSound(e.getPlayer(), Sound.BLOCK_COMPARATOR_CLICK, 1, 2);
                    }
                }
            }
        }
    }


    public static void compileWeapons() {
        FileConfiguration cfg = FileManager.getConfig("weapons.yml").toCfg();
        for (String e : getYamlWeapons()) {
            if (cfg.getConfigurationSection("Weapons." + e) != null) {
                String key = "Weapons." + e;
                String mat = ".material";
                String sound = ".sound";

                try {
                    Weapon weapon = new Weapon(Material.valueOf(cfg.getString(key + mat).toUpperCase()),
                            e,
                            cfg.getString(key + ".key"), cfg.getInt(key + ".length"), getColor(e),
                            Float.parseFloat(cfg.getString(key + ".particleSize")), cfg.getDouble(key + ".damage"), cfg.getDouble(key + ".xpOnKill"), cfg.getInt(key + ".cooldownMillis"), Sound.valueOf(cfg.getString(key + sound).toUpperCase()),
                            Float.parseFloat(cfg.getString(key + ".vol")), Float.parseFloat(cfg.getString(key + ".pitch")), cfg.getInt(key + ".soundDistance"), cfg.getBoolean(key + ".isDefault"), cfg.getInt(key + ".shopPrice"));
                    activeWeapons.add(weapon);
                } catch (NumberFormatException | ConfigException ex) {
                    ex.printStackTrace();
                    Main.getInstance().getLogger().log(Level.SEVERE, "There was an error compiling the weapon.yml structure.");
                }
            }
        }
    }

    public static Color getColor(String e) throws ConfigException {
        String key = "Weapons." + e + ".color.";
        int r, g, b;
        try {
            r = FileManager.getConfig("weapons.yml").toCfg().getInt(key + "r", 0);
            g = FileManager.getConfig("weapons.yml").toCfg().getInt(key + "g", 0);
            b = FileManager.getConfig("weapons.yml").toCfg().getInt(key + "b", 0);

            return Color.fromRGB(r, g, b);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private boolean cooldown(UUID uuid, long millis) {
        if (cooldownMap.containsKey(uuid)) {
            if (millis > System.currentTimeMillis()) {
                cooldownMap.remove(uuid);
                return false;
            }
        } else {
            cooldownMap.put(uuid, millis);
        }
        return true;
    }

    public static List<String> getDefaultWeaponKeys() {
        return activeWeapons.stream().filter(Weapon::isDefault).map(Weapon::getKey).collect(Collectors.toList());
    }

    public static Weapon getWeapon(String key) {
        return getActiveWeapons().stream().filter(k -> k.getKey().equals(key)).toList().get(0);
    }

    public static List<String> getYamlWeapons() {
        return FileManager.getConfig("weapons.yml").toCfg().getConfigurationSection("Weapons").getKeys(false).stream().toList();
    }

    public static ArrayList<Weapon> getActiveWeapons() {
        return activeWeapons;
    }
}
