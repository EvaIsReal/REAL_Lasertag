package de.iv.game.lasertag.menus;

import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.game.GameItem;
import de.iv.iutils.exceptions.ManagerSetupException;
import de.iv.iutils.exceptions.MenuManagerException;
import de.iv.iutils.items.ItemBuilder;
import de.iv.iutils.menus.InventoryContext;
import de.iv.iutils.menus.Menu;
import de.iv.iutils.menus.MenuManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

public class InventoryMenu extends Menu {

    public InventoryMenu(InventoryContext mapper) {
        super(mapper);
    }

    @Override
    public String getMenuName() {
        return context.getOwner().getName() + "'s Inventory";
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handle(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING)) {

            String value = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING);
            if(value.startsWith("gun_")) {
                GameItem item = API.getGameItem(value.substring(4));
                API.applyGun(item.key(), p.getUniqueId().toString());
                p.getInventory().setItem(0, item.invItem());
                p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                p.sendMessage(ILib.color(Uni.PREFIX + "&aYou applied &6" + item.key()));

            } else if(value.startsWith("agun_")) {
                GameItem item = API.getGameItem(value.substring(5));
                API.applyGun(item.key(), p.getUniqueId().toString());
                p.getInventory().setItem(0, item.invItem());
                p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                p.sendMessage(ILib.color(Uni.PREFIX + "&aYou applied &6" + item.key()));
            }


        } else if(e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "other"), PersistentDataType.STRING)) {
            if(e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "other"), PersistentDataType.STRING).equals("close")) {
                try {
                    MenuManager.openMenu(BaseMenu.class, p);
                } catch (MenuManagerException | ManagerSetupException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        for (int i = 0; i < 9; i++) inventory.setItem(i, super.FILLER_GLASS);
        inventory.setItem(9, super.FILLER_GLASS);
        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        for (int i = 27; i < 36; i++) inventory.setItem(i, super.FILLER_GLASS);

        for (GameItem item : API.getPlayerInventoryItems(context.getOwner().getUniqueId().toString())) {
            inventory.addItem(item.invItem());
        }


        inventory.setItem(31, new ItemBuilder(Material.BARRIER).setName(ILib.color("&4close"))
                .addToPDC("other", "close")
                .build());
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
