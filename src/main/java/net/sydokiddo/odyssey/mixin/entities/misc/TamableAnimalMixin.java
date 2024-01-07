package net.sydokiddo.odyssey.mixin.entities.misc;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Objects;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin extends Animal {

    private TamableAnimalMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;sendSystemMessage(Lnet/minecraft/network/chat/Component;)V"), cancellable = true)
    private void odyssey$preventMobDoubleDeathMessage(DamageSource damageSource, CallbackInfo info) {
        if (Odyssey.getConfig().entities.miscEntitiesConfig.named_mob_death_messages && this.hasCustomName()) {
            info.cancel();
            Objects.requireNonNull(this.level().getServer()).getPlayerList().broadcastSystemMessage(this.getCombatTracker().getDeathMessage(), false);
        }
    }
}