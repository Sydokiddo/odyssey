package net.sydokiddo.odyssey.mixin.entities.passive;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.animal.allay.AllayAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;

@Mixin(AllayAi.class)
public class AllayAiMixin {

    // Prevents Allays from attempting to move towards their liked player when attached to a lead

    @Inject(method = "getLikedPlayerPositionTracker", at = @At("HEAD"), cancellable = true)
    private static void odyssey$preventAllaysMovingTowardsPlayersWhenLeashed(LivingEntity livingEntity, CallbackInfoReturnable<Optional<PositionTracker>> cir) {
        if (livingEntity instanceof Mob allay && allay.isLeashed()) {
            cir.setReturnValue(Optional.empty());
        }
    }
}