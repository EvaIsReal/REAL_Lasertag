package de.iv.game.lasertag.events;

import de.iv.game.lasertag.core.API;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameXpChangeEvent extends Event {

    public static HandlerList handlerList = new HandlerList();
    private Player player;
    private double newValue;
    private double oldValue;

    public GameXpChangeEvent(Player player, double oldValue, double newValue) {
        this.player = player;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public double getOldValue() {
        return oldValue;
    }

    public Player getPlayer() {
        return player;
    }

    public double getScore() {
        return API.getPlayerGameScore(player.getUniqueId().toString());
    }

    public double getNewValue() {
        return newValue;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
