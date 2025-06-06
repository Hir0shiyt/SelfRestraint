package net.hir0shiyt.selfrestraint;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hir0shiyt.selfrestraint.event.PlayerLimiterHandler;
import net.hir0shiyt.selfrestraint.network.ServerNetworking;
import net.hir0shiyt.selfrestraint.network.packet.SetLimiterPayload;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
					CommandManager.literal("testingselfrestraint")
							.requires(source -> source.hasPermissionLevel(2))
							.executes(context -> {
								ServerPlayerEntity player = context.getSource().getPlayer();
								if (player != null) {
									// Apply Jump Boost for 10 seconds, amplifier 1
									StatusEffectInstance jumpBoost = new StatusEffectInstance(
											StatusEffects.JUMP_BOOST, 200, 1, false, true);
									player.addStatusEffect(jumpBoost);
									context.getSource().sendFeedback(() ->
											Text.literal("Jump Boost applied! Try jumping."), false);
								}
								return 1;
							})
			);
		});
	}

}