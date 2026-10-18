package com.bruxobestiario;

import com.bruxobestiario.entity.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class ModEntities {
    public static final EntityType<DrownerEntity> DROWNER = reg("drowner", DrownerEntity::new, 0.6f, 1.9f);
    public static final EntityType<GhoulEntity> GHOUL = reg("ghoul", GhoulEntity::new, 0.7f, 1.6f);
    public static final EntityType<NekkerEntity> NEKKER = reg("nekker", NekkerEntity::new, 0.5f, 1.1f);
    public static final EntityType<GriffinEntity> GRIFFIN = reg("griffin", GriffinEntity::new, 1.2f, 2.0f);
    public static final EntityType<WerewolfEntity> WEREWOLF = reg("werewolf", WerewolfEntity::new, 0.9f, 2.4f);

    private static <T extends Entity> EntityType<T> reg(String name, EntityType.EntityFactory<T> factory, float w, float h) {
        return Registry.register(Registries.ENTITY_TYPE, WitcherMod.id(name),
                FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, factory).dimensions(EntityDimensions.fixed(w, h)).build());
    }

    public static void register() {
        FabricDefaultAttributeRegistry.register(DROWNER, DrownerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GHOUL, GhoulEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(NEKKER, NekkerEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GRIFFIN, GriffinEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(WEREWOLF, WerewolfEntity.createAttributes());

        SpawnRestriction.register(DROWNER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        SpawnRestriction.register(GHOUL, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        SpawnRestriction.register(NEKKER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        SpawnRestriction.register(GRIFFIN, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
        SpawnRestriction.register(WEREWOLF, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SWAMP, BiomeKeys.MANGROVE_SWAMP, BiomeKeys.RIVER), SpawnGroup.MONSTER, DROWNER, 20, 1, 3);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.DESERT, BiomeKeys.PLAINS, BiomeKeys.SAVANNA, BiomeKeys.BADLANDS), SpawnGroup.MONSTER, GHOUL, 8, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.FOREST, BiomeKeys.BIRCH_FOREST, BiomeKeys.TAIGA, BiomeKeys.PLAINS), SpawnGroup.MONSTER, NEKKER, 25, 2, 5);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.STONY_PEAKS, BiomeKeys.JAGGED_PEAKS, BiomeKeys.GROVE), SpawnGroup.MONSTER, GRIFFIN, 5, 1, 1);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.DARK_FOREST, BiomeKeys.TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA), SpawnGroup.MONSTER, WEREWOLF, 6, 1, 1);
    }
}
