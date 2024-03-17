package net.sydokiddo.odyssey.misc.util.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.helpers.BlockHelper;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class FillCracksOnCrackedBlocksDispenserBehavior implements DispenseItemBehavior {

    @Override
    public @NotNull ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {

        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
        BlockPos blockPos = blockSource.pos().relative(direction);
        ServerLevel serverLevel = blockSource.level();
        Optional<BlockState> fillableCrackedBlocks = OdysseyRegistry.getFillableCrackedBlocks(serverLevel.getBlockState(blockPos));

        if (fillableCrackedBlocks.isPresent()) {

            BlockHelper.playDispenserSound(blockSource);
            BlockHelper.playDispenserAnimation(blockSource, direction);

            if (!serverLevel.isClientSide()) {
                for (int particleAmount = 0; particleAmount < 5; ++particleAmount) {
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.CLAY.defaultBlockState()), blockPos.getX() + serverLevel.getRandom().nextDouble(), blockPos.getY() + 1, blockPos.getZ() + serverLevel.getRandom().nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                }
            }

            serverLevel.setBlockAndUpdate(blockPos, fillableCrackedBlocks.get());
            serverLevel.playSound(null, blockPos, ModSoundEvents.CLAY_BALL_FILL_CRACKS, SoundSource.BLOCKS, 1.0F, 1.0F);
            serverLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);

            if (Chrysalis.IS_DEBUG) Odyssey.LOGGER.info("Dispenser has successfully filled the cracks on a cracked block at {}", blockPos);

            itemStack.shrink(1);
            return itemStack;
        }

        return BlockHelper.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
    }
}