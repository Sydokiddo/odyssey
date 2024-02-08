package net.sydokiddo.odyssey.mixin.blocks.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.GunpowderBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireBlock.class)
public class FireBlockMixin {

    // Explodes Gunpowder Blocks when they catch on fire

    @Inject(method = "checkBurnOut", at = @At("HEAD"))
    private void odyssey$explodeGunpowderBlockWhenOnFire(Level level, BlockPos blockPos, int i, RandomSource randomSource, int j, CallbackInfo info) {
        if (level.getBlockState(blockPos).getBlock() instanceof GunpowderBlock) GunpowderBlock.explode(level, blockPos);
    }
}