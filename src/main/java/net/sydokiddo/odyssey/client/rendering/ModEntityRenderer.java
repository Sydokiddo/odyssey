package net.sydokiddo.odyssey.client.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.sydokiddo.odyssey.client.rendering.block_entities.GunpowderBlockEntityRenderer;
import net.sydokiddo.odyssey.client.rendering.block_entities.ModBrushableBlockRenderer;
import net.sydokiddo.odyssey.registry.entities.registry.ModEntities;

@Environment(EnvType.CLIENT)
public class ModEntityRenderer {

    @SuppressWarnings("ALL")
    public static void registerRenderers() {
        BlockEntityRendererRegistry.register(ModEntities.BRUSHABLE_BLOCK, ModBrushableBlockRenderer::new);
        EntityRendererRegistry.register(ModEntities.GUNPOWDER_BLOCK, GunpowderBlockEntityRenderer::new);
    }
}