package de.iv.game.lasertag.fs;

import de.iv.game.lasertag.core.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {

    private static ArrayList<Config> configs = new ArrayList<>();

    public static void registerConfigs() {
        //add configs to list
        configs.add(new Config("weapons.yml", Main.getInstance().getDataFolder()));
        configs.add(new Config("config.yml", Main.getInstance().getDataFolder()));
        configs.add(new Config("scoreboard.yml", Main.getInstance().getDataFolder()));
    }

    public static void save(String fileName) {
        FileConfiguration cfg;
        for(File file : Arrays.stream(Main.getInstance().getDataFolder().listFiles()).toList()) {
            if(file.getName().equals(fileName)) {
                cfg = YamlConfiguration.loadConfiguration(file);
                try {
                    cfg.save(file);
                    System.out.println("SAVED CONFIG " + file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Config getConfig(String name) {
        return configs.stream().filter(c -> c.getName().equals(name)).toList().get(0);
    }


    public static ArrayList<Config> getConfigs() {
        return configs;
    }

}
