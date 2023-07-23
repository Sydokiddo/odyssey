package net.sydokiddo.odyssey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.client.rendering.ModEntityRenderer;
import net.sydokiddo.odyssey.client.rendering.block_entities.PotionCauldronRendering;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;

@Environment(EnvType.CLIENT)
public class OdysseyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        if (Odyssey.chrysalisInitialized()) {

            ModEntityRenderer.registerRenderers();
            PotionCauldronRendering.setRenderColors();

            // Renders Blocks in List as Transparent (Without Translucency)

            BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                ModBlocks.REDSTONE_LANTERN
            );
        }
    }
}
