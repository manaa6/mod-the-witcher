package com.witchermod.sign;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.*;

/** Toda a lógica dos cinco Sinais (roda só no servidor). */
public final class SignManager {
    private static final Map<UUID, long[]> COOLDOWNS = new HashMap<>();
    private static final List<YrdenZone> YRDEN_ZONES = new ArrayList<>();
    private static final List<AxiiLock> AXII_LOCKS = new ArrayList<>();

    private SignManager() {}

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(SignManager::tickWorld);
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                COOLDOWNS.remove(handler.getPlayer().getUuid()));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            COOLDOWNS.clear();
            YRDEN_ZONES.clear();
            AXII_LOCKS.clear();
        });
    }

    // ------------------------------------------------------------------
    // Lançamento
    // ------------------------------------------------------------------
    public static void cast(ServerPlayerEntity player, SignType sign) {
        if (player.isSpectator() || !player.isAlive()) return;

        ServerWorld world = player.getServerWorld();
        long now = world.getTime();
        long[] cd = COOLDOWNS.computeIfAbsent(player.getUuid(), id -> new long[SignType.values().length]);

        if (now < cd[sign.ordinal()]) {
            long seconds = (cd[sign.ordinal()] - now + 19) / 20;
            player.sendMessage(Text.translatable("message.witcher.cooldown",
                    Text.translatable(sign.translationKey()), seconds), true);
            return;
        }
        if (!player.isCreative() && player.getHungerManager().getFoodLevel() <= 6) {
            player.sendMessage(Text.translatable("message.witcher.tired"), true);
            return;
        }

        boolean cast = switch (sign) {
            case AARD -> castAard(player, world);
            case IGNI -> castIgni(player, world);
            case QUEN -> castQuen(player, world);
            case YRDEN -> castYrden(player, world);
            case AXII -> castAxii(player, world);
        };
        if (!cast) return;

        cd[sign.ordinal()] = now + (player.isCreative() ? 10 : sign.cooldown());
        if (!player.isCreative()) player.addExhaustion(sign.exhaustion());
    }

    // ------------------------------------------------------------------
    // AARD: onda de choque em cone, empurra e causa dano leve
    // ------------------------------------------------------------------
    private static boolean castAard(ServerPlayerEntity player, ServerWorld world) {
        Vec3d eye = player.getEyePos();
        Vec3d look = player.getRotationVec(1.0f);

        for (LivingEntity target : coneTargets(player, world, 7.0, 0.6)) {
            target.damage(world.getDamageSources().playerAttack(player), 2.0f);
            target.takeKnockback(2.2, -look.x, -look.z);
            target.velocityModified = true;
        }

        for (int d = 1; d <= 7; d++) {
            double spread = 0.15 + d * 0.07;
            world.spawnParticles(ParticleTypes.CLOUD,
                    eye.x + look.x * d, eye.y + look.y * d - 0.2, eye.z + look.z * d,
                    4, spread, spread * 0.6, spread, 0.03);
        }
        world.spawnParticles(ParticleTypes.POOF, eye.x + look.x, eye.y - 0.2 + look.y, eye.z + look.z,
                8, 0.3, 0.3, 0.3, 0.05);
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK,
                SoundCategory.PLAYERS, 1.2f, 0.6f);
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE,
                SoundCategory.PLAYERS, 0.3f, 1.8f);
        return true;
    }

    // ------------------------------------------------------------------
    // IGNI: cone de fogo, incendeia e causa dano
    // ------------------------------------------------------------------
    private static boolean castIgni(ServerPlayerEntity player, ServerWorld world) {
        Vec3d eye = player.getEyePos();
        Vec3d look = player.getRotationVec(1.0f);

        for (LivingEntity target : coneTargets(player, world, 7.0, 0.8)) {
            target.damage(world.getDamageSources().playerAttack(player), 3.0f);
            target.setOnFireFor(6);
        }

        for (int d = 1; d <= 7; d++) {
            double spread = 0.1 + d * 0.08;
            world.spawnParticles(ParticleTypes.FLAME,
                    eye.x + look.x * d, eye.y + look.y * d - 0.2, eye.z + look.z * d,
                    5, spread, spread * 0.6, spread, 0.02);
            if (d % 2 == 0) {
                world.spawnParticles(ParticleTypes.SMOKE,
                        eye.x + look.x * d, eye.y + look.y * d - 0.2, eye.z + look.z * d,
                        2, spread, spread * 0.6, spread, 0.01);
            }
        }
        world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE,
                SoundCategory.PLAYERS, 1.2f, 0.8f);
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT,
                SoundCategory.PLAYERS, 0.6f, 1.0f);
        return true;
    }

    // ------------------------------------------------------------------
    // QUEN: escudo de absorção
    // ------------------------------------------------------------------
    private static boolean castQuen(ServerPlayerEntity player, ServerWorld world) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 20 * 30, 2, false, false, true));

        for (int i = 0; i < 24; i++) {
            double angle = (i / 24.0) * Math.PI * 2;
            for (double y = 0.1; y < 2.0; y += 0.6) {
                world.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                        player.getX() + Math.cos(angle) * 1.0, player.getY() + y, player.getZ() + Math.sin(angle) * 1.0,
                        1, 0, 0, 0, 0);
            }
        }
        world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_BEACON_ACTIVATE,
                SoundCategory.PLAYERS, 0.7f, 1.6f);
        return true;
    }

    // ------------------------------------------------------------------
    // YRDEN: armadilha mágica no chão que deixa inimigos lentos e brilhando
    // ------------------------------------------------------------------
    private static boolean castYrden(ServerPlayerEntity player, ServerWorld world) {
        YRDEN_ZONES.removeIf(z -> z.owner.equals(player.getUuid())); // 1 armadilha por jogador
        YRDEN_ZONES.add(new YrdenZone(world, player.getPos(), player.getUuid(), 20 * 20, 5.0));

        world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL,
                SoundCategory.PLAYERS, 1.0f, 0.7f);
        return true;
    }

    // ------------------------------------------------------------------
    // AXII: controla a mente do mob na mira; ele ataca outros mobs
    // ------------------------------------------------------------------
    private static boolean castAxii(ServerPlayerEntity player, ServerWorld world) {
        MobEntity target = lookedAtMob(player, world, 12.0);
        if (target == null) {
            player.sendMessage(Text.translatable("message.witcher.no_target"), true);
            return false;
        }

        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20 * 15, 1));
        AXII_LOCKS.removeIf(l -> l.mob == target);
        AXII_LOCKS.add(new AxiiLock(target, player.getUuid(), 20 * 15));

        world.spawnParticles(ParticleTypes.HEART, target.getX(), target.getBodyY(1.0) + 0.3, target.getZ(),
                6, 0.4, 0.3, 0.4, 0.0);
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EVOKER_CAST_SPELL,
                SoundCategory.PLAYERS, 1.0f, 1.3f);
        return true;
    }

    // ------------------------------------------------------------------
    // Tick: Yrden e Axii
    // ------------------------------------------------------------------
    private static void tickWorld(ServerWorld world) {
        Iterator<YrdenZone> yIt = YRDEN_ZONES.iterator();
        while (yIt.hasNext()) {
            YrdenZone zone = yIt.next();
            if (zone.world != world) continue;
            if (--zone.ticksLeft <= 0) {
                yIt.remove();
                continue;
            }
            tickYrden(zone);
        }

        Iterator<AxiiLock> aIt = AXII_LOCKS.iterator();
        while (aIt.hasNext()) {
            AxiiLock lock = aIt.next();
            if (lock.mob.getWorld() != world) continue;
            if (--lock.ticksLeft <= 0 || !lock.mob.isAlive() || lock.mob.isRemoved()) {
                aIt.remove();
                continue;
            }
            tickAxii(lock, world);
        }
    }

    private static void tickYrden(YrdenZone zone) {
        ServerWorld world = zone.world;
        Vec3d c = zone.center;

        if (zone.ticksLeft % 3 == 0) {
            int points = 28;
            for (int i = 0; i < points; i++) {
                double angle = (i / (double) points) * Math.PI * 2 + zone.ticksLeft * 0.04;
                world.spawnParticles(ParticleTypes.WITCH,
                        c.x + Math.cos(angle) * zone.radius, c.y + 0.1, c.z + Math.sin(angle) * zone.radius,
                        1, 0, 0, 0, 0);
            }
            double a = zone.ticksLeft * 0.3;
            double r = (zone.ticksLeft % 30) / 30.0 * zone.radius;
            world.spawnParticles(ParticleTypes.PORTAL,
                    c.x + Math.cos(a) * r, c.y + 0.2, c.z + Math.sin(a) * r, 2, 0.1, 0.1, 0.1, 0.05);
        }

        if (zone.ticksLeft % 10 == 0) {
            Box box = new Box(c.x - zone.radius, c.y - 1, c.z - zone.radius,
                    c.x + zone.radius, c.y + 3, c.z + zone.radius);
            for (LivingEntity e : world.getEntitiesByClass(LivingEntity.class, box,
                    en -> en.isAlive() && !isFriendly(en, zone.owner))) {
                double dx = e.getX() - c.x;
                double dz = e.getZ() - c.z;
                if (dx * dx + dz * dz > zone.radius * zone.radius) continue;
                e.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30, 3, false, true));
                e.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 30, 0, false, false));
            }
        }
    }

    private static void tickAxii(AxiiLock lock, ServerWorld world) {
        MobEntity mob = lock.mob;

        if (lock.ticksLeft % 20 == 0) {
            world.spawnParticles(ParticleTypes.HEART, mob.getX(), mob.getBodyY(1.0) + 0.3, mob.getZ(),
                    1, 0.3, 0.2, 0.3, 0.0);
        }
        if (lock.ticksLeft % 5 != 0) return;

        if (lock.victim == null || !lock.victim.isAlive() || lock.victim.isRemoved()) {
            lock.victim = nearestVictim(mob, world, lock.owner);
        }
        if (lock.victim != null) {
            mob.setTarget(lock.victim);
            if (lock.victim.getTarget() == null) lock.victim.setTarget(mob);
        } else {
            mob.setTarget(null);
            mob.getNavigation().stop();
        }
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------
    private static boolean isFriendly(LivingEntity e, UUID ownerId) {
        if (e.getUuid().equals(ownerId)) return true;
        return e instanceof TameableEntity tame && ownerId.equals(tame.getOwnerUuid());
    }

    /** Entidades vivas dentro de um cone à frente do jogador (com linha de visão). */
    private static List<LivingEntity> coneTargets(ServerPlayerEntity player, ServerWorld world,
                                                  double range, double minDot) {
        Vec3d eye = player.getEyePos();
        Vec3d look = player.getRotationVec(1.0f);
        List<LivingEntity> result = new ArrayList<>();

        for (LivingEntity e : world.getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(range),
                en -> en.isAlive() && !isFriendly(en, player.getUuid()))) {
            Vec3d to = e.getPos().add(0, e.getHeight() / 2.0, 0).subtract(eye);
            double dist = to.length();
            if (dist > range || dist < 0.001) continue;
            if (to.normalize().dotProduct(look) < minDot) continue;
            if (!player.canSee(e)) continue;
            result.add(e);
        }
        return result;
    }

    private static MobEntity lookedAtMob(ServerPlayerEntity player, ServerWorld world, double range) {
        Vec3d eye = player.getEyePos();
        Vec3d look = player.getRotationVec(1.0f);
        MobEntity best = null;
        double bestDist = Double.MAX_VALUE;

        for (MobEntity e : world.getEntitiesByClass(MobEntity.class, player.getBoundingBox().expand(range),
                en -> en.isAlive() && !isFriendly(en, player.getUuid()))) {
            Vec3d to = e.getPos().add(0, e.getHeight() / 2.0, 0).subtract(eye);
            double dist = to.length();
            if (dist > range || dist < 0.001) continue;
            if (to.normalize().dotProduct(look) < 0.96) continue;
            if (!player.canSee(e)) continue;
            if (dist < bestDist) {
                bestDist = dist;
                best = e;
            }
        }
        return best;
    }

  private static MobEntity nearestVictim(MobEntity mob, ServerWorld world, UUID owner) {
        return world.getEntitiesByClass(MobEntity.class, mob.getBoundingBox().expand(12),
                        e -> e != mob && e.isAlive() && !isFriendly(e, owner))
                .stream()
                .min(Comparator.comparingDouble(mob::squaredDistanceTo))
                .orElse(null);
    }

    // ------------------------------------------------------------------
    // Estado
    // ------------------------------------------------------------------
    private static final class YrdenZone {
        final ServerWorld world;
        final Vec3d center;
        final UUID owner;
        final double radius;
        int ticksLeft;

        YrdenZone(ServerWorld world, Vec3d center, UUID owner, int ticksLeft, double radius) {
            this.world = world;
            this.center = center;
            this.owner = owner;
            this.ticksLeft = ticksLeft;
            this.radius = radius;
        }
    }

    private static final class AxiiLock {
        final MobEntity mob;
        final UUID owner;
        int ticksLeft;
       MobEntity victim;

        AxiiLock(MobEntity mob, UUID owner, int ticksLeft) {
            this.mob = mob;
            this.owner = owner;
            this.ticksLeft = ticksLeft;
        }
    }
}
