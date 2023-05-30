package de.iv.game.lasertag.game.guns;

import de.iv.ILib;
import de.iv.game.lasertag.core.Main;
import de.iv.game.lasertag.core.Uni;
import de.iv.game.lasertag.game.AbilityPerformer;
import de.iv.game.lasertag.game.Weapon;
import de.iv.iutils.items.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.ChatPaginator;

import java.util.Arrays;
import java.util.List;

public class AbilityGun extends Weapon implements AbilityPerformer {

    public AbilityGun() {
        super(Material.NETHERITE_HOE, "Ability Performer",
                "a_ability_gun", 100, Color.PURPLE, 0.4F, 10.0,
                2.2, 1, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 2, 12, false, 1000);
    }

    @Override
    public String description() {
        //Werde eins mit der Dunkelheit und entkomme so brennsligen Situationen.
        return "Umhülle dich mit einem Dunklen schleier, der dich vor anderen Versteckt und es dir ermöglicht Schnell deine Position zu wechseln. " +
                "Werde eins mit der Dunkelheit und entkomme so brennsligen Situationen.";
    }

    @Override
    public String abilityTitle() {
        return "Call of the Night";
    }

    @Override
    public Weapon getWeapon() {
        return this;
    }

    @Override
    public long abilityCooldownMillis() {
        return 10*1000;
    }

    @Override
    public int ultiXP() {
        return 10;
    }

    @Override
    public void performUlti(Player player, Weapon weapon) {
        player.sendMessage("Ulti");
    }

    @Override
    public ItemStack toItem() {
        ItemStack i = new ItemBuilder(this.getMaterial()).setName(ChatColor.GREEN + this.getName()).build();
        ItemMeta im = i.getItemMeta();
        im.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "game_item"), PersistentDataType.STRING,"agun_" + this.getKey());
        i.setItemMeta(im);
        return i;
    }

    @Override
    public ItemStack shopItem() {
        return new ItemBuilder(this.getMaterial())
                .addToPDC("game_item", "agun_" + this.getKey())
                .setName(ILib.color("&b" + this.getName()))
                .setLore(
                        List.of(ILib.color("&7Damage: &c" + getDamage()),
                                ILib.color("&7Kill-XP: &b" + getXp()),
                                ILib.color("&7Schuss-Cooldown: &e" + getCooldownMillis() / 1000 + "s"),
                                "",
                                ILib.color("&6&lFÄHIGKEIT: &6" + abilityTitle())),
                        Arrays.stream(ChatPaginator.wordWrap(ILib.color("&7" + description()), 30)).toList(),
                        List.of(
                                "",
                                ILib.color("&e&lDROP"),
                                ILib.color("&7Ability-Cooldown: &b" + abilityCooldownMillis() / 1000 + "s"),
                                "",
                                ILib.color("&7Kosten"),
                                ILib.color("&6 " + Uni.convertWithDelimiter(price(), ',') + " Coins")
                        )
                ).build();
    }

    @Override
    public ItemStack invItem() {
        return new ItemBuilder(this.getMaterial())
                .addToPDC("game_item", "agun_" + this.getKey())
                .setName(ILib.color("&b" + this.getName()))
                .setLore(
                        List.of(ILib.color("&7Damage: &c" + getDamage()),
                                ILib.color("&7Kill-XP: &b" + getXp()),
                                ILib.color("&7Schuss-Cooldown: &e" + getCooldownMillis() / 1000 + "s"),
                                "",
                                ILib.color("&6&lFÄHIGKEIT: &6" + abilityTitle())),
                        Arrays.stream(ChatPaginator.wordWrap(ILib.color("&7" + description()), 30)).toList(),
                        List.of(
                                "",
                                ILib.color("&e&lDROP"),
                                ILib.color("&7Ability-Cooldown: &b" + abilityCooldownMillis() / 1000 + "s")
                        )
                ).build();
    }

    @Override
    public ItemStack soldItem() {
        String itemDescription = String.join(",", Arrays.toString(ChatPaginator.wordWrap(ILib.color("&7" + description()), 10)));
        String loreString = itemDescription.replaceAll("[\\[\\]]", "").replace(",", "\n");

        return new ItemBuilder(this.getMaterial())
                .addToPDC("game_item", "agun_" + this.getKey())
                .setName(ILib.color("&b" + this.getName()))
                .setLore(
                        List.of(ILib.color("&7Damage: &c" + getDamage()),
                                ILib.color("&7Kill-XP: &b" + getXp()),
                                ILib.color("&7Schuss-Cooldown: &e" + getCooldownMillis() / 1000 + "s"),
                                "",
                                ILib.color("&6&lFÄHIGKEIT: &6" + abilityTitle())),
                        Arrays.stream(ChatPaginator.wordWrap(ILib.color("&7" + description()), 30)).toList(),
                        List.of(
                                "",
                                ILib.color("&e&lDROP"),
                                ILib.color("&7Ability-Cooldown: &b" + abilityCooldownMillis() / 1000 + "s"),
                                "",
                                ILib.color("&7Kosten"),
                                ILib.color("&c&m" + Uni.convertWithDelimiter(price(), ',') + " Coins"),
                                ILib.color("&eDu besitzt dieses Item bereits.")
                        )
                ).build();
    }

    @Override
    public void performAbility(Player player, Weapon weapon) {
        Uni.getNearbyPlayers(player.getLocation(), weapon.getSoundDistance()).forEach(p -> p.playSound(p, Sound.ENTITY_BAT_TAKEOFF, 1, 1));

        Uni.runPlayerTaskLater(player, 4*20, p -> Uni.particleCylinder(p.getLocation(), 1, 15, 2.3, 10, Particle.SMOKE_LARGE, 1, 0.05));

        Uni.particleCylinder(player.getLocation(), 1, 15, 2.3, 10, Particle.SMOKE_LARGE, 1, 0.05);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 4*20, 5, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 4*20, 1, false, false, true));
    }
}
