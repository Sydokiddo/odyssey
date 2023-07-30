package net.sydokiddo.odyssey.registry.misc.technical;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;
import net.sydokiddo.odyssey.Odyssey;

public class ModCriteriaTriggers extends CriteriaTriggers {

    // List of Criteria Triggers:

    public static final PlayerTrigger DUPLICATE_ALLAY = ModCriteriaTriggers.register(new PlayerTrigger(new ResourceLocation(Odyssey.MOD_ID, "duplicate_allay")));

    // Registry for Criteria Triggers:

    public static void registerCriteriaTriggers() {}
}