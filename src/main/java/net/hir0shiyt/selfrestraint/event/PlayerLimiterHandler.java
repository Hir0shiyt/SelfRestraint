package net.hir0shiyt.selfrestraint.event;

import net.hir0shiyt.selfrestraint.player.PlayerSpeedJumpLimiter;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlayerLimiterHandler {

    public static final Identifier SPEED_LIMITER_ID = Identifier.of("selfrestraint", "speed_limiter");
    private static final Map<UUID, Boolean> wasOnGroundMap = new HashMap<>();
    private static final int MAX_JUMP_AMPLIFIER = 4;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                onPlayerTick(player);
            }
        });
    }

    public static void onPlayerTick(ServerPlayerEntity player) {
        PlayerSpeedJumpLimiter limiter = PlayerSpeedJumpLimiter.getLimiterForPlayer(player);
        if (limiter == null) return;

        EntityAttributeInstance speedAttr = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (speedAttr != null) {
            EntityAttributeModifier oldMod = speedAttr.getModifier(SPEED_LIMITER_ID);
            if (oldMod != null) {
                speedAttr.removeModifier(oldMod);
            }

            float limitedSpeed = limiter.getLimitedSpeed();
            float baseSpeed = limiter.getMaxSpeedFromSkills();

            if (baseSpeed > 0 && limitedSpeed >= PlayerSpeedJumpLimiter.VANILLA_WALK_SPEED) {
                double multiplier = limitedSpeed / baseSpeed;

                speedAttr.addTemporaryModifier(new EntityAttributeModifier(
                        SPEED_LIMITER_ID,
                        multiplier - 1.0,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ));
            }
        }

        var currentEffect = player.getStatusEffect(StatusEffects.JUMP_BOOST);
        if (currentEffect != null) {
            // Save original amplifier if it changed
            int amplifier = currentEffect.getAmplifier();
            if (limiter.getBaseJumpBoostAmplifier() < 0 && currentEffect != null) {
                limiter.setBaseJumpBoostAmplifier(currentEffect.getAmplifier());
            }
        }

        if (limiter.getJumpLimiter() <= 0.2f || limiter.getBaseJumpBoostAmplifier() <= 0) {
            player.removeStatusEffect(StatusEffects.JUMP_BOOST);
        } else {
            int scaledAmplifier = (int)((limiter.getJumpLimiter() * (limiter.getBaseJumpBoostAmplifier() + 1)) - 1);
            scaledAmplifier = Math.max(0, Math.min(scaledAmplifier, limiter.getBaseJumpBoostAmplifier()));

            StatusEffectInstance limitedEffect = new StatusEffectInstance(
                    StatusEffects.JUMP_BOOST,
                    40,  // 2 seconds
                    scaledAmplifier,
                    false, false, false // ambient, particles, icon
            );
            player.addStatusEffect(limitedEffect);
        }
        wasOnGroundMap.put(player.getUuid(), player.isOnGround());
    }
}
