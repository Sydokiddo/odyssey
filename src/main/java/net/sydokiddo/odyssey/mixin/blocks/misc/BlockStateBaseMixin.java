package net.sydokiddo.odyssey.mixin.blocks.misc;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.sydokiddo.odyssey.registry.misc.ModNoteBlockInstruments;
import net.sydokiddo.odyssey.registry.misc.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {

    @Shadow public abstract boolean is(TagKey<Block> tagKey);

    @Inject(at = @At("HEAD"), method = "instrument", cancellable = true)
    private void odyssey$setNoteBlockInstruments(CallbackInfoReturnable<NoteBlockInstrument> cir) {
        if (this.is(ModTags.TRUMPET_NOTE_BLOCK_INSTRUMENT)) {
            cir.setReturnValue(ModNoteBlockInstruments.TRUMPET);
        }
        if (this.is(ModTags.BONGO_NOTE_BLOCK_INSTRUMENT)) {
            cir.setReturnValue(ModNoteBlockInstruments.BONGO);
        }
        if (this.is(ModTags.VIOLIN_NOTE_BLOCK_INSTRUMENT)) {
            cir.setReturnValue(ModNoteBlockInstruments.VIOLIN);
        }
    }
}