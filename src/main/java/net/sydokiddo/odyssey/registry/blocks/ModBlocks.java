package net.sydokiddo.odyssey.registry.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.GunpowderBlock;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.PotionCauldronBlock;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.PotionCauldronBlockEntity;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.PotionCauldronInteraction;

@SuppressWarnings("ALL")
public class ModBlocks {

    // List of Blocks:

    public static PotionCauldronBlock POTION_CAULDRON_STATE = new PotionCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON), PotionCauldronInteraction.POTION_CAULDRON_BEHAVIOR);

    public static final Block POTION_CAULDRON = registerBlockWithoutBlockItem("potion_cauldron", POTION_CAULDRON_STATE);

    public static final BlockEntityType<PotionCauldronBlockEntity> POTION_CAULDRON_ENTITY = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE, Odyssey.MOD_ID + "potion_cauldron_entity",
        FabricBlockEntityTypeBuilder.create(PotionCauldronBlockEntity::new, POTION_CAULDRON).build(null));

    public static final Block GUNPOWDER_BLOCK = registerBlock("gunpowder_block",
        new GunpowderBlock(FabricBlockSettings.of().mapColor(MapColor.COLOR_GRAY)
        .sound(SoundType.SAND).instrument(NoteBlockInstrument.SNARE).strength(0.5f)));

    public static final Block REFINED_AMETHYST_BLOCK = registerBlock("refined_amethyst_block",
        new AmethystBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).sound(SoundType.AMETHYST)));

    public static final Block POLISHED_DEEPSLATE_PRESSURE_PLATE = registerBlock("polished_deepslate_pressure_plate",
        new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE)
        .forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
        .noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), BlockSetType.STONE));

    public static final Block POLISHED_DEEPSLATE_BUTTON = registerBlock("polished_deepslate_button", RegistryHelpers.registerStoneButton());

    // Registry for Blocks:

    private static Boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return true;
    }

    private static Block registerBlockWithoutBlockItem(String string, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Odyssey.MOD_ID, string), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Odyssey.MOD_ID, name),
        new BlockItem(block, new FabricItemSettings()));
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
