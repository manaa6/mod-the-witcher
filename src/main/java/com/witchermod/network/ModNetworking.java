package com.witchermod.network;

import com.witchermod.WitcherMod;
import com.witchermod.sign.SignManager;
import com.witchermod.sign.SignType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public final class ModNetworking {
    public static final Identifier CAST_SIGN = WitcherMod.id("cast_sign");

    private ModNetworking() {}

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(CAST_SIGN, (server, player, handler, buf, responseSender) -> {
            int index = buf.readVarInt(); // lê o buffer ANTES de mandar pro thread do servidor
            server.execute(() -> SignManager.cast(player, SignType.byIndex(index)));
        });
    }
}
