package net.sydokiddo.odyssey.mixin.blocks.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SporeBlossomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SporeBlossomBlock.class)
public class SporeBlossomMixin implements BonemealableBlock {

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
            RegistryHelpers.popResourceBelow(serverLevel, blockPos, new ItemStack(Items.SPORE_BLOSSOM), 0.0);
        }
    }
}