package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vex.class)
public abstract class VexMixin extends Monster implements TraceableEntity {

    private VexMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    // Vexes die alongside the Evoker that initially summoned them

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void odyssey$killVexesOnEvokerDeath(CallbackInfo ci) {

        Entity owner = this.getOwner();

        if (!this.level().isClientSide && Odyssey.getConfig().entities.vexes_die_with_evokers && owner instanceof Evoker && !owner.isAlive()) {

            if (Chrysalis.IS_DEBUG) {
                Odyssey.LOGGER.info("{} has been killed as its owner {} is no longer alive", this.getName().getString(), owner.getName().getString());
            }

            this.kill();
        }
    }
}