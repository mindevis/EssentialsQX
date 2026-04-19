package com.essentials.qx.fabric;

import com.essentials.qx.EssentialsQXMod;
import com.essentials.qx.HelpManager;
import com.essentials.qx.TimeManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.Commands;

public final class EssentialsQXFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        EssentialsQXMod.init(FabricLoader.getInstance().getConfigDir());
        PayloadTypeRegistry.playS2C().register(HelpManager.TYPE, HelpManager.STREAM_CODEC);
        HelpManager.setPayloadSender(ServerPlayNetworking::send);

        ServerLifecycleEvents.SERVER_STARTED.register(EssentialsQXMod::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(EssentialsQXMod::onServerStopping);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> EssentialsQXMod.onPlayerJoin(handler.player));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> EssentialsQXMod.onPlayerQuit(handler.player));
        ServerTickEvents.END_SERVER_TICK.register(EssentialsQXMod::onServerTick);
        ServerTickEvents.END_WORLD_TICK.register(TimeManager::onLevelTick);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            EssentialsQXMod.registerCommands(dispatcher, registryAccess, Commands.CommandSelection.ALL)
        );
    }
}
