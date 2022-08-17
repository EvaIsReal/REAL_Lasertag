package de.iv.game.lasertag.core;

import de.iv.ILib;
import de.iv.game.lasertag.commands.GiveGunCommand;
import de.iv.game.lasertag.elements.Weapon;
import de.iv.game.lasertag.elements.WeaponManager;
import de.iv.game.lasertag.exceptions.ConfigException;
import de.iv.game.lasertag.fs.Config;
import de.iv.game.lasertag.fs.FileManager;
import de.iv.iutils.menus.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        MenuManager.setup(getServer(), instance);
        FileManager.registerConfigs();

        //WeaponManager.getYamlWeapons().forEach(System.out::println);
        WeaponManager.compileWeapons();
        WeaponManager.getActiveWeapons().forEach(w -> System.out.println(w.getName()));


        registerCommands();
        registerListeners(Bukkit.getPluginManager());

        Bukkit.getConsoleSender().sendMessage(ILib.color("&aLasertag plugin started successfully."));
    }

    private void registerListeners(PluginManager pm) {
        pm.registerEvents(new WeaponManager(), instance);
    }

    private void registerCommands() {
        getCommand("givegun").setExecutor(new GiveGunCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        FileManager.getConfigs().forEach(c -> FileManager.save(c.getName()));
    }

    public static Main getInstance() {
        return instance == null ? new Main() : instance;
    }

}
