package net.sydokiddo.odyssey.mixin.entities.passive;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Rabbit.class)
public abstract class RabbitMixin extends Animal {

    private RabbitMixin(EntityType<? extends Rabbit> entityType, Level level) {
        super(entityType, level);
    }

    // Rabbits now take reduced fall damage

    @Override
    protected int calculateFallDamage(float f, float g) {
        if (Odyssey.getConfig().entities.passiveMobsConfig.improved_rabbits) {
            return super.calculateFallDamage(f, g) - 10;
        } else {
            return super.calculateFallDamage(f, g);
        }
    }

    // Rabbits can now step up 1 block

    @Override
    public float maxUpStep() {
        if (Odyssey.getConfig().entities.passiveMobsConfig.improved_rabbits) {
            return 1.0F;
        }
        return super.maxUpStep();
    }
}