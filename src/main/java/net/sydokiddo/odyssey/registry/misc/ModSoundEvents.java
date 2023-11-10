package net.sydokiddo.odyssey.registry.misc;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.sydokiddo.odyssey.Odyssey;

public class ModSoundEvents {

    // region Block Sounds

    public static final SoundEvent ANVIL_REPAIR = registerSoundEvent("block.anvil.repair");

    public static final SoundEvent SMALL_FLOWER_PICK = registerSoundEvent("block.small_flower.pick");

    public static final SoundEvent REDSTONE_LANTERN_POWER_ON = registerSoundEvent("block.redstone_lantern.power_on");
    public static final SoundEvent REDSTONE_LANTERN_POWER_OFF = registerSoundEvent("block.redstone_lantern.power_off");

    public static final SoundEvent CAULDRON_TIP_ARROW = registerSoundEvent("block.cauldron.tip_arrow");
    public static final SoundEvent CAULDRON_POISON_POTATO = registerSoundEvent("block.cauldron.poison_potato");
    public static final SoundEvent CAULDRON_POTION_DISSIPATE = registerSoundEvent("block.cauldron.potion_dissipate");

    public static final SoundEvent PISTON_APPLY_SLIMEBALL = registerSoundEvent("block.piston.apply_slimeball");
    public static final SoundEvent PISTON_REMOVE_SLIMEBALL = registerSoundEvent("block.piston.remove_slimeball");

    public static final SoundEvent METAL_BUTTON_CLICK_OFF = registerSoundEvent("block.metal_button.click_off");
    public static final SoundEvent METAL_BUTTON_CLICK_ON = registerSoundEvent("block.metal_button.click_on");

    public static final SoundEvent FRAGILE_BUTTON_BREAK = registerSoundEvent("block.fragile_button.break");
    public static final SoundEvent FRAGILE_BUTTON_STEP = registerSoundEvent("block.fragile_button.step");
    public static final SoundEvent FRAGILE_BUTTON_PLACE = registerSoundEvent("block.fragile_button.place");
    public static final SoundEvent FRAGILE_BUTTON_HIT = registerSoundEvent("block.fragile_button.hit");
    public static final SoundEvent FRAGILE_BUTTON_FALL = registerSoundEvent("block.fragile_button.fall");
    public static final SoundEvent FRAGILE_BUTTON_SHATTER = registerSoundEvent("block.fragile_button.shatter");
    public static final SoundEvent FRAGILE_BUTTON_CLICK_ON = registerSoundEvent("block.fragile_button.click_on");

    public static final SoundEvent FRAGILE_PRESSURE_PLATE_BREAK = registerSoundEvent("block.fragile_pressure_plate.break");
    public static final SoundEvent FRAGILE_PRESSURE_PLATE_STEP = registerSoundEvent("block.fragile_pressure_plate.step");
    public static final SoundEvent FRAGILE_PRESSURE_PLATE_PLACE = registerSoundEvent("block.fragile_pressure_plate.place");
    public static final SoundEvent FRAGILE_PRESSURE_PLATE_HIT = registerSoundEvent("block.fragile_pressure_plate.hit");
    public static final SoundEvent FRAGILE_PRESSURE_PLATE_FALL = registerSoundEvent("block.fragile_pressure_plate.fall");
    public static final SoundEvent FRAGILE_PRESSURE_PLATE_SHATTER = registerSoundEvent("block.fragile_pressure_plate.shatter");
    public static final SoundEvent FRAGILE_PRESSURE_PLATE_CLICK_ON = registerSoundEvent("block.fragile_pressure_plate.click_on");

    // endregion

    // region Item Sounds

    public static final SoundEvent ENVIRONMENT_DETECTOR_USE = registerSoundEvent("item.environment_detector.use");

    public static final SoundEvent BUCKET_FILL_FROG = registerSoundEvent("item.bucket.fill_frog");
    public static final SoundEvent BUCKET_EMPTY_FROG = registerSoundEvent("item.bucket.empty_frog");

