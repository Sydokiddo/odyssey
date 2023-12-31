package net.sydokiddo.odyssey.mixin.entities.misc;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.odyssey.registry.misc.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    private MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract boolean isAggressive();

    // Friendly mobs can be right-clicked with a brush to brush them :)

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void odyssey$brushFriendlyMobs(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {

        if (this.isAlive() && !this.isAggressive() && player.getItemInHand(interactionHand).is(Items.BRUSH) && this.getType().is(ModTags.CAN_BE_BRUSHED)) {

            cir.cancel();
            this.level().playSound(player, this.getOnPos(), SoundEvents.BRUSH_GENERIC, SoundSource.NEUTRAL);
            this.gameEvent(GameEvent.ENTITY_INTERACT, player);

            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.HEART, this.getX(), this.getEyeY() + 0.5, this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }

            player.awardStat(Stats.ITEM_USED.get(player.getItemInHand(interactionHand).getItem()));

            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
        }
    }
}