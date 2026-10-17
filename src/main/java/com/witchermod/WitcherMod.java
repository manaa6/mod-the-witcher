package com.witchermod;

import com.witchermod.item.ModItems;
import com.witchermod.network.ModNetworking;
import com.witchermod.sign.SignManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class WitcherMod implements ModInitializer {
    public static final String MOD_ID = "witcher";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModItems.register();
        ModNetworking.registerServer();
        SignManager.init();
    }
}
