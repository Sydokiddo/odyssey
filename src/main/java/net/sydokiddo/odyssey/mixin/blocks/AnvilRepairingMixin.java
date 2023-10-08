package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import net.sydokiddo.odyssey.registry.misc.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public class AnvilRepairingMixin {

    // Anvils can be repaired by right-clicking on them with any items in the repairs_anvils tag

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void odyssey$anvilRepairing(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {

        ItemStack itemInHand = player.getItemInHand(hand);

        if (!level.isClientSide && itemInHand.is(ModTags.REPAIRS_ANVILS) && Odyssey.getConfig().blocks.anvil_repairing && blockState.getBlock() != Blocks.ANVIL) {

            Block anvilType;

            if (blockState.getBlock() == Blocks.DAMAGED_ANVIL) {
                anvilType = Blocks.CHIPPED_ANVIL;
            } else {
                anvilType = Blocks.ANVIL;
            }

            level.setBlockAndUpdate(blockPos, anvilType.defaultBlockState().setValue(AnvilBlock.FACING, blockState.getValue(AnvilBlock.FACING)));
            level.playSound(null, blockPos, ModSoundEvents.ANVIL_REPAIR, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player));

            if (!player.getAbilities().instabuild) {
                itemInHand.shrink(1);
            }

            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}