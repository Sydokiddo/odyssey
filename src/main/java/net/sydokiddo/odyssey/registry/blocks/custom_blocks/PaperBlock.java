package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sydokiddo.odyssey.registry.blocks.ModBlockStateProperties;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class PaperBlock extends Block {

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape[] HITBOX = new VoxelShape[] {
        Shapes.empty(),
        Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
        Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
        Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
        Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
        Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
        Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
        Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
        Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
    };

    public PaperBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(ModBlockStateProperties.SHEETS, 8).setValue(WATERLOGGED, false));
    }

    // region Hitbox

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        if (Objects.requireNonNull(pathComputationType) == PathComputationType.LAND) {
            return blockState.getValue(ModBlockStateProperties.SHEETS) < 5;
        }
        return false;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return HITBOX[blockState.getValue(ModBlockStateProperties.SHEETS)];
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 1.0F;
    }

    // endregion

    // region Block State Initialization

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ModBlockStateProperties.SHEETS, WATERLOGGED);
    }

    @Override @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        return Objects.requireNonNull(super.getStateForPlacement(blockPlaceContext)).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState adjacentState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos adjacentPos) {
        if (blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(blockState, direction, adjacentState, levelAccessor, blockPos, adjacentPos);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    // endregion

    // region Mechanics

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        if (player.mayBuild()) {

            ItemStack itemInHand = player.getItemInHand(interactionHand);
            Item paper = Items.PAPER;
            int sheets = blockState.getValue(ModBlockStateProperties.SHEETS);
            int maxSheetAmount = 8;

            boolean interacted = false;

            BlockState blockStateValue = blockState.setValue(ModBlockStateProperties.SHEETS, sheets);
            SoundEvent soundEvent = SoundEvents.EMPTY;

            // region Removing Paper

            if (player.getMainHandItem().isEmpty() && sheets <= maxSheetAmount && sheets >= 1) {

                interacted = true;
                soundEvent = ModSoundEvents.PAPER_BLOCK_TAKE_PAPER;

                if (sheets == 1) {
                    blockStateValue = blockState.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                } else {
                    blockStateValue = blockState.setValue(ModBlockStateProperties.SHEETS, sheets - 1);
                }

                ItemStack itemStack = new ItemStack(paper);

                if (player.isShiftKeyDown()) {
                    if (!player.getInventory().add(itemStack)) {
                        player.drop(itemStack, false);
                    }
                } else {
                    Block.popResourceFromFace(level, blockPos, Direction.UP, itemStack);
                }
            }

            // endregion

            // region Adding Paper

            if (itemInHand.is(paper) && sheets < maxSheetAmount) {

                interacted = true;
                blockStateValue = blockState.setValue(ModBlockStateProperties.SHEETS, Math.min(maxSheetAmount, sheets + 1));
                soundEvent = ModSoundEvents.PAPER_BLOCK_ADD_PAPER;

                player.awardStat(Stats.ITEM_USED.get(itemInHand.getItem()));
                if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemInHand);
                if (!player.getAbilities().instabuild) itemInHand.shrink(1);
            }

            // endregion

            // region Interacting

            if (interacted) {

                Block.pushEntitiesUp(blockState, blockStateValue, level, blockPos);
                level.setBlock(blockPos, blockStateValue, 3);
                level.updateNeighbourForOutputSignal(blockPos, this);

                level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, blockState));

                return InteractionResult.sidedSuccess(level.isClientSide());
            }

            // endregion
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        if (!level.isClientSide()) {
            return blockState.getValue(ModBlockStateProperties.SHEETS);
        }
        return super.getAnalogOutputSignal(blockState, level, blockPos);
    }

    // endregion
}