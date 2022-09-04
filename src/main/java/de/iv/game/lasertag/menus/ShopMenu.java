package de.iv.game.lasertag.menus;

import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.elements.GameItem;
import de.iv.game.lasertag.elements.Weapon;
import de.iv.game.lasertag.elements.WeaponManager;
import de.iv.iutils.exceptions.ManagerSetupException;
import de.iv.iutils.exceptions.MenuManagerException;
import de.iv.iutils.items.ItemBuilder;
import de.iv.iutils.menus.InventoryMapper;
import de.iv.iutils.menus.Menu;
import de.iv.iutils.menus.MenuManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ShopMenu extends Menu {

    public ShopMenu(InventoryMapper mapper) {
        super(mapper);
    }

    @Override
    public String getMenuName() {
        return ILib.color("&5Shop &8>> Coins: " + API.getPlayerBalance(mapper.getOwner().getUniqueId().toString()));
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
        if(e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING)) {
            String key = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING);
            GameItem i = API.getGameItem(key.substring(4));
            if(API.getPlayerInventoryItems(mapper.getOwner().getUniqueId().toString()).contains(i)) {
                mapper.getOwner().playSound(mapper.getOwner(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
            } else if(API.getPlayerBalance(mapper.getOwner().getUniqueId().toString()) >= i.price()){
                API.addPlayerInventoryItem(mapper.getOwner().getUniqueId().toString(), key.substring(4));
                API.setPlayerBalance(mapper.getOwner().getUniqueId().toString(), API.getPlayerBalance(mapper.getOwner().getUniqueId().toString()) - i.price());
                mapper.getOwner().playSound(mapper.getOwner(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                mapper.getOwner().sendMessage(ILib.color(Uni.PREFIX + "&aYou bought " + i.invItem().getItemMeta().getDisplayName() + " from the shop for &6" + i.price() + " Coins."));
                try {
                    reload();
                } catch (MenuManagerException | ManagerSetupException ex) {
                    ex.printStackTrace();
                }
                API.checkDB(mapper.getOwner().getUniqueId().toString());
            } else mapper.getOwner().playSound(mapper.getOwner(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);
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
        inventory.setItem(26, super.FILLER_GLASS);;
        inventory.setItem(18, super.FILLER_GLASS);
        for (int i = 27; i < 36; i++) inventory.setItem(i, super.FILLER_GLASS);
        //iterate through available game items and exclude items in players possession

        for (GameItem item : API.getGameItems()) {
            if(API.getPlayerInventoryItems(mapper.getOwner().getUniqueId().toString()).contains(item) && !item.defaultItem()) {
                inventory.addItem(item.soldItem());
            } else if(!API.getPlayerInventoryItems(mapper.getOwner().getUniqueId().toString()).contains(item) && !item.defaultItem()) {
                inventory.addItem(item.shopItem());
            }
        }


        inventory.setItem(31, new ItemBuilder(Material.BARRIER).setName(ILib.color("&4close")).addToPDC("other", "close").build());
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
