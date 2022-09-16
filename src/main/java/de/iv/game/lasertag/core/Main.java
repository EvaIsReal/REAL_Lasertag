package de.iv.game.lasertag.core;

import de.iv.ILib;
import de.iv.game.lasertag.commands.*;
import de.iv.game.lasertag.elements.WeaponManager;
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
        API.init();
        SQLite.setup(instance);
        SQLite.connect();
        initSQL();

        //Register weapons
        WeaponManager.compileWeapons();
        System.out.println(API.getGameItems());

        updateOverlay(Bukkit.getOnlinePlayers());

        LasertagCommandManager lasertagCommandManager = new LasertagCommandManager();

        registerCommands();
        registerListeners(getServer().getPluginManager());
        Bukkit.getConsoleSender().sendMessage(ILib.color(Uni.PREFIX + "&aLasertag plugin started successfully."));
    }

    private void registerListeners(PluginManager pm) {
        pm.registerEvents(new WeaponManager(), instance);
        pm.registerEvents(new JoinListener(), instance);
        pm.registerEvents(new DeathListener(), instance);
        pm.registerEvents(new PlayerGameLevelUpListener(), instance);
        pm.registerEvents(new XpReceiveListener(), instance);
        pm.registerEvents(new QuitListener(), instance);
    }

    private void registerCommands() {
        getCommand("givegun").setExecutor(new GiveGunCommand());
        getCommand("menu").setExecutor(new MenuCommand());
        getCommand("setlevel").setExecutor(new GameLevelCommand());
        getCommand("accreset").setExecutor(new UserResetCommand());
        getCommand("getLevel").setExecutor(new GetLevelCommand());
        getCommand("lasertag").setExecutor(new LasertagCommandManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        FileManager.getConfigs().forEach(c -> FileManager.save(c.getName()));
        SQLite.disconnect();
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.kickPlayer(ILib.color("&c&lServer is restarting..."));
        });
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
                        OverlayManager.update(sb, p);
                        if(Main.getInstance().getBars().containsKey(p.getUniqueId())) {
                            BossBar bar = Main.getInstance().getBars().get(p.getUniqueId());
                            bar.setTitle(ILib.color("&7&lLevel &5&l" + String.valueOf(API.getPlayerLevel(p.getUniqueId().toString()))));
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
