package net.sydokiddo.odyssey.registry;

import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.PotionCauldronInteraction;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.items.ModPotions;
import net.sydokiddo.odyssey.registry.misc.ModCreativeModeTabs;
import net.sydokiddo.odyssey.registry.misc.ModLootTableModifiers;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;

public class OdysseyRegistry {

    public static void registerAll() {

        // - Blocks, Items, Etc.

        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModSoundEvents.registerSounds();

        PotionCauldronInteraction.bootstrap();
        ModPotions.registerPotions();

        ModCreativeModeTabs.registerCreativeTabs();
        ModLootTableModifiers.modifyLootTables();

        System.out.println("Registering Content for Odyssey");
    }
}