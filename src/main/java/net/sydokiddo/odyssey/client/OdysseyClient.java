package net.sydokiddo.odyssey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.client.rendering.ModEntityRenderer;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;

@Environment(EnvType.CLIENT)
public class OdysseyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        if (Chrysalis.CHRYSALIS_INITIALIZED) {

            ModEntityRenderer.registerRenderers();
            ModEntityRenderer.setPotionCauldronRenderColors();

            // region Block Rendering

            BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                ModBlocks.REDSTONE_LANTERN,
                ModBlocks.POTTED_SMALL_DRIPLEAF,
                ModBlocks.POTTED_SPORE_BLOSSOM,
                ModBlocks.POTTED_SEA_PICKLE
            );

            BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(),
                ModBlocks.TINTED_GLASS_PANE
            );

            // endregion
        }
    }
}