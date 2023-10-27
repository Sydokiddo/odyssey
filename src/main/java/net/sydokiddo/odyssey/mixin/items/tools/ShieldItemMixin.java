package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void odyssey$addShieldTooltip(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (Odyssey.getConfig().items.more_tooltips) {
            OdysseyRegistry.addItemDurabilityTooltip(itemStack, tooltip, tooltipFlag);
            ChrysalisRegistry.addUseTooltip(tooltip);
            tooltip.add(Component.translatable("item.odyssey.shield.desc").withStyle(ChatFormatting.BLUE));
            OdysseyRegistry.addSpaceOnTooltipIfEnchanted(itemStack, tooltip);
        }
    }
}