package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.Odyssey;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WaterlilyBlock.class)
public abstract class LilyPadBlockMixin implements BonemealableBlock {

    // region Bone-Mealing

    @Shadow protected abstract boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos);

    @Unique
    private boolean canGrowTo(BlockPos pos, BlockGetter world) {
        return this.mayPlaceOn(world.getBlockState(pos.below()), world, pos.below()) && world.getBlockState(pos).isAir();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return Odyssey.getConfig().blocks.lily_pad_bone_mealing;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return Odyssey.getConfig().blocks.lily_pad_bone_mealing;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {

        if (Odyssey.getConfig().blocks.lily_pad_bone_mealing) {

            stopGrowth:

            for (int i = 0; i < 24; ++i) {

                BlockPos growPos = blockPos;

                for (int j = 0; j < i / 16; ++j) {
                    growPos = growPos.offset(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
                    if (level.getBlockState(growPos).isCollisionShapeFullBlock(level, growPos)) {
                        continue stopGrowth;
                    }
                }

                if (this.canGrowTo(growPos, level)) {
                    level.setBlockAndUpdate(growPos, Blocks.LILY_PAD.defaultBlockState());
                    blockState.tick(level, growPos, random);
                }
            }
        }
    }

    // endregion
}