package net.sydokiddo.odyssey.mixin.entities.hostile;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.misc.util.helpers.DebugHelper;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Guardian.class)
public class GuardianMixin extends Monster {

    private GuardianMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    // Guardians struck by lighting will turn into Elder Guardians, making Elder Guardians and their drops renewable

    @Override
    public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt) {
        if (this.getType() != EntityType.ELDER_GUARDIAN && Odyssey.getConfig().entities.hostileMobsConfig.renewable_elder_guardians) {
            ElderGuardian elderGuardian = EntityType.ELDER_GUARDIAN.create(serverLevel);
            DebugHelper.sendEntityConversionDebugMessage(this, elderGuardian);
            this.convertTo(EntityType.ELDER_GUARDIAN, true);
        } else {
            super.thunderHit(serverLevel, lightningBolt);
        }
    }
}