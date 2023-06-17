package net.sydokiddo.odyssey.misc.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "odyssey")
@Config.Gui.Background("odyssey:textures/block/refined_amethyst_block.png")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject()
    public ItemChanges itemChanges = new ItemChanges();

    public static class ItemChanges {

        @ConfigEntry.Gui.Tooltip
        public boolean anvil_renaming_only_costs_one_xp = true;

        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        @ConfigEntry.Gui.Tooltip
        public int sign_stack_size = 64;

        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        @ConfigEntry.Gui.Tooltip
        public int boat_stack_size = 64;

        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        @ConfigEntry.Gui.Tooltip
        public int minecart_stack_size = 64;

        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        @ConfigEntry.Gui.Tooltip
        public int banner_pattern_stack_size = 64;
    }

    @ConfigEntry.Gui.CollapsibleObject()
    public BlockChanges blockChanges = new BlockChanges();

    public static class BlockChanges {

        @ConfigEntry.Gui.Tooltip
        public boolean anvil_repairing = true;

        @ConfigEntry.Gui.Tooltip
        public boolean feather_falling_negates_farmland_trampling = true;

        @ConfigEntry.Gui.Tooltip
        public boolean lily_pad_bone_mealing = true;

        @ConfigEntry.Gui.Tooltip
        public boolean spore_blossom_bone_mealing = true;

        @ConfigEntry.Gui.Tooltip
        public boolean renewable_deepslate = true;

        @ConfigEntry.Gui.Tooltip
        public boolean harder_budding_amethyst = true;

        @ConfigEntry.Gui.Tooltip
        public boolean sniffer_egg_hatch_preventing = true;
    }

    @ConfigEntry.Gui.CollapsibleObject()
    public EntityChanges entityChanges = new EntityChanges();

    public static class EntityChanges {

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
    }
}