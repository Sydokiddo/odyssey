package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityAccess;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAccess {

    @Shadow public abstract BlockPos getOnPos();
    @Shadow public abstract double getY();
    @Shadow public abstract BlockState getFeetBlockState();

    @Unique
    private double getCauldronContentHeight(BlockState blockState) {
        return (6.0 + (double) blockState.getValue(LayeredCauldronBlock.LEVEL) * 3.0) / 16.0;
    }

    @Unique
    private boolean isEntityInsideCauldron(BlockState blockState, BlockPos blockPos) {
        return blockState.getBlock() == Blocks.WATER_CAULDRON && this.getY() < (double)blockPos.getY() + getCauldronContentHeight(blockState) && this.getBoundingBox().maxY > (double)blockPos.getY() + 0.25;
    }

    @Inject(at = @At("RETURN"), method = "isInWaterOrRain", cancellable = true)
    private void odyssey$allowRiptideInWaterCauldrons(CallbackInfoReturnable<Boolean> cir) {
        if (isEntityInsideCauldron(this.getFeetBlockState(), this.getOnPos()) && Odyssey.getConfig().items.improved_riptide) {
            cir.setReturnValue(true);
        }
    }
}