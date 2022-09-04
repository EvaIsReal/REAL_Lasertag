package de.iv.game.lasertag.menus;

import de.iv.ILib;
import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.core.Uni;
import de.iv.iutils.exceptions.ManagerSetupException;
import de.iv.iutils.exceptions.MenuManagerException;
import de.iv.iutils.items.ItemBuilder;
import de.iv.iutils.menus.InventoryMapper;
import de.iv.iutils.menus.Menu;
import de.iv.iutils.menus.MenuManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

public class BaseMenu extends Menu {

    public BaseMenu(InventoryMapper mapper) {
        super(mapper);
    }

    @Override
    public String getMenuName() {
        return ILib.color(Uni.PREFIX);
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handle(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "base_menu"), PersistentDataType.STRING)) {
            switch (e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "base_menu"), PersistentDataType.STRING)) {
                case "shop" -> {
                    try {
                        MenuManager.openMenu(ShopMenu.class, p);
                    } catch (MenuManagerException | ManagerSetupException ex) {
                        ex.printStackTrace();
                    }
                }
                case "inv" -> {
                    try {
                        MenuManager.openMenu(InventoryMenu.class, p);
                    } catch (MenuManagerException | ManagerSetupException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        setFillerGlass();
        inventory.setItem(11, new ItemBuilder(Material.PLAYER_HEAD)
                .setSkullData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjMwOGJmNWNjM2U5ZGVjYWYwNzcwYzNmZGFkMWUwNDIxMjFjZjM5Y2MyNTA1YmJiODY2ZTE4YzZkMjNjY2QwYyJ9fX0=")
                .setName(ILib.color("&aInventory"))
                .addToPDC("base_menu", "inv")
                .build());
        inventory.setItem(15, new ItemBuilder(Material.PLAYER_HEAD)
                .setSkullData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjFiZjZhODM4NjYxZTE1ZGY2NjE5OTA5NDE2NWI3NTM4MzJjNzIzNDcxZDJiMjA0ZDRkNTA3NGFhNjA4Yzc4NCJ9fX0=")
                .setName(ILib.color("&aShop"))
                .addToPDC("base_menu", "shop")
                .build());
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
