package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ALL")
@Mixin(targets = "net/minecraft/world/entity/monster/Slime$SlimeFloatGoal", priority = 1500)
public class SlimeFloatGoalMixin {

    @Mutable @Final @Shadow private final Slime slime;

    private SlimeFloatGoalMixin(Slime slime) {
        this.slime = slime;
    }

    @Inject(method = "canUse", at = @At(value = "HEAD"), cancellable = true)
    private void odyssey_preventSlimeFloatingOnWater(CallbackInfoReturnable<Boolean> cir) {
        if (slime instanceof MagmaCube magmaCube && magmaCube.isInWater() && Odyssey.getConfig().entities.slime_and_magma_cube_converting) {
            cir.setReturnValue(false);
        }
    }
}