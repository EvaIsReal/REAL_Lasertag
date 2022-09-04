package de.iv.game.lasertag.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGameLevelUpEvent extends Event {

    public static HandlerList handlerList = new HandlerList();

    private Player player;
    private int oldLevel;
    private int newLevel;
    private double score;

    public PlayerGameLevelUpEvent(Player player, int oldLevel, int newLevel, double score) {
        this.player = player;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.score = score;
    }

    public Player getPlayer() {
        return player;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public double getScore() {
        return score;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
