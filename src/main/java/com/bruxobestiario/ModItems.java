package com.bruxobestiario;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    public static final Item STEEL_SWORD = reg("steel_sword", new SwordItem(WitcherTier.STEEL, 3, -2.4f, new FabricItemSettings()));
    public static final Item SILVER_SWORD = reg("silver_sword", new SilverSwordItem(WitcherTier.SILVER, 3, -2.4f, new FabricItemSettings()));
    public static final Item SIGN_AARD = reg("sign_aard", new SignItem(SignItem.Sign.AARD, new FabricItemSettings().maxCount(1)));
    public static final Item SIGN_IGNI = reg("sign_igni", new SignItem(SignItem.Sign.IGNI, new FabricItemSettings().maxCount(1)));
    public static final Item SIGN_QUEN = reg("sign_quen", new SignItem(SignItem.Sign.QUEN, new FabricItemSettings().maxCount(1)));
    public static final Item SIGN_YRDEN = reg("sign_yrden", new SignItem(SignItem.Sign.YRDEN, new FabricItemSettings().maxCount(1)));
    public static final Item SIGN_AXII = reg("sign_axii", new SignItem(SignItem.Sign.AXII, new FabricItemSettings().maxCount(1)));
    public static final Item DROWNER_EGG = reg("drowner_spawn_egg", new SpawnEggItem(ModEntities.DROWNER, 0x6c8f86, 0x3f5c58, new FabricItemSettings()));
    public static final Item GHOUL_EGG = reg("ghoul_spawn_egg", new SpawnEggItem(ModEntities.GHOUL, 0x7d6d5c, 0xd23a2a, new FabricItemSettings()));
    public static final Item NEKKER_EGG = reg("nekker_spawn_egg", new SpawnEggItem(ModEntities.NEKKER, 0xa86a32, 0xffe04a, new FabricItemSettings()));
    public static final Item GRIFFIN_EGG = reg("griffin_spawn_egg", new SpawnEggItem(ModEntities.GRIFFIN, 0xb08a4f, 0xe0b030, new FabricItemSettings()));
    public static final Item WEREWOLF_EGG = reg("werewolf_spawn_egg", new SpawnEggItem(ModEntities.WEREWOLF, 0x5b4533, 0xffd23a, new FabricItemSettings()));

    private static <T extends Item> T reg(String name, T item) {
        return Registry.register(Registries.ITEM, WitcherMod.id(name), item);
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(e -> {
            e.add(STEEL_SWORD); e.add(SILVER_SWORD);
            e.add(SIGN_AARD); e.add(SIGN_IGNI); e.add(SIGN_QUEN); e.add(SIGN_YRDEN); e.add(SIGN_AXII);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(e -> {
            e.add(DROWNER_EGG); e.add(GHOUL_EGG); e.add(NEKKER_EGG); e.add(GRIFFIN_EGG); e.add(WEREWOLF_EGG);
        });
    }
}
