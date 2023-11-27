package net.sydokiddo.odyssey.misc.util.dispenser;

import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import java.util.Optional;

public class ScrapeBlockDispenserBehavior implements DispenseItemBehavior {

    public static final ScrapeBlockDispenserBehavior INSTANCE = new ScrapeBlockDispenserBehavior();

    @SuppressWarnings("all")
    private Optional<BlockState> getStripped(BlockState blockState) {
        return Optional.ofNullable(AxeItemAccessor.getStrippedBlocks().get(blockState.getBlock())).map(block -> block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS)));
    }

    private static void doBlockConversionEvents(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, SoundEvent soundEvent, ItemStack itemStack) {

        serverLevel.setBlockAndUpdate(blockPos, blockState);
        serverLevel.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        serverLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);

        if (itemStack.hurt(1, serverLevel.getRandom(), null)) {
            itemStack.setCount(0);
        }
    }

    @Override
    public ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {

        BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
        ServerLevel serverLevel = blockSource.level();
        BlockState blockState = serverLevel.getBlockState(blockPos);

        Optional<BlockState> strippableWoodBlocks = this.getStripped(blockState);
        Optional<BlockState> previousCopperState = WeatheringCopper.getPrevious(blockState);
        Optional<BlockState> waxedCopperState = Optional.ofNullable(HoneycombItem.WAX_OFF_BY_BLOCK.get().get(blockState.getBlock())).map(block -> block.withPropertiesOf(blockState));

        // Wood Stripping

        if (strippableWoodBlocks.isPresent()) {
            doBlockConversionEvents(serverLevel, blockPos, strippableWoodBlocks.get(), SoundEvents.AXE_STRIP, itemStack);
            return itemStack;
        }

        // Copper Scraping

        if (previousCopperState.isPresent()) {

            doBlockConversionEvents(serverLevel, blockPos, previousCopperState.get(), SoundEvents.AXE_SCRAPE, itemStack);
            serverLevel.levelEvent(3005, blockPos, 0);

            if (serverLevel.getRandom().nextFloat() < 0.25F) {
                Block.popResource(serverLevel, blockPos, new ItemStack(ModItems.PATINA));
            }

            return itemStack;
        }

        // Removing Wax from Copper

        if (waxedCopperState.isPresent()) {
            doBlockConversionEvents(serverLevel, blockPos, waxedCopperState.get(), SoundEvents.AXE_WAX_OFF, itemStack);
            serverLevel.levelEvent(3004, blockPos, 0);
            return itemStack;
        }

        // Converting Sticky Pistons into Pistons

        if (blockState.is(Blocks.STICKY_PISTON) && !blockState.getValue(PistonBaseBlock.EXTENDED) && Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.piston_interactions) {

            Direction direction = blockState.getValue(PistonBaseBlock.FACING);

            if (!serverLevel.isClientSide()) {
                for (int i = 0; i < 5; ++i) {
                    serverLevel.sendParticles(ParticleTypes.ITEM_SLIME, blockPos.getX() + serverLevel.getRandom().nextDouble(), blockPos.getY() + 1, blockPos.getZ() + serverLevel.getRandom().nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                }
            }

            doBlockConversionEvents(serverLevel, blockPos, Blocks.PISTON.defaultBlockState().setValue(PistonBaseBlock.FACING, direction), ModSoundEvents.PISTON_REMOVE_SLIMEBALL, itemStack);
            Block.popResourceFromFace(serverLevel, blockPos, direction, new ItemStack(Items.SLIME_BALL));
            return itemStack;
        }

        OdysseyRegistry.playDispenserFailSound(blockSource);
        return itemStack;
    }
}