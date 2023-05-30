package de.iv.game.lasertag.commands;

import de.iv.ILib;
import de.iv.game.lasertag.core.Uni;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player p) {
            if(args.length == 0) {
                p.sendMessage(ILib.color("&7Willkommen bei " + Uni.PREFIX));
                p.sendMessage("");
                p.sendMessage(ILib.color("&7Über was möchtest du dich informieren?"));
                p.spigot().sendMessage(
                        new ComponentBuilder(ILib.color("&61. &7Spielprinzip"))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ltinfo 1"))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ILib.color("&e&lCLICK!"))))
                                .create()
                );
                p.spigot().sendMessage(
                        new ComponentBuilder(ILib.color("&62. &7Guns"))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ltinfo 2"))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ILib.color("&e&lCLICK!"))))
                                .create()
                );
                p.spigot().sendMessage(
                        new ComponentBuilder(ILib.color("&63. &7Winmechanik"))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ltinfo 3"))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ILib.color("&e&lCLICK!"))))
                                .create()
                );
                p.spigot().sendMessage(
                        new ComponentBuilder(ILib.color("&64. &7Commands"))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ltinfo 4"))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ILib.color("&e&lCLICK!"))))
                                .create()
                );
                p.sendMessage("");
                return true;
            }
            if(args.length >= 2) {
                Uni.sendErrorMessage(p, "Argumentenlänge ungültig. 1 erwartet, " + args.length + " bekommen");
                return false;
            }
            switch (args[0]) {
                case "1" -> {
                    p.sendMessage(ILib.color("&7Spielprinzip ist im FFA, also schieße auf jeden so schnell du kannst." +
                            " Es wird keine Teams geben."));
                    p.sendMessage("");
                }
                case "2" -> {
                    p.sendMessage(ILib.color(
                            """
                                  &7Jede Waffe schießt mit &6&lRechtsklick &7und castet ohne Drop. Das bedeutet, dass ein Spieler als getroffen gilt, wenn er auf deinem Fadenkreuz steht und sich in der Reichweite der Waffe befindet. Wird ein Spieler von dir getroffen, bekommst du einen kurzen Audio-Cue zur bestätigung.

                                    Jede Waffe hat einen &bSchusscooldown&7, in der sie nicht schießen kann. Versuchst du in dieser Zeit zu schießen, hörst du nur ein kurzes Klicken und es wird kein Schuss gecastet.
                                    
                                    Manche Waffen besitzen eine &6Fähigkeit&7, die sich mit dem &e&lDROP-KEY &7aktivieren lässt. Bei dieser werden entweder Du oder andere Spieler betroffen. Die Fähigkeiten besitzen jeweils alle einen eigenen Cooldown, unabhängig vom Schusscooldown. In dieser Zeit kann die Fähigkeit nicht erneut aktiviert werden.
                                    
                                    Waffen kannst du dir im Shop mit &bCoins &7kaufen.\s
                               \s"""
                    ));
                    p.sendMessage("");
                }

                case "3" -> {
                    p.sendMessage(ILib.color(
                            """
                                &7"Gewinne", indem du andere Spieler mit deinen Schüssen triffst und ihnen so lange Health abziehst, bis sie respawnen.
                                Der Kill gilt als deiner, wenn du den letzten schuss triffst, d.h. dass du auf schnellere Spieler acht geben solltest.
                                Ein erfolgreicher Kill bringt dir eine feste Anzahl an &bXP &7und eine variierende Anzahl an &6Coins &7in einem Bestimmten bereich.
                                Gebe &6Coins &7im Shop für neue Guns aus.
                                
                                &bXP &7zeigen an, wie lange ein Spieler bereits spielt. Sie haben z.z. keinen Einfluss auf das Spielgeschehen.
                                \s"""
                    ));
                }
                case "4" -> {
                    p.sendMessage(ILib.color(
                            """
                                &6/menu &8| &7Öffne dein Spielmenu
                                
                                """
                    ));
                }

            }

        }

        return false;
    }
}
