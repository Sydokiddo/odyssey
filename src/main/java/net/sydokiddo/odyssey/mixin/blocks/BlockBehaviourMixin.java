package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    // Small Flowers can be picked by right-clicking on them with an empty hand

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void odyssey$flowerPicking(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (Odyssey.getConfig().blocks.flower_picking && blockState.is(BlockTags.SMALL_FLOWERS) && player.mayBuild() && player.getMainHandItem().isEmpty() && !player.isSecondaryUseActive() && player.mayInteract(level, blockPos)) {

            Block.dropResources(blockState, level, blockPos);
            level.removeBlock(blockPos, false);
            level.playSound(player, blockPos, ModSoundEvents.SMALL_FLOWER_PICK, SoundSource.BLOCKS, 0.5F, 1.0F);
            level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(player, blockState));

            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}