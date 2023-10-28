package net.sydokiddo.odyssey.mixin.items;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.*;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void odyssey$addItemTooltips(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (Odyssey.getConfig().items.more_tooltips) {

            OdysseyRegistry.addItemDurabilityTooltip(itemStack, tooltip, tooltipFlag);

            if (itemStack.isEdible() && !FabricLoader.getInstance().isModLoaded("appleskin")) {
                tooltip.add(Component.translatable("item.odyssey.food.nutrition_points", Objects.requireNonNull(itemStack.getItem().getFoodProperties()).getNutrition()).withStyle(ChatFormatting.BLUE));
                tooltip.add(Component.translatable("item.odyssey.food.saturation_points", Objects.requireNonNull(itemStack.getItem().getFoodProperties()).getSaturationModifier()).withStyle(ChatFormatting.BLUE));
            }

            if (itemStack.getItem() instanceof HoneyBottleItem) {
                ChrysalisRegistry.addDrinkTooltip(tooltip);
                tooltip.add(Component.translatable("item.odyssey.honey_bottle.desc").withStyle(ChatFormatting.BLUE));
            }
            if (itemStack.getItem() instanceof MilkBucketItem) {
                ChrysalisRegistry.addDrinkTooltip(tooltip);
                tooltip.add(Component.translatable("item.odyssey.milk_bucket.desc").withStyle(ChatFormatting.BLUE));
            }

            if (itemStack.is(Items.CARROT_ON_A_STICK)) {
                ChrysalisRegistry.addHoldingTooltip(tooltip);
                tooltip.add(Component.translatable("item.odyssey.carrot_on_a_stick.desc").withStyle(ChatFormatting.BLUE));
                OdysseyRegistry.addSpaceOnTooltipIfEnchanted(itemStack, tooltip);
            }
            if (itemStack.is(Items.WARPED_FUNGUS_ON_A_STICK)) {
                ChrysalisRegistry.addHoldingTooltip(tooltip);
                tooltip.add(Component.translatable("item.odyssey.warped_fungus_on_a_stick.desc").withStyle(ChatFormatting.BLUE));
                OdysseyRegistry.addSpaceOnTooltipIfEnchanted(itemStack, tooltip);
            }

            if (itemStack.is(Items.TOTEM_OF_UNDYING)) {
                ChrysalisRegistry.addHoldingTooltip(tooltip);
                tooltip.add(Component.translatable("item.odyssey.totem_of_undying.desc").withStyle(ChatFormatting.BLUE));
            }
            if (itemStack.getItem() instanceof SpawnEggItem) {
                ChrysalisRegistry.addUseTooltip(tooltip);
                tooltip.add(Component.translatable("item.odyssey.spawn_egg.desc").withStyle(ChatFormatting.BLUE));
            }
        }
    }
}