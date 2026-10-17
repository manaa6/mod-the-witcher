package com.witchermod.item;

import com.witchermod.WitcherMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

public final class ModItems {
    public static final Item SILVER_INGOT = register("silver_ingot", new Item(new Item.Settings()));

    public static final Item STEEL_SWORD = register("steel_sword",
            new WitcherSword(WitcherToolMaterials.STEEL, 3, -2.4f, new Item.Settings(),
                    WitcherSword.Bonus.HUMANS, 4.0f, false, "tooltip.witcher.steel"));

    public static final Item SILVER_SWORD = register("silver_sword",
            new WitcherSword(WitcherToolMaterials.SILVER, 3, -2.4f, new Item.Settings(),
                    WitcherSword.Bonus.MONSTERS, 6.0f, false, "tooltip.witcher.silver"));

    public static final Item AERONDIGHT = register("aerondight",
            new WitcherSword(WitcherToolMaterials.AERONDIGHT, 3, -2.2f,
                    new Item.Settings().rarity(Rarity.EPIC).fireproof(),
                    WitcherSword.Bonus.MONSTERS, 10.0f, true, "tooltip.witcher.aerondight"));

    private ModItems() {}

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, WitcherMod.id(name), item);
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(STEEL_SWORD);
            entries.add(SILVER_SWORD);
            entries.add(AERONDIGHT);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> entries.add(SILVER_INGOT));
    }
}
