package net.hir0shiyt.selfrestraint.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.hir0shiyt.selfrestraint.network.packet.SetLimiterPayload;

public class ClientNetworking {
    public static void sendLimitersToServer(float speedLimiter, float jumpLimiter) {
        ClientPlayNetworking.send(new SetLimiterPayload(speedLimiter, jumpLimiter));
    }
}
