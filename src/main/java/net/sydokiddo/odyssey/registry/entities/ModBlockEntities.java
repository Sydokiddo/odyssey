package net.sydokiddo.odyssey.registry.entities;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.entities.block_entities.ModBrushableBlockEntity;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class ModBlockEntities {

    // Block Entity Types:

    public static BlockEntityType<ModBrushableBlockEntity> ODYSSEY_BRUSHABLE_BLOCK = QuiltBlockEntityTypeBuilder.create(ModBrushableBlockEntity::new, ModBlocks.SUSPICIOUS_RED_SAND).build(null);

    public static void registerBlockEntities() {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Odyssey.MOD_ID + ":brushable_block", ODYSSEY_BRUSHABLE_BLOCK);
    }
}