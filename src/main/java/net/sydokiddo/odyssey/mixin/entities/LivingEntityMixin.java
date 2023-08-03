package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract boolean hasEffect(MobEffect effect);

    private LivingEntityMixin(EntityType<?> variant, Level world) {
        super(variant, world);
    }

    // Entities with the Fire Resistance status effect will not render the fire overlay

    @Override
    public boolean displayFireAnimation() {
        if (this.hasEffect(MobEffects.FIRE_RESISTANCE) && Odyssey.getConfig().entities.hidden_fire_overlay_with_fire_resistance) {
            return false;
        }
        return super.displayFireAnimation();
    }
}