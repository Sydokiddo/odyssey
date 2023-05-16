package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Rabbit.class)
public abstract class RabbitMixin extends Animal {

    private RabbitMixin(EntityType<? extends Rabbit> entityType, Level level) {
        super(entityType, level);
    }

    // Rabbits now take reduced fall damage

    @Override
    protected int calculateFallDamage(float f, float g) {
        return super.calculateFallDamage(f, g) - 10;
    }
}