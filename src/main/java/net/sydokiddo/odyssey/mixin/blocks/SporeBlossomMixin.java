package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SporeBlossomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import java.util.function.Supplier;

@Mixin(SporeBlossomBlock.class)
public class SporeBlossomMixin implements BonemealableBlock {

    // region Bone-Mealing

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return Odyssey.getConfig().blocks.boneMealingConfig.spore_blossom_bone_mealing;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return Odyssey.getConfig().blocks.boneMealingConfig.spore_blossom_bone_mealing;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        if (Odyssey.getConfig().blocks.boneMealingConfig.spore_blossom_bone_mealing) {
            popResourceBelow(serverLevel, blockPos, new ItemStack(Items.SPORE_BLOSSOM));
        }
    }

    @Unique
    private static void popResourceBelow(Level level, BlockPos blockPos, ItemStack itemStack) {

        double itemHeight = (double)EntityType.ITEM.getHeight() / 2.0D;
        double x = (double)blockPos.getX() + 0.5D + Mth.nextDouble(level.getRandom(), -0.25D, 0.25D);
        double y = (double)blockPos.getY() - 0.25D + Mth.nextDouble(level.getRandom(), -0.25D, 0.25D) - itemHeight;
        double z = (double)blockPos.getZ() + 0.5D + Mth.nextDouble(level.getRandom(), -0.25D, 0.25D);

        popResourceBelow(level, () -> new ItemEntity(level, x, y, z, itemStack), itemStack);
    }

    @Unique
    private static void popResourceBelow(Level level, Supplier<ItemEntity> supplier, ItemStack itemStack) {
        if (!level.isClientSide() && !itemStack.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            ItemEntity itemEntity = supplier.get();
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }

    // endregion
}