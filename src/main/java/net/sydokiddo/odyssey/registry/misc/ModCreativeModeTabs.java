package net.sydokiddo.odyssey.registry.misc;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.items.ModItems;

public class ModCreativeModeTabs {

    @SuppressWarnings("ALL")
    public static void registerCreativeTabs() {

        // Natural Blocks Tab:

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.HAY_BLOCK, ModBlocks.GUNPOWDER_BLOCK);
        });

        // Food and Drinks Tab:

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.addAfter(Items.POISONOUS_POTATO, ModItems.IRON_POTATO, ModItems.ENCHANTED_IRON_POTATO);
        });

        // Ingredients Tab

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.BONE_MEAL, ModItems.WITHER_SKULL_FRAGMENT);
            entries.addAfter(Items.PRISMARINE_CRYSTALS, ModItems.ELDER_GUARDIAN_THORN);
        });
    }
}
