package net.sydokiddo.odyssey.misc.config.options;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class HostileMobsConfig {

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean renewable_elder_guardians = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean vexes_die_with_evokers = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean slime_and_magma_cube_converting = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean more_heads_from_charged_creepers = true;
}