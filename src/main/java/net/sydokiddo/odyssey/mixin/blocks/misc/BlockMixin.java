package net.sydokiddo.odyssey.mixin.blocks.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "stepOn", at = @At("HEAD"))
    private void odyssey$stepOnBlock(Level level, BlockPos blockPos, BlockState blockState, Entity entity, CallbackInfo info) {
        if (blockState.is(ModTags.BOUNCY_BLOCKS) && Math.abs(entity.getDeltaMovement().y) < 0.1 && !entity.isSteppingCarefully() && Odyssey.getConfig().blocks.miscBlocksConfig.bouncy_mushroom_blocks) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
        }
    }

    @Inject(at = @At("HEAD"), method = "fallOn", cancellable = true)
    private void odyssey$fallOnBlock(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float fallDistance, CallbackInfo info) {

        // Falling on Note Blocks

        if (!level.isClientSide() && !entity.isShiftKeyDown() && fallDistance > 0 && blockState.getBlock() instanceof NoteBlock noteBlock && !blockState.getValue(NoteBlock.INSTRUMENT).worksAboveNoteBlock() && Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.noteBlockConfig.note_block_sensitivity) {

            if (entity instanceof Player player) {
                player.awardStat(Stats.PLAY_NOTEBLOCK);
            }

            if (Chrysalis.IS_DEBUG) {
                Odyssey.LOGGER.info("{} has fallen onto a Note Block at {}", entity.getName().getString(), blockPos);
            }

            noteBlock.playNote(entity, blockState, level, blockPos);
        }

        // Falling on Mushroom Blocks

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