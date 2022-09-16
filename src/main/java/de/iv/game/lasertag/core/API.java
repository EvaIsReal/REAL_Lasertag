package de.iv.game.lasertag.core;

import com.google.gson.Gson;
import de.iv.ILib;
import de.iv.game.lasertag.elements.GameItem;
import de.iv.game.lasertag.elements.Weapon;
import de.iv.game.lasertag.elements.WeaponManager;
import de.iv.game.lasertag.events.GameXpChangeEvent;
import de.iv.game.lasertag.events.PlayerGameLevelUpEvent;
import de.iv.game.lasertag.exceptions.RegisterUserException;
import de.iv.game.lasertag.exceptions.ShopInteractionException;
import de.iv.game.lasertag.fs.FileManager;
import de.iv.iutils.sqlite.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class API {

    private static ArrayList<? extends GameItem> gameItems;
    private static boolean isSetup = false;

    public static void init() {
        gameItems = WeaponManager.getActiveWeapons();
    }

    public static void registerUser(String uuid) throws RegisterUserException {
        if(!isRegistered(uuid)) {
            SQLite.update("INSERT INTO playerData(uuid, balance, score, level, inventory_items) VALUES ('"+uuid+"', '"+1000+"', '"+7.75+"', '"+1+"', '"+ initialInventoryItemData()+"')");
        }
        else throw new RegisterUserException();
    }

    public static double round(double x, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(x));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void logInfo(java.lang.String str) {
        Main.getInstance().getLogger().info(str);
    }


    public static boolean isRegistered(String uuid) {
        ResultSet rs = SQLite.query("SELECT * FROM playerData WHERE uuid = '"+uuid+"'");
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<? extends GameItem> getPlayerInventoryItems(String uuid) {
        Gson gson = new Gson();
        ResultSet rs = SQLite.query("SELECT inventory_items FROM playerData WHERE uuid = '"+uuid+"'");
        try {
            if(rs.next()) {
                String json = rs.getString("inventory_items");
                //Type listType = new TypeToken<List<GameItem>>() {}.getType();
                String[] gameItems = gson.fromJson(json, String[].class);
                List<GameItem> r = new ArrayList<>();
                for (String key : gameItems) {
                    GameItem i = getGameItem(key);
                    if(i != null) {
                        r.add(i);
                    } else {
                        List<String> upload = Arrays.stream(gameItems).toList();
                        upload.remove(key);
                        SQLite.update("UPDATE playerData SET inventory_items = '"+gson.toJson(upload)+"' WHERE uuid = '"+uuid+"'");

                    }

                }
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Weapon getAppliedGun(String uuid) {
        ResultSet rs = SQLite.query("SELECT applied_gun FROM playerData WHERE uuid = '"+uuid+"'");
        try {
            if(rs.next()) {
                String key = rs.getString("applied_gun");
                return WeaponManager.getWeapon(key);
            }
        } catch (SQLException | ArrayIndexOutOfBoundsException ignored) {}
        return null;
    }

    public static double getPlayerGameScore(String uuid) {
        ResultSet rs = SQLite.query("SELECT score FROM playerData WHERE uuid = '"+uuid+"'");
        try {
            if(rs.next()) {
                return rs.getDouble("score");
            } else System.out.println("Score not found");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static void checkDB(String uuid) {
        Gson gson = new Gson();
        ResultSet rs = SQLite.query("SELECT inventory_items FROM playerData WHERE uuid = '"+uuid+"'");
        try {
            if(rs.next()) {
                String[] json = gson.fromJson(rs.getString("inventory_items"), String[].class);
                ArrayList<String> res = new ArrayList<>(List.of(json));
                for (int i = 0; i < json.length; i++) {
                    GameItem item = getGameItem(json[i]);
                    if(item == null) {
                        //key does not exist anymore -> skip key in result
                        res.remove(i);
                    }
                    System.out.println(res.toString());
                }
                SQLite.update("UPDATE playerData SET inventory_items = '"+ res.toString() +"' WHERE uuid = '"+uuid+"'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T extends GameItem> boolean playerInventoryContainsGameItem(Player p, T item) {
        return p.getInventory().contains(item.invItem());
    }

    public static void applyGun(String key, String uuid) {
        SQLite.update("UPDATE playerData SET applied_gun = '"+key+"' WHERE uuid = '"+uuid+"'");
    }


    public static void setPlayerInventoryItems(String uuid, List<? extends GameItem> gameItems) {
        String json = new Gson().toJson(gameItems.stream().map(GameItem::key));
        SQLite.update("UPDATE playerData SET inventory_items = '"+json+"' WHERE uuid = '"+uuid+"'");
    }

    public static void addPlayerInventoryItem(String uuid, String key) {
        ResultSet rs = SQLite.query("SELECT inventory_items FROM playerData WHERE uuid = '"+uuid+"'");
        Gson gson = new Gson();
        try {
            if(rs.next()) {
                String[] json = gson.fromJson(rs.getString("inventory_items"), String[].class);
                ArrayList<String> res = new ArrayList<>(List.of(json));
                GameItem i = getGameItem(key);
                if(i == null) throw new NullPointerException("No weapon '"+key+"' found");
                res.add(i.key());
                SQLite.update("UPDATE playerData SET inventory_items = '"+ res.toString() +"' WHERE uuid = '"+uuid+"'");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getPlayerLevel(String uuid) {
        ResultSet rs = SQLite.query("SELECT level FROM playerData WHERE uuid = '"+uuid+"'");
        try {
            if(rs.next()) {
                return rs.getInt("level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void setPlayerGameScore(String uuid, double score) {
        Main.getInstance().getServer().getPluginManager().callEvent(new GameXpChangeEvent(Bukkit.getPlayer(UUID.fromString(uuid)), getPlayerGameScore(uuid), score));
        SQLite.update("UPDATE playerData SET score = '"+score+"' WHERE uuid = '"+uuid+"'");
    }

    public static int getLevelFromScore(double score) {
        return (int) ILib.eval(FileManager.getConfig("config.yml").toCfg().getString("level_function").replace("s", String.valueOf(score)));
    }

    public static void setPlayerLevel(int level, String uuid) {
        Main.getInstance().getServer().getPluginManager().callEvent(new PlayerGameLevelUpEvent(Bukkit.getPlayer(UUID.fromString(uuid)),
                getPlayerLevel(uuid), level, getPlayerGameScore(uuid)));
        SQLite.update("UPDATE playerData SET level = '"+level+"' WHERE uuid = '"+uuid+"'");
    }

    public static double getRequiredScore(int level) {
        return round(ILib.eval(FileManager.getConfig("config.yml").toCfg().getString("score_function").replace("l", String.valueOf(level))), 2);
    }


    public static GameItem getGameItem(String key) {
        try {
            return gameItems.stream().filter(g -> g.key().equals(key)).toList().get(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static void buyItem(String key, String uuid) throws ShopInteractionException {
        double bal = getPlayerBalance(uuid);
        GameItem item = getGameItem(key);
        if(bal < item.price()) throw new ShopInteractionException("Player does not have enough money");

    }

    public static void setPlayerBalance(String uuid, double balance) {
        SQLite.update("UPDATE playerData SET balance = '"+balance+"' WHERE uuid = '"+uuid+"'");
    }

    public static double getPlayerBalance(String uuid) {
        ResultSet rs = SQLite.query("SELECT balance FROM playerData WHERE uuid = '"+uuid+"'");
        try {
            if(rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void resetPlayer(String uuid) {
        SQLite.update("DELETE FROM playerData WHERE uuid = '"+uuid+"'");
    }


    private static String initialInventoryItemData() {
        return new Gson().toJson(WeaponManager.getDefaultWeaponKeys());
    }

    public static ArrayList<? extends GameItem> getGameItems() {
        return gameItems;
    }
}
