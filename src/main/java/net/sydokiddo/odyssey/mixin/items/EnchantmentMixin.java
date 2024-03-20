package net.sydokiddo.odyssey.mixin.items;

import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void odyssey$allowEnchantmentsOnSpecificItems(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {

        Enchantment enchantment = (Enchantment) (Object) this;

        // Shears can now be enchanted with Silk Touch

        if (itemStack.getItem() instanceof ShearsItem && (enchantment instanceof UntouchingEnchantment || enchantment == Enchantments.BLOCK_FORTUNE) && Odyssey.getConfig().items.silk_touch_and_fortune_on_shears) {
            cir.setReturnValue(true);
        }
    }
}