package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour {

    private BlockMixin(Properties properties) {
        super(properties);
    }

    // Changes the sound groups of various blocks

    @Inject(at = @At("HEAD"), method = "getSoundType", cancellable = true)
    private void odyssey$changeBlockSounds(BlockState blockState, CallbackInfoReturnable<SoundType> info) {
        if (blockState.is(ModBlocks.POLISHED_DEEPSLATE_PRESSURE_PLATE) || blockState.is(ModBlocks.POLISHED_DEEPSLATE_BUTTON)) {
            info.setReturnValue(SoundType.POLISHED_DEEPSLATE);
        }
    }
}