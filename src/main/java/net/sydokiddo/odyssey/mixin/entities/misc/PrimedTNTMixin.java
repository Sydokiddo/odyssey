package net.sydokiddo.odyssey.mixin.entities.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import net.sydokiddo.odyssey.registry.misc.OCommonMethods;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PrimedTnt.class)
public abstract class PrimedTNTMixin extends Entity {

    @Shadow public abstract int getFuse();
    @Shadow @Nullable private LivingEntity owner;
    @Unique PrimedTnt primedTnt = (PrimedTnt) (Object) this;

    private PrimedTNTMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damageAmount) {
        if (Odyssey.getConfig().entities.miscEntitiesConfig.tnt_knockback) {
            if (this.isInvulnerableTo(damageSource)) {
                return false;
            } else {
                this.markHurt();
                Entity entity = damageSource.getEntity();

                if (entity != null) {

                    if (!this.level().isClientSide()) {

                        this.setDeltaMovement(entity.getLookAngle());

                        if (entity instanceof LivingEntity livingEntity) this.owner = livingEntity;
                        if (entity instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity livingEntity) this.owner = livingEntity;
                    }
                    return true;

                } else {
                    return false;
                }
            }
        }

        return super.hurt(damageSource, damageAmount);
    }

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);
        boolean canDefuse = !itemStack.isEmpty() && itemStack.getItem() instanceof ShearsItem && this.getFuse() > OCommonMethods.TNT_CANNOT_DEFUSE_TICKS;
        BlockPos blockPos = this.getOnPos().above();

        if (Odyssey.getConfig().entities.miscEntitiesConfig.tnt_defusing) {

            if (!this.level().isClientSide()) {

                if (canDefuse) {
                    OCommonMethods.defusePrimedTNT(this.level(), this.primedTnt, blockPos, ModSoundEvents.TNT_SHEAR);
                    player.gameEvent(GameEvent.SHEAR);
                    itemStack.hurtAndBreak(1, player, (shears) -> shears.broadcastBreakEvent(interactionHand));
                }
                return InteractionResult.CONSUME;

            } else {
                if (canDefuse) this.level().addParticle(ParticleTypes.LARGE_SMOKE, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), 0.0D, 0.0D, 0.0D);
                return canDefuse ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }
        }

        return super.interact(player, interactionHand);
    }
}