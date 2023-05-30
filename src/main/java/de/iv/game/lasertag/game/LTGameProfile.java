package de.iv.game.lasertag.game;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Stores non-persistent information about the player.
 * Resets when the player logs off.
 */
public class LTGameProfile {

    private Player player;
    private HashMap<String, Object> properties;

    public LTGameProfile(Player player) {
        this.player = player;
        this.properties = new HashMap<>();
        this.properties.put("a_xp", 0D);
        this.properties.put("ulti_ready", false);
    }

    public Player getPlayer() {
        return player;
    }

    public HashMap<String, Object> getProperties() {
        return properties;
    }
}
