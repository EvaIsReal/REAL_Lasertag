package de.iv.game.lasertag.util;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    private HashMap<UUID, Long> cooldownMap = new HashMap<>();


    public void setCooldown(UUID uuid, long cooldownMillis) {
        cooldownMap.put(uuid, cooldownMillis);
    }

    public long getCooldown(UUID uuid) {
        return cooldownMap.getOrDefault(uuid, 0L);
    }
}
