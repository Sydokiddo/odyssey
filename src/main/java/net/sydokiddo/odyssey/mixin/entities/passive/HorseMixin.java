package net.sydokiddo.odyssey.mixin.entities.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorse.class)
public abstract class HorseMixin extends Animal {

    private HorseMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "playStepSound", at = @At("RETURN"))
    private void odyssey$playHorseWalkingThroughLeavesSounds(BlockPos blockPos, BlockState blockState, CallbackInfo info) {

        BlockState aboveState = this.level().getBlockState(blockPos.above());
        BlockState aboveAboveState = this.level().getBlockState(blockPos.above().above());
        TagKey<Block> leavesTag = BlockTags.LEAVES;

        if ((aboveState.is(leavesTag) || aboveAboveState.is(leavesTag)) && Odyssey.getConfig().entities.passiveMobsConfig.improved_horses) {

            SoundType soundType;

            if (aboveState.is(leavesTag)) {
                soundType = aboveState.getSoundType();
            } else {
                soundType = aboveAboveState.getSoundType();
            }

            this.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15F, soundType.getPitch());
        }
    }
}