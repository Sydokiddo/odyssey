package net.sydokiddo.odyssey.registry.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sydokiddo.chrysalis.misc.util.helpers.BlockHelper;
import net.sydokiddo.chrysalis.misc.util.helpers.RegistryHelper;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.*;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;

public class ModBlocks {

    // Block Set Types

    public static final BlockSetType POLISHED_DEEPSLATE_TYPE = BlockSetTypeBuilder.copyOf(BlockSetType.STONE)
        .soundGroup(SoundType.POLISHED_DEEPSLATE).register(Odyssey.id("polished_deepslate"));

    public static final BlockSetType TERRACOTTA_TYPE = BlockSetTypeBuilder.copyOf(BlockSetType.STONE)
        .pressurePlateClickOffSound(SoundEvents.EMPTY).pressurePlateClickOnSound(ModSoundEvents.FRAGILE_PRESSURE_PLATE_CLICK_ON)
        .buttonClickOffSound(SoundEvents.EMPTY).buttonClickOnSound(ModSoundEvents.FRAGILE_BUTTON_CLICK_ON)
        .register(Odyssey.id("terracotta"));

    public static final BlockSetType METAL_TYPE = BlockSetTypeBuilder.copyOf(BlockSetType.STONE)
        .soundGroup(SoundType.METAL).buttonClickOffSound(ModSoundEvents.METAL_BUTTON_CLICK_OFF).buttonClickOnSound(ModSoundEvents.METAL_BUTTON_CLICK_ON)
        .register(Odyssey.id("metal"));

    // Blocks

    // region Uncategorized

    public static final Block GUNPOWDER_BLOCK = registerBlock("gunpowder_block",
        new GunpowderBlock(FabricBlockSettings.create().mapColor(MapColor.COLOR_GRAY)
        .sound(SoundType.SAND).instrument(NoteBlockInstrument.SNARE).strength(0.5F).ignitedByLava()));

