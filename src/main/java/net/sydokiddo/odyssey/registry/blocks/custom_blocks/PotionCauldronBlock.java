package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.registry.entities.block_entities.PotionCauldronBlockEntity;
import net.sydokiddo.odyssey.registry.entities.registry.ModEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

public class PotionCauldronBlock extends LayeredCauldronBlock implements EntityBlock {

    public Potion potion;

    public PotionCauldronBlock(Properties properties, Map<Item, CauldronInteraction> map) {
        super(properties, predicate -> predicate == Biome.Precipitation.NONE, map);
    }

    // region Initialization

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new PotionCauldronBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ItemStack(Blocks.CAULDRON);
    }

    public void setPotion(Potion potion) {
        this.potion = potion;
    }

    // endregion

    // region Block Entity Ticking

    @SuppressWarnings("unchecked")
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityType<E> blockEntityType2, BlockEntityTicker<? super E> blockEntityTicker) {
        return blockEntityType2 == blockEntityType ? (BlockEntityTicker<A>) blockEntityTicker : null;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return PotionCauldronBlock.createTickerHelper(blockEntityType, ModEntities.POTION_CAULDRON, (level1, blockPos, blockState1, potionCauldronBlockEntity) -> PotionCauldronBlockEntity.particleTick(level1, blockPos, potionCauldronBlockEntity));
        }
        return null;
    }

    // endregion
}