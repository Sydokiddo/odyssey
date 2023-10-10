package net.sydokiddo.odyssey.registry.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.*;

@SuppressWarnings("unused")
public class ModBlocks {

    // Blocks

    public static final Block GUNPOWDER_BLOCK = registerBlock("gunpowder_block",
        new GunpowderBlock(FabricBlockSettings.create().mapColor(MapColor.COLOR_GRAY)
        .sound(SoundType.SAND).instrument(NoteBlockInstrument.SNARE).strength(0.5F).ignitedByLava()));

    public static final Block REFINED_AMETHYST_BLOCK = registerBlock("refined_amethyst_block",
        new AmethystBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).sound(SoundType.AMETHYST)));

    public static final Block SUGAR_CANE_BLOCK = registerBlock("sugar_cane_block",
        new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT)
        .instrument(NoteBlockInstrument.BASS).strength(0.5F).sound(SoundType.GRASS).ignitedByLava()));

    public static final Block REDSTONE_LANTERN = registerBlock("redstone_lantern",
        new RedstoneLanternBlock(FabricBlockSettings.copyOf(Blocks.LANTERN).luminance(RegistryHelpers.blockStateShouldEmitLight(7))));

    public static final Block SUSPICIOUS_RED_SAND = registerBlock("suspicious_red_sand",
        new ModBrushableBlock(Blocks.RED_SAND, FabricBlockSettings.copyOf(Blocks.SUSPICIOUS_SAND)
        .mapColor(MapColor.COLOR_ORANGE), SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND_COMPLETED));

    public static final Block TINTED_GLASS_PANE = registerBlock("tinted_glass_pane",
        new TintedGlassPaneBlock(FabricBlockSettings.copyOf(Blocks.GLASS_PANE).mapColor(MapColor.COLOR_GRAY).noOcclusion()
        .isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never)));

    // region Potion Cauldron

    public static PotionCauldronBlock POTION_CAULDRON_STATE = new PotionCauldronBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON), PotionCauldronInteraction.POTION_CAULDRON_BEHAVIOR);

    public static final Block POTION_CAULDRON = registerBlockWithoutBlockItem("potion_cauldron", POTION_CAULDRON_STATE);

    // endregion

    // region Deepslate Variants

    public static final Block POLISHED_DEEPSLATE_PRESSURE_PLATE = registerBlock("polished_deepslate_pressure_plate", RegistryHelpers.registerStonePressurePlate(MapColor.DEEPSLATE, BlockSetType.STONE));

    public static final Block POLISHED_DEEPSLATE_BUTTON = registerBlock("polished_deepslate_button", RegistryHelpers.registerStoneButton());

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