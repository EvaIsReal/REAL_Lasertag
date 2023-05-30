package de.iv.game.lasertag.util;

import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.fs.FileManager;
import de.iv.game.lasertag.game.AbilityPerformer;
import de.iv.game.lasertag.game.Weapon;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class OverlayManager {

    private static FileConfiguration cfg = FileManager.getConfig("scoreboard.yml").getCfg();
    public static HashMap<Player, Team> playerListMap = new HashMap<>();

    public static Scoreboard getBaseScoreboard(Player player) {
        Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = s.registerNewObjective("main", "display", ILib.color(cfg.getString("title")));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        /*for (int i = 0; i < Uni.compileScoreboardLines().size(); i++) {
            String value = Uni.compileScoreboardLines().get(i);
            Weapon w = API.getAppliedGun(player.getUniqueId().toString());
            if(w != null) {
                //if(value.equals("%ulti_xp")) objective.getScore(ILib.color(value)).setScore(1);
                value = value.replace("%score%", String.valueOf(API.getPlayerGameScore(player.getUniqueId().toString())))
                        .replace("%balance%", String.valueOf(API.getPlayerBalance(player.getUniqueId().toString())))
                        .replace("%selected_gun%", API.getAppliedGun(player.getUniqueId().toString()).getName())
                        //.replace("%ulti_xp%", ILib.color("&r")).replace("Ulti-XP", ILib.color("&r"))
                        .replace("%level%", String.valueOf(API.getPlayerLevel(player.getUniqueId().toString())));
                if(w.getKey().startsWith("a_")) {
                    AbilityPerformer ap = (AbilityPerformer) w;
                    value = value.replace("%score%", String.valueOf(API.getPlayerGameScore(player.getUniqueId().toString())))
                            .replace("%balance%", String.valueOf(API.getPlayerBalance(player.getUniqueId().toString())))
                            .replace("%selected_gun%", API.getAppliedGun(player.getUniqueId().toString()).getName())
                            .replace("%level%", String.valueOf(API.getPlayerLevel(player.getUniqueId().toString())))
                            .replace("%ulti_xp%", ILib.color("&b" + API.getAccumulatedXp(player) + "&7/&b" + ap.ultiXP()));
                }
            } else {
                value = value.replace("%score%", String.valueOf(API.getPlayerGameScore(player.getUniqueId().toString())))
                        .replace("%balance%", String.valueOf(API.getPlayerBalance(player.getUniqueId().toString())))
                        .replace("%selected_gun%", ILib.color("&cnone"))
                        .replace("%level%", String.valueOf(API.getPlayerLevel(player.getUniqueId().toString())));
                        //.replace("%ulti_xp%", ILib.color("&r")).replace("Ulti-XP", ILib.color("&r"));
            }


            objective.getScore(ILib.color(value)).setScore(i);
        }*/
        Weapon w = API.getAppliedGun(player.getUniqueId().toString());

        objective.getScore(ILib.color("&1")).setScore(0);

        if(w != null) objective.getScore(ILib.color("&d" + w.getName())).setScore(1);
        else objective.getScore(ILib.color("&cNicht ausgewählt")).setScore(1);

        objective.getScore(ILib.color("&7Gun")).setScore(2);
        objective.getScore(ILib.color("&2")).setScore(3);
        objective.getScore(ILib.color("&6" + API.getPlayerBalance(player.getUniqueId().toString()))).setScore(4);
        objective.getScore(ILib.color("&7Coins")).setScore(5);
        objective.getScore(ILib.color("&3")).setScore(6);
        objective.getScore(ILib.color("&b" + API.getPlayerLevel(player.getUniqueId().toString()))).setScore(7);
        objective.getScore(ILib.color("&7Level")).setScore(8);
        objective.getScore(ILib.color("&4")).setScore(9);
        objective.getScore(ILib.color("&5" + Uni.round(API.getPlayerGameScore(player.getUniqueId().toString()), 1))).setScore(10);
        objective.getScore(ILib.color("&7Score")).setScore(11);
        objective.getScore(ILib.color("&5")).setScore(12);
        if(w.getKey().startsWith("a_")) {
            objective.getScore(ILib.color("&b"+ API.getAccumulatedXp(player) + "&7/&b" +
                    ((AbilityPerformer)w).ultiXP())).setScore(13);
            objective.getScore(ILib.color("&7Ulti-XP")).setScore(14);
            objective.getScore(ILib.color("&6")).setScore(16);
        }




        return s;
    }

    public static void updatePlayerList() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!playerListMap.isEmpty()) {
                    playerListMap.keySet().forEach(k -> {
                        playerListMap.get(k).setPrefix(ILib.color("&7[&5&l"+ API.getPlayerLevel(k.getUniqueId().toString()) +"&7] &7"));
                    });
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

    public static void applyBossBar(Player p) {
        BossBar bar;
        if(Main.getInstance().getBars().containsKey(p.getUniqueId())) {
            bar = Main.getInstance().bars.get(p.getUniqueId());
        } else {
            bar = Bukkit.createBossBar(ILib.color("&7&lLevel &5&l" + API.getPlayerLevel(p.getUniqueId().toString())), BarColor.GREEN, BarStyle.SOLID);
            Main.getInstance().getBars().put(p.getUniqueId(), bar);
        }
        bar.setVisible(true);
        bar.addPlayer(p);
    }

    public static void removeBossBar(Player p) {
        BossBar bar;
        if(Main.getInstance().getBars().containsKey(p.getUniqueId())) {
            bar = Main.getInstance().bars.get(p.getUniqueId());
            bar.removePlayer(p);
            Main.getInstance().getBars().remove(p.getUniqueId());
        }

    }

    public static void update(Scoreboard sb, Player player) {
        player.setScoreboard(sb);
    }

}
