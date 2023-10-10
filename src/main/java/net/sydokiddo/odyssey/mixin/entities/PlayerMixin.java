package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow protected abstract void removeEntitiesOnShoulder();

    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;removeEntitiesOnShoulder()V"), method = { "aiStep" })
    private void odyssey$cancelRemovingShoulderEntities(Player player) {
        if (!Odyssey.getConfig().entities.passiveMobsConfig.improved_parrots) {
            this.removeEntitiesOnShoulder();
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void odyssey$changeRemovingShoulderEntities(CallbackInfo ci) {
        if (Odyssey.getConfig().entities.passiveMobsConfig.improved_parrots && !this.level().isClientSide && (this.fallDistance > 1.0F && this.getDeltaMovement().horizontalDistance() == 0) || (this.isUnderWater() || this.isInLava() || this.isInPowderSnow)) {
            this.removeEntitiesOnShoulder();
        }
    }
}