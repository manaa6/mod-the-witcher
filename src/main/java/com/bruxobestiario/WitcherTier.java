package com.bruxobestiario;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public enum WitcherTier implements ToolMaterial {
    STEEL(1400, 8.0f, 3.0f, 12),
    SILVER(1000, 8.0f, 2.0f, 15);

    private final int durability; private final float speed; private final float damage; private final int enchant;
    WitcherTier(int d, float s, float dmg, int e) { durability = d; speed = s; damage = dmg; enchant = e; }
    @Override public int getDurability() { return durability; }
    @Override public float getMiningSpeedMultiplier() { return speed; }
    @Override public float getAttackDamage() { return damage; }
    @Override public int getMiningLevel() { return 3; }
    @Override public int getEnchantability() { return enchant; }
    @Override public Ingredient getRepairIngredient() { return Ingredient.ofItems(Items.IRON_INGOT); }
}
