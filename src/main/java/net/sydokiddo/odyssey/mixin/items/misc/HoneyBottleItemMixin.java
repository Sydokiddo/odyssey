package net.sydokiddo.odyssey.mixin.items.misc;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.registry.misc.ModCriteriaTriggers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoneyBottleItem.class)
public class HoneyBottleItemMixin {

    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    public void odyssey_curePoisonFromHoneyAdvancement(ItemStack itemStack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (livingEntity instanceof ServerPlayer serverPlayer && serverPlayer.hasEffect(MobEffects.POISON)) {
            ModCriteriaTriggers.CURE_POISON_WITH_HONEY.trigger(serverPlayer);
        }
    }
}