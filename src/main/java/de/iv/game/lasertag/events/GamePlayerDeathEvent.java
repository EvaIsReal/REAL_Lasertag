package de.iv.game.lasertag.events;

import de.iv.game.lasertag.game.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GamePlayerDeathEvent extends Event {

    public static HandlerList handlerList = new HandlerList();
    private Player killer;
    private Player target;
    private Weapon weapon;

    public GamePlayerDeathEvent(Player killer, Player target, Weapon weapon) {
        this.killer = killer;
        this.target = target;
        this.weapon = weapon;
    }

    public Player getKiller() {
        return killer;
    }

    public Player getTarget() {
        return target;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
