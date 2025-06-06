package net.hir0shiyt.selfrestraint.player;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerSpeedJumpLimiter {

    private float speedLimiterPercentage = 1.0f;
    private float jumpLimiterPercentage = 1.0f;

    private float maxSpeedFromSkills = 0.3f;
    private float maxJumpFromSkills = 0.6f;
    private int baseJumpBoostAmplifier = -1; // -1 = unset


    public static final float VANILLA_WALK_SPEED = 0.1f;
    public static final float VANILLA_JUMP_VELOCITY = 0.42f;

    private static final Map<UUID, PlayerSpeedJumpLimiter> LIMITERS = new ConcurrentHashMap<>();

    public static PlayerSpeedJumpLimiter getLimiter(UUID playerUuid) {
        return LIMITERS.computeIfAbsent(playerUuid, uuid -> new PlayerSpeedJumpLimiter());
    }

    public static PlayerSpeedJumpLimiter getLimiterForPlayer(PlayerEntity player) {
        return getLimiter(player.getUuid());
    }

    public static void setLimiter(UUID uuid, float speed, float jump) {
        PlayerSpeedJumpLimiter limiter = getLimiter(uuid);
        limiter.setSpeedLimiter(speed);
        limiter.setJumpLimiter(jump);
    }

    public float getSpeedLimiter() {
        return speedLimiterPercentage;
    }

    public void setSpeedLimiter(float percentage) {
        this.speedLimiterPercentage = Math.max(0f, Math.min(1f, percentage));
    }

    public float getJumpLimiter() {
        return jumpLimiterPercentage;
    }

    public void setJumpLimiter(float percentage) {
        this.jumpLimiterPercentage = Math.max(0f, Math.min(1f, percentage));
    }

    public float getMaxSpeedFromSkills() {
        return maxSpeedFromSkills;
    }

    public void setMaxSpeedFromSkills(float maxSpeedFromSkills) {
        this.maxSpeedFromSkills = maxSpeedFromSkills;
    }

    public float getMaxJumpFromSkills() {
        return maxJumpFromSkills;
    }

    public void setMaxJumpFromSkills(float maxJumpFromSkills) {
        this.maxJumpFromSkills = maxJumpFromSkills;
    }

    public float getLimitedSpeed() {
        float range = maxSpeedFromSkills - VANILLA_WALK_SPEED;
        return Math.max(VANILLA_WALK_SPEED, VANILLA_WALK_SPEED + speedLimiterPercentage * range);
    }

    public float getLimitedJumpVelocity() {
        float range = maxJumpFromSkills - VANILLA_JUMP_VELOCITY;
        return VANILLA_JUMP_VELOCITY + jumpLimiterPercentage * range;
    }

    public void setBaseJumpBoostAmplifier(int amplifier) {
        this.baseJumpBoostAmplifier = amplifier;
    }

    public int getBaseJumpBoostAmplifier() {
        return baseJumpBoostAmplifier;
    }
}

