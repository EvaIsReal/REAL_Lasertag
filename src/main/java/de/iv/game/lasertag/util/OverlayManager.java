package de.iv.game.lasertag.util;

import de.iv.ILib;
import de.iv.game.lasertag.core.API;
import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.fs.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class OverlayManager {

    private static FileConfiguration cfg = FileManager.getConfig("scoreboard.yml").toCfg();

    public static Scoreboard getBaseScoreboard(Player player) {
        Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = s.registerNewObjective("main", "display", ILib.color(cfg.getString("title")));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 0; i < Uni.compileScoreboardLines().size(); i++) {
            String value = Uni.compileScoreboardLines().get(i);
            if(API.getAppliedGun(player.getUniqueId().toString()) != null) {
                value = value.replace("%score%", String.valueOf(API.getPlayerGameScore(player.getUniqueId().toString())))
                        .replace("%balance%", String.valueOf(API.getPlayerBalance(player.getUniqueId().toString())))
                        .replace("%selected_gun%", API.getAppliedGun(player.getUniqueId().toString()).getName())
                        .replace("%level%", String.valueOf(API.getPlayerLevel(player.getUniqueId().toString())));
            } else {
                value = value.replace("%score%", String.valueOf(API.getPlayerGameScore(player.getUniqueId().toString())))
                        .replace("%balance%", String.valueOf(API.getPlayerBalance(player.getUniqueId().toString())))
                        .replace("%selected_gun%", ILib.color("&cnone"))
                        .replace("%level%", String.valueOf(API.getPlayerLevel(player.getUniqueId().toString())));
            }


            objective.getScore(ILib.color(value)).setScore(i);
        }

        return s;
    }

    public static void applyBossBar(Player p) {
        BossBar bar;
        if(Main.getInstance().getBars().containsKey(p.getUniqueId())) {
            bar = Main.getInstance().bars.get(p.getUniqueId());
        } else {
            bar = Bukkit.createBossBar(ILib.color("&7&lLevel &5&l" + String.valueOf(API.getPlayerLevel(p.getUniqueId().toString()))), BarColor.GREEN, BarStyle.SOLID);
            Main.getInstance().getBars().put(p.getUniqueId(), bar);
        }
        bar.setVisible(true);
        bar.addPlayer(p);
    }

    public static void update(Scoreboard sb, Player player) {
        player.setScoreboard(sb);
    }

}
