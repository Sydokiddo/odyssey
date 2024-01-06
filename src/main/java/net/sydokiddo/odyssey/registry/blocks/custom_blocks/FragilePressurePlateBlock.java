package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;

public class FragilePressurePlateBlock extends PressurePlateBlock {

    private static final BooleanProperty CRACKED = BlockStateProperties.CRACKED;

    public FragilePressurePlateBlock(BlockSetType blockSetType, Properties properties) {
        super(blockSetType, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(CRACKED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CRACKED);
    }

    @Override
    public @NotNull SoundType getSoundType(BlockState blockState) {
        if (blockState.getValue(CRACKED)) {
            return ModSoundEvents.FRAGILE_PRESSURE_PLATE_CRACKED;
        }
        return ModSoundEvents.FRAGILE_PRESSURE_PLATE;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {

        super.tick(blockState, serverLevel, blockPos, randomSource);

        if (this.getSignalForState(blockState) > 0) {
            serverLevel.setBlock(blockPos, blockState.setValue(CRACKED, true), 0);
            serverLevel.destroyBlock(blockPos, true);
        }
    }
}