package de.iv.game.lasertag.elements;

import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.util.WeaponAction;
import de.iv.iutils.items.ItemBuilder;
import org.bukkit.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Weapon {

    private Material material;
    private String name;
    private String key;
    private int length;
    private Color color;
    private float particleSize;
    private double damage;
    private double xp;
    private Sound sound;
    private float vol;
    private float pitch;
    private int soundDistance;

    public Weapon(Material material, String name, String key, int length, Color color, float particleSize, double damage, double xp, Sound sound, float vol, float pitch, int soundDistance) {
        this.material = material;
        this.name = name;
        this.key = key;
        this.length = length;
        this.color = color;
        this.particleSize = particleSize;
        this.damage = damage;
        this.xp = xp;
        this.sound = sound;
        this.vol = vol;
        this.pitch = pitch;
        this.soundDistance = soundDistance;
    }

    public ItemStack toItem() {
        ItemStack i = new ItemBuilder(material).setName(ChatColor.GREEN + name).build();
        ItemMeta im = i.getItemMeta();
        im.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "guns"), PersistentDataType.STRING, key);
        i.setItemMeta(im);
        return i;
    }

    public void fire(PlayerInteractEvent e) {
        Uni.raycast(e, color, particleSize, damage, sound, vol, pitch, soundDistance);
    }

    public float getParticleSize() {
        return particleSize;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public int getLength() {
        return length;
    }

    public Color getColor() {
        return color;
    }

    public double getDamage() {
        return damage;
    }

    public double getXp() {
        return xp;
    }

    public Sound getSound() {
        return sound;
    }

    public float getVol() {
        return vol;
    }

    public float getPitch() {
        return pitch;
    }

    public int getSoundDistance() {
        return soundDistance;
    }
}
