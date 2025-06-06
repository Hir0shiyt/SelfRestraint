package net.hir0shiyt.selfrestraint.mixin;

import net.hir0shiyt.selfrestraint.player.PlayerSpeedJumpLimiter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
    private void scaleJumpBasedOnLimiter(CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof PlayerEntity player) {
            float limiter = PlayerSpeedJumpLimiter.getLimiter(player.getUuid()).getJumpLimiter(); // 0.0 to 1.0

            // Only activate when limiter > 0
            if (limiter <= 0.0f) return;

            float baseJump = 0.42f;
            float fullJump = cir.getReturnValueF(); // Includes potion/mod effects
            float adjustedJump = fullJump - (fullJump - baseJump) * limiter;

            cir.setReturnValue(adjustedJump);
            player.sendMessage(Text.literal("Limiter active. Adjusted Jump: " + adjustedJump), true);
        }
    }

}