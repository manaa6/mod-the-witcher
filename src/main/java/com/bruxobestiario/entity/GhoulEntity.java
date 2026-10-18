package com.bruxobestiario.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class GhoulEntity extends WitcherMonster {
    public GhoulEntity(EntityType<? extends HostileEntity> type, World world) { super(type, world); }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 26.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0);
    }

    @Override protected SoundEvent getAmbientSound() { return SoundEvents.ENTITY_ZOMBIE_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ENTITY_ZOMBIE_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_ZOMBIE_DEATH; }
}
