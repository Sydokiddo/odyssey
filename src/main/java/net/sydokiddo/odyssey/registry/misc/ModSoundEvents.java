package net.sydokiddo.odyssey.registry.misc;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.sydokiddo.odyssey.Odyssey;

public class ModSoundEvents {

    // List of Sounds:

    // - Block Sounds:

    public static final SoundEvent ANVIL_REPAIR = registerSoundEvent("block.anvil.repair");

    // - Entity Sounds:

    public static final SoundEvent ARMOR_STAND_ADD_ARMS = registerSoundEvent("entity.armor_stand.add_arms");
    public static final SoundEvent SLIME_CONVERTED_TO_MAGMA_CUBE = registerSoundEvent("entity.slime.converted_to_magma_cube");
    public static final SoundEvent MAGMA_CUBE_CONVERTED_TO_SLIME = registerSoundEvent("entity.magma_cube.converted_to_slime");

    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(Odyssey.MOD_ID, name);
        SoundEvent se = SoundEvent.createVariableRangeEvent(id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, se);
    }

    public static void registerSounds() {}
}
