package net.sydokiddo.odyssey.mixin.entities.misc;

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
import net.sydokiddo.chrysalis.Chrysalis;
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

    private ItemFrameMixin(EntityType<? extends HangingEntity> entityType, Level level) {
        super(entityType, level);
    }

    // region NBT

    @Unique private static final String WAXED_TAG = "Waxed";

    @Inject(at = @At("HEAD"), method = "defineSynchedData")
    private void odyssey$defineItemFrameNBT(CallbackInfo info) {
        this.getEntityData().define(OdysseyRegistry.WAXED, false);
    }

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void odyssey$addItemFrameNBT(CompoundTag compoundTag, CallbackInfo info) {
        compoundTag.putBoolean(WAXED_TAG, this.isWaxed());
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
    private void odyssey$readItemFrameNBT(CompoundTag compoundTag, CallbackInfo info) {
        this.setWaxed(compoundTag.getBoolean(WAXED_TAG));
    }

    @Unique
    private boolean isWaxed() {
        return this.getEntityData().get(OdysseyRegistry.WAXED);
    }

    @Unique
    private void setWaxed(boolean isWaxed) {
        this.entityData.set(OdysseyRegistry.WAXED, isWaxed);
    }

    // endregion

    // region Item Frame Interactions

    @Unique
    private void doItemFrameInteractionEvents(Player player, ItemStack itemStack) {
        this.gameEvent(GameEvent.BLOCK_CHANGE, player);
        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
    }

    @Inject(at = @At("HEAD"), method = "interact", cancellable = true)
    private void odyssey$waxingAndShearingItemFrames(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {

        ItemStack itemInHand = player.getItemInHand(interactionHand);

        if (!this.getItem().isEmpty() && player.mayBuild() && Odyssey.getConfig().entities.miscEntitiesConfig.item_frame_interactions) {

            // region Waxing

            if (itemInHand.is(Items.HONEYCOMB) && !this.isWaxed()) {

                this.setWaxed(true);
                this.level().levelEvent(player, 3003, this.pos, 0);
                this.doItemFrameInteractionEvents(player, itemInHand);

                if (!player.isCreative()) {
                    itemInHand.shrink(1);
                }

                if (Chrysalis.IS_DEBUG) {
                    Odyssey.LOGGER.info("{} has been successfully waxed by {}", this.getName().getString(), player.getName().getString());
                }

                cir.setReturnValue(InteractionResult.sidedSuccess(player.level().isClientSide()));
            }

            // endregion

            // region Shearing

            if (itemInHand.is(Items.SHEARS) && !this.isInvisible()) {

                this.setInvisible(true);
                this.playSound(ModSoundEvents.ITEM_FRAME_SHEAR, 1.0f, 1.0f);
                this.displayPoofParticles();
                this.doItemFrameInteractionEvents(player, itemInHand);

                if (!player.getAbilities().instabuild) {
                    itemInHand.hurtAndBreak(1, player, (shears) -> shears.broadcastBreakEvent(interactionHand));
                }

                if (Chrysalis.IS_DEBUG) {
                    Odyssey.LOGGER.info("Setting {} as invisible as it has been sheared by {}", this.getName().getString(), player.getName().getString());
                }

                cir.setReturnValue(InteractionResult.sidedSuccess(player.level().isClientSide()));
            }

            // endregion
        }
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/entity/Entity;)V"))
    private void odyssey$makeItemFrameVisibleUponRemovingItem(DamageSource damageSource, float damageAmount, CallbackInfoReturnable<Boolean> cir) {

        // Makes the item frame visible again if it is invisible without an item in it

        if (this.isInvisible()) {

            this.setInvisible(false);
            this.displayPoofParticles();

            if (Chrysalis.IS_DEBUG) {
                Odyssey.LOGGER.info("Setting {} as visible again as its item has been removed", this.getName().getString());
            }
        }
    }

    @Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;setRotation(I)V"), cancellable = true)
    private void odyssey$cancelRotationIfItemFrameIsWaxed(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {

        // Prevents the item inside the Item Frame from being rotated if the Item Frame is waxed

        if (this.isWaxed()) {

            if (Chrysalis.IS_DEBUG) {
                Odyssey.LOGGER.info("{} is waxed, preventing items inside of it from being rotated", this.getName().getString());
            }

            cir.cancel();
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    // endregion

    // region Particles and Sounds

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

    @Redirect(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void odyssey$playSeparateInteractionSoundWhenWaxed(ItemFrame itemFrame, SoundEvent soundEvent, float volume, float pitch) {

        SoundEvent interactionSound;

        if (this.isWaxed()) {
            if (this.getType() == EntityType.GLOW_ITEM_FRAME) {
                interactionSound = ModSoundEvents.WAXED_GLOW_ITEM_FRAME_INTERACT_FAIL;
            } else {
                interactionSound = ModSoundEvents.WAXED_ITEM_FRAME_INTERACT_FAIL;
            }
        } else {
            interactionSound = this.getRotateItemSound();
        }

        this.playSound(interactionSound, 1.0F, 1.0F);
    }

    // endregion
}