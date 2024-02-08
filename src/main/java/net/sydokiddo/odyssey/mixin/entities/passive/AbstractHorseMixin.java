package net.sydokiddo.odyssey.mixin.entities.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.items.custom_items.OwnershipContractItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Animal {

    @Unique AbstractHorse abstractHorse = (AbstractHorse) (Object) this;
    @Unique private static final Item ownershipContract = ModItems.OWNERSHIP_CONTRACT;

    private AbstractHorseMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "playStepSound", at = @At("RETURN"))
    private void odyssey$playHorseWalkingThroughLeavesSounds(BlockPos blockPos, BlockState blockState, CallbackInfo info) {

        BlockState aboveState = this.level().getBlockState(blockPos.above());
        BlockState aboveAboveState = this.level().getBlockState(blockPos.above().above());
        TagKey<Block> leavesTag = BlockTags.LEAVES;

        if (Odyssey.getConfig().entities.passiveMobsConfig.improved_horses && (aboveState.is(leavesTag) || aboveAboveState.is(leavesTag))) {
            SoundType soundType = aboveState.is(leavesTag) ? aboveState.getSoundType() : aboveAboveState.getSoundType();
            this.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15F, soundType.getPitch());
        }
    }

    @Redirect(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSecondaryUseActive()Z"))
    private boolean odyssey$preventOpeningHorseInventoryWithOwnershipContract(Player player) {
        return (player.isSecondaryUseActive() && !(player.getMainHandItem().is(ownershipContract) && !OwnershipContractItem.isContractBound(player.getMainHandItem()) || player.getOffhandItem().is(ownershipContract) && !OwnershipContractItem.isContractBound(player.getOffhandItem())));
    }

    @Inject(method = "doPlayerRide", at = @At("HEAD"), cancellable = true)
    private void odyssey$preventRidingHorseWithOwnershipContract(Player player, CallbackInfo info) {

        if (abstractHorse instanceof Camel) return;

        if (player.getMainHandItem().is(ownershipContract) && !OwnershipContractItem.isContractBound(player.getMainHandItem()) || player.getOffhandItem().is(ownershipContract) && !OwnershipContractItem.isContractBound(player.getOffhandItem())) {
            info.cancel();
        }
    }
}