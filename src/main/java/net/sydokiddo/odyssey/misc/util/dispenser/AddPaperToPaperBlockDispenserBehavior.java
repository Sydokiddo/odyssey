package net.sydokiddo.odyssey.misc.util.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.blocks.ModBlockStateProperties;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;

public class AddPaperToPaperBlockDispenserBehavior implements DispenseItemBehavior {

    @Override
    public @NotNull ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {

        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
        BlockPos blockPos = blockSource.pos().relative(direction);
        ServerLevel serverLevel = blockSource.level();
        BlockState blockState = serverLevel.getBlockState(blockPos);

        if (blockState.is(ModBlocks.PAPER_BLOCK) && blockState.getValue(ModBlockStateProperties.SHEETS) < 8) {

            RegistryHelpers.playDispenserSound(blockSource);
            RegistryHelpers.playDispenserAnimation(blockSource, direction);

            Block.pushEntitiesUp(blockState, blockState, serverLevel, blockPos);
            serverLevel.setBlock(blockPos, blockState.setValue(ModBlockStateProperties.SHEETS, Math.min(8, blockState.getValue(ModBlockStateProperties.SHEETS) + 1)), 3);
            serverLevel.playSound(null, blockPos, ModSoundEvents.PAPER_BLOCK_ADD_PAPER, SoundSource.BLOCKS, 1.0F, 1.0F);
            serverLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);

            if (Chrysalis.IS_DEBUG) {
                Odyssey.LOGGER.info("Dispenser has added Paper to a Paper Block at {}", blockPos);
            }

            itemStack.shrink(1);
            return itemStack;
        }
        return RegistryHelpers.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
    }
}