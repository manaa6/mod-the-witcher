package com.bruxobestiario.client;

import com.bruxobestiario.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class WitcherClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.DROWNER, ctx -> new WitcherRenderer<>(ctx, "drowner"));
        EntityRendererRegistry.register(ModEntities.GHOUL, ctx -> new WitcherRenderer<>(ctx, "ghoul"));
        EntityRendererRegistry.register(ModEntities.NEKKER, ctx -> new WitcherRenderer<>(ctx, "nekker"));
        EntityRendererRegistry.register(ModEntities.GRIFFIN, ctx -> new WitcherRenderer<>(ctx, "griffin"));
        EntityRendererRegistry.register(ModEntities.WEREWOLF, ctx -> new WitcherRenderer<>(ctx, "werewolf"));
    }
}
