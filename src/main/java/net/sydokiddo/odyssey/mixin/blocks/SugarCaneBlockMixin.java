package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin extends Block implements SimpleWaterloggedBlock {

    @Shadow @Final public static IntegerProperty AGE;
    @Unique private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private SugarCaneBlockMixin(Properties properties) {
        super(properties);
    }

    // region Block State Initialization

    @Inject(method = "<init>", at = @At("RETURN"))
    private void odyssey$registerSugarCaneDefaultBlockStates(Properties properties, CallbackInfo ci) {
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(WATERLOGGED, false));
    }

    @Inject(method = "createBlockStateDefinition", at = @At("HEAD"))
    private void odyssey$createSugarCaneBlockStates(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(WATERLOGGED);
    }

    @Nullable @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        boolean inWater = fluidState.getType() == Fluids.WATER;
        return Objects.requireNonNull(super.getStateForPlacement(blockPlaceContext)).setValue(WATERLOGGED, inWater);
    }

    @Inject(method = "updateShape", at = @At("RETURN"))
    private void odyssey$updateShapeForSugarCane(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2, CallbackInfoReturnable<BlockState> cir) {
        if (blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
    private void odyssey$canSugarCaneSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {

        BlockState belowState = levelReader.getBlockState(blockPos.below());

        if ((belowState.is(this) || belowState.is(BlockTags.DIRT) || belowState.is(BlockTags.SAND)) && blockState.getValue(WATERLOGGED)) {
            cir.setReturnValue(true);
        }
    }

    // endregion
}