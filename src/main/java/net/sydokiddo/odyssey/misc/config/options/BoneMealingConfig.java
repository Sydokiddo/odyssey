package net.sydokiddo.odyssey.misc.config.options;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class BoneMealingConfig {

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean lily_pad_bone_mealing = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean spore_blossom_bone_mealing = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean small_dripleaf_bone_mealing = true;
}