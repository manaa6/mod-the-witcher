package com.witchermod.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.damage.DamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.RaiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Espada de bruxo: aço é bom contra humanos (saqueadores/bruxas),
 * prata é boa contra monstros (todo Monster que não seja saqueador).
 */
public class WitcherSword extends SwordItem {
    public enum Bonus { MONSTERS, HUMANS }

    private final Bonus bonus;
    private final float bonusDamage;
    private final boolean glint;
    private final String tooltipKey;

    public WitcherSword(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings,
                        Bonus bonus, float bonusDamage, boolean glint, String tooltipKey) {
        super(material, attackDamage, attackSpeed, settings);
        this.bonus = bonus;
        this.bonusDamage = bonusDamage;
        this.glint = glint;
        this.tooltipKey = tooltipKey;
    }

    private boolean applies(LivingEntity target) {
        return switch (bonus) {
            case MONSTERS -> target instanceof Monster && !(target instanceof RaiderEntity);
            case HUMANS -> target instanceof RaiderEntity;
        };
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = attacker.getWorld();
        if (!world.isClient && applies(target)) {
            DamageSource source;
            if (bonus == Bonus.MONSTERS) {
                source = world.getDamageSources().indirectMagic(attacker, attacker);
            } else if (attacker instanceof PlayerEntity player) {
                source = world.getDamageSources().playerAttack(player);
            } else {
                source = world.getDamageSources().mobAttack(attacker);
            }
            target.timeUntilRegen = 0; // permite o segundo golpe de dano extra
            target.damage(source, bonusDamage);

            ((ServerWorld) world).spawnParticles(
                    bonus == Bonus.MONSTERS ? ParticleTypes.ENCHANTED_HIT : ParticleTypes.CRIT,
                    target.getX(), target.getBodyY(0.5), target.getZ(), 10, 0.3, 0.4, 0.3, 0.2);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return glint || super.hasGlint(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(tooltipKey).formatted(Formatting.GRAY));
    }
}
