package net.sydokiddo.odyssey.client.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.sydokiddo.odyssey.client.rendering.block_entities.ModBrushableBlockRenderer;
import net.sydokiddo.odyssey.registry.entities.ModBlockEntities;

@SuppressWarnings("ALL")
@Environment(EnvType.CLIENT)
public class ModEntityRenderer {

    public static void registerRenderers() {
        BlockEntityRendererRegistry.register(ModBlockEntities.ODYSSEY_BRUSHABLE_BLOCK, ModBrushableBlockRenderer::new);
    }
}