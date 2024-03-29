package net.sydokiddo.odyssey.registry.entities.non_living_entities;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GunpowderBlockEntity extends Entity implements TraceableEntity {

    private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(GunpowderBlockEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(GunpowderBlockEntity.class, EntityDataSerializers.BLOCK_STATE);

    @Nullable public LivingEntity owner;
    private final String fuseString = "fuse";
    private final String blockStateString = "block_state";

    public GunpowderBlockEntity(EntityType<? extends GunpowderBlockEntity> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    // region Tags

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_FUSE_ID, 1);
        this.getEntityData().define(DATA_BLOCK_STATE_ID, ModBlocks.GUNPOWDER_BLOCK.defaultBlockState());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tags) {
        tags.putShort(this.fuseString, (short) this.getFuse());
        tags.put(this.blockStateString, NbtUtils.writeBlockState(this.getBlockState()));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tags) {
        this.setFuse(tags.getShort(this.fuseString));
        if (tags.contains(this.blockStateString, 10)) this.setBlockState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), tags.getCompound(this.blockStateString)));
    }

    public int getFuse() {
        return this.getEntityData().get(DATA_FUSE_ID);
    }

    private void setFuse(int fuseTime) {
        this.getEntityData().set(DATA_FUSE_ID, fuseTime);
    }

    public BlockState getBlockState() {
        return this.getEntityData().get(DATA_BLOCK_STATE_ID);
    }

    public void setBlockState(BlockState blockState) {
        this.getEntityData().set(DATA_BLOCK_STATE_ID, blockState);
    }

    // endregion

    // region Ticking

    @Override
    public void tick() {

        if (!this.isNoGravity()) this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        if (this.onGround()) this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));

        int fuse = this.getFuse() - 1;
        this.setFuse(fuse);

        if (fuse <= 0) {
            this.discard();
            if (!this.level().isClientSide()) this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), 5.0F, true, Level.ExplosionInteraction.TNT);
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
    protected Entity.@NotNull MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return this.owner;
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return 0.15F;
    }

    @Override
    public void restoreFrom(Entity entity) {
        super.restoreFrom(entity);
        if (entity instanceof GunpowderBlockEntity gunpowderBlockEntity) this.owner = gunpowderBlockEntity.owner;
    }

    // endregion
}