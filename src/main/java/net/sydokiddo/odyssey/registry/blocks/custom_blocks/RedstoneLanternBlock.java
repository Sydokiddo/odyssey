package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;

public class RedstoneLanternBlock extends LanternBlock {

    private final int tickTime = 2;
    private static final BooleanProperty LIT = BlockStateProperties.LIT;

    public RedstoneLanternBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, false).setValue(LIT, true).setValue(WATERLOGGED, false));
    }

    // region Block State Initialization

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HANGING, LIT, WATERLOGGED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState neighborState, boolean bl) {
        for (Direction direction : Direction.values()) {
            level.updateNeighborsAt(blockPos.relative(direction), this);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState neighborState, boolean moved) {
        if (!moved) {
            for (Direction direction : Direction.values()) {
                level.updateNeighborsAt(blockPos.relative(direction), this);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getValue(LIT) ? 15 : 0;
    }

    // endregion

    // region Interacting

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (blockState.getValue(LIT)) {
            turnOffLantern(level, blockPos, blockState);
        } else {
            turnOnLantern(level, blockPos, blockState);
        }

        level.updateNeighborsAt(blockPos, this);

        for (Direction direction : Direction.values()) {
            level.updateNeighborsAt(blockPos.relative(direction), this);
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private void turnOnLantern(Level level, BlockPos blockPos, BlockState blockState) {
        level.playSound(null, blockPos, ModSoundEvents.REDSTONE_LANTERN_POWER_ON, SoundSource.BLOCKS);
        level.setBlock(blockPos, blockState.setValue(LIT, true), tickTime);
        level.gameEvent(null, GameEvent.BLOCK_ACTIVATE, blockPos);
    }

    private void turnOffLantern(Level level, BlockPos blockPos, BlockState blockState) {

        level.playSound(null, blockPos, ModSoundEvents.REDSTONE_LANTERN_POWER_OFF, SoundSource.BLOCKS);
        level.setBlock(blockPos, blockState.setValue(LIT, false), tickTime);
        level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, blockPos);

        if (level instanceof ServerLevel serverLevel) {

            for (int i = 0; i < 10; ++i) {

                SimpleParticleType particleType;

                if (serverLevel.getBlockState(blockPos).getValue(WATERLOGGED)) {
                    particleType = ParticleTypes.BUBBLE;
                } else {
                    particleType = ParticleTypes.SMOKE;
                }

                serverLevel.sendParticles(particleType, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.7D, (double) blockPos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    // endregion

    // region Particles

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
        if (blockState.getValue(LIT)) {

            Direction direction = Direction.getRandom(random);

            if (direction != Direction.UP) {
                double d = direction.getStepX() == 0 ? random.nextDouble() : 0.3D + (double) direction.getStepX() * 0.1D;
                double e = direction.getStepY() == 0 ? random.nextDouble() : 0.3D + (double) direction.getStepY() * 0.1D;
                double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.3D + (double) direction.getStepZ() * 0.1D;
                level.addParticle(DustParticleOptions.REDSTONE, (double) blockPos.getX() + d, (double) blockPos.getY() + e, (double) blockPos.getZ() + f, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    // endregion
}