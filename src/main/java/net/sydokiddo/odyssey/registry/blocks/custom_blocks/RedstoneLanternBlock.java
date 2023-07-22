package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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

@SuppressWarnings("ALL")
public class RedstoneLanternBlock extends LanternBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public RedstoneLanternBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HANGING, false).setValue(LIT, true).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HANGING, LIT, WATERLOGGED);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if (state.getValue(LIT)) {

            world.setBlock(pos, (BlockState)state.setValue(LIT, false), 3);
            world.playSound(null, pos, ModSoundEvents.REDSTONE_LANTERN_POWER_OFF, SoundSource.BLOCKS);
            world.gameEvent(player, GameEvent.BLOCK_DEACTIVATE, pos);

            for(int i = 0; i < 10; ++i) {
                double d = (double) pos.getX() + 0.5D;
                double e = (double) pos.getY() + 0.7D;
                double f = (double) pos.getZ() + 0.5D;
                world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0D, 0.0D, 0.0D);
            }

        } else {
            world.setBlock(pos, (BlockState)state.setValue(LIT, true), 3);
            world.playSound(null, pos, ModSoundEvents.REDSTONE_LANTERN_POWER_ON, SoundSource.BLOCKS);
            world.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
        }

        world.scheduleTick(pos, this, 2);
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {

            Direction direction = Direction.getRandom(random);

            if (direction != Direction.UP) {
                double d = direction.getStepX() == 0 ? random.nextDouble() : 0.3D + (double) direction.getStepX() * 0.1D;
                double e = direction.getStepY() == 0 ? random.nextDouble() : 0.3D + (double) direction.getStepY() * 0.1D;
                double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.3D + (double) direction.getStepZ() * 0.1D;
                world.addParticle(DustParticleOptions.REDSTONE, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + f, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        Direction[] directionValues = Direction.values();
        for (Direction direction : directionValues) {
            world.updateNeighborsAt(pos.relative(direction), this);
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved) {
            Direction[] directionValues = Direction.values();
            for (Direction direction : directionValues) {
                world.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return state.getValue(LIT) ? 15 : 0;
    }
}
