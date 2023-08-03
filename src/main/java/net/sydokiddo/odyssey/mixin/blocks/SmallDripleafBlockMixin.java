package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.SmallDripleafBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmallDripleafBlock.class)
public class SmallDripleafBlockMixin {

    // Small Dripleaves can be duplicated by bone-mealing them

    @Inject(method = "performBonemeal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BigDripleafBlock;placeWithRandomHeight(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)V"))
    private void odyssey_dropSmallDripleafOnBoneMeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (Odyssey.getConfig().blocks.small_dripleaf_bone_mealing) {
            for (int i = 0; i < 2; ++i) {
                SmallDripleafBlock.popResource(serverLevel, blockPos, new ItemStack(Items.SMALL_DRIPLEAF));
            }
        }
    }
}