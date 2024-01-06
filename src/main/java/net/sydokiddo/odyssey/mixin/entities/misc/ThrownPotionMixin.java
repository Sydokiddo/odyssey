package net.sydokiddo.odyssey.mixin.entities.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import net.sydokiddo.odyssey.registry.misc.OCommonMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.function.Predicate;

@Mixin(ThrownPotion.class)
public abstract class ThrownPotionMixin extends ThrowableItemProjectile {

    @Unique private static final Predicate<PrimedTnt> CAN_DEFUSE = (primedTnt) -> primedTnt.getFuse() > OCommonMethods.TNT_CANNOT_DEFUSE_TICKS;

    private ThrownPotionMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "applyWater", at = @At("HEAD"))
    private void odyssey$splashTNTWithWater(CallbackInfo info) {

        if (!Odyssey.getConfig().entities.miscEntitiesConfig.tnt_defusing) return;

        AABB boundingBox = this.getBoundingBox().inflate(4.0, 2.0, 4.0);
        List<PrimedTnt> list = this.level().getEntitiesOfClass(PrimedTnt.class, boundingBox, CAN_DEFUSE);

        for (PrimedTnt primedTnt : list) {

            double distance = this.distanceToSqr(primedTnt);

            if (distance < 16.0) {

                BlockPos blockPos = primedTnt.getOnPos().above();

                OCommonMethods.defusePrimedTNT(level(), primedTnt, blockPos, ModSoundEvents.TNT_SPLASH);

                if (level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), 0, 0.0, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}