    public static final SoundEvent BUCKET_FILL_SQUID = registerSoundEvent("item.bucket.fill_squid");
    public static final SoundEvent BUCKET_EMPTY_SQUID = registerSoundEvent("item.bucket.empty_squid");

    public static final SoundEvent BUCKET_FILL_GLOW_SQUID = registerSoundEvent("item.bucket.fill_glow_squid");
    public static final SoundEvent BUCKET_EMPTY_GLOW_SQUID = registerSoundEvent("item.bucket.empty_glow_squid");

    public static final SoundEvent SADDLE_UNEQUIP = registerSoundEvent("item.saddle.unequip");

    public static final SoundEvent SHOVEL_DIG_SNOW = registerSoundEvent("item.shovel.dig_snow");

    // endregion

    // region Entity Sounds

    public static final SoundEvent ARMOR_STAND_ADD_ARMS = registerSoundEvent("entity.armor_stand.add_arms");

    public static final SoundEvent SLIME_CONVERTED_TO_MAGMA_CUBE = registerSoundEvent("entity.slime.converted_to_magma_cube");
    public static final SoundEvent MAGMA_CUBE_CONVERTED_TO_SLIME = registerSoundEvent("entity.magma_cube.converted_to_slime");

    public static final SoundEvent ITEM_FRAME_SHEAR = registerSoundEvent("entity.item_frame.shear");
    public static final SoundEvent WAXED_ITEM_FRAME_INTERACT_FAIL = registerSoundEvent("entity.item_frame.waxed_interact_fail");
    public static final SoundEvent WAXED_GLOW_ITEM_FRAME_INTERACT_FAIL = registerSoundEvent("entity.glow_item_frame.waxed_interact_fail");

    // endregion

    // region Block Sound Groups

    // endregion

    public static final SoundType FRAGILE_BUTTON = new SoundType(1F, 1F,
        ModSoundEvents.FRAGILE_BUTTON_BREAK, ModSoundEvents.FRAGILE_BUTTON_STEP, ModSoundEvents.FRAGILE_BUTTON_PLACE,
        ModSoundEvents.FRAGILE_BUTTON_HIT, ModSoundEvents.FRAGILE_BUTTON_FALL);

    public static final SoundType FRAGILE_BUTTON_CRACKED = new SoundType(1F, 1F,
        ModSoundEvents.FRAGILE_BUTTON_SHATTER, ModSoundEvents.FRAGILE_BUTTON_STEP, ModSoundEvents.FRAGILE_BUTTON_PLACE,
        ModSoundEvents.FRAGILE_BUTTON_HIT, ModSoundEvents.FRAGILE_BUTTON_FALL);

    public static final SoundType FRAGILE_PRESSURE_PLATE = new SoundType(1F, 1F,
        ModSoundEvents.FRAGILE_PRESSURE_PLATE_BREAK, ModSoundEvents.FRAGILE_PRESSURE_PLATE_STEP, ModSoundEvents.FRAGILE_PRESSURE_PLATE_PLACE,
        ModSoundEvents.FRAGILE_PRESSURE_PLATE_HIT, ModSoundEvents.FRAGILE_PRESSURE_PLATE_FALL);

    public static final SoundType FRAGILE_PRESSURE_PLATE_CRACKED = new SoundType(1F, 1F,
        ModSoundEvents.FRAGILE_PRESSURE_PLATE_SHATTER, ModSoundEvents.FRAGILE_PRESSURE_PLATE_STEP, ModSoundEvents.FRAGILE_PRESSURE_PLATE_PLACE,
        ModSoundEvents.FRAGILE_PRESSURE_PLATE_HIT, ModSoundEvents.FRAGILE_PRESSURE_PLATE_FALL);

    // Registry

    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(Odyssey.MOD_ID, name);
        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(resourceLocation);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, soundEvent);
    }

    public static void registerSounds() {}
}