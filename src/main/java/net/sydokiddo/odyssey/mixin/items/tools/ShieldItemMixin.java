package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.registry.misc.OCommonMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {

    @Unique
    private static void addBannerTextToShield(ItemStack itemStack, List<Component> list) {

        CompoundTag blockEntityData = BlockItem.getBlockEntityData(itemStack);
        String patternsString = "Patterns";

        if (blockEntityData == null || !blockEntityData.contains(patternsString)) {
            return;
        }

        ListTag listTag = blockEntityData.getList(patternsString, 10);

        for (int i = 0; i < listTag.size() && i < 6; ++i) {

            CompoundTag compoundTag = listTag.getCompound(i);

            DyeColor dyeColor = DyeColor.byId(compoundTag.getInt("Color"));
            Holder<BannerPattern> holder = BannerPattern.byHash(compoundTag.getString("Pattern"));

            if (holder == null) continue;

            holder.unwrapKey().map(resourceKey -> resourceKey.location().toShortLanguageKey()).ifPresent(string -> list.add(CommonComponents.space().append(Component.translatable("block.minecraft.banner." + string + "." + dyeColor.getName()).withStyle(ChatFormatting.BLUE))));
        }
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void odyssey$addShieldTooltip(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo info) {

        CompoundTag compoundTag = BlockItem.getBlockEntityData(itemStack);

        info.cancel();
        OCommonMethods.addItemDurabilityTooltip(itemStack, tooltip, tooltipFlag);

        if (compoundTag != null && compoundTag.contains("Patterns")) {
            tooltip.add(Component.translatable("gui.odyssey.item.shield.banner").withStyle(ChatFormatting.GRAY));
            addBannerTextToShield(itemStack, tooltip);
            RegistryHelpers.addSpaceOnTooltipIfEnchantedOrTrimmed(itemStack, tooltip);
        }
    }
}