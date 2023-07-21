package net.sydokiddo.odyssey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.sydokiddo.odyssey.client.rendering.ModEntityRenderer;
import net.sydokiddo.odyssey.client.rendering.block_entities.PotionCauldronRendering;

@SuppressWarnings("ALL")
@Environment(EnvType.CLIENT)
public class OdysseyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModEntityRenderer.registerRenderers();
        PotionCauldronRendering.setRenderColors();
    }
}
