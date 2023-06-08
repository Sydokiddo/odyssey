package net.sydokiddo.odyssey.mixin.blocks;

import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(LiquidBlock.class)
public class LiquidBlockMixin {

    Random random = new Random();

    // Cobblestone generators are replaced with Cobbled Deepslate generators below Y=0

    @ModifyArg(method = "shouldSpreadLiquid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"), index = 1)
    private BlockState odyssey_generateCobbledDeepslate(BlockPos pos, BlockState state) {

        int y_level = pos.getY();

        if (state.is(Blocks.COBBLESTONE) && (random.nextInt(1, 9) - y_level >= 0) && Odyssey.getConfig().blockChanges.renewable_deepslate) {
            state = Blocks.COBBLED_DEEPSLATE.defaultBlockState();
        }
        return state;
    }
}