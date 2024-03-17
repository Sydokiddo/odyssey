package net.sydokiddo.odyssey.misc.config.options;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class QualityOfLifeBlockConfig {

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean anvil_repairing = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean feather_falling_negates_farmland_trampling = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean renewable_deepslate = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean harder_budding_amethyst = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean flower_picking = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean snow_layer_right_clicking = true;

    @ConfigEntry.Gui.RequiresRestart()
    @ConfigEntry.Gui.Tooltip
    public boolean piston_interactions = true;

    @ConfigEntry.Gui.RequiresRestart()
    @ConfigEntry.Gui.Tooltip
    public boolean cracked_and_mossy_block_interactions = true;

    @ConfigEntry.Gui.RequiresRestart()
    @ConfigEntry.Gui.Tooltip
    public boolean axe_dispenser_functionality = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.CollapsibleObject()
    @ConfigEntry.Gui.Tooltip
    public NoteBlockConfig noteBlockConfig = new NoteBlockConfig();
}