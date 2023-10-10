package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TintedGlassPaneBlock extends IronBarsBlock {

    public TintedGlassPaneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockGetter.getMaxLightLevel();
    }
}