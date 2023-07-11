package net.sydokiddo.odyssey.registry.misc;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.items.ModItems;

public class ModCreativeModeTabs {

    @SuppressWarnings("ALL")
    public static void registerCreativeTabs() {

        // Building Blocks Tab:

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.addAfter(Items.AMETHYST_BLOCK, ModBlocks.REFINED_AMETHYST_BLOCK);
            entries.addAfter(Items.POLISHED_DEEPSLATE_WALL, ModBlocks.POLISHED_DEEPSLATE_PRESSURE_PLATE, ModBlocks.POLISHED_DEEPSLATE_BUTTON);
        });

        // Redstone Blocks Tab:

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> {
            entries.addAfter(Items.AMETHYST_BLOCK, ModBlocks.REFINED_AMETHYST_BLOCK);
        });

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

        // Tools and Utilities Tab:

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.addAfter(Items.TADPOLE_BUCKET, ModItems.FROG_BUCKET);
        });
    }
}
