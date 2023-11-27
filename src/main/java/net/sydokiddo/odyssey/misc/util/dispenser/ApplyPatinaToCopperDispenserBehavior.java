package net.sydokiddo.odyssey.misc.util.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.items.custom_items.PatinaItem;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import java.util.Optional;

public class ApplyPatinaToCopperDispenserBehavior implements DispenseItemBehavior {

    public static final ApplyPatinaToCopperDispenserBehavior INSTANCE = new ApplyPatinaToCopperDispenserBehavior();

    @Override
    public ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {

        BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
        ServerLevel serverLevel = blockSource.level();
        BlockState blockState = serverLevel.getBlockState(blockPos);
        Optional<BlockState> copperState = PatinaItem.getNextCopperState(blockState);

        if (copperState.isPresent()) {

            serverLevel.setBlockAndUpdate(blockPos, copperState.get());
            serverLevel.playSound(null, blockPos, ModSoundEvents.PATINA_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            serverLevel.levelEvent(3005, blockPos, 0);
            serverLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);

            if (Chrysalis.IS_DEBUG) {
                Odyssey.LOGGER.info("Dispenser has successfully dispensed Patina at {}", blockPos);
            }

            itemStack.shrink(1);
            return itemStack;
        }
        return RegistryHelpers.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
    }
}