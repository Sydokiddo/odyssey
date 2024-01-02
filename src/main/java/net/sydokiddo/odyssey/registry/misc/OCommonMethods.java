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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.Odyssey;
import java.util.List;

public class OCommonMethods {

    // region Mechanics

    public static void shearPrimedTNT(Level level, Entity primedTnt, BlockPos blockPos) {

        if (RegistryHelpers.isBlockStateFree(level.getBlockState(blockPos)) && !level.isOutsideBuildHeight(blockPos)) {
            level.setBlock(blockPos, Blocks.TNT.defaultBlockState(), 3);
        } else if (level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            primedTnt.spawnAtLocation(Items.TNT);
        }

        level.playSound(null, blockPos, ModSoundEvents.TNT_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
        primedTnt.discard();
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
    public static void renderCompassOverlay(GuiGraphics guiGraphics) {

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.gui.getDebugOverlay().showDebugScreen() || !Odyssey.getConfig().items.compass_gui_rendering) return;

        if (minecraft.getCameraEntity() != null) {

            Player player = minecraft.player;
            assert player != null;

            boolean hasCompass = false;

            for (int slots = 0; slots <= 35; slots++) {
                if (player.getInventory().getItem(slots).is(Items.COMPASS) || player.getOffhandItem().is(Items.COMPASS)) {
                    hasCompass = true;
                    break;
                }
            }

            if (hasCompass) {

                BlockPos blockPos = minecraft.getCameraEntity().blockPosition();
                int heightOffset;

                if (FabricLoader.getInstance().isModLoaded(RegistryHelpers.manic)) {
                    heightOffset = 30;
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