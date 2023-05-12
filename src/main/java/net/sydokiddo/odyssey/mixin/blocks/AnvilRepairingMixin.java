package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public class AnvilRepairingMixin {

    // Anvils can be repaired by right-clicking on them with an Iron Block

    @SuppressWarnings("ALL")
    @Inject(at=@At("HEAD"), method="use", cancellable=true)
    private void odyssey_repairAnvilWithIronBlock(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> ci) {

        ItemStack itemStack = player.getItemInHand(hand);
        BlockState anvilState = level.getBlockState(pos);

        if (!level.isClientSide && itemStack.getItem() == Item.byBlock(Blocks.IRON_BLOCK)) {

            boolean consume = false;

            if (anvilState.getBlock() == Blocks.DAMAGED_ANVIL) {
                level.setBlockAndUpdate(pos, Blocks.CHIPPED_ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, anvilState.getValue(AnvilBlock.FACING)));
                consume = true;
            } else if (anvilState.getBlock() == Blocks.CHIPPED_ANVIL) {
                level.setBlockAndUpdate(pos, Blocks.ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, anvilState.getValue(AnvilBlock.FACING)));
                consume = true;
            }

            if (consume) {

                level.playSound(null, pos, SoundEvents.ANVIL_HIT, SoundSource.BLOCKS, 1.0f, 1.0f);
                player.gameEvent(GameEvent.BLOCK_CHANGE);

                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                ci.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}