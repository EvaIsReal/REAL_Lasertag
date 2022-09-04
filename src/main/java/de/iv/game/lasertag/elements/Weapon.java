package de.iv.game.lasertag.elements;

import de.iv.ILib;
import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.core.Uni;
import de.iv.iutils.items.ItemBuilder;
import org.bukkit.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Weapon implements GameItem {

    @Override
    public String key() {
        return getKey();
    }

    @Override
    public ItemStack invItem() {
        return toItem();
    }

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
    private boolean isDefault;
    private int cooldownMillis;
    private int shopPrice;

    public Weapon(Material material, String name, String key, int length, Color color, float particleSize, double damage, double xp, int cooldownMillis,
                  Sound sound, float vol, float pitch, int soundDistance, boolean isDefault, int shopPrice) {
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
        this.isDefault = isDefault;
        this.cooldownMillis = cooldownMillis;
        this.shopPrice = shopPrice;
    }

    @Override
    public boolean defaultItem() {
        return isDefault();
    }

    @Override
    public int price() {
        return getShopPrice();
    }

    public ItemStack toItem() {
        ItemStack i = new ItemBuilder(material).setName(ChatColor.GREEN + name).build();
        ItemMeta im = i.getItemMeta();
        im.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING,"gun_" + key);
        i.setItemMeta(im);
        return i;
    }

    public void fire(PlayerInteractEvent e) {
        Uni.raycast(e, color, length, particleSize, damage, sound, vol, pitch, soundDistance, this);
    }

    @Override
    public ItemStack shopItem() {
        return new ItemBuilder(material).setName(ChatColor.GREEN + name).setLore(
                ILib.color("&8long range"),
                ILib.color("&8Cooldown ticks: &b" + cooldownMillis),
                ILib.color(""),
                ILib.color("&7Price: &6" + shopPrice),
                ILib.color("")
        ).addToPDC("game_item", "gun_" + key).build();
    }

    @Override
    public ItemStack soldItem() {
        return new ItemBuilder(material).setName(ChatColor.GREEN + name).setLore(
                ILib.color("&8long range"),
                ILib.color("&8Cooldown ticks: &b" + cooldownMillis),
                ILib.color(""),
                ILib.color("&c&mPrice: " + shopPrice),
                ILib.color("&cYou already bought this item."),
                ""
        ).addToPDC("game_item", "gun_" + key).build();
    }

    public int getCooldownMillis() {
        return cooldownMillis;
    }

    public int getShopPrice() {
        return shopPrice;
    }

    public boolean isDefault() {
        return isDefault;
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
