package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Squid.class)
public class SquidMixin extends WaterAnimal implements Bucketable {

    @Unique private static final String FROM_BUCKET_TAG = "FromBucket";

    private SquidMixin(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
    }

    // region NBT

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OdysseyRegistry.FROM_BUCKET, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean(FROM_BUCKET_TAG, this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setFromBucket(compoundTag.getBoolean(FROM_BUCKET_TAG));
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(OdysseyRegistry.FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(OdysseyRegistry.FROM_BUCKET, fromBucket);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void saveToBucketTag(ItemStack itemStack) {
        Bucketable.saveDefaultDataToBucketTag(this, itemStack);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void loadFromBucketTag(CompoundTag compoundTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag);
    }

    // endregion

    // region Squid Bucketing

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        if (this.getType() == EntityType.GLOW_SQUID) {
            return new ItemStack(ModItems.GLOW_SQUID_BUCKET);
        } else {
            return new ItemStack(ModItems.SQUID_BUCKET);
        }
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        if (this.getType() == EntityType.GLOW_SQUID) {
            return ModSoundEvents.BUCKET_FILL_GLOW_SQUID;
        } else {
            return ModSoundEvents.BUCKET_FILL_SQUID;
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand interactionHand) {
        if (this.isAlive() && Odyssey.getConfig().entities.passiveMobsConfig.bucketable_squids) {
            return Bucketable.bucketMobPickup(player, interactionHand, this).orElse(super.mobInteract(player, interactionHand));
        }
        return super.mobInteract(player, interactionHand);
    }

    // endregion

    // region Misc Methods

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    // endregion
}