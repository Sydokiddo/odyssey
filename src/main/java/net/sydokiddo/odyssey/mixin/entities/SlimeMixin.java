package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slime.class)
public abstract class SlimeMixin extends Mob {

    // Slimes can be converted into Magma Cubes and vise-versa

    @Shadow public abstract int getSize();

    private static final String MAGMA_CONVERSION_TAG = "MagmaCubeConversionTime";
    private static final String SLIME_CONVERSION_TAG = "SlimeConversionTime";

    private int onMagmaTime;
    private int inWaterTime;
    private int conversionTime;

    private SlimeMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private boolean isMagmaConverting() {
        return this.getEntityData().get(OdysseyRegistry.MAGMA_CUBE_CONVERSION);
    }

    @Unique
    private void setMagmaConverting(boolean bl) {
        this.entityData.set(OdysseyRegistry.MAGMA_CUBE_CONVERSION, bl);
    }

    @Unique
    private boolean isSlimeConverting() {
        return this.getEntityData().get(OdysseyRegistry.SLIME_CONVERSION);
    }

    @Unique
    private void setSlimeConverting(boolean bl) {
        this.entityData.set(OdysseyRegistry.SLIME_CONVERSION, bl);
    }

    @Inject(at = @At("HEAD"), method = "defineSynchedData")
    private void odyssey_defineSlimeSyncedData(CallbackInfo ci) {
        this.getEntityData().define(OdysseyRegistry.MAGMA_CUBE_CONVERSION, false);
        this.getEntityData().define(OdysseyRegistry.SLIME_CONVERSION, false);
    }

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void odyssey_addSlimeAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putInt(MAGMA_CONVERSION_TAG, this.isMagmaConverting() ? this.conversionTime : -1);
        compoundTag.putInt(SLIME_CONVERSION_TAG, this.isSlimeConverting() ? this.conversionTime : -1);
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
    private void odyssey_readSlimeAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        if (Odyssey.getConfig().entityChanges.slime_and_magma_cube_converting) {
            if (compoundTag.contains(MAGMA_CONVERSION_TAG, 99) && compoundTag.getInt(MAGMA_CONVERSION_TAG) > -1) {
                this.startMagmaConversion(compoundTag.getInt(MAGMA_CONVERSION_TAG));
            } else if (compoundTag.contains(SLIME_CONVERSION_TAG, 99) && compoundTag.getInt(SLIME_CONVERSION_TAG) > -1) {
                this.startSlimeConversion(compoundTag.getInt(SLIME_CONVERSION_TAG));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getJumpDelay", cancellable = true)
    private void odyssey_preventSlimeJumping(CallbackInfoReturnable<Integer> cir) {
        if ((this.isSlimeConverting() || this.isMagmaConverting()) && Odyssey.getConfig().entityChanges.slime_and_magma_cube_converting) {
            cir.setReturnValue(conversionTime);
        }
    }

    // Slimes no longer take damage from Magma as Magma Blocks will now convert them instead

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (damageSource.is(DamageTypes.HOT_FLOOR) && Odyssey.getConfig().entityChanges.slime_and_magma_cube_converting) {
            return true;
        }
        return super.isInvulnerableTo(damageSource);
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void odyssey_tickSlimeConversion(CallbackInfo ci) {

        if (!this.level().isClientSide && this.isAlive() && Odyssey.getConfig().entityChanges.slime_and_magma_cube_converting) {

            if (this.getType() == EntityType.SLIME) {

                if (this.getBlockStateOn().is(Blocks.MAGMA_BLOCK)) {

                    if (this.isMagmaConverting()) {
                        --this.conversionTime;
                        if (this.conversionTime < 0) {
                            this.doMagmaConversion();
                        }
                    } else {
                        ++this.onMagmaTime;
                        if (this.onMagmaTime >= 40) {
                            this.startMagmaConversion(200);
                        }
                    }
                } else {
                    this.onMagmaTime = -1;
                    this.setMagmaConverting(false);
                }
            }

            else if (this.getType() == EntityType.MAGMA_CUBE) {

                if (this.isInWater()) {

                    if (this.isSlimeConverting()) {
                        --this.conversionTime;
                        if (this.conversionTime < 0) {
                            this.doSlimeConversion();
                        }
                    } else {
                        ++this.inWaterTime;
                        if (this.inWaterTime >= 40) {
                            this.startSlimeConversion(200);
                        }
                    }
                } else {
                    this.inWaterTime = -1;
                    this.setSlimeConverting(false);
                }
            }
        }
    }

    @Unique
    private void startMagmaConversion(int i) {
        this.conversionTime = i;
        this.setMagmaConverting(true);
    }

    @Unique
    private void startSlimeConversion(int i) {
        this.conversionTime = i;
        this.setSlimeConverting(true);
    }

    @Unique
    private void doMagmaConversion() {

        MagmaCube magmaCube = EntityType.MAGMA_CUBE.create(this.level());

        if (magmaCube != null) {

            magmaCube.setSize(this.getSize(), true);
            magmaCube.copyPosition(this);
            magmaCube.setNoAi(this.isNoAi());
            magmaCube.setInvulnerable(this.isInvulnerable());

            if (this.hasCustomName()) {
                magmaCube.setCustomName(this.getCustomName());
                magmaCube.setCustomNameVisible(this.isCustomNameVisible());
            }
            if (this.isPersistenceRequired()) {
                magmaCube.setPersistenceRequired();
            }

            if (!this.isSilent()) {
                this.playSound(ModSoundEvents.SLIME_CONVERTED_TO_MAGMA_CUBE);
            }

            this.level().addFreshEntity(magmaCube);

            if (this.isPassenger()) {
                Entity entity = this.getVehicle();
                this.stopRiding();
                magmaCube.startRiding(entity, true);
            }

            this.discard();
        }
    }

    @Unique
    private void doSlimeConversion() {

        Slime slime = EntityType.SLIME.create(this.level());

        if (slime != null) {

            slime.setSize(this.getSize(), true);
            slime.copyPosition(this);
            slime.setNoAi(this.isNoAi());
            slime.setInvulnerable(this.isInvulnerable());

            if (this.hasCustomName()) {
                slime.setCustomName(this.getCustomName());
                slime.setCustomNameVisible(this.isCustomNameVisible());
            }
            if (this.isPersistenceRequired()) {
                slime.setPersistenceRequired();
            }

            if (!this.isSilent()) {
                this.playSound(ModSoundEvents.MAGMA_CUBE_CONVERTED_TO_SLIME);
            }

            this.level().addFreshEntity(slime);

            if (this.isPassenger()) {
                Entity entity = this.getVehicle();
                this.stopRiding();
                slime.startRiding(entity, true);
            }

            this.discard();
        }
    }
}
