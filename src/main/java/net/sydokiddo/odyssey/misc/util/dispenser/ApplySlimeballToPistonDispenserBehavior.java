package net.sydokiddo.odyssey.misc.util.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;

public class ApplySlimeballToPistonDispenserBehavior implements DispenseItemBehavior {

    public static final ApplySlimeballToPistonDispenserBehavior INSTANCE = new ApplySlimeballToPistonDispenserBehavior();

    @Override
    public ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {

        BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
        ServerLevel serverLevel = blockSource.level();
        BlockState blockState = serverLevel.getBlockState(blockPos);
        Direction direction = blockState.getValue(PistonBaseBlock.FACING);

        if (blockState.is(Blocks.PISTON) && !blockState.getValue(PistonBaseBlock.EXTENDED)) {

            if (!serverLevel.isClientSide) {
                for (int i = 0; i < 5; ++i) {
                    serverLevel.sendParticles(ParticleTypes.ITEM_SLIME, blockPos.getX() + serverLevel.random.nextDouble(), blockPos.getY() + 1, blockPos.getZ() + serverLevel.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                }
            }

            serverLevel.setBlockAndUpdate(blockPos, Blocks.STICKY_PISTON.defaultBlockState().setValue(PistonBaseBlock.FACING, direction));
            serverLevel.playSound(null, blockPos, ModSoundEvents.PISTON_APPLY_SLIMEBALL, SoundSource.BLOCKS, 1.0F, 1.0F);
            serverLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);

            if (Chrysalis.IS_DEBUG) {
                Odyssey.LOGGER.info("Dispenser has converted a Piston into a Sticky Piston at {}", blockPos);
            }

            itemStack.shrink(1);
            return itemStack;
        }
        return RegistryHelpers.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
    }
}