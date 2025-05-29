package net.hir0shiyt.selfrestraint.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSpeedJumpLimiter {

    private float speedLimiterPercentage = 1.0f;
    private float jumpLimiterPercentage = 1.0f;

    private float maxSpeedFromSkills = 0.3f;  // default example
    private float maxJumpFromSkills = 0.6f;   // default example

    private int baseJumpBoostAmplifier = -1; // -1 = unset

    public static final float VANILLA_WALK_SPEED = 0.1f;
    public static final float VANILLA_JUMP_VELOCITY = 0.42f;

    private static final Map<UUID, PlayerSpeedJumpLimiter> limiterMap = new HashMap<>();

    public static void setLimiter(UUID uuid, float speed, float jump) {
        PlayerSpeedJumpLimiter limiter = limiterMap.computeIfAbsent(uuid, id -> new PlayerSpeedJumpLimiter());
        limiter.setSpeedLimiter(speed);
        limiter.setJumpLimiter(jump);
    }

    public void setBaseJumpBoostAmplifier(int amplifier) {
        this.baseJumpBoostAmplifier = amplifier;
    }

    public int getBaseJumpBoostAmplifier() {
        return baseJumpBoostAmplifier;
    }

    public void setSpeedLimiter(float percentage) {
        this.speedLimiterPercentage = Math.max(0f, Math.min(1f, percentage));
    }

    public void setJumpLimiter(float percentage) {
        this.jumpLimiterPercentage = Math.max(0f, Math.min(1f, percentage));
    }

    public float getSpeedLimiter() {
        return speedLimiterPercentage;
    }

    public float getJumpLimiter() {
        return jumpLimiterPercentage;
    }

    public float getMaxSpeedFromSkills() {
        return maxSpeedFromSkills;
    }

    public float getMaxJumpFromSkills() {
        return maxJumpFromSkills;
    }

    public float getLimitedSpeed() {
        float range = maxSpeedFromSkills - VANILLA_WALK_SPEED;
        float limited = VANILLA_WALK_SPEED + speedLimiterPercentage * range;
        return Math.max(limited, VANILLA_WALK_SPEED); // <- clamp so it never goes below vanilla
    }

    public float getLimitedJumpVelocity() {
        float range = maxJumpFromSkills - VANILLA_JUMP_VELOCITY;
        return VANILLA_JUMP_VELOCITY + jumpLimiterPercentage * range;
    }

    public static PlayerSpeedJumpLimiter getLimiterForPlayer(PlayerEntity player) {
        return limiterMap.computeIfAbsent(player.getUuid(), id -> new PlayerSpeedJumpLimiter());
    }

    public void setMaxJumpFromSkills(float maxJumpFromSkills) {
        this.maxJumpFromSkills = maxJumpFromSkills;
    }

    public void setMaxSpeedFromSkills(float maxSpeedFromSkills) {
        this.maxSpeedFromSkills = maxSpeedFromSkills;
    }
    // TODO: Add methods to fetch maxSpeedFromSkills and maxJumpFromSkills from skill mods if available
}
