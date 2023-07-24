package net.sydokiddo.odyssey.registry.misc;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.sydokiddo.odyssey.Odyssey;

public class ModSoundEvents {

    // List of Sounds:

    // - Item Sounds:

    public static final SoundEvent BUCKET_FILL_FROG = registerSoundEvent("item.bucket.fill_frog");
    public static final SoundEvent BUCKET_EMPTY_FROG = registerSoundEvent("item.bucket.empty_frog");
    public static final SoundEvent BUCKET_FILL_SQUID = registerSoundEvent("item.bucket.fill_squid");
    public static final SoundEvent BUCKET_EMPTY_SQUID = registerSoundEvent("item.bucket.empty_squid");
    public static final SoundEvent BUCKET_FILL_GLOW_SQUID = registerSoundEvent("item.bucket.fill_glow_squid");
    public static final SoundEvent BUCKET_EMPTY_GLOW_SQUID = registerSoundEvent("item.bucket.empty_glow_squid");

    // - Block Sounds:

    public static final SoundEvent ANVIL_REPAIR = registerSoundEvent("block.anvil.repair");
    public static final SoundEvent SMALL_FLOWER_PICK = registerSoundEvent("block.small_flower.pick");
    public static final SoundEvent REDSTONE_LANTERN_POWER_ON = registerSoundEvent("block.redstone_lantern.power_on");
    public static final SoundEvent REDSTONE_LANTERN_POWER_OFF = registerSoundEvent("block.redstone_lantern.power_off");

    // - Entity Sounds:

    public static final SoundEvent ARMOR_STAND_ADD_ARMS = registerSoundEvent("entity.armor_stand.add_arms");
    public static final SoundEvent SLIME_CONVERTED_TO_MAGMA_CUBE = registerSoundEvent("entity.slime.converted_to_magma_cube");
    public static final SoundEvent MAGMA_CUBE_CONVERTED_TO_SLIME = registerSoundEvent("entity.magma_cube.converted_to_slime");
    public static final SoundEvent WAXED_ITEM_FRAME_INTERACT_FAIL = registerSoundEvent("entity.item_frame.waxed_interact_fail");
    public static final SoundEvent WAXED_GLOW_ITEM_FRAME_INTERACT_FAIL = registerSoundEvent("entity.glow_item_frame.waxed_interact_fail");

    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(Odyssey.MOD_ID, name);
        SoundEvent se = SoundEvent.createVariableRangeEvent(id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, se);
    }

    public static void registerSounds() {}
}