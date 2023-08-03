package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin extends HangingEntity {

    @Shadow public abstract ItemStack getItem();
    @Shadow public abstract SoundEvent getRotateItemSound();

    private static final String WAXED_TAG = "Waxed";

    private ItemFrameMixin(EntityType<? extends HangingEntity> entityType, Level level) {
        super(entityType, level);
    }

    // Allows for filled Item Frames and Glow Item Frames to be waxed with Honeycomb to prevent item rotation

    @Unique
    private boolean isWaxed() {
        return this.getEntityData().get(OdysseyRegistry.WAXED);
    }

    @Unique
    private void setWaxed(boolean bl) {
        this.entityData.set(OdysseyRegistry.WAXED, bl);
    }

    @Unique
    private void displayPoofParticles() {
        if (level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 5; ++i) {
                double x = (double) this.pos.getX() + 0.5D;
                double y = (double) this.pos.getY() + 0.5D;
                double z = (double) this.pos.getZ() + 0.5D;
                serverLevel.sendParticles(ParticleTypes.POOF, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "defineSynchedData")
    private void odyssey_defineItemFrameSyncedData(CallbackInfo ci) {
        this.getEntityData().define(OdysseyRegistry.WAXED, false);
    }

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void odyssey_addItemFrameAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean(WAXED_TAG, this.isWaxed());
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
    private void odyssey_readItemFrameAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        this.setWaxed(compoundTag.getBoolean(WAXED_TAG));
    }

    @Inject(at = @At("HEAD"), method = "interact", cancellable = true)
    private void odyssey_waxingAndShearingItemFrames(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!this.getItem().isEmpty() && player.mayBuild()) {

            if (itemStack.is(Items.HONEYCOMB) && !this.isWaxed() && Odyssey.getConfig().entities.item_frame_waxing) {

                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }

                this.setWaxed(true);
                this.level().levelEvent(player, 3003, this.pos, 0);

                this.gameEvent(GameEvent.BLOCK_CHANGE, player);
                player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                cir.setReturnValue(InteractionResult.SUCCESS);
            }

            if (itemStack.is(Items.SHEARS) && !this.isInvisible() && Odyssey.getConfig().entities.item_frame_shearing) {

                if (!player.isCreative()) {
                    itemStack.hurtAndBreak(1, player, playerx -> player.broadcastBreakEvent(interactionHand));
                }

                this.setInvisible(true);
                this.playSound(ModSoundEvents.ITEM_FRAME_SHEAR, 1.0f, 1.0f);
                this.displayPoofParticles();

                this.gameEvent(GameEvent.BLOCK_CHANGE, player);
                player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

    // Makes the item frame visible again if invisible without an item in it

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/entity/Entity;)V"))
    private void odyssey_makeItemFrameVisibleUponRemovingItem(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (this.isInvisible()) {
            this.setInvisible(false);
            this.displayPoofParticles();
        }
    }

    // Prevents the item  in the item frame from being rotated if it is waxed

    @Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;setRotation(I)V"), cancellable = true)
    private void odyssey_cancelRotationIfItemFrameWaxed(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.isWaxed()) {
            cir.cancel();
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Redirect(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void odyssey_playSeparateSoundIfItemFrameWaxed(ItemFrame instance, SoundEvent soundEvent, float v, float w) {
        if (this.isWaxed()) {
            if (this.getType() == EntityType.GLOW_ITEM_FRAME) {
                this.playSound(ModSoundEvents.WAXED_GLOW_ITEM_FRAME_INTERACT_FAIL, 1.0f, 1.0f);
            } else {
                this.playSound(ModSoundEvents.WAXED_ITEM_FRAME_INTERACT_FAIL, 1.0f, 1.0f);
            }
        } else {
            this.playSound(this.getRotateItemSound(), 1.0f, 1.0f);
        }
    }
}