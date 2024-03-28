package net.sydokiddo.odyssey.mixin.blocks.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CaveVinesBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CaveVinesBlock.class)
public abstract class CaveVinesBlockMixin extends GrowingPlantHeadBlock {

    private CaveVinesBlockMixin(Properties properties, Direction direction, VoxelShape voxelShape, boolean scheduleFluidTicks, double growPerTickProbability) {
        super(properties, direction, voxelShape, scheduleFluidTicks, growPerTickProbability);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        if (levelReader.getBlockState(blockPos.above()).is(BlockTags.LEAVES) && !levelReader.isWaterAt(blockPos)) {
            return true;
        }
        return super.canSurvive(blockState, levelReader, blockPos);
    }
}