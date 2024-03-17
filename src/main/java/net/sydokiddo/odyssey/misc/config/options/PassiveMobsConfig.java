package net.sydokiddo.odyssey.misc.config.options;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class PassiveMobsConfig {

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean saddle_removing = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean bucketable_frogs = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean bucketable_squids = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean improved_mount_hud = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean improved_rabbits = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean improved_horses = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean improved_parrots = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean improved_sniffer_stats = true;

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.Tooltip
    public boolean snow_golem_pumpkin_equipping = true;
}