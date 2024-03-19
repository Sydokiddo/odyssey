package net.sydokiddo.odyssey.registry.misc;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.PlayerTrigger;

public class ModCriteriaTriggers extends CriteriaTriggers {

    // Criteria Triggers

    public static final PlayerTrigger REPAIR_CRACKED_BLOCK = net.sydokiddo.chrysalis.registry.misc.ChrysalisCriteriaTriggers.register("odyssey:repair_cracked_block", new PlayerTrigger());
    public static final PlayerTrigger SCRAPE_MOSSY_BLOCK = net.sydokiddo.chrysalis.registry.misc.ChrysalisCriteriaTriggers.register("odyssey:scrape_mossy_block", new PlayerTrigger());

    // Registry

    public static void registerCriteriaTriggers() {}
}