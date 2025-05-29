package net.hir0shiyt.selfrestraint.mixin;

import net.hir0shiyt.selfrestraint.player.PlayerSpeedJumpLimiter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {

    @Inject(method = "jump", at = @At("HEAD"))
    private void onJump(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity)(Object)this;
        PlayerSpeedJumpLimiter limiter = PlayerSpeedJumpLimiter.getLimiterForPlayer(self);
        if (limiter == null) return;

        double limitedJumpVelocity = limiter.getLimitedJumpVelocity();

        Vec3d currentVelocity = self.getVelocity();
        self.setVelocity(currentVelocity.x, limitedJumpVelocity, currentVelocity.z);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void onWriteNbt(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        PlayerSpeedJumpLimiter limiter = PlayerSpeedJumpLimiter.getLimiterForPlayer(player);
        NbtCompound selfRestraint = new NbtCompound();

        selfRestraint.putFloat("SpeedLimiter", limiter.getSpeedLimiter());
        selfRestraint.putFloat("JumpLimiter", limiter.getJumpLimiter());
        selfRestraint.putInt("BaseJumpBoostAmplifier", limiter.getBaseJumpBoostAmplifier());

        nbt.put("SelfRestraint", selfRestraint);

        System.out.println("Saving limiter: speed=" + limiter.getSpeedLimiter() + " jump=" + limiter.getJumpLimiter());

    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        PlayerSpeedJumpLimiter limiter = PlayerSpeedJumpLimiter.getLimiterForPlayer(player);
        if (nbt.contains("SelfRestraint")) {
            NbtCompound selfRestraint = nbt.getCompound("SelfRestraint");
            limiter.setSpeedLimiter(selfRestraint.getFloat("SpeedLimiter"));
            limiter.setJumpLimiter(selfRestraint.getFloat("JumpLimiter"));

            if (selfRestraint.contains("BaseJumpBoostAmplifier")) {
                limiter.setBaseJumpBoostAmplifier(selfRestraint.getInt("BaseJumpBoostAmplifier"));
            }
        }
    }
}
