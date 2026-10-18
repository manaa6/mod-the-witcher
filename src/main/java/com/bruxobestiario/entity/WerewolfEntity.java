package com.bruxobestiario.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class WerewolfEntity extends WitcherMonster {
    public WerewolfEntity(EntityType<? extends HostileEntity> type, World world) { super(type, world); }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.32)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0);
    }

    @Override protected SoundEvent getAmbientSound() { return SoundEvents.ENTITY_WOLF_GROWL; }
    @Override protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ENTITY_WOLF_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.ENTITY_WOLF_DEATH; }
}
