package de.iv.game.lasertag.game;

import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.game.guns.AbilityGun;
import de.iv.game.lasertag.exceptions.ConfigException;
import de.iv.game.lasertag.fs.FileManager;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class WeaponManager implements Listener {

    private static ArrayList<Weapon> activeWeapons = new ArrayList<>();
    private HashMap<UUID, Long> cooldownMap = new HashMap<>();
    private HashMap<UUID, Long> abilityCooldownMap = new HashMap<>();
    private static ArrayList<AbilityPerformer> abilityWeapons = new ArrayList<>();



    public static void registerAbilityGuns() {
        getAbilityWeapons().add(new AbilityGun());
        getActiveWeapons().addAll(getAbilityWeapons().stream().map(AbilityPerformer::getWeapon).toList());
   }

   public static void unregisterGuns() {
        getActiveWeapons().clear();
   }

    @EventHandler
    public void handleFire(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (e.hasItem()) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) | e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && item != null) {
                if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING)) {
                    String gunKey = e.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING);
                    if(gunKey.startsWith("gun_")) {
                        Weapon w = getWeapon(gunKey.substring(4));
                        if (w == null) return;
                        if (!cooldownMap.containsKey(e.getPlayer().getUniqueId()) || System.currentTimeMillis() - cooldownMap.get(e.getPlayer().getUniqueId()) >= w.getCooldownMillis()) {
                            cooldownMap.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
                            w.fire(e);
                        } else {
                            e.getPlayer().playSound(e.getPlayer(), Sound.BLOCK_COMPARATOR_CLICK, 1, 2);
                        }
                    } else if(gunKey.startsWith("agun_")) {
                        Weapon w = getWeapon(gunKey.substring(5));
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
    }

    @EventHandler
    public void handleAbility(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        Player p = e.getPlayer();
        if(!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if(meta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING)) {
            String gunKey = meta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING);
            if(!gunKey.startsWith("agun")) return;
            e.setCancelled(true);
            AbilityPerformer w = getAbilityWeapon(gunKey.substring(5));
            if(w == null) return;
            if(!p.isSneaking()) {
                if(!abilityCooldownMap.containsKey(p.getUniqueId()) || System.currentTimeMillis() - abilityCooldownMap.get(p.getUniqueId()) >= w.abilityCooldownMillis()) {
                    abilityCooldownMap.put(p.getUniqueId(), System.currentTimeMillis());
                    w.performAbility(p, w.getWeapon());
                } else {
                    ILib.sendActionbar(p, ILib.color("&c&lFÄHIGKEIT LÄDT NACH..."));
                    Uni.getNearbyPlayers(p.getLocation(), w.getWeapon().getSoundDistance()).forEach(np -> {
                        np.playSound(np, Sound.BLOCK_BEACON_DEACTIVATE, 1, 2);
                    });
                }
                return;
            } else if((boolean) API.getGameProfile(p).getProperties().get("ulti_ready")) {
                API.setAccumulatedXp(p, 0);
                w.performUlti(p, w.getWeapon());
                p.setGlowing(false);
                API.getGameProfile(p).getProperties().put("ulti_ready", false);
                return;
            } else {
                ILib.sendActionbar(p, ILib.color("&c&lNICHT GENÜGEND XP!"));
                p.playSound(p, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 2);
            }
        }
    }


    public static void compileWeapons() {
        FileConfiguration cfg = FileManager.getConfig("weapons.yml").getCfg();
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
            r = FileManager.getConfig("weapons.yml").getCfg().getInt(key + "r", 0);
            g = FileManager.getConfig("weapons.yml").getCfg().getInt(key + "g", 0);
            b = FileManager.getConfig("weapons.yml").getCfg().getInt(key + "b", 0);

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

    private boolean abilityCooldown(UUID uuid, long millis) {
        if (abilityCooldownMap.containsKey(uuid)) {
            if (millis > System.currentTimeMillis()) {
                abilityCooldownMap.remove(uuid);
                return false;
            }
        } else {
            abilityCooldownMap.put(uuid, millis);
        }
        return true;
    }

    public static List<String> getDefaultWeaponKeys() {
        return activeWeapons.stream().filter(Weapon::isDefault).map(Weapon::getKey).collect(Collectors.toList());
    }

    public static Weapon getWeapon(String key) {
        return getActiveWeapons().stream().filter(k -> k.getKey().equals(key)).toList().get(0);
    }

    public static AbilityPerformer getAbilityWeapon(String key) {
        return (AbilityPerformer) getAbilityWeapons().stream().filter(k -> k.getWeapon().getKey().equals(key)).toList().get(0);
    }

    public static List<String> getYamlWeapons() {
        return FileManager.getConfig("weapons.yml").getCfg().getConfigurationSection("Weapons").getKeys(false).stream().toList();
    }

    public static ArrayList<AbilityPerformer> getAbilityWeapons() {
        return abilityWeapons;
    }

    public static ArrayList<Weapon> getActiveWeapons() {
        return activeWeapons;
    }
}
