package net.sydokiddo.odyssey.client;

import net.fabricmc.api.ClientModInitializer;
import net.sydokiddo.odyssey.client.rendering.CauldronRendering;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class OdysseyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CauldronRendering.setRenderColors();
    }
}
