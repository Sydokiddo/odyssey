package net.sydokiddo.odyssey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.client.rendering.ModEntityRenderer;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import net.sydokiddo.odyssey.registry.blocks.ModBlockStateProperties;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.items.custom_items.OwnershipContractItem;
import net.sydokiddo.odyssey.registry.misc.ModParticles;
import net.sydokiddo.odyssey.registry.misc.OCommonMethods;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class OdysseyClient implements ClientModInitializer {

    @SuppressWarnings("deprecation")
    @Override
    public void onInitializeClient() {
        if (Chrysalis.CHRYSALIS_INITIALIZED) {

            ModEntityRenderer.init();
            ModParticles.registerParticles();
            HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> OCommonMethods.renderCompassOverlay(guiGraphics));
            TooltipComponentCallback.EVENT.register(OCommonMethods::shouldRenderMapTooltip);

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

                    Holder<Biome> biome = client.getBiome(livingEntity.getOnPos());

                    if (biome.is(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)) {
                        return 0.2F;
                    }
                    else if (biome.is(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)) {
                        return 0.3F;
                    } else {
                        return 0.1F;
                    }
                }
                return 0.1F;
            });

            FabricModelPredicateProviderRegistry.register(Items.AXOLOTL_BUCKET, new ResourceLocation("variant"), (itemStack, client, livingEntity, i) -> {

                CompoundTag compoundTag = itemStack.getTag();
                float axolotlType = 0;
                String variantString = "Variant";

                if (compoundTag != null && compoundTag.contains(variantString)) axolotlType = compoundTag.getInt(variantString);
                return axolotlType * 0.01F + 0.0001F;
            });

            FabricModelPredicateProviderRegistry.register(ModItems.FROG_BUCKET, new ResourceLocation("variant"), (itemStack, client, livingEntity, i) -> {

                CompoundTag compoundTag = itemStack.getTag();

                if (compoundTag != null && compoundTag.contains(Frog.VARIANT_KEY)) {

                    String frogVariant = compoundTag.getString(Frog.VARIANT_KEY);

                    if (Objects.equals(frogVariant, "minecraft:temperate")) {
                        return 0.1F;
                    } else if (Objects.equals(frogVariant, "minecraft:warm")) {
                        return 0.2F;
                    } else if (Objects.equals(frogVariant, "minecraft:cold")) {
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

            // region Packets

            // Environment Detector Packet

            ClientPlayNetworking.registerGlobalReceiver(OdysseyRegistry.ENVIRONMENT_DETECTOR_PACKET_ID,((client, handler, buf, responseSender) -> {

                Holder<Biome> biome = client.level.getBiome(client.player.getOnPos());

                biome.unwrapKey().ifPresent(key -> {
                    Component biomeName = Component.translatable(Util.makeDescriptionId("biome", key.location()));
                    Component component = Component.translatable("gui.odyssey.item.environment_detector.biome", biomeName);

                    Minecraft.getInstance().gui.setOverlayMessage(component, false);
                    Minecraft.getInstance().getNarrator().sayNow(component);
                });
            }));

            // Ownership Contract Packet

            ClientPlayNetworking.registerGlobalReceiver(OdysseyRegistry.OWNERSHIP_CONTRACT_PACKET_ID,((client, handler, buf, responseSender) -> {

                ItemStack itemStack = client.player.getItemInHand(client.player.getUsedItemHand());
                CompoundTag compoundTag = itemStack.getOrCreateTag();

                Component messageToSend = Component.translatable("gui.odyssey.item.ownership_contract.failed_message").withStyle(ChatFormatting.RED); // Failed Message

                switch (buf.readInt()) {
                    case 0 -> messageToSend = Component.translatable("gui.odyssey.item.ownership_contract.could_not_find", compoundTag.getString(OwnershipContractItem.mobNameString)).withStyle(ChatFormatting.RED); // Missing Message
                    case 1 -> messageToSend = Component.translatable("gui.odyssey.item.ownership_contract.already_owned", compoundTag.getString(OwnershipContractItem.mobNameString)).withStyle(ChatFormatting.RED); // Already Owned Message
                    case 2 -> messageToSend = Component.translatable("gui.odyssey.item.ownership_contract.transfer_ownership_old_owner", compoundTag.getString(OwnershipContractItem.mobNameString), buf.readNbt().getString("NewOwner")).withStyle(ChatFormatting.WHITE); // Old Owner Message
                    case 3 -> messageToSend = Component.translatable("gui.odyssey.item.ownership_contract.transfer_ownership_new_owner", compoundTag.getString(OwnershipContractItem.mobNameString)).withStyle(ChatFormatting.WHITE); // New Owner Message
                    case 4 -> messageToSend = Component.translatable("gui.odyssey.item.ownership_contract.bind_success", buf.readNbt().getString("SelectedMob"), itemStack.getHoverName().getString()).withStyle(ChatFormatting.WHITE); // Bind Success Message
                    case 5 -> messageToSend = Component.translatable("gui.odyssey.item.ownership_contract.bind_fail", buf.readNbt().getString("SelectedMob"), itemStack.getHoverName().getString()).withStyle(ChatFormatting.RED); // Bind Fail Message
                }

                Minecraft.getInstance().gui.setOverlayMessage(messageToSend, false);
                Minecraft.getInstance().getNarrator().sayNow(messageToSend);
            }));

            // Note Block Packet

            ClientPlayNetworking.registerGlobalReceiver(OdysseyRegistry.NOTE_BLOCK_PACKET_ID,((client, handler, buf, responseSender) -> {

                BlockHitResult lookedAtBlock = (BlockHitResult) client.hitResult;
                BlockState blockState = client.level.getBlockState(lookedAtBlock.getBlockPos());

                if (lookedAtBlock.getType() == HitResult.Type.BLOCK && blockState.getBlock() instanceof NoteBlock) {

                    int notePitch;

                    if (blockState.getValue(ModBlockStateProperties.WAXED) || buf.readInt() == 0) {
                        notePitch = blockState.getValue(NoteBlock.NOTE);
                    } else {
                        notePitch = blockState.getValue(NoteBlock.NOTE) + 1;
                    }

                    if (notePitch == 25) notePitch = 0;
                    Component messageToSend = Component.translatable("gui.odyssey.block.note_block.note_pitch", notePitch);

                    Minecraft.getInstance().gui.setOverlayMessage(messageToSend, true);
                    Minecraft.getInstance().getNarrator().sayNow(messageToSend);
                }
            }));

            // endregion
        }
    }
}