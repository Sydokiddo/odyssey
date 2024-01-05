package net.sydokiddo.odyssey.mixin.entities.passive;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.misc.util.entities.ContainerMob;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Allay.class)
public abstract class AllayMixin extends PathfinderMob implements ContainerMob, InventoryCarrier {

    @Shadow protected abstract boolean canDuplicate();
    @Shadow private long duplicationCooldown;
    @Shadow @Final private static EntityDataAccessor<Boolean> DATA_CAN_DUPLICATE;
    @Shadow public abstract boolean hasItemInHand();

    @Shadow @Final private SimpleContainer inventory;
    @Unique private static final String FROM_BOOK_TAG = "FromBook";
    @Unique private static final String DUPLICATION_COOLDOWN_TAG = "DuplicationCooldown";
    @Unique private static final String CAN_DUPLICATE_TAG = "CanDuplicate";

    private AllayMixin(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    // region NBT

    @Inject(at = @At("HEAD"), method = "defineSynchedData")
    private void odyssey$defineAllayNBT(CallbackInfo info) {
        this.entityData.define(OdysseyRegistry.ALLAY_FROM_BOOK, false);
    }

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void odyssey$addAllayNBT(CompoundTag compoundTag, CallbackInfo info) {
        compoundTag.putBoolean(FROM_BOOK_TAG, this.fromItem());
    }

    @Override
    public boolean fromItem() {
        return this.entityData.get(OdysseyRegistry.ALLAY_FROM_BOOK);
    }

    @Override
    public void setFromItem(boolean fromItem) {
        this.entityData.set(OdysseyRegistry.ALLAY_FROM_BOOK, fromItem);
    }

    @Override
    public void saveToItemTag(ItemStack itemStack) {
        itemStack.getOrCreateTag().putLong(DUPLICATION_COOLDOWN_TAG, this.duplicationCooldown);
        itemStack.getOrCreateTag().putBoolean(CAN_DUPLICATE_TAG, this.canDuplicate());
        ContainerMob.saveDefaultDataToItemTag(this, itemStack);
    }

    @Override
    public void loadFromItemTag(CompoundTag compoundTag) {
        this.duplicationCooldown = compoundTag.getInt(DUPLICATION_COOLDOWN_TAG);
        this.entityData.set(DATA_CAN_DUPLICATE, compoundTag.getBoolean(CAN_DUPLICATE_TAG));
        ContainerMob.loadDefaultDataFromItemTag(this, compoundTag);
    }

    // endregion

    @Override
    public ItemStack getResultItemStack() {
        return new ItemStack(ModItems.ALLAY_BOUND_BOOK);
    }

    @Override
    public SoundEvent getPickupSound() {
        return ModSoundEvents.BOOK_CAPTURE_ALLAY;
    }

    @Unique
    private boolean canPickUpAllay(Player player) {
        if (!this.hasItemInHand()) {
            return player.isShiftKeyDown();
        }
        return true;
    }

    @Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
    private void odyssey$captureAllayInBook(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {

        Item containerItem = Items.BOOK;

        if (this.isAlive() && player.getItemInHand(interactionHand).is(containerItem) && this.canPickUpAllay(player) && Odyssey.getConfig().entities.miscEntitiesConfig.capture_allays_and_vexes_in_books) {
            this.dropHeldItems();
            cir.setReturnValue(ContainerMob.containerMobPickup(player, interactionHand, this, containerItem).orElse(super.mobInteract(player, interactionHand)));
        }
    }

    @Unique
    private void dropHeldItems() {

        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);

        if (!itemStack.isEmpty()) {
            this.spawnAtLocation(itemStack);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }
}