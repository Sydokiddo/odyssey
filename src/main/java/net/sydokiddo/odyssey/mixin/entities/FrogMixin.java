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
import java.util.Objects;

@Mixin(Frog.class)
public abstract class FrogMixin extends Animal implements ContainerMob {

    @Shadow public abstract FrogVariant getVariant();
    @Shadow public abstract void setVariant(FrogVariant frogVariant);

    private FrogMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    // region Frog Bucketing

    @Override
    public boolean fromItem() {
        return true;
    }

    @Override
    public void setFromItem(boolean fromItem) {}

    @Override
    public void saveToItemTag(ItemStack itemStack) {
        ContainerMob.saveDefaultDataToItemTag(this, itemStack);
        itemStack.getOrCreateTag().putString(Frog.VARIANT_KEY, Objects.requireNonNull(BuiltInRegistries.FROG_VARIANT.getKey(this.getVariant())).toString());
    }

    @Override
    public void loadFromItemTag(CompoundTag compoundTag) {

        ContainerMob.loadDefaultDataFromItemTag(this, compoundTag);
        FrogVariant frogVariant = BuiltInRegistries.FROG_VARIANT.get(ResourceLocation.tryParse(compoundTag.getString(Frog.VARIANT_KEY)));

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
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand interactionHand) {

        Item containerItem = Items.BUCKET;

        if (this.isAlive() && player.getItemInHand(interactionHand).is(containerItem) && Odyssey.getConfig().entities.passiveMobsConfig.bucketable_frogs) {
            return ContainerMob.containerMobPickup(player, interactionHand, this, containerItem).orElse(super.mobInteract(player, interactionHand));
        }
        return super.mobInteract(player, interactionHand);
    }

    // endregion
}