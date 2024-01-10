package net.sydokiddo.odyssey.mixin.blocks.functional_blocks;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.sydokiddo.odyssey.registry.misc.ModNoteBlockInstruments;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Arrays;
import java.util.Locale;

@Mixin(NoteBlockInstrument.class)
public class NoteBlockInstrumentMixin {

    @Mutable @Shadow @Final private static NoteBlockInstrument[] $VALUES;

    @SuppressWarnings("unused")
    private <T> NoteBlockInstrumentMixin(String enumName, int ordinal, String lowerCase, Holder<T> direct, NoteBlockInstrument.Type type) {
        throw new UnsupportedOperationException("Replaced by Mixin");
    }

    @Unique
    private static NoteBlockInstrument createNoteBlockInstrument(String enumName, int ordinal, SoundEvent soundEvent) {
        return (NoteBlockInstrument)(Object) new NoteBlockInstrumentMixin(enumName, ordinal, enumName.toLowerCase(Locale.ROOT), Holder.direct(soundEvent), NoteBlockInstrument.Type.BASE_BLOCK);
    }

    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/state/properties/NoteBlockInstrument;$VALUES:[Lnet/minecraft/world/level/block/state/properties/NoteBlockInstrument;", shift = At.Shift.AFTER))
    private static void odyssey$addNoteBlockInstruments(CallbackInfo info) {
        int ordinal = $VALUES.length;
        $VALUES = Arrays.copyOf($VALUES, ordinal + 4);

        ModNoteBlockInstruments.TRUMPET = $VALUES[ordinal] = createNoteBlockInstrument("trumpet", ordinal, ModSoundEvents.NOTE_BLOCK_TRUMPET);
        ModNoteBlockInstruments.BONGO = $VALUES[ordinal + 1] = createNoteBlockInstrument("bongo", ordinal + 1, ModSoundEvents.NOTE_BLOCK_BONGO);
        ModNoteBlockInstruments.VIOLIN = $VALUES[ordinal + 2] = createNoteBlockInstrument("violin", ordinal + 2, ModSoundEvents.NOTE_BLOCK_VIOLIN);
        ModNoteBlockInstruments.ELECTRIC_GUITAR = $VALUES[ordinal + 3] = createNoteBlockInstrument("electric_guitar", ordinal + 3, ModSoundEvents.NOTE_BLOCK_ELECTRIC_GUITAR);
    }
}