    public static final Block REFINED_AMETHYST_BLOCK = registerBlock("refined_amethyst_block",
        new AmethystBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).sound(SoundType.AMETHYST)));

    public static final Block SUGAR_CANE_BLOCK = registerBlock("sugar_cane_block",
        new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT)
        .strength(0.5F).sound(SoundType.GRASS).ignitedByLava()));

    public static final Block PAPER_BLOCK = registerBlock("paper_block",
        new PaperBlock(FabricBlockSettings.create().mapColor(MapColor.SNOW)
        .strength(0.5F).sound(ModSoundEvents.PAPER_BLOCK).ignitedByLava()));

    public static final Block REDSTONE_LANTERN = registerBlock("redstone_lantern",
        new RedstoneLanternBlock(FabricBlockSettings.copyOf(Blocks.LANTERN).luminance(BlockHelper.blockStateShouldEmitLight(7))));

    public static final Block GOLDEN_BUTTON = registerBlock("golden_button", RegistryHelper.registerButton(METAL_TYPE, 10));

    public static final Block IRON_BUTTON = registerBlock("iron_button", RegistryHelper.registerButton(METAL_TYPE, 40));

    public static final Block FRAGILE_BUTTON = registerBlock("fragile_button",
        new FragileButtonBlock(TERRACOTTA_TYPE, 20, BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));

    public static final Block FRAGILE_PRESSURE_PLATE = registerBlock("fragile_pressure_plate",
        new FragilePressurePlateBlock(TERRACOTTA_TYPE, BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_RED).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY)));

    public static final Block SUSPICIOUS_RED_SAND = registerBlock("suspicious_red_sand",
        new ModBrushableBlock(Blocks.RED_SAND, SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND_COMPLETED,
        FabricBlockSettings.copyOf(Blocks.SUSPICIOUS_SAND).mapColor(MapColor.COLOR_ORANGE)));

    public static final Block TINTED_GLASS_PANE = registerBlock("tinted_glass_pane",
        new TintedGlassPaneBlock(FabricBlockSettings.copyOf(Blocks.GLASS_PANE).mapColor(MapColor.COLOR_GRAY)));

    // endregion

    // region Potion Cauldron

    public static PotionCauldronBlock POTION_CAULDRON_STATE = new PotionCauldronBlock(Biome.Precipitation.NONE, PotionCauldronInteraction.POTION_CAULDRON_BEHAVIOR, FabricBlockSettings.copyOf(Blocks.CAULDRON));

    public static final Block POTION_CAULDRON = registerBlockWithoutBlockItem("potion_cauldron", POTION_CAULDRON_STATE);

    // endregion

    // region Deepslate Variants

    public static final Block POLISHED_DEEPSLATE_BUTTON = registerBlock("polished_deepslate_button", RegistryHelper.registerButton(POLISHED_DEEPSLATE_TYPE, 20));

    public static final Block POLISHED_DEEPSLATE_PRESSURE_PLATE = registerBlock("polished_deepslate_pressure_plate", RegistryHelper.registerStonePressurePlate(POLISHED_DEEPSLATE_TYPE, MapColor.DEEPSLATE));

    // endregion

    // region Prismarine Variants

    public static final Block PRISMARINE_BRICK_WALL = registerBlock("prismarine_brick_wall",
        new WallBlock(FabricBlockSettings.copyOf(Blocks.PRISMARINE_BRICKS).sound(SoundType.STONE)));

    public static final Block DARK_PRISMARINE_WALL = registerBlock("dark_prismarine_wall",
        new WallBlock(FabricBlockSettings.copyOf(Blocks.DARK_PRISMARINE).sound(SoundType.STONE)));

    // endregion

    // region Polished Granite, Diorite, and Andesite Walls

    public static final Block POLISHED_GRANITE_WALL = registerBlock("polished_granite_wall",
        new WallBlock(FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE).sound(SoundType.STONE)));

    public static final Block POLISHED_DIORITE_WALL = registerBlock("polished_diorite_wall",
        new WallBlock(FabricBlockSettings.copyOf(Blocks.POLISHED_DIORITE).sound(SoundType.STONE)));

    public static final Block POLISHED_ANDESITE_WALL = registerBlock("polished_andesite_wall",
        new WallBlock(FabricBlockSettings.copyOf(Blocks.POLISHED_ANDESITE).sound(SoundType.STONE)));

    // endregion

    // region Stone and Smooth Stone Variants

    public static final Block STONE_WALL = registerBlock("stone_wall",
        new WallBlock(FabricBlockSettings.copyOf(Blocks.STONE).sound(SoundType.STONE)));

    public static final Block SMOOTH_STONE_STAIRS = registerBlock("smooth_stone_stairs",
        new StairBlock(Blocks.SMOOTH_STONE.defaultBlockState(), FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE)));

    public static final Block SMOOTH_STONE_WALL = registerBlock("smooth_stone_wall",
        new WallBlock(FabricBlockSettings.copyOf(Blocks.SMOOTH_STONE).sound(SoundType.STONE)));

    // endregion

    // region Potted Plants

    public static final Block POTTED_SMALL_DRIPLEAF = registerBlockWithoutBlockItem("potted_small_dripleaf",
        new FlowerPotBlock(Blocks.SMALL_DRIPLEAF, FabricBlockSettings.copyOf(Blocks.FLOWER_POT)));

    public static final Block POTTED_SPORE_BLOSSOM = registerBlockWithoutBlockItem("potted_spore_blossom",
        new PottedSporeBlossomBlock(Blocks.SPORE_BLOSSOM, FabricBlockSettings.copyOf(Blocks.FLOWER_POT)));

    public static final Block POTTED_SEA_PICKLE = registerBlockWithoutBlockItem("potted_sea_pickle",
        new FlowerPotBlock(Blocks.SEA_PICKLE, FabricBlockSettings.copyOf(Blocks.FLOWER_POT).luminance(blockState -> SeaPickleBlock.isDead(blockState) ? 0 : 6)));

    public static final Block POTTED_SUGAR_CANE = registerBlockWithoutBlockItem("potted_sugar_cane",
        new FlowerPotBlock(Blocks.SUGAR_CANE, FabricBlockSettings.copyOf(Blocks.FLOWER_POT)));

    public static final Block POTTED_SHORT_GRASS = registerBlockWithoutBlockItem("potted_short_grass",
        new FlowerPotBlock(Blocks.SHORT_GRASS, FabricBlockSettings.copyOf(Blocks.FLOWER_POT)));

    public static final Block POTTED_NETHER_WART = registerBlockWithoutBlockItem("potted_nether_wart",
        new FlowerPotBlock(Blocks.NETHER_WART, FabricBlockSettings.copyOf(Blocks.FLOWER_POT)));

    // endregion

    // Registry

    private static Block registerBlockWithoutBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, Odyssey.id(name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, Odyssey.id(name),
        new BlockItem(block, new FabricItemSettings()));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, Odyssey.id(name), block);
    }

    public static void registerModBlocks() {}
}