package net.sydokiddo.odyssey.mixin.blocks.misc;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {

    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("HEAD"), cancellable = true)
    private static void odyssey$horsesWalkingOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (Odyssey.getConfig().entities.passiveMobsConfig.improved_horses && entity instanceof AbstractHorse horse && horse.getItemBySlot(EquipmentSlot.CHEST).is(Items.LEATHER_HORSE_ARMOR)) {
            cir.setReturnValue(true);
        }
    }
}