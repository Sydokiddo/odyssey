package net.sydokiddo.odyssey.misc.config.options;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ItemStackSizeConfig {

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
    public int banner_stack_size = 64;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
    @ConfigEntry.Gui.Tooltip
    public int banner_pattern_stack_size = 64;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
    @ConfigEntry.Gui.Tooltip
    public int armor_stand_stack_size = 64;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
    @ConfigEntry.Gui.Tooltip
    public int decorated_pot_stack_size = 64;
}