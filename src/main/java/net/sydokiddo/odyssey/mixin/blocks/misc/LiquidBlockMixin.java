package net.sydokiddo.odyssey.mixin.blocks.misc;

import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(LiquidBlock.class)
public class LiquidBlockMixin {

    @Unique Random random = new Random();

    // Cobblestone generators are replaced with Cobbled Deepslate generators below Y=0

    @ModifyArg(method = "shouldSpreadLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"), index = 1)
    private BlockState odyssey$generateCobbledDeepslate(BlockPos blockPos, BlockState blockState) {
        if (blockState.is(Blocks.COBBLESTONE) && (random.nextInt(1, 9) - blockPos.getY() >= 0) && Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.renewable_deepslate) {
            blockState = Blocks.COBBLED_DEEPSLATE.defaultBlockState();
        }
        return blockState;
    }
}