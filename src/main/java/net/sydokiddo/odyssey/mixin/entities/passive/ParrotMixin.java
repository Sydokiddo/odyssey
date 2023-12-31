package net.sydokiddo.odyssey.mixin.entities.passive;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Parrot;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Parrot.class)
public class ParrotMixin {

    @Inject(at = @At("HEAD"), method = "createAttributes", cancellable = true)
    private static void odyssey$improvedParrotAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        if (Odyssey.getConfig().entities.passiveMobsConfig.improved_parrots) {
            cir.setReturnValue(Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FLYING_SPEED, 0.4F).add(Attributes.MOVEMENT_SPEED, 0.2F));
        }
    }
}