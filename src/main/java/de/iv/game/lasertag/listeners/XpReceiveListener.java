package de.iv.game.lasertag.listeners;

import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.events.GameXpChangeEvent;
import de.iv.game.lasertag.game.AbilityPerformer;
import de.iv.game.lasertag.game.Weapon;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.atomic.AtomicInteger;

public class XpReceiveListener implements Listener {

    @EventHandler
    public void onXpReceive(GameXpChangeEvent e) {
        Player p = e.getPlayer();
        double score = e.getNewValue();
        double dScore = e.getNewValue() - e.getOldValue();

        double aXp = (double) API.getAccumulatedXp(p);
        //API.getGameProfile(p).getProperties().put("a_xp", Uni.round(aXp + dScore, 1));
        API.setAccumulatedXp(p, Uni.round(aXp + dScore, 1));

        Weapon w = API.getAppliedGun(p.getUniqueId().toString());
        if(w != null && w.getKey().startsWith("a_")) {
            AbilityPerformer ap = (AbilityPerformer) w;
            if((double) API.getGameProfile(p).getProperties().get("a_xp") >= ((double) ap.ultiXP()) && !((boolean)API.getGameProfile(p)
                    .getProperties().get("ulti_ready"))) {
                 p.playSound(p, Sound.ENTITY_WITHER_SPAWN, 1, 1);
                 AtomicInteger t = new AtomicInteger();
                 Uni.runPlayerOnTick(p, 0, player -> {
                     Uni.particleCylinder(player.getLocation(), Uni.round(t.doubleValue()/2, 2), 10, 0.2, 1, Particle.VILLAGER_HAPPY, 1, 1);
                     t.getAndIncrement();
                 }, player -> t.get() >= 10);
                 p.setGlowing(true);
                 API.getGameProfile(p).getProperties().put("ulti_ready", true);
            }
        }

        int nextLevel = API.getPlayerLevel(p.getUniqueId().toString()) + 1;

        if(score >= API.getRequiredScore(nextLevel)) {
            API.setPlayerLevel(API.getLevelFromScore(score), p.getUniqueId().toString());
        }

    }

}
