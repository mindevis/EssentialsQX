package com.essentials.qx.fabric;

import com.essentials.qx.HelpManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;

public final class EssentialsQXFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PayloadTypeRegistry.playS2C().register(HelpManager.TYPE, HelpManager.STREAM_CODEC);
        HelpManager.setClientOpenScreenCallback(json ->
            Minecraft.getInstance().setScreen(new QxHelpScreen(json))
        );
        ClientPlayNetworking.registerGlobalReceiver(HelpManager.TYPE, (payload, context) ->
            context.client().execute(() -> HelpManager.onClientReceive(payload.json()))
        );
    }
}
