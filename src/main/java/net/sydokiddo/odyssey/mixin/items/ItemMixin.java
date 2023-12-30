package net.sydokiddo.odyssey.mixin.items;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.awt.*;
import java.util.*;
import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    @Unique private static final int FIREPROOF_COLOR = Color.decode("#766A76").getRGB();

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void odyssey$addItemTooltipsBeforeEnchantments(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo info) {

        OdysseyRegistry.addItemDurabilityTooltip(itemStack, tooltip, tooltipFlag);

        if (!FabricLoader.getInstance().isModLoaded("appleskin") && Odyssey.getConfig().items.tooltipConfig.food_information) {

            String nutritionString = "item.odyssey.food.nutrition_points";
            String saturationString = "item.odyssey.food.saturation_points";

            if (itemStack.is(Items.CAKE)) {
                tooltip.add(Component.translatable("gui.odyssey.item.cake.for_each_slice").withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable(nutritionString, 2).withStyle(ChatFormatting.BLUE));
                tooltip.add(Component.translatable(saturationString, 0.4).withStyle(ChatFormatting.BLUE));
            }

            if (itemStack.isEdible()) {

                tooltip.add(Component.translatable(nutritionString, Objects.requireNonNull(itemStack.getItem().getFoodProperties()).getNutrition()).withStyle(ChatFormatting.BLUE));
                tooltip.add(Component.translatable(saturationString, RegistryHelpers.getFoodSaturation(itemStack)).withStyle(ChatFormatting.BLUE));

                if (itemStack.getItem() instanceof SuspiciousStewItem && tooltipFlag.isCreative()) {
                    tooltip.add(CommonComponents.EMPTY);
                }
            }
        }

        if ((itemStack.getItem().isFireResistant() || itemStack.is(ChrysalisTags.IMMUNE_TO_FIRE)) && Odyssey.getConfig().items.tooltipConfig.fireproof_items) {
            tooltip.add(Component.translatable("gui.odyssey.item.fireproof").withStyle(style -> style.withItalic(true).withColor(FIREPROOF_COLOR)));
            RegistryHelpers.addSpaceOnTooltipIfEnchantedOrTrimmed(itemStack, tooltip);
        }

        if (itemStack.getItem() instanceof HoneyBottleItem && Odyssey.getConfig().items.tooltipConfig.honey_bottles) {
            RegistryHelpers.addDrinkTooltip(tooltip);
            tooltip.add(CommonComponents.space().append(Component.translatable("item.odyssey.honey_bottle.desc").withStyle(ChatFormatting.BLUE)));
        }

        if (itemStack.getItem() instanceof MilkBucketItem && Odyssey.getConfig().items.tooltipConfig.milk_buckets) {
            RegistryHelpers.addDrinkTooltip(tooltip);
            tooltip.add(CommonComponents.space().append(Component.translatable("item.odyssey.milk_bucket.desc").withStyle(ChatFormatting.BLUE)));
        }

        if (itemStack.is(Items.TOTEM_OF_UNDYING) && Odyssey.getConfig().items.tooltipConfig.totems_of_undying) {
            RegistryHelpers.addHoldingTooltip(tooltip);
            tooltip.add(CommonComponents.space().append(Component.translatable("item.odyssey.totem_of_undying.desc").withStyle(ChatFormatting.BLUE)));
        }

        if (itemStack.is(Items.ENDER_EYE) && Odyssey.getConfig().items.tooltipConfig.eyes_of_ender) {
            RegistryHelpers.addUseTooltip(tooltip);
            tooltip.add(CommonComponents.space().append(Component.translatable("item.odyssey.ender_eye.desc").withStyle(ChatFormatting.BLUE)));
        }

        if (itemStack.getItem() instanceof SpawnEggItem && Odyssey.getConfig().items.tooltipConfig.spawn_eggs) {
            RegistryHelpers.addUseTooltip(tooltip);
            tooltip.add(CommonComponents.space().append(Component.translatable("item.odyssey.spawn_egg.desc").withStyle(ChatFormatting.BLUE)));
        }

        if (itemStack.is(Items.SPYGLASS) && Odyssey.getConfig().items.tooltipConfig.spyglasses) {
            RegistryHelpers.addUseTooltip(tooltip);
            tooltip.add(CommonComponents.space().append(Component.translatable("item.odyssey.spyglass.desc").withStyle(ChatFormatting.BLUE)));
        }

        if (itemStack.is(Items.TURTLE_HELMET) && Odyssey.getConfig().items.tooltipConfig.turtle_helmets && level != null) {

            int effectTime;

            if (Odyssey.getConfig().items.improved_turtle_helmets) {
                effectTime = 600;
            } else {
                effectTime = 200;
            }

            MobEffectInstance waterBreathing = new MobEffectInstance(MobEffects.WATER_BREATHING, effectTime, 0, false, false, true);

            if (!RegistryHelpers.hasEnchantmentOrTrim(itemStack)) tooltip.add(CommonComponents.EMPTY);
            tooltip.add(Component.translatable("gui.odyssey.item.turtle_helmet.when_entering_water").withStyle(ChatFormatting.GRAY));
            tooltip.add(CommonComponents.space().append(Component.translatable("potion.withDuration", Component.translatable(waterBreathing.getDescriptionId()), MobEffectUtil.formatDuration(waterBreathing, 1.0F, level.tickRateManager().tickrate())).withStyle(ChatFormatting.BLUE)));
            RegistryHelpers.addSpaceOnTooltipIfEnchantedOrTrimmed(itemStack, tooltip);
        }
    }
}