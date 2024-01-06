package net.sydokiddo.odyssey.client.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.sydokiddo.odyssey.client.rendering.block_entities.GunpowderBlockEntityRenderer;
import net.sydokiddo.odyssey.client.rendering.block_entities.ModBrushableBlockRenderer;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.entities.block_entities.PotionCauldronBlockEntity;
import net.sydokiddo.odyssey.registry.entities.registry.ModEntities;

@Environment(EnvType.CLIENT)
public class ModEntityRenderer {

    public static void init() {
        registerRenderers();
        setPotionCauldronRenderColors();
    }

    @SuppressWarnings("deprecation")
    private static void registerRenderers() {
        BlockEntityRendererRegistry.register(ModEntities.BRUSHABLE_BLOCK, ModBrushableBlockRenderer::new);
        EntityRendererRegistry.register(ModEntities.GUNPOWDER_BLOCK, GunpowderBlockEntityRenderer::new);
    }

    public static void setPotionCauldronRenderColors() {

        ColorProviderRegistry.BLOCK.register((blockState, level, blockPos, tintIndex) -> {

            assert level != null;
            BlockEntity blockEntity = level.getBlockEntity(blockPos);

            if (blockPos != null && blockEntity instanceof PotionCauldronBlockEntity potionCauldron) {
                return potionCauldron.getColor();
            }
            return 0;

        }, ModBlocks.POTION_CAULDRON);
    }
}