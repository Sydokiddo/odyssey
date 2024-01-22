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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
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
import java.util.function.BiConsumer;

@SuppressWarnings("deprecation")
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

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState adjacentBlockState, boolean moved) {
        this.updateRedstoneLanternNeighbors(level, blockPos);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState adjacentBlockState, boolean moved) {
        if (!moved) {
            this.updateRedstoneLanternNeighbors(level, blockPos);
        }
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getValue(LIT) ? 15 : 0;
    }

    // endregion

    // region Interacting

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        if (blockState.getValue(LIT)) {
            this.turnOffLantern(level, blockPos, blockState);
        } else {
            this.turnOnLantern(level, blockPos, blockState);
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void onExplosionHit(BlockState blockState, Level level, BlockPos blockPos, Explosion explosion, BiConsumer<ItemStack, BlockPos> biConsumer) {

        if (!level.isClientSide() && explosion.getBlockInteraction() == Explosion.BlockInteraction.TRIGGER_BLOCK && blockState.getValue(LIT)) {
            this.turnOffLantern(level, blockPos, blockState);
        }

        super.onExplosionHit(blockState, level, blockPos, explosion, biConsumer);
    }

    private void turnOnLantern(Level level, BlockPos blockPos, BlockState blockState) {
        level.playSound(null, blockPos, ModSoundEvents.REDSTONE_LANTERN_POWER_ON, SoundSource.BLOCKS);
        level.setBlock(blockPos, blockState.setValue(LIT, true), tickTime);
        level.gameEvent(null, GameEvent.BLOCK_ACTIVATE, blockPos);
        this.updateRedstoneLanternNeighbors(level, blockPos);
    }

    private void turnOffLantern(Level level, BlockPos blockPos, BlockState blockState) {

        level.playSound(null, blockPos, ModSoundEvents.REDSTONE_LANTERN_POWER_OFF, SoundSource.BLOCKS);
        level.setBlock(blockPos, blockState.setValue(LIT, false), tickTime);
        level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, blockPos);
        this.updateRedstoneLanternNeighbors(level, blockPos);

        if (level instanceof ServerLevel serverLevel) {

            for (int particleAmount = 0; particleAmount < 10; ++particleAmount) {

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

    private void updateRedstoneLanternNeighbors(Level level, BlockPos blockPos) {

        level.updateNeighborsAt(blockPos, this);

        for (Direction direction : Direction.values()) {
            level.updateNeighborsAt(blockPos.relative(direction), this);
        }
    }

    // endregion

    // region Particles

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {

        if (blockState.getValue(LIT)) {

            Direction direction = Direction.getRandom(randomSource);

            if (direction != Direction.UP) {
                double x = direction.getStepX() == 0 ? randomSource.nextDouble() : 0.3D + (double) direction.getStepX() * 0.1D;
                double y = direction.getStepY() == 0 ? randomSource.nextDouble() : 0.3D + (double) direction.getStepY() * 0.1D;
                double z = direction.getStepZ() == 0 ? randomSource.nextDouble() : 0.3D + (double) direction.getStepZ() * 0.1D;
                level.addParticle(DustParticleOptions.REDSTONE, (double) blockPos.getX() + x, (double) blockPos.getY() + y, (double) blockPos.getZ() + z, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    // endregion
}