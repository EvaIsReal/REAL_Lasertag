package de.iv.game.lasertag.game;

import org.bukkit.entity.Player;

public interface AbilityPerformer {

    Weapon getWeapon();
    void performAbility(Player player, Weapon weapon);
    String description();
    String abilityTitle();
    long abilityCooldownMillis();
    int ultiXP();
    void performUlti(Player player, Weapon weapon);

}
