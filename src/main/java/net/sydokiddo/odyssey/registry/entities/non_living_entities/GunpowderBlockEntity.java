package net.sydokiddo.odyssey.registry.entities.non_living_entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GunpowderBlockEntity extends Entity implements TraceableEntity {

    private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(GunpowderBlockEntity.class, EntityDataSerializers.INT);

    @Nullable public LivingEntity owner;
    private final String fuseString = "Fuse";

    public GunpowderBlockEntity(EntityType<? extends GunpowderBlockEntity> entityType, Level level) {
        super(entityType, level);
    }

    // region NBT

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_FUSE_ID, 1);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putShort(fuseString, (short) this.getFuse());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.setFuse(compoundTag.getShort(fuseString));
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }

    private void setFuse(int i) {
        this.entityData.set(DATA_FUSE_ID, i);
    }

    // endregion

    // region Ticking

    @Override
    public void tick() {

        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
        }

        int fuse = this.getFuse() - 1;
        this.setFuse(fuse);

        if (fuse <= 0) {

            this.discard();

            if (!this.level().isClientSide) {
                this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), 5.0F, true, Level.ExplosionInteraction.TNT);
            }

        } else {
            this.updateInWaterStateAndDoFluidPushing();
        }
    }

    // endregion

    // region Misc Methods

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Nullable @Override
    public Entity getOwner() {
        return this.owner;
    }

    // endregion
}