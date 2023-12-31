package net.sydokiddo.odyssey.registry.misc;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.items.ModItems;

public class ModCreativeModeTabs {

    // Creative Mode Tab Registry:

    private static final ResourceKey<CreativeModeTab> ODYSSEY_CREATIVE_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Odyssey.id(Odyssey.MOD_ID));

    @SuppressWarnings("all")
    public static void registerCreativeTabs() {

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ODYSSEY_CREATIVE_TAB, FabricItemGroup.builder()
            .title(Component.translatable("itemGroup.odyssey"))
            .icon(() -> new ItemStack(ModBlocks.REFINED_AMETHYST_BLOCK))
            .build()
        );

        // region Odyssey Tab

        ItemGroupEvents.modifyEntriesEvent(ODYSSEY_CREATIVE_TAB).register(content -> {

            content.accept(ModItems.ELDER_GUARDIAN_THORN);

            content.addAfter(ModItems.ELDER_GUARDIAN_THORN, ModItems.WITHER_SKULL_FRAGMENT, ModItems.PATINA, ModItems.IRON_POTATO, ModItems.ENCHANTED_IRON_POTATO,
            ModItems.SQUID_BUCKET, ModItems.GLOW_SQUID_BUCKET, ModItems.FROG_BUCKET, ModItems.ALLAY_BOUND_BOOK, ModItems.VEX_BOUND_BOOK, ModItems.ENVIRONMENT_DETECTOR,
            ModBlocks.GUNPOWDER_BLOCK, ModBlocks.REFINED_AMETHYST_BLOCK, ModBlocks.SUGAR_CANE_BLOCK, ModBlocks.SUSPICIOUS_RED_SAND, ModBlocks.GOLDEN_BUTTON, ModBlocks.IRON_BUTTON,
            ModBlocks.FRAGILE_BUTTON, ModBlocks.FRAGILE_PRESSURE_PLATE, ModBlocks.POLISHED_DEEPSLATE_BUTTON, ModBlocks.POLISHED_DEEPSLATE_PRESSURE_PLATE, ModBlocks.REDSTONE_LANTERN,
            ModBlocks.TINTED_GLASS_PANE, ModBlocks.STONE_WALL, ModBlocks.SMOOTH_STONE_STAIRS, ModBlocks.SMOOTH_STONE_WALL, ModBlocks.POLISHED_GRANITE_WALL, ModBlocks.POLISHED_DIORITE_WALL, ModBlocks.POLISHED_ANDESITE_WALL,
            ModBlocks.PRISMARINE_BRICK_WALL, ModBlocks.DARK_PRISMARINE_WALL, Items.FLOWER_POT, Items.SMALL_DRIPLEAF, Items.SPORE_BLOSSOM, Items.SEA_PICKLE, Items.SUGAR_CANE, Items.NETHER_WART);
        });

        // endregion

        // region Building Blocks Tab

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.addAfter(Items.STONE_SLAB, ModBlocks.STONE_WALL);
            entries.addBefore(Items.SMOOTH_STONE_SLAB, ModBlocks.SMOOTH_STONE_STAIRS);
            entries.addAfter(Items.SMOOTH_STONE_SLAB, ModBlocks.SMOOTH_STONE_WALL);
            entries.addAfter(Items.POLISHED_GRANITE_SLAB, ModBlocks.POLISHED_GRANITE_WALL);
            entries.addAfter(Items.POLISHED_DIORITE_SLAB, ModBlocks.POLISHED_DIORITE_WALL);
            entries.addAfter(Items.POLISHED_ANDESITE_SLAB, ModBlocks.POLISHED_ANDESITE_WALL);
            entries.addAfter(Items.AMETHYST_BLOCK, ModBlocks.REFINED_AMETHYST_BLOCK);
            entries.addAfter(Items.POLISHED_DEEPSLATE_WALL, ModBlocks.POLISHED_DEEPSLATE_PRESSURE_PLATE, ModBlocks.POLISHED_DEEPSLATE_BUTTON);
            entries.addAfter(Items.PRISMARINE_BRICK_SLAB, ModBlocks.PRISMARINE_BRICK_WALL);
            entries.addAfter(Items.DARK_PRISMARINE_SLAB, ModBlocks.DARK_PRISMARINE_WALL);
            entries.addAfter(Items.HEAVY_WEIGHTED_PRESSURE_PLATE, ModBlocks.IRON_BUTTON);
            entries.addAfter(Items.LIGHT_WEIGHTED_PRESSURE_PLATE, ModBlocks.GOLDEN_BUTTON);
        });

        // endregion

        // region Colored Blocks Tab

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS).register(entries -> {
            entries.addAfter(Items.GLASS_PANE, ModBlocks.TINTED_GLASS_PANE);
        });

        // endregion

        // region Natural Blocks Tab:

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.HAY_BLOCK, ModBlocks.GUNPOWDER_BLOCK);
            entries.addAfter(Items.SUGAR_CANE, ModBlocks.SUGAR_CANE_BLOCK);
        });

        // endregion

        // region Functional Blocks Tab

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.SUSPICIOUS_SAND, ModBlocks.SUSPICIOUS_RED_SAND);
            entries.addAfter(Items.SOUL_LANTERN, ModBlocks.REDSTONE_LANTERN);
            entries.addAfter(Items.TINTED_GLASS, ModBlocks.TINTED_GLASS_PANE);
        });

        // endregion

        // region Redstone Blocks Tab

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> {
            entries.addAfter(Items.AMETHYST_BLOCK, ModBlocks.REFINED_AMETHYST_BLOCK);
            entries.addAfter(Items.REDSTONE_TORCH, ModBlocks.REDSTONE_LANTERN);
            entries.addAfter(Items.STONE_BUTTON, ModBlocks.FRAGILE_BUTTON, ModBlocks.GOLDEN_BUTTON, ModBlocks.IRON_BUTTON);
            entries.addAfter(Items.STONE_PRESSURE_PLATE, ModBlocks.FRAGILE_PRESSURE_PLATE);
        });

        // endregion

        // region Tools and Utilities Tab

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.addAfter(Items.PUFFERFISH_BUCKET, ModItems.SQUID_BUCKET, ModItems.GLOW_SQUID_BUCKET);
            entries.addAfter(Items.TADPOLE_BUCKET, ModItems.FROG_BUCKET);
            entries.addAfter(Items.CLOCK, ModItems.ENVIRONMENT_DETECTOR);
            entries.addAfter(Items.WRITABLE_BOOK, Items.BOOK, ModItems.ALLAY_BOUND_BOOK, ModItems.VEX_BOUND_BOOK);
        });

        // endregion

        // region Combat Tab

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.addAfter(Items.TNT, ModBlocks.GUNPOWDER_BLOCK);
            entries.addAfter(Items.TOTEM_OF_UNDYING, ModItems.VEX_BOUND_BOOK);
        });

        // endregion

        // region Food and Drinks Tab

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.addAfter(Items.POISONOUS_POTATO, ModItems.IRON_POTATO, ModItems.ENCHANTED_IRON_POTATO);
        });

        // endregion

        // region Ingredients Tab

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.BONE_MEAL, ModItems.WITHER_SKULL_FRAGMENT);
            entries.addAfter(Items.PRISMARINE_CRYSTALS, ModItems.ELDER_GUARDIAN_THORN);
            entries.addBefore(Items.HONEYCOMB, ModItems.PATINA);
        });

        // endregion
    }
}