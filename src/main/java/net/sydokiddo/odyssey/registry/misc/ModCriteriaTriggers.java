package net.sydokiddo.odyssey.registry.misc;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class ModCriteriaTriggers extends CriteriaTriggers {

    // List of Criteria Triggers:

    public static final PlayerTrigger CURE_POISON_WITH_HONEY = ModCriteriaTriggers.register("odyssey:cure_poison_with_honey", new PlayerTrigger());

    // Registry for Criteria Triggers:

    public static void registerCriteriaTriggers() {}
}