package com.bruxobestiario.client;

import com.bruxobestiario.WitcherMod;
import com.bruxobestiario.entity.WitcherMonster;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WitcherRenderer<T extends WitcherMonster> extends GeoEntityRenderer<T> {
    public WitcherRenderer(EntityRendererFactory.Context ctx, String name) {
        super(ctx, new DefaultedEntityGeoModel<>(WitcherMod.id(name)));
    }
}
