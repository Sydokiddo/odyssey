package net.sydokiddo.odyssey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.item.Items;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.client.rendering.ModEntityRenderer;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.items.custom_items.OwnershipContractItem;
import net.sydokiddo.odyssey.registry.misc.ModParticles;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class OdysseyClient implements ClientModInitializer {

    @SuppressWarnings("deprecation")
    @Override
    public void onInitializeClient() {
        if (Chrysalis.CHRYSALIS_INITIALIZED) {

            ModEntityRenderer.init();
            ModParticles.registerParticles();

            // region Block Rendering

            BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                ModBlocks.REDSTONE_LANTERN,
                ModBlocks.POTTED_SMALL_DRIPLEAF,
                ModBlocks.POTTED_SPORE_BLOSSOM,
                ModBlocks.POTTED_SEA_PICKLE,
                ModBlocks.POTTED_SUGAR_CANE,
                ModBlocks.POTTED_SHORT_GRASS,
                ModBlocks.POTTED_NETHER_WART
            );

            BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(),
                ModBlocks.TINTED_GLASS_PANE
            );

            ColorProviderRegistry.BLOCK.register((blockState, level, blockPos, tintIndex) ->
            BiomeColors.getAverageGrassColor(Objects.requireNonNull(level), Objects.requireNonNull(blockPos)),
                ModBlocks.POTTED_SUGAR_CANE,
                ModBlocks.POTTED_SHORT_GRASS
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

            FabricModelPredicateProviderRegistry.register(Items.AXOLOTL_BUCKET, new ResourceLocation("variant"), (itemStack, client, livingEntity, i) -> {

                float axolotlType = 0;
                String variantString = "Variant";

                if (itemStack.getTag() != null && itemStack.getTag().contains(variantString)) {
                    axolotlType = itemStack.getTag().getInt(variantString);
                }

                return axolotlType * 0.01F + 0.0001F;
            });

            FabricModelPredicateProviderRegistry.register(ModItems.FROG_BUCKET, new ResourceLocation("variant"), (itemStack, client, livingEntity, i) -> {

                CompoundTag compoundTag = itemStack.getTag();

                if (compoundTag != null && compoundTag.contains(Frog.VARIANT_KEY)) {
                    if (Objects.equals(compoundTag.getString(Frog.VARIANT_KEY), "minecraft:temperate")) {
                        return 0.1F;
                    } else if (Objects.equals(compoundTag.getString(Frog.VARIANT_KEY), "minecraft:warm")) {
                        return 0.2F;
                    } else if (Objects.equals(compoundTag.getString(Frog.VARIANT_KEY), "minecraft:cold")) {
                        return 0.3F;
                    }
                    return 0.1F;
                }
                return 0.1F;
            });

            FabricModelPredicateProviderRegistry.register(ModItems.OWNERSHIP_CONTRACT, new ResourceLocation("bound"), (itemStack, client, livingEntity, i) -> {
                if (OwnershipContractItem.isContractBound(itemStack)) {
                    return 1.0F;
                }
                return 0.0F;
            });

            // endregion
        }
    }
}