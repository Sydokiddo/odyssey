package net.sydokiddo.odyssey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.sydokiddo.odyssey.client.rendering.CauldronRendering;

@SuppressWarnings("ALL")
@Environment(EnvType.CLIENT)
public class OdysseyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CauldronRendering.setRenderColors();
    }
}
