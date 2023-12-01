package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends ProjectileWeaponItem {

    private CrossbowItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void odyssey$addCrossbowTooltip(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo info) {

        OdysseyRegistry.addItemDurabilityTooltip(itemStack, tooltip, tooltipFlag);

        CompoundTag compoundTag = itemStack.getTag();

        if (itemStack.isEnchanted() && itemStack.isDamaged() && !(compoundTag != null && compoundTag.getBoolean("Charged")) && !tooltipFlag.isAdvanced()) {
            tooltip.add(CommonComponents.EMPTY);
        }
    }

    // To remove when Chrysalis is updated

    @Inject(method = "appendHoverText", at = @At("TAIL"))
    private void odyssey$addSpaceBeforeCrossbowEnchantments(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo info) {
        OdysseyRegistry.addSpaceOnTooltipIfEnchantedOrTrimmed(itemStack, tooltip);
    }
}