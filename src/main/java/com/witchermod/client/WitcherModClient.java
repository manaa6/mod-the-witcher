package com.witchermod.client;

import com.witchermod.network.ModNetworking;
import com.witchermod.sign.SignType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class WitcherModClient implements ClientModInitializer {
    private static KeyBinding castKey;
    private static KeyBinding nextKey;
    private static KeyBinding prevKey;
    private static int selected = 0;

    @Override
    public void onInitializeClient() {
        castKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.witcher.cast_sign", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.witcher"));
        nextKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.witcher.next_sign", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "category.witcher"));
        prevKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.witcher.prev_sign", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "category.witcher"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (nextKey.wasPressed()) changeSelection(client, 1);
            while (prevKey.wasPressed()) changeSelection(client, -1);

            while (castKey.wasPressed()) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeVarInt(selected);
                ClientPlayNetworking.send(ModNetworking.CAST_SIGN, buf);
            }
        });

        HudRenderCallback.EVENT.register((ctx, tickDelta) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.options.hudHidden) return;
            SignType sign = SignType.byIndex(selected);
            Text text = Text.translatable("hud.witcher.sign", Text.translatable(sign.translationKey()));
            int w = mc.textRenderer.getWidth(text);
            ctx.drawTextWithShadow(mc.textRenderer, text,
                    ctx.getScaledWindowWidth() - w - 8,
                    ctx.getScaledWindowHeight() - 20,
                    0xFF000000 | sign.color());
        });
    }

    private static void changeSelection(MinecraftClient client, int delta) {
        selected = Math.floorMod(selected + delta, SignType.values().length);
        client.player.sendMessage(Text.translatable("message.witcher.selected",
                Text.translatable(SignType.byIndex(selected).translationKey())), true);
    }
}
