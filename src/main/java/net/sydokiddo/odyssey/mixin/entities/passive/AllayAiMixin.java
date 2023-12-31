package net.sydokiddo.odyssey.mixin.entities.passive;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.allay.AllayAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;
import java.util.UUID;

@Mixin(AllayAi.class)
public abstract class AllayAiMixin {

    // Prevents Allays from attempting to move towards their liked player when attached to a lead

    @Inject(method = "getLikedPlayerPositionTracker", at = @At("HEAD"), cancellable = true)
    private static void odyssey$preventAllaysMovingTowardsPlayersWhenLeashed(LivingEntity livingEntity, CallbackInfoReturnable<Optional<PositionTracker>> cir) {

        Optional<UUID> optional = livingEntity.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
        Optional<ServerPlayer> likedPlayer = AllayAi.getLikedPlayer(livingEntity);

        if (livingEntity instanceof Allay allay && allay.isLeashed() && optional.isPresent() && likedPlayer.isPresent() && !allay.closerThan(likedPlayer.get(), 8)) {
            cir.setReturnValue(Optional.empty());
        }
    }
}