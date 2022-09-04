package de.iv.game.lasertag.listeners;

import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.events.PlayerGameLevelUpEvent;
import de.iv.game.lasertag.util.OverlayManager;
import de.iv.iutils.sqlite.SQLite;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerGameLevelUpListener implements Listener {

    @EventHandler
    public void onLevelUp(PlayerGameLevelUpEvent e) {
        Player p = e.getPlayer();
        int oldLevel = e.getOldLevel();
        int newLevel = e.getNewLevel();

        p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        p.sendTitle(ILib.color("&5&lL&d&level &5&lU&d&lp"), ILib.color("&7" + oldLevel + " &8&l-> &6" + newLevel), 5, 23, 5);


    }

}
