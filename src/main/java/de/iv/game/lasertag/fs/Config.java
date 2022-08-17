package de.iv.game.lasertag.fs;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;

public class Config {

    private File destination;
    private FileConfiguration cfg;
    private String name;
    private File file;

    public Config(String resourceName, File destination) {
        this.destination = destination;
        this.name = resourceName;

        try {
            if(!destination.exists()) destination.mkdir();
            File file = new File(destination, resourceName);
            this.file = file;
            if(!file.exists()) {
                if(file.createNewFile()) {
                    InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName);
                    if(in != null) {
                        FileUtils.copyInputStreamToFile(in, file);
                    } else System.out.println("In is null");
                }
            }
            cfg = new YamlConfiguration();
            cfg.load(file);
        } catch (SecurityException | IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public File getDestination() {
        return destination;
    }

    public FileConfiguration getCfg() {
        return YamlConfiguration.loadConfiguration(file);
    }
    public File getSource() {
        return file;
    }
}
