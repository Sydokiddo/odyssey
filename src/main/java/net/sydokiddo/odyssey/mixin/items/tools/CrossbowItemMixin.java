package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
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
    private void odyssey$addCrossbowTooltip(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo ci) {
        OdysseyRegistry.addItemDurabilityTooltip(itemStack, tooltip, tooltipFlag);
    }

    @Inject(method = "appendHoverText", at = @At("TAIL"))
    private void odyssey$addSpaceBeforeCrossbowEnchantments(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo ci) {
        OdysseyRegistry.addSpaceOnTooltipIfEnchantedOrTrimmed(itemStack, tooltip);
    }
}