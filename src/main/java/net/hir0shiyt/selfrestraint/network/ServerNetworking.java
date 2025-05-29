package net.hir0shiyt.selfrestraint.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hir0shiyt.selfrestraint.network.packet.SetLimiterPayload;
import net.hir0shiyt.selfrestraint.player.PlayerSpeedJumpLimiter;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerNetworking {
    public static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(SetLimiterPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();

            float speed = payload.speed();
            float jump = payload.jump();

            PlayerSpeedJumpLimiter.setLimiter(player.getUuid(), speed, jump);
        });
    }
}