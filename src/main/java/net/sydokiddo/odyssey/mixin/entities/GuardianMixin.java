package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Guardian.class)
public class GuardianMixin extends Monster {

    private GuardianMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    // Guardians struck by lighting will turn into Elder Guardians, making Elder Guardians and their drops renewable

    @Override
    public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt) {

        ElderGuardian elderGuardian = EntityType.ELDER_GUARDIAN.create(serverLevel);

        if (elderGuardian != null && Odyssey.getConfig().entities.renewable_elder_guardians && this.getType() != EntityType.ELDER_GUARDIAN) {

            elderGuardian.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
            elderGuardian.setNoAi(this.isNoAi());

            if (this.hasCustomName()) {
                elderGuardian.setCustomName(this.getCustomName());
                elderGuardian.setCustomNameVisible(this.isCustomNameVisible());
            }

            elderGuardian.setPersistenceRequired();
            serverLevel.addFreshEntity(elderGuardian);
            OdysseyRegistry.sendMobConversionDebugMessage(this, elderGuardian);
            this.discard();

        } else {
            super.thunderHit(serverLevel, lightningBolt);
        }
    }
}