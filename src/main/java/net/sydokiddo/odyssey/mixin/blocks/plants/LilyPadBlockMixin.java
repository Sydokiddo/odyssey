package net.sydokiddo.odyssey.mixin.blocks.plants;

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
    private boolean canGrowTo(BlockPos blockPos, BlockGetter blockGetter) {
        return this.mayPlaceOn(blockGetter.getBlockState(blockPos.below()), blockGetter, blockPos.below()) && blockGetter.getBlockState(blockPos).isAir();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return Odyssey.getConfig().blocks.boneMealingConfig.lily_pad_bone_mealing;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return Odyssey.getConfig().blocks.boneMealingConfig.lily_pad_bone_mealing;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {

        if (Odyssey.getConfig().blocks.boneMealingConfig.lily_pad_bone_mealing) {

            stopGrowth:

            for (int radius = 0; radius < 24; ++radius) {

                BlockPos growPos = blockPos;

                for (int growthOffset = 0; growthOffset < radius / 16; ++growthOffset) {

                    growPos = growPos.offset(randomSource.nextInt(3) - 1, 0, randomSource.nextInt(3) - 1);

                    if (serverLevel.getBlockState(growPos).isCollisionShapeFullBlock(serverLevel, growPos)) {
                        continue stopGrowth;
                    }
                }

                if (this.canGrowTo(growPos, serverLevel)) {
                    serverLevel.setBlockAndUpdate(growPos, Blocks.LILY_PAD.defaultBlockState());
                    blockState.tick(serverLevel, growPos, randomSource);
                }
            }
        }
    }

    // endregion
}