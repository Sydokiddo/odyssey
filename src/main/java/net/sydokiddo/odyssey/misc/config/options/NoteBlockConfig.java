package net.sydokiddo.odyssey.misc.config.options;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class NoteBlockConfig {

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean note_block_waxing = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean note_block_muffling = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean note_block_sensitivity = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean note_block_gui_rendering = true;
}