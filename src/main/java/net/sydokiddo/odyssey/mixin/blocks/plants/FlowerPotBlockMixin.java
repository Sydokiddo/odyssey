package net.sydokiddo.odyssey.mixin.blocks.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.odyssey.registry.misc.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin extends Block implements SimpleWaterloggedBlock {

    @Shadow public abstract Block getPotted();
    @Unique private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private FlowerPotBlockMixin(Properties properties) {
        super(properties);
    }

    // region Block State Initialization

    @Inject(method = "<init>", at = @At("RETURN"))
    private void odyssey$registerFlowerPotDefaultBlockStates(Block block, Properties properties, CallbackInfo info) {
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Nullable @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        boolean inWater = fluidState.getType() == Fluids.WATER;
        return Objects.requireNonNull(super.getStateForPlacement(blockPlaceContext)).setValue(WATERLOGGED, inWater);
    }

    @Inject(method = "updateShape", at = @At("RETURN"))
    private void odyssey$updateShapeForFlowerPots(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2, CallbackInfoReturnable<BlockState> cir) {
        if (blockState.getValue(WATERLOGGED)) {
            if (!blockState.is(ModTags.UNDERWATER_FLOWER_POTS) && !blockState.is(Blocks.FLOWER_POT)) {
                popResource((Level) levelAccessor, blockPos, new ItemStack(this.getPotted()));
                levelAccessor.setBlock(blockPos, Blocks.FLOWER_POT.defaultBlockState(), 3);
            }
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    // endregion

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void odyssey$preventUnderwaterUseIfNotWaterPlant(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {

        ItemStack itemInHand = player.getItemInHand(interactionHand);

        if (blockState.getValue(WATERLOGGED) && !itemInHand.is(ModTags.UNDERWATER_POTTABLE_PLANTS) && blockState.is(Blocks.FLOWER_POT)) {
            cir.cancel();
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 0))
    private boolean odyssey$setFilledFlowerPotAsWaterlogged(Level level, BlockPos blockPos, BlockState blockState, int tickDelay) {
        if (level.getBlockState(blockPos).getValue(WATERLOGGED)) {
            return level.setBlock(blockPos, blockState.setValue(WATERLOGGED, true), 3);
        }
        return level.setBlock(blockPos, blockState, 3);
    }

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 1))
    private boolean odyssey$setEmptyFlowerPotAsWaterlogged(Level level, BlockPos blockPos, BlockState blockState, int tickDelay) {
        if (level.getBlockState(blockPos).getValue(WATERLOGGED)) {
            return level.setBlock(blockPos, Blocks.FLOWER_POT.defaultBlockState().setValue(WATERLOGGED, true), 3);
        }
        return level.setBlock(blockPos, Blocks.FLOWER_POT.defaultBlockState(), 3);
    }
}