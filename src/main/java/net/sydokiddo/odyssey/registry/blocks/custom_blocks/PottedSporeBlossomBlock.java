package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PottedSporeBlossomBlock extends FlowerPotBlock {

    public PottedSporeBlossomBlock(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {

        int i = blockPos.getX();
        int j = blockPos.getY();
        int k = blockPos.getZ();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int l = 0; l < 7; ++l) {
            mutableBlockPos.set(i + Mth.nextInt(randomSource, -5, 5), j - randomSource.nextInt(5), k + Mth.nextInt(randomSource, -5, 5));
            BlockState blockState2 = level.getBlockState(mutableBlockPos);
            if (blockState2.isCollisionShapeFullBlock(level, mutableBlockPos)) continue;
            level.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, (double)mutableBlockPos.getX() + randomSource.nextDouble(), (double)mutableBlockPos.getY() + randomSource.nextDouble(), (double)mutableBlockPos.getZ() + randomSource.nextDouble(), 0.0, 0.0, 0.0);
        }
    }
}