package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract boolean hasEffect(MobEffect mobEffect);
    @Shadow public abstract CombatTracker getCombatTracker();
    @Shadow protected boolean dead;

    private LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    // Entities with the Fire Resistance status effect will not render the fire overlay

    @Override
    public boolean displayFireAnimation() {
        if (this.hasEffect(MobEffects.FIRE_RESISTANCE) && Odyssey.getConfig().entities.miscEntitiesConfig.hidden_fire_overlay_with_fire_resistance) {
            return false;
        }
        return super.displayFireAnimation();
    }

    // Displays a death message to the chat if a named mob dies

    @Inject(method = "die", at = @At("HEAD"))
    private void odyssey$displayMobDeathMessage(DamageSource damageSource, CallbackInfo info) {
        if (!this.isRemoved() && !this.dead && !this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.hasCustomName() && Odyssey.getConfig().entities.miscEntitiesConfig.named_mob_death_messages) {
            Objects.requireNonNull(this.level().getServer()).getPlayerList().broadcastSystemMessage(this.getCombatTracker().getDeathMessage(), false);
        }
    }
}