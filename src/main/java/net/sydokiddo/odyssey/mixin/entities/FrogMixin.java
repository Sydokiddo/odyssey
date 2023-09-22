package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.misc.util.mobs.ContainerMob;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Frog.class)
public abstract class FrogMixin extends Animal implements ContainerMob {

    // Frogs can now be picked up in Empty Buckets

    private static final String VARIANT_TAG = "variant";

    @Shadow public abstract FrogVariant getVariant();
    @Shadow public abstract void setVariant(FrogVariant frogVariant);

    private FrogMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean fromItem() {
        return true;
    }

    @Override
    public void setFromItem(boolean bl) {}

    @SuppressWarnings("ALL")
    @Override
    public void saveToItemTag(ItemStack itemStack) {
        ContainerMob.saveDefaultDataToItemTag(this, itemStack);
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.putString(VARIANT_TAG, BuiltInRegistries.FROG_VARIANT.getKey(this.getVariant()).toString());
    }

    @SuppressWarnings("ALL")
    @Override
    public void loadFromItemTag(CompoundTag compoundTag) {

        ContainerMob.loadDefaultDataFromItemTag(this, compoundTag);
        FrogVariant frogVariant = (FrogVariant)BuiltInRegistries.FROG_VARIANT.get(ResourceLocation.tryParse(compoundTag.getString(VARIANT_TAG)));

        if (frogVariant != null) {
            this.setVariant(frogVariant);
        }
    }

    @Override
    public ItemStack getResultItemStack() {
        return new ItemStack(ModItems.FROG_BUCKET);
    }

    @Override
    public SoundEvent getPickupSound() {
        return ModSoundEvents.BUCKET_FILL_FROG;
    }

    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand interactionHand) {

        ItemStack itemInHand = player.getItemInHand(interactionHand);
        Item containerItem = Items.BUCKET;

        if (this.isAlive() && itemInHand.is(containerItem) && Odyssey.getConfig().entities.bucketable_frogs) {
            return ContainerMob.containerMobPickup(player, interactionHand, this, containerItem).orElse(super.mobInteract(player, interactionHand));
        }
        return super.mobInteract(player, interactionHand);
    }
}