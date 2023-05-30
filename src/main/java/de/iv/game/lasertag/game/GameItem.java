package de.iv.game.lasertag.game;

import org.bukkit.inventory.ItemStack;

public interface GameItem {

    String key();
    ItemStack invItem();
    ItemStack shopItem();
    ItemStack soldItem();
    boolean defaultItem();
    int price();

    ItemStack toItem();

}
