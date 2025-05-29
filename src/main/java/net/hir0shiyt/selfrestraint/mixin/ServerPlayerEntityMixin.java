package net.hir0shiyt.selfrestraint.mixin;

import net.hir0shiyt.selfrestraint.network.packet.SpeedJumpLimiter;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void selfrestraint$readLimiterData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("SpeedLimiterSpeed") && nbt.contains("SpeedLimiterJump")) {
            float speed = nbt.getFloat("SpeedLimiterSpeed");
            float jump = nbt.getFloat("SpeedLimiterJump");
            SpeedJumpLimiter limiter = SpeedJumpLimiter.create(speed, jump);
            SpeedJumpLimiter.put(((ServerPlayerEntity) (Object) this).getUuid(), limiter);
            System.out.println("[SelfRestraint] Loaded limiter: speed=" + speed + " jump=" + jump);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void selfrestraint$writeLimiterData(NbtCompound nbt, CallbackInfo ci) {
        SpeedJumpLimiter limiter = SpeedJumpLimiter.get(((ServerPlayerEntity) (Object) this).getUuid());
        if (limiter != null) {
            nbt.putFloat("SpeedLimiterSpeed", limiter.getSpeed());
            nbt.putFloat("SpeedLimiterJump", limiter.getJump());
            System.out.println("[SelfRestraint] Saved limiter: speed=" + limiter.getSpeed() + " jump=" + limiter.getJump());
        }
    }
}