package com.witchermod.item;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public enum WitcherToolMaterials implements ToolMaterial {
    // miningLevel, durability, miningSpeed, attackDamage, enchantability, reparo
    STEEL(2, 900, 7.0f, 2.5f, 10, () -> Ingredient.ofItems(Items.IRON_INGOT)),
    SILVER(2, 700, 7.0f, 3.0f, 20, () -> Ingredient.ofItems(ModItems.SILVER_INGOT)),
    AERONDIGHT(4, 2200, 9.0f, 4.0f, 22, () -> Ingredient.ofItems(ModItems.SILVER_INGOT, Items.NETHERITE_SCRAP));

    private final int miningLevel;
    private final int durability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repair;

    WitcherToolMaterials(int miningLevel, int durability, float miningSpeed, float attackDamage,
                         int enchantability, Supplier<Ingredient> repair) {
        this.miningLevel = miningLevel;
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repair = repair;
    }

    @Override public int getDurability() { return durability; }
    @Override public float getMiningSpeedMultiplier() { return miningSpeed; }
    @Override public float getAttackDamage() { return attackDamage; }
    @Override public int getMiningLevel() { return miningLevel; }
    @Override public int getEnchantability() { return enchantability; }
    @Override public Ingredient getRepairIngredient() { return repair.get(); }
}
