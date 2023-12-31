package net.sydokiddo.odyssey.mixin.blocks.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(at = @At("HEAD"), method = "fallOn", cancellable = true)
    private void odyssey$fallOnBouncyBlock(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float fallDistance, CallbackInfo info) {
        if (!entity.isSuppressingBounce() && blockState.is(ModTags.BOUNCY_BLOCKS) && Odyssey.getConfig().blocks.miscBlocksConfig.bouncy_mushroom_blocks) {
            entity.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
            level.playSound(null, blockPos, blockState.getSoundType().getStepSound(), SoundSource.BLOCKS, blockState.getSoundType().getVolume() * 0.15F, blockState.getSoundType().getPitch());
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "updateEntityAfterFallOn", cancellable = true)
    private void odyssey$updateEntityAfterFallingOnBouncyBlock(BlockGetter blockGetter, Entity entity, CallbackInfo info) {
        if (!entity.isSuppressingBounce() && blockGetter.getBlockState(entity.getOnPos()).is(ModTags.BOUNCY_BLOCKS) && Odyssey.getConfig().blocks.miscBlocksConfig.bouncy_mushroom_blocks) {
            this.bounceEntityUp(entity);
            info.cancel();
        }
    }

    @Unique
    private void bounceEntityUp(Entity entity) {

        Vec3 movement = entity.getDeltaMovement();

        if (movement.y < 0.0) {
            double d = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setDeltaMovement(movement.x, -movement.y * d, movement.z);
        }
    }
}