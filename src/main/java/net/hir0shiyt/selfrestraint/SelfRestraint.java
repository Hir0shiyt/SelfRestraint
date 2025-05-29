package net.hir0shiyt.selfrestraint;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hir0shiyt.selfrestraint.event.PlayerLimiterHandler;
import net.hir0shiyt.selfrestraint.network.ServerNetworking;
import net.hir0shiyt.selfrestraint.network.packet.SetLimiterPayload;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelfRestraint implements ModInitializer {
	public static final String MOD_ID = "selfrestraint";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		PlayerLimiterHandler.register();
		PayloadTypeRegistry.playC2S().register(SetLimiterPayload.ID, SetLimiterPayload.CODEC);
		ServerNetworking.registerReceivers();
	}

}