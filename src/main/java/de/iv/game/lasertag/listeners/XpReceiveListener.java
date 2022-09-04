package de.iv.game.lasertag.listeners;

import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.events.GameXpChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class XpReceiveListener implements Listener {

    @EventHandler
    public void onXpReceive(GameXpChangeEvent e) {
        Player p = e.getPlayer();
        double score = e.getNewValue();
        int nextLevel = API.getPlayerLevel(p.getUniqueId().toString()) + 1;

        if(score >= API.getRequiredScore(nextLevel)) {
            API.setPlayerLevel(API.getLevelFromScore(score), p.getUniqueId().toString());
        }

    }

}
