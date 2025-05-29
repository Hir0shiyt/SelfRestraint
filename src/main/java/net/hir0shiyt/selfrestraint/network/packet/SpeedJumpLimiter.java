package net.hir0shiyt.selfrestraint.network.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeedJumpLimiter {
    private static final Map<UUID, SpeedJumpLimiter> limiterMap = new HashMap<>();
    public static SpeedJumpLimiter create(float speed, float jump) {
        SpeedJumpLimiter limiter = new SpeedJumpLimiter();
        limiter.setSpeed(speed);
        limiter.setJump(jump);
        return limiter;
    }

    private float speed;
    private float jump;

    // getters
    public float getSpeed() { return speed; }
    public float getJump() { return jump; }

        public SpeedJumpLimiter() {
            // default constructor
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public void setJump(float jump) {
            this.jump = jump;
        }

    public static SpeedJumpLimiter get(UUID uuid) {
        return limiterMap.get(uuid);
    }

    public static void put(UUID uuid, SpeedJumpLimiter limiter) {
        limiterMap.put(uuid, limiter);
    }
}

