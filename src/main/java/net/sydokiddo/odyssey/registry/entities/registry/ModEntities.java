package net.sydokiddo.odyssey.registry.entities.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.entities.block_entities.PotionCauldronBlockEntity;
import net.sydokiddo.odyssey.registry.entities.block_entities.ModBrushableBlockEntity;
import net.sydokiddo.odyssey.registry.entities.non_living_entities.GunpowderBlockEntity;

public class ModEntities {

    // Block Entities:

    public static BlockEntityType<ModBrushableBlockEntity> BRUSHABLE_BLOCK = FabricBlockEntityTypeBuilder.create(ModBrushableBlockEntity::new, ModBlocks.SUSPICIOUS_RED_SAND).build(null);

    public static BlockEntityType<PotionCauldronBlockEntity> POTION_CAULDRON = FabricBlockEntityTypeBuilder.create(PotionCauldronBlockEntity::new, ModBlocks.POTION_CAULDRON).build(null);

    // Non-Living Entities:

    public static final EntityType<GunpowderBlockEntity> GUNPOWDER_BLOCK = FabricEntityTypeBuilder.create(MobCategory.MISC, GunpowderBlockEntity::new).dimensions(EntityDimensions.fixed(1F, 1F)).trackRangeChunks(4).trackedUpdateRate(10).build();

    // Registry for Entities:

    public static void registerModEntities() {
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Odyssey.id("brushable_block"), BRUSHABLE_BLOCK);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Odyssey.id("potion_cauldron"), POTION_CAULDRON);
        Registry.register(BuiltInRegistries.ENTITY_TYPE, Odyssey.id("gunpowder_block"), GUNPOWDER_BLOCK);
    }
}