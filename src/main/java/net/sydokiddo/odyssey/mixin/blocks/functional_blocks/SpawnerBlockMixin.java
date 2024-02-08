package net.sydokiddo.odyssey.mixin.blocks.functional_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
@Mixin(SpawnerBlock.class)
public abstract class SpawnerBlockMixin extends BaseEntityBlock implements SimpleWaterloggedBlock {

    @Unique private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private SpawnerBlockMixin(Properties properties) {
        super(properties);
    }

    // region Block State Initialization

    @Inject(method = "<init>", at = @At("RETURN"))
    private void odyssey$registerSpawnerDefaultBlockStates(Properties properties, CallbackInfo info) {
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

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    // endregion

    @Inject(method = "spawnAfterBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SpawnerBlock;popExperience(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;I)V"), cancellable = true)
    private void odyssey$preventSpawnerXPFromSilkTouch(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean brokenByPlayer, CallbackInfo info) {
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) > 0) info.cancel();
    }

    @Inject(method = "appendHoverText", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Spawner;appendHoverText(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;Ljava/lang/String;)V"), cancellable = true)
    private void odyssey$hideSpawnerTooltipInSurvival(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo info) {
        if (!tooltipFlag.isCreative()) info.cancel();
    }
}