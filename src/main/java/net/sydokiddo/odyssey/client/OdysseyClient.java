package net.sydokiddo.odyssey.client;

import net.fabricmc.api.ClientModInitializer;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class OdysseyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CauldronRendering.setRenderColors();
    }
}
