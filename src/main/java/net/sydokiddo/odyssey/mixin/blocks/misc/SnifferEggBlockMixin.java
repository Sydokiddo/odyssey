package net.sydokiddo.odyssey.mixin.blocks.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnifferEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnifferEggBlock.class)
public class SnifferEggBlockMixin extends Block {

    private SnifferEggBlockMixin(Properties properties) {
        super(properties);
    }

    // Placing Sniffer Eggs on Magma Blocks will prevent them from hatching

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void odyssey$preventSnifferEggTicking(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo info) {
        if (preventsHatching(serverLevel, blockPos)) info.cancel();
    }

    @Inject(at = @At("RETURN"), method = "onPlace")
    private void odyssey$displayParticlesWhenSnifferEggCannotHatch(BlockState blockState, Level level, BlockPos blockPos, BlockState adjacentBlockState, boolean bl, CallbackInfo info) {
        if (!level.isClientSide() && preventsHatching(level, blockPos)) level.levelEvent(1501, blockPos, 0);
    }

    @Unique
    private static boolean preventsHatching(BlockGetter blockGetter, BlockPos blockPos) {
        return blockGetter.getBlockState(blockPos.below()).is(ModTags.PREVENTS_SNIFFER_EGG_HATCHING) && Odyssey.getConfig().blocks.miscBlocksConfig.sniffer_egg_hatch_preventing;
    }
}