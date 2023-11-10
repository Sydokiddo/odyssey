package net.sydokiddo.odyssey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.client.rendering.ModEntityRenderer;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.items.ModItems;

@Environment(EnvType.CLIENT)
public class OdysseyClient implements ClientModInitializer {

    @SuppressWarnings("deprecation")
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

            // region Item Model Loading

            FabricModelPredicateProviderRegistry.register(ModItems.ENVIRONMENT_DETECTOR, new ResourceLocation("type"), (itemStack, client, livingEntity, i) -> {

                if (client != null && livingEntity != null) {
                    if (client.getBiome(livingEntity.getOnPos()).is(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)) {
                        return 0.2F;
                    }
                    else if (client.getBiome(livingEntity.getOnPos()).is(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)) {
                        return 0.3F;
                    } else {
                        return 0.1F;
                    }
                }
                return 0.1F;
            });

            // endregion
        }
    }
}