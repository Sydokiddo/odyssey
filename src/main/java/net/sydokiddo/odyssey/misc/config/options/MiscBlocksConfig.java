package net.sydokiddo.odyssey.misc.config.options;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class MiscBlocksConfig {

    @ConfigEntry.Gui.Tooltip
    public boolean sniffer_egg_hatch_preventing = true;

    @ConfigEntry.Gui.Tooltip
    public boolean unstable_gunpowder_blocks = true;
}