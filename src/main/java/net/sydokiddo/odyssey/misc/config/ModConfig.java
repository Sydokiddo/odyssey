package net.sydokiddo.odyssey.misc.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.misc.config.options.ItemStackSizeConfig;

@Config(name = Odyssey.MOD_ID)
@Config.Gui.Background("odyssey:textures/block/refined_amethyst_block.png")
public class ModConfig implements ConfigData {

    // region Items

    @ConfigEntry.Gui.CollapsibleObject()
    public Items items = new Items();

    public static class Items {

        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public ItemStackSizeConfig itemStackSizeConfig = new ItemStackSizeConfig();

        @ConfigEntry.Gui.Tooltip
        public boolean more_repairable_items = true;

        @ConfigEntry.Gui.Tooltip
        public boolean silk_touch_and_fortune_on_shears = true;

        @ConfigEntry.Gui.Tooltip
        public boolean mob_bucket_variant_tooltips = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        public boolean allow_mining_fatigue_potions = true;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        public boolean allow_haste_potions = true;
    }

    // endregion

    // region Blocks

    @ConfigEntry.Gui.CollapsibleObject()
    public Blocks blocks = new Blocks();

    public static class Blocks {

        @ConfigEntry.Gui.Tooltip
        public boolean anvil_repairing = true;

        @ConfigEntry.Gui.Tooltip
        public boolean feather_falling_negates_farmland_trampling = true;

        @ConfigEntry.Gui.Tooltip
        public boolean lily_pad_bone_mealing = true;

        @ConfigEntry.Gui.Tooltip
        public boolean spore_blossom_bone_mealing = true;

        @ConfigEntry.Gui.Tooltip
        public boolean small_dripleaf_bone_mealing = true;

        @ConfigEntry.Gui.Tooltip
        public boolean renewable_deepslate = true;

        @ConfigEntry.Gui.Tooltip
        public boolean harder_budding_amethyst = true;

        @ConfigEntry.Gui.Tooltip
        public boolean sniffer_egg_hatch_preventing = true;

        @ConfigEntry.Gui.Tooltip
        public boolean unstable_gunpowder_blocks = true;

        @ConfigEntry.Gui.Tooltip
        public boolean flower_picking = true;

        @ConfigEntry.Gui.Tooltip
        public boolean piston_interactions = true;
    }

    // endregion

    // region Entitites

    @ConfigEntry.Gui.CollapsibleObject()
    public Entities entities = new Entities();

    public static class Entities {

        @ConfigEntry.Gui.Tooltip
        public boolean saddle_removing = true;

        @ConfigEntry.Gui.Tooltip
        public boolean decreased_rabbit_fall_damage = true;

        @ConfigEntry.Gui.Tooltip
        public boolean renewable_elder_guardians = true;

        @ConfigEntry.Gui.Tooltip
        public boolean armor_stand_arms = true;

        @ConfigEntry.Gui.Tooltip
        public boolean vexes_die_with_evokers = true;

        @ConfigEntry.Gui.Tooltip
        public boolean slime_and_magma_cube_converting = true;

        @ConfigEntry.Gui.Tooltip
        public boolean bucketable_frogs = true;

        @ConfigEntry.Gui.Tooltip
        public boolean bucketable_squids = true;

        @ConfigEntry.Gui.Tooltip
        public boolean hidden_fire_overlay_with_fire_resistance = true;

        @ConfigEntry.Gui.Tooltip
        public boolean item_frame_waxing = true;

        @ConfigEntry.Gui.Tooltip
        public boolean item_frame_shearing = true;

        @ConfigEntry.Gui.Tooltip
        public boolean improved_mount_hud = true;
    }

    // endregion
}