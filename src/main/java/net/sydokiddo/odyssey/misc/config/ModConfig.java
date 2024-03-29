package net.sydokiddo.odyssey.misc.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.misc.config.options.*;

@Config(name = Odyssey.MOD_ID)
@Config.Gui.Background("odyssey:textures/block/refined_amethyst_block.png")
public class ModConfig implements ConfigData {

    // region Items

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.CollapsibleObject()
    public Items items = new Items();

    public static class Items {

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public ItemStackSizeConfig itemStackSizeConfig = new ItemStackSizeConfig();

        @ConfigEntry.Gui.RequiresRestart()
        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public PotionConfig potionConfig = new PotionConfig();

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public TooltipConfig tooltipConfig = new TooltipConfig();

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.Tooltip
        public boolean more_repairable_items = true;

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.Tooltip
        public boolean silk_touch_and_fortune_on_shears = true;

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.Tooltip
        public boolean improved_turtle_helmets = true;

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.Tooltip
        public boolean improved_riptide = true;

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.Tooltip
        public float patina_drop_chance = 0.25F;

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.Tooltip
        public boolean compass_gui_rendering = true;

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.Tooltip
        public boolean equipable_banners = true;
    }

    // endregion

    // region Blocks

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.CollapsibleObject()
    public Blocks blocks = new Blocks();

    public static class Blocks {

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public QualityOfLifeBlockConfig qualityOfLifeBlockConfig = new QualityOfLifeBlockConfig();

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public BoneMealingConfig boneMealingConfig = new BoneMealingConfig();

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public MiscBlocksConfig miscBlocksConfig = new MiscBlocksConfig();
    }

    // endregion

    // region Entitites

    @ConfigEntry.Gui.RequiresRestart(value = false)
    @ConfigEntry.Gui.CollapsibleObject()
    public Entities entities = new Entities();

    public static class Entities {

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public PassiveMobsConfig passiveMobsConfig = new PassiveMobsConfig();

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public HostileMobsConfig hostileMobsConfig = new HostileMobsConfig();

        @ConfigEntry.Gui.RequiresRestart(value = false)
        @ConfigEntry.Gui.CollapsibleObject()
        @ConfigEntry.Gui.Tooltip
        public MiscEntitiesConfig miscEntitiesConfig = new MiscEntitiesConfig();
    }

    // endregion
}