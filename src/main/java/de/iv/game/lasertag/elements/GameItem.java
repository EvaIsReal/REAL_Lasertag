package de.iv.game.lasertag.elements;

import org.bukkit.inventory.ItemStack;

public interface GameItem {

    public abstract String key();
    public abstract ItemStack invItem();
    public abstract ItemStack shopItem();
    public abstract ItemStack soldItem();
    public abstract boolean defaultItem();
    public abstract int price();

}
