package net.sydokiddo.odyssey.misc.config.options;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class MiscEntitiesConfig {

    @ConfigEntry.Gui.Tooltip
    public boolean armor_stand_arms = true;

    @ConfigEntry.Gui.Tooltip
    public boolean hidden_fire_overlay_with_fire_resistance = true;

    @ConfigEntry.Gui.Tooltip
    public boolean item_frame_interactions = true;

    @ConfigEntry.Gui.Tooltip
    public boolean tnt_defusing = true;

    @ConfigEntry.Gui.Tooltip
    public boolean tnt_knockback = true;

    @ConfigEntry.Gui.Tooltip
    public boolean named_mob_death_messages = true;

    @ConfigEntry.Gui.Tooltip
    public boolean capture_allays_and_vexes_in_books = true;
}