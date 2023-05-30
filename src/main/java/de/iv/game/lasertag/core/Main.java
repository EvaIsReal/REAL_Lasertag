package de.iv.game.lasertag.core;

import de.iv.ILib;
import de.iv.game.lasertag.commands.*;
import de.iv.game.lasertag.game.WeaponManager;
import de.iv.game.lasertag.fs.FileManager;
import de.iv.game.lasertag.listeners.*;
import de.iv.game.lasertag.util.OverlayManager;
import de.iv.iutils.items.ItemBuilder;
import de.iv.iutils.menus.MenuManager;
import de.iv.iutils.sqlite.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main instance;
    public HashMap<UUID, BossBar> bars = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        MenuManager.setup(getServer(), instance);
        FileManager.registerConfigs();
        ItemBuilder.setup(instance);
        SQLite.setup(instance);
        SQLite.connect();
        initSQL();

        //Register weapons
        WeaponManager.compileWeapons();
        API.init();
        System.out.println(API.getGameItems());

        updateOverlay(Bukkit.getOnlinePlayers());
        OverlayManager.updatePlayerList();

        registerCommands();
        registerListeners(getServer().getPluginManager());
        WeaponManager.getActiveWeapons().forEach(w -> getLogger().info(w.key()));
        Bukkit.getConsoleSender().sendMessage(ILib.color(Uni.PREFIX + "&aLasertag plugin started successfully."));
    }

    private void registerListeners(PluginManager pm) {
        pm.registerEvents(new WeaponManager(), instance);
        pm.registerEvents(new BukkitPlayerListener(), instance);
        pm.registerEvents(new DeathListener(), instance);
        pm.registerEvents(new PlayerGameLevelUpListener(), instance);
        pm.registerEvents(new XpReceiveListener(), instance);
    }

    private void registerCommands() {
        getCommand("givegun").setExecutor(new GiveGunCommand());
        getCommand("menu").setExecutor(new MenuCommand());
        getCommand("inventoryitems").setExecutor(new InventoryItemCommand());
        getCommand("setlevel").setExecutor(new GameLevelCommand());
        getCommand("accreset").setExecutor(new UserResetCommand());
        getCommand("getLevel").setExecutor(new GetLevelCommand());
        getCommand("ltinfo").setExecutor(new InfoCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        WeaponManager.unregisterGuns();
        FileManager.getConfigs().forEach(c -> FileManager.save(c.getName()));
        Bukkit.getOnlinePlayers().forEach(OverlayManager::removeBossBar);
        SQLite.disconnect();
    }

    private void initSQL() {
        SQLite.update("CREATE TABLE IF NOT EXISTS `playerData`(" +
                "`uuid` VARCHAR NOT NULL," +
                "`balance` DOUBLE," +
                "`score` DOUBLE," +
                "`level` INT," +
                "`inventory_items` VARCHAR," +
                "`applied_gun` VARCHAR" +
                ");");
    }

    private void updateOverlay(Collection<? extends Player> playerList) {
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if(!playerList.isEmpty()) {
                    playerList.forEach(p -> {
                        OverlayManager.applyBossBar(p);
                        Scoreboard sb = OverlayManager.getBaseScoreboard(p);
                        // Scoreboard sb = p.getScoreboard();
                        OverlayManager.update(sb, p);
                        if(Main.getInstance().getBars().containsKey(p.getUniqueId())) {
                            BossBar bar = Main.getInstance().getBars().get(p.getUniqueId());
                            bar.setTitle(ILib.color("&7&lLevel &5&l" + API.getPlayerLevel(p.getUniqueId().toString())));
                            bar.setVisible(true);
                            double score = API.getPlayerGameScore(p.getUniqueId().toString());
                            double reqC = API.getRequiredScore(API.getPlayerLevel(p.getUniqueId().toString()));
                            double reqN = API.getRequiredScore(API.getPlayerLevel(p.getUniqueId().toString()) + 1);
                            bar.setProgress((score-reqC)/(reqN-reqC));
                        }
                    });
                }
            }
        }.runTaskTimer(Main.getInstance(), 10, 10);


    }

    public static Main getInstance() {
        return instance == null ? new Main() : instance;
    }

    public HashMap<UUID, BossBar> getBars() {
        return bars;
    }
}
