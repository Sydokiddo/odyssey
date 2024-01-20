package net.sydokiddo.odyssey.misc.config.options;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class NoteBlockConfig {

    @ConfigEntry.Gui.Tooltip
    public boolean note_block_waxing = true;

    @ConfigEntry.Gui.Tooltip
    public boolean note_block_muffling = true;

    @ConfigEntry.Gui.Tooltip
    public boolean note_block_sensitivity = true;

    @ConfigEntry.Gui.Tooltip
    public boolean allow_placing_decorations_on_note_blocks = true;

    @ConfigEntry.Gui.Tooltip
    public boolean note_block_gui_rendering = true;
}