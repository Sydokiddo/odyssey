package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sniffer.class)
public class SnifferMixin {

    @Inject(at = @At("HEAD"), method = "createAttributes", cancellable = true)
    private static void odyssey$improvedSnifferAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        if (Odyssey.getConfig().entities.improved_sniffer_stats) {
            cir.setReturnValue(Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.1F).add(Attributes.MAX_HEALTH, 24.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.6F));
        }
    }
}