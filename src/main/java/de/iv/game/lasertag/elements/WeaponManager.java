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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class WeaponManager implements Listener {

    private static ArrayList<Weapon> activeWeapons = new ArrayList<>();

    @EventHandler
    public void handleFire(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if(e.hasItem()) {
            if(e.getAction().equals(Action.RIGHT_CLICK_AIR) | e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && item != null) {
                if(e.getItem().hasItemMeta() && e.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "guns"), PersistentDataType.STRING)) {
                    String gunKey = e.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "guns"), PersistentDataType.STRING);
                    Weapon w = getWeapon(gunKey);
                    w.fire(e);
                }
            }
        }
    }




    public static void compileWeapons() {
        FileConfiguration cfg = FileManager.getConfig("weapons.yml").getCfg();
        for (String e : getYamlWeapons()) {
            if(cfg.getConfigurationSection("Weapons." + e) != null) {
                System.out.println(cfg.getConfigurationSection("Weapons." + e).getKeys(false));
                String key = "Weapons." + e;
                String mat = ".material";
                String sound = ".sound";


                try {
                    Weapon weapon = new Weapon(Material.valueOf(cfg.getString(key + mat).toUpperCase()),
                            "jeff",
                            cfg.getString(key + ".key"), cfg.getInt(key + ".length"), getColor(e),
                            Float.parseFloat(cfg.getString(key + ".particleSize")), cfg.getDouble(key + ".damage"), cfg.getDouble(key + ".xpOnKill"), Sound.valueOf(cfg.getString(key + sound).toUpperCase()),
                            Float.parseFloat(cfg.getString(key + ".vol")), Float.parseFloat(cfg.getString(key + ".pitch")), cfg.getInt(key + ".soundDistance"));
                    activeWeapons.add(weapon);
                } catch (NumberFormatException | ConfigException ex) {
                    ex.printStackTrace();

                    //Main.getInstance().getLogger().log(Level.SEVERE, "There was an error compiling the weapon.yml structure.");
                }
            }
        }
    }

    public static Color getColor(String e) throws ConfigException {
        String key = "Weapons." + e + ".color.";
        int r, g, b;
        try {
           r = FileManager.getConfig("weapons.yml").getCfg().getInt(key + "r", 0);
           g = FileManager.getConfig("weapons.yml").getCfg().getInt(key + "g", 0);
           b = FileManager.getConfig("weapons.yml").getCfg().getInt(key + "b", 0);

           return Color.fromRGB(r, g, b);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Weapon getWeapon(String key) {
        return activeWeapons.stream().filter(k -> k.getKey().equals(key)).toList().get(0);
    }

    public static List<String> getYamlWeapons() {
        return FileManager.getConfig("weapons.yml").getCfg().getConfigurationSection("Weapons").getKeys(false).stream().toList();
    }

    public static ArrayList<Weapon> getActiveWeapons() {
        return activeWeapons;
    }
}
