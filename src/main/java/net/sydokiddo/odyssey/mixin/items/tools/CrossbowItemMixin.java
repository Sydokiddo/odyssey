package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.registry.misc.OCommonMethods;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void odyssey$addCrossbowTooltip(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo info) {

        OCommonMethods.addItemDurabilityTooltip(itemStack, tooltip, tooltipFlag);

        CompoundTag compoundTag = itemStack.getTag();

        if (itemStack.isEnchanted() && itemStack.isDamaged() && !(compoundTag != null && compoundTag.getBoolean("Charged")) && !tooltipFlag.isAdvanced()) {
            tooltip.add(CommonComponents.EMPTY);
        }
    }
}