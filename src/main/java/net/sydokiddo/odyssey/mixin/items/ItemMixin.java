package net.sydokiddo.odyssey.mixin.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void odyssey$addItemTooltips(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (Odyssey.getConfig().items.more_tooltips) {
            if (itemStack.is(Items.HONEY_BOTTLE)) {
                ChrysalisRegistry.addDrinkTooltip(tooltip);
                tooltip.add(Component.translatable("item.odyssey.honey_bottle.desc").withStyle(ChatFormatting.BLUE));
            }
            if (itemStack.is(Items.MILK_BUCKET)) {
                ChrysalisRegistry.addDrinkTooltip(tooltip);
                tooltip.add(Component.translatable("item.odyssey.milk_bucket.desc").withStyle(ChatFormatting.BLUE));
            }
        }
    }
}