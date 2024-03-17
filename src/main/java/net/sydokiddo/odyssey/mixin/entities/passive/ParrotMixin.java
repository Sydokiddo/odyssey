package net.sydokiddo.odyssey.mixin.entities.passive;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Parrot.class)
public abstract class ParrotMixin extends ShoulderRidingEntity {

    @Shadow public abstract boolean isFood(ItemStack itemStack);

    private ParrotMixin(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "createAttributes", cancellable = true)
    private static void odyssey$improvedParrotAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        if (Odyssey.getConfig().entities.passiveMobsConfig.improved_parrots) {
            cir.setReturnValue(Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FLYING_SPEED, 0.4F).add(Attributes.MOVEMENT_SPEED, 0.2F));
        }
    }

    @Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
    private void odyssey$parrotFeeding(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (this.isTame() && this.getHealth() < this.getMaxHealth() && this.isFood(itemStack) && Odyssey.getConfig().entities.passiveMobsConfig.improved_parrots) {

            if (!player.getAbilities().instabuild) itemStack.shrink(1);
            if (!this.isSilent()) this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARROT_EAT, this.getSoundSource(), 1.0F, 1.0F + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F);

            if (!this.level().isClientSide()) {
                this.heal(this.getHealth() / 4);
                this.level().broadcastEntityEvent(this, (byte) 7);
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
        }
    }
}