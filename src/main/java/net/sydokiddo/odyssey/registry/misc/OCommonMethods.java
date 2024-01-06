package net.sydokiddo.odyssey.registry.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.social.PlayerEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.InventoryHelper;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.Odyssey;
import java.util.List;

public class OCommonMethods {

    // region Mechanics

    public static final int TNT_CANNOT_DEFUSE_TICKS = 10;

    public static void defusePrimedTNT(Level level, PrimedTnt primedTnt, BlockPos blockPos, SoundEvent soundEvent) {

        if (level.getRandom().nextFloat() < 0.01F) {
            primedTnt.setFuse(1);
        } else {
            if (RegistryHelpers.isBlockStateFree(level.getBlockState(blockPos)) && !level.isOutsideBuildHeight(blockPos)) {
                level.setBlock(blockPos, Blocks.TNT.defaultBlockState(), 3);
            } else if (level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                primedTnt.spawnAtLocation(Items.TNT);
            }

            if (Chrysalis.IS_DEBUG && primedTnt.isAlive()) {
                Odyssey.LOGGER.info("TNT has been successfully defused at {}", blockPos);
            }

            level.playSound(null, blockPos, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
            primedTnt.discard();
        }
    }

    public static void doSaddleRemovingEvents(LivingEntity livingEntity, Player player, InteractionHand interactionHand) {

        livingEntity.level().playSound(null, livingEntity, ModSoundEvents.SADDLE_UNEQUIP, SoundSource.NEUTRAL, 1.0F, 1.0F);
        player.setItemInHand(interactionHand, Items.SADDLE.getDefaultInstance());

        if (Chrysalis.IS_DEBUG && livingEntity instanceof Saddleable saddleable && saddleable.isSaddled()) {
            Odyssey.LOGGER.info("Saddle has been successfully removed from {} by {}", livingEntity.getName().getString(), player.getName().getString());
        }
    }

    // endregion

    // region Tooltips

    public static void addItemDurabilityTooltip(ItemStack itemStack, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (Odyssey.getConfig().items.tooltipConfig.durability_information && itemStack.isDamaged() && !tooltipFlag.isAdvanced()) {

            tooltip.add(Component.translatable("item.durability", itemStack.getMaxDamage() - itemStack.getDamageValue(), itemStack.getMaxDamage()).withStyle(ChatFormatting.GRAY));

            if (!itemStack.is(ModTags.TOOLTIP_SPACE_BLACKLISTED)) {
                RegistryHelpers.addSpaceOnTooltipIfEnchantedOrTrimmed(itemStack, tooltip);
            }
        }
    }

    // endregion

    // region Debugging

    public static void sendMobConversionDebugMessage(LivingEntity startingEntity, LivingEntity resultEntity) {
        if (Chrysalis.IS_DEBUG) {
            Odyssey.LOGGER.info("{} has been converted into {}", startingEntity.getName().getString(), resultEntity.getName().getString());
        }
    }

    // endregion

    // region Rendering

    @Environment(EnvType.CLIENT)
    public static void renderCompassAndMapOverlay(GuiGraphics guiGraphics) {

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.gui.getDebugOverlay().showDebugScreen() || !Odyssey.getConfig().items.compass_and_map_gui_rendering) return;

        if (minecraft.getCameraEntity() != null) {

            Player player = minecraft.player;
            assert player != null;

            if (InventoryHelper.hasItemInInventory(Items.COMPASS, player) || InventoryHelper.hasItemInInventory(Items.FILLED_MAP, player)) {

                BlockPos blockPos = minecraft.getCameraEntity().blockPosition();
                int heightOffset;

                if (FabricLoader.getInstance().isModLoaded(RegistryHelpers.manic) || FabricLoader.getInstance().isModLoaded("mr_nevermore") || FabricLoader.getInstance().isModLoaded("mr_manic")) {
                    heightOffset = 35;
                } else {
                    heightOffset = 5;
                }

                PoseStack poseStack = guiGraphics.pose();
                poseStack.pushPose();

                guiGraphics.drawString(minecraft.font, Component.translatable("gui.chrysalis.coordinates", blockPos.getX(), blockPos.getY(), blockPos.getZ()), 5, heightOffset, PlayerEntry.PLAYERNAME_COLOR, true);
                guiGraphics.drawString(minecraft.font, Component.translatable("gui.chrysalis.facing_direction", Component.translatable("gui.chrysalis.direction." + player.getDirection().getName())), 5, heightOffset + 10, PlayerEntry.PLAYERNAME_COLOR, true);

                poseStack.popPose();
            }
        }
    }

    // endregion
}