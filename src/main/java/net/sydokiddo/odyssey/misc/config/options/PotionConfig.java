package net.sydokiddo.odyssey.misc.config.options;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class PotionConfig {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart()
    public boolean allow_mining_fatigue_potions = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.RequiresRestart()
    public boolean allow_haste_potions = true;
}