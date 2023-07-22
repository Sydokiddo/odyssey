package net.sydokiddo.odyssey.registry.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.*;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

@SuppressWarnings("ALL")
public class ModBlocks {

    // List of Blocks:

    public static PotionCauldronBlock POTION_CAULDRON_STATE = new PotionCauldronBlock(QuiltBlockSettings.copy(Blocks.CAULDRON), PotionCauldronInteraction.POTION_CAULDRON_BEHAVIOR);

    public static final Block POTION_CAULDRON = registerBlockWithoutBlockItem("potion_cauldron", POTION_CAULDRON_STATE);

    public static final BlockEntityType<PotionCauldronBlockEntity> POTION_CAULDRON_ENTITY = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE, Odyssey.MOD_ID + "potion_cauldron_entity",
        QuiltBlockEntityTypeBuilder.create(PotionCauldronBlockEntity::new, POTION_CAULDRON).build(null));

    public static final Block GUNPOWDER_BLOCK = registerBlock("gunpowder_block",
        new GunpowderBlock(QuiltBlockSettings.of().mapColor(MapColor.COLOR_GRAY)
        .sound(SoundType.SAND).instrument(NoteBlockInstrument.SNARE).strength(0.5f)));

    public static final Block REFINED_AMETHYST_BLOCK = registerBlock("refined_amethyst_block",
        new AmethystBlock(QuiltBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).sound(SoundType.AMETHYST)));

    public static final Block POLISHED_DEEPSLATE_PRESSURE_PLATE = registerBlock("polished_deepslate_pressure_plate",
        new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE)
        .forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
        .noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), BlockSetType.STONE));

    public static final Block POLISHED_DEEPSLATE_BUTTON = registerBlock("polished_deepslate_button", RegistryHelpers.registerStoneButton());

    public static final Block SUSPICIOUS_RED_SAND = registerBlock("suspicious_red_sand",
        new ModBrushableBlock(Blocks.RED_SAND, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.SNARE)
        .strength(0.25F).sound(SoundType.SUSPICIOUS_SAND).pushReaction(PushReaction.DESTROY), SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND_COMPLETED));

    public static final Block PRISMARINE_BRICK_WALL = registerBlock("prismarine_brick_wall",
        new WallBlock(FabricBlockSettings.copyOf(Blocks.PRISMARINE_BRICKS).sound(SoundType.STONE)));

    public static final Block DARK_PRISMARINE_WALL = registerBlock("dark_prismarine_wall",
        new WallBlock(FabricBlockSettings.copyOf(Blocks.DARK_PRISMARINE).sound(SoundType.STONE)));

    public static final Block REDSTONE_LANTERN = registerBlock("redstone_lantern",
        new RedstoneLanternBlock(FabricBlockSettings.copyOf(Blocks.LANTERN).lightLevel(RegistryHelpers.blockStateShouldEmitLight(7))));

    // Registry for Blocks:

    private static Block registerBlockWithoutBlockItem(String string, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Odyssey.MOD_ID, string), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Odyssey.MOD_ID, name),
        new BlockItem(block, new QuiltItemSettings()));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Odyssey.MOD_ID, name), block);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Odyssey.MOD_ID, name), item);
    }

    public static void registerModBlocks() {}
}
