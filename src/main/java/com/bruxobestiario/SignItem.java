package com.bruxobestiario;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/** Sinais do bruxo. Clique direito para usar; cada Sinal tem seu tempo de recarga. */
public class SignItem extends Item {
    public enum Sign { AARD(100), IGNI(100), QUEN(600), YRDEN(400), AXII(300);
        public final int cooldown; Sign(int c) { cooldown = c; } }

    private final Sign sign;

    public SignItem(Sign sign, Settings settings) { super(settings); this.sign = sign; }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient && world instanceof ServerWorld sw) {
            cast(sw, player);
            player.getItemCooldownManager().set(this, sign.cooldown);
        }
        return TypedActionResult.success(stack, world.isClient);
    }

    private List<LivingEntity> cone(ServerWorld w, PlayerEntity p, double range, double minDot) {
        Vec3d look = p.getRotationVec(1f).normalize();
        return w.getEntitiesByClass(LivingEntity.class, p.getBoundingBox().expand(range), e -> {
            if (e == p || !e.isAlive()) return false;
            Vec3d d = e.getPos().add(0, e.getHeight() / 2, 0).subtract(p.getEyePos());
            double len = d.length();
            return len > 0.001 && len <= range && d.normalize().dotProduct(look) >= minDot;
        });
    }

    private void line(ServerWorld w, PlayerEntity p, ParticleEffect type, double range) {
        Vec3d look = p.getRotationVec(1f).normalize(), eye = p.getEyePos();
        for (int i = 1; i <= range; i++)
            w.spawnParticles(type, eye.x + look.x * i, eye.y + look.y * i - 0.2, eye.z + look.z * i, 4, 0.25, 0.25, 0.25, 0.01);
    }

    private void sound(ServerWorld w, PlayerEntity p, SoundEvent s) {
        w.playSound(null, p.getX(), p.getY(), p.getZ(), s, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    private void cast(ServerWorld w, PlayerEntity p) {
        switch (sign) {
            case AARD -> {
                Vec3d look = p.getRotationVec(1f);
                for (LivingEntity e : cone(w, p, 7, 0.6)) {
                    e.damage(p.getDamageSources().playerAttack(p), 2.0f);
                    e.takeKnockback(1.8, -look.x, -look.z);
                    e.velocityModified = true;
                }
                line(w, p, ParticleTypes.CLOUD, 7);
                sound(w, p, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK);
            }
            case IGNI -> {
                for (LivingEntity e : cone(w, p, 7, 0.75)) {
                    e.setOnFireFor(5);
                    e.damage(p.getDamageSources().playerAttack(p), 3.0f);
                }
                line(w, p, ParticleTypes.FLAME, 7);
                sound(w, p, SoundEvents.ITEM_FIRECHARGE_USE);
            }
            case QUEN -> {
                p.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600, 1));
                p.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0));
                w.spawnParticles(ParticleTypes.ENCHANT, p.getX(), p.getY() + 1, p.getZ(), 40, 0.6, 0.8, 0.6, 0.2);
                sound(w, p, SoundEvents.BLOCK_BEACON_ACTIVATE);
            }
            case YRDEN -> {
                for (LivingEntity e : w.getEntitiesByClass(LivingEntity.class, p.getBoundingBox().expand(6), x -> x != p && x.isAlive())) {
                    e.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 160, 3));
                    e.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 160));
                }
                for (int i = 0; i < 36; i++) {
                    double a = i * Math.PI / 18;
                    w.spawnParticles(ParticleTypes.WITCH, p.getX() + Math.cos(a) * 6, p.getY() + 0.1, p.getZ() + Math.sin(a) * 6, 1, 0, 0, 0, 0);
                }
                sound(w, p, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE);
            }
            case AXII -> {
                for (LivingEntity e : cone(w, p, 8, 0.9)) {
                    e.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 1));
                    e.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
                    if (e instanceof MobEntity mob) mob.setTarget(null);
                }
                line(w, p, ParticleTypes.HAPPY_VILLAGER, 8);
                sound(w, p, SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL);
            }
        }
    }
}
