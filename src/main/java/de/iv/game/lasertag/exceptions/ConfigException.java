package de.iv.game.lasertag.exceptions;

import de.iv.game.lasertag.fs.Config;

public class ConfigException extends Throwable {

    private Config config;

    public ConfigException(Config config) {
        this.config = config;
    }

    @Override
    public String getMessage() {
        return "There was an error in config " + config.getName();
    }
}
