package com.bruxobestiario;

import com.bruxobestiario.entity.WitcherMonster;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

/** Espada de prata: dano extra contra monstros do mod e mortos-vivos. */
public class SilverSwordItem extends SwordItem {
    public SilverSwordItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof WitcherMonster || target.getGroup() == EntityGroup.UNDEAD) {
            target.timeUntilRegen = 0;
            DamageSource src = attacker instanceof PlayerEntity p
                    ? attacker.getDamageSources().playerAttack(p)
                    : attacker.getDamageSources().mobAttack(attacker);
            target.damage(src, 4.0f);
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60));
        }
        return super.postHit(stack, target, attacker);
    }
}
