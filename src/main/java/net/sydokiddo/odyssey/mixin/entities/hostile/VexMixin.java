package net.sydokiddo.odyssey.mixin.entities.hostile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.entities.ContainerMob;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vex.class)
public abstract class VexMixin extends Monster implements TraceableEntity, ContainerMob {

    @Shadow private int limitedLifeTicks;
    @Shadow private boolean hasLimitedLife;
    @Shadow public abstract void setLimitedLife(int limitedLifeTicks);
    @Shadow public abstract void setOwner(Mob mob);

    @Unique private static final String FROM_BOOK_TAG = "from_book";
    @Unique private static final String LIFE_TICKS_TAG = "LifeTicks";

    private VexMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    // Vexes die alongside the Evoker that initially summoned them

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void odyssey$killVexesOnEvokerDeath(CallbackInfo info) {

        Entity owner = this.getOwner();

        if (!this.level().isClientSide() && Odyssey.getConfig().entities.hostileMobsConfig.vexes_die_with_evokers && owner instanceof Evoker && !owner.isAlive() && !this.fromItem()) {
            if (Chrysalis.IS_DEBUG) Odyssey.LOGGER.info("{} has been killed as its owner {} is no longer alive", this.getName().getString(), owner.getName().getString());
            this.kill();
        }
    }

    // region Tags

    @Inject(at = @At("HEAD"), method = "defineSynchedData")
    private void odyssey$defineVexTags(CallbackInfo info) {
        this.getEntityData().define(OdysseyRegistry.VEX_FROM_BOOK, false);
    }

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void odyssey$addVexTags(CompoundTag compoundTag, CallbackInfo info) {
        compoundTag.putBoolean(FROM_BOOK_TAG, this.fromItem());
    }

    @Override
    public boolean fromItem() {
        return this.getEntityData().get(OdysseyRegistry.VEX_FROM_BOOK);
    }

    @Override
    public void setFromItem(boolean fromItem) {
        this.getEntityData().set(OdysseyRegistry.VEX_FROM_BOOK, fromItem);
    }

    @Override
    public void saveToItemTag(ItemStack itemStack) {
        if (this.hasLimitedLife) itemStack.getOrCreateTag().putInt(LIFE_TICKS_TAG, this.limitedLifeTicks);
        ContainerMob.saveDefaultDataToItemTag(this, itemStack);
    }

    @Override
    public void loadFromItemTag(CompoundTag compoundTag) {
        if (compoundTag.contains(LIFE_TICKS_TAG)) this.setLimitedLife(compoundTag.getInt(LIFE_TICKS_TAG));
        ContainerMob.loadDefaultDataFromItemTag(this, compoundTag);
    }

    // endregion

    @Override
    public ItemStack getResultItemStack() {
        return new ItemStack(ModItems.VEX_BOUND_BOOK);
    }

    @Override
    public SoundEvent getPickupSound() {
        return ModSoundEvents.BOOK_CAPTURE_VEX;
    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player player, InteractionHand interactionHand) {

        Item containerItem = Items.BOOK;

        if (this.isAlive() && player.getItemInHand(interactionHand).is(containerItem) && Odyssey.getConfig().entities.miscEntitiesConfig.capture_allays_and_vexes_in_books) {
            this.setOwner(null);
            return ContainerMob.containerMobPickup(player, interactionHand, this, containerItem).orElse(super.mobInteract(player, interactionHand));
        }

        return super.mobInteract(player, interactionHand);
    }
}