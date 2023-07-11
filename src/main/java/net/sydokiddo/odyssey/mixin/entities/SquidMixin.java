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
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({Squid.class})
public class SquidMixin extends WaterAnimal implements Bucketable {

    // Squids and Glow Squids can now be picked up in Water Buckets

    private SquidMixin(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean fromBucket() {
        return true;
    }

    @Override
    public void setFromBucket(boolean bl) {}

    @SuppressWarnings("ALL")
    @Override
    public void saveToBucketTag(ItemStack itemStack) {
        Bucketable.saveDefaultDataToBucketTag(this, itemStack);
    }

    @SuppressWarnings("ALL")
    @Override
    public void loadFromBucketTag(CompoundTag compoundTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag);
    }

    @Override
    public ItemStack getBucketItemStack() {
        if (this.getType() == EntityType.GLOW_SQUID) {
            return new ItemStack(ModItems.GLOW_SQUID_BUCKET);
        } else {
            return new ItemStack(ModItems.SQUID_BUCKET);
        }
    }

    @Override
    public SoundEvent getPickupSound() {
        if (this.getType() == EntityType.GLOW_SQUID) {
            return ModSoundEvents.BUCKET_FILL_GLOW_SQUID;
        } else {
            return ModSoundEvents.BUCKET_FILL_SQUID;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand interactionHand) {
        if (this.isAlive() && Odyssey.getConfig().entityChanges.bucketable_squids) {
            return Bucketable.bucketMobPickup(player, interactionHand, this).orElse(super.mobInteract(player, interactionHand));
        }
        return super.mobInteract(player, interactionHand);
    }
}
