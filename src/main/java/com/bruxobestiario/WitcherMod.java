package com.bruxobestiario;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class WitcherMod implements ModInitializer {
    public static final String MOD_ID = "bruxo_bestiario";
    public static Identifier id(String path) { return new Identifier(MOD_ID, path); }

    @Override
    public void onInitialize() {
        ModEntities.register();
        ModItems.register();
    }
}
