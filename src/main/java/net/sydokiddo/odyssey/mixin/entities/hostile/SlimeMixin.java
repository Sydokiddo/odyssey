package net.sydokiddo.odyssey.mixin.entities.hostile;

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
import net.sydokiddo.odyssey.registry.misc.OCommonMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slime.class)
public abstract class SlimeMixin extends Mob {

    @Shadow public abstract int getSize();

    @Unique private static final String MAGMA_CONVERSION_TAG = "MagmaCubeConversionTime";
    @Unique private static final String SLIME_CONVERSION_TAG = "SlimeConversionTime";

    @Unique private int onMagmaTime;
    @Unique private int inWaterTime;
    @Unique private int conversionTime;

    private SlimeMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    // region NBT

    @Unique
    private boolean isMagmaConverting() {
        return this.getEntityData().get(OdysseyRegistry.MAGMA_CUBE_CONVERSION);
    }

    @Unique
    private void setMagmaConverting(boolean isMagmaConverting) {
        this.entityData.set(OdysseyRegistry.MAGMA_CUBE_CONVERSION, isMagmaConverting);
    }

    @Unique
    private boolean isSlimeConverting() {
        return this.getEntityData().get(OdysseyRegistry.SLIME_CONVERSION);
    }

    @Unique
    private void setSlimeConverting(boolean isSlimeConverting) {
        this.entityData.set(OdysseyRegistry.SLIME_CONVERSION, isSlimeConverting);
    }

    @Inject(at = @At("HEAD"), method = "defineSynchedData")
    private void odyssey$defineSlimeNBT(CallbackInfo info) {
        this.getEntityData().define(OdysseyRegistry.MAGMA_CUBE_CONVERSION, false);
        this.getEntityData().define(OdysseyRegistry.SLIME_CONVERSION, false);
    }

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void odyssey$addSlimeNBT(CompoundTag compoundTag, CallbackInfo info) {
        compoundTag.putInt(MAGMA_CONVERSION_TAG, this.isMagmaConverting() ? this.conversionTime : -1);
        compoundTag.putInt(SLIME_CONVERSION_TAG, this.isSlimeConverting() ? this.conversionTime : -1);
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
    private void odyssey$readSlimeNBT(CompoundTag compoundTag, CallbackInfo info) {
        if (Odyssey.getConfig().entities.hostileMobsConfig.slime_and_magma_cube_converting) {
            if (compoundTag.contains(MAGMA_CONVERSION_TAG, 99) && compoundTag.getInt(MAGMA_CONVERSION_TAG) > -1) {
                this.startMagmaConversion(compoundTag.getInt(MAGMA_CONVERSION_TAG));
            } else if (compoundTag.contains(SLIME_CONVERSION_TAG, 99) && compoundTag.getInt(SLIME_CONVERSION_TAG) > -1) {
                this.startSlimeConversion(compoundTag.getInt(SLIME_CONVERSION_TAG));
            }
        }
    }

    // endregion

    // region Slime and Magma Cube Converting

    @Inject(at = @At("RETURN"), method = "tick")
    private void odyssey$tickSlimeConversion(CallbackInfo info) {

        if (!this.level().isClientSide() && this.isAlive() && Odyssey.getConfig().entities.hostileMobsConfig.slime_and_magma_cube_converting) {

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
    private void startMagmaConversion(int time) {
        this.conversionTime = time;
        this.setMagmaConverting(true);
    }

    @Unique
    private void startSlimeConversion(int time) {
        this.conversionTime = time;
        this.setSlimeConverting(true);
    }

    @Unique
    private void doMagmaConversion() {

        MagmaCube magmaCube = EntityType.MAGMA_CUBE.create(this.level());

        if (magmaCube != null) {
            if (!this.isSilent()) this.playSound(ModSoundEvents.SLIME_CONVERTED_TO_MAGMA_CUBE);
            magmaCube.setSize(this.getSize(), true);
            doEntityConversionEvents(magmaCube);
            this.discard();
        }
    }

    @Unique
    private void doSlimeConversion() {

        Slime slime = EntityType.SLIME.create(this.level());

        if (slime != null) {
            if (!this.isSilent()) this.playSound(ModSoundEvents.MAGMA_CUBE_CONVERTED_TO_SLIME);
            slime.setSize(this.getSize(), true);
            doEntityConversionEvents(slime);
            this.discard();
        }
    }

    @Unique
    private void doEntityConversionEvents(Mob mob) {

        mob.copyPosition(this);
        mob.setNoAi(this.isNoAi());
        mob.setInvulnerable(this.isInvulnerable());

        if (this.hasCustomName()) {
            mob.setCustomName(this.getCustomName());
            mob.setCustomNameVisible(this.isCustomNameVisible());
        }
        if (this.isPersistenceRequired()) {
            mob.setPersistenceRequired();
        }

        if (this.isPassenger()) {
            Entity vehicle = this.getVehicle();
            this.stopRiding();
            mob.startRiding(vehicle, true);
        }

        OCommonMethods.sendMobConversionDebugMessage(this, mob);
        this.level().addFreshEntity(mob);
    }

    // endregion

    // region Misc Methods

    @Inject(at = @At("HEAD"), method = "getJumpDelay", cancellable = true)
    private void odyssey$preventSlimeJumpingWhileConverting(CallbackInfoReturnable<Integer> cir) {
        if ((this.isSlimeConverting() || this.isMagmaConverting()) && Odyssey.getConfig().entities.hostileMobsConfig.slime_and_magma_cube_converting) {
            cir.setReturnValue(conversionTime);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (damageSource.is(DamageTypes.HOT_FLOOR) && Odyssey.getConfig().entities.hostileMobsConfig.slime_and_magma_cube_converting) {
            return true;
        }
        return super.isInvulnerableTo(damageSource);
    }

    // endregion
}