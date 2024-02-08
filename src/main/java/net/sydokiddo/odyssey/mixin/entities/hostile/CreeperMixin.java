package net.sydokiddo.odyssey.mixin.entities.hostile;

import net.minecraft.world.entity.monster.Creeper;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Creeper.class)
public abstract class CreeperMixin {

    @Shadow public abstract boolean isPowered();

    @Inject(method = "canDropMobsSkull", at = @At("HEAD"), cancellable = true)
    private void odyssey$allowMultipleHeadsFromChargedCreepers(CallbackInfoReturnable<Boolean> cir) {
        if (Odyssey.getConfig().entities.hostileMobsConfig.more_heads_from_charged_creepers) cir.setReturnValue(this.isPowered());
    }
}