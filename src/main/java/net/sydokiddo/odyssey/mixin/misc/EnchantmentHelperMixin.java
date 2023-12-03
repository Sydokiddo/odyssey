package net.sydokiddo.odyssey.mixin.misc;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(at = @At("RETURN"), method = "getDepthStrider", cancellable = true)
    private static void odyssey$fixRiptideWithDepthStrider(LivingEntity livingEntity, CallbackInfoReturnable<Integer> cir) {
        if (livingEntity.isAutoSpinAttack() && Odyssey.getConfig().items.improved_riptide) {
            cir.setReturnValue(0);
        }
    }
}