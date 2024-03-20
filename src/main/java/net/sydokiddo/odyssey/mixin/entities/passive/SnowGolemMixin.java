package net.sydokiddo.odyssey.mixin.entities.passive;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowGolem.class)
public abstract class SnowGolemMixin extends AbstractGolem {

    private SnowGolemMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract boolean hasPumpkin();
    @Shadow public abstract void setPumpkin(boolean hasPumpkin);

    @Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
    private void odyssey$snowGolemPumpkinInteraction(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!this.hasPumpkin() && itemStack.is(Items.CARVED_PUMPKIN) && Odyssey.getConfig().entities.passiveMobsConfig.snow_golem_pumpkin_equipping) {

            if (!player.getAbilities().instabuild) itemStack.shrink(1);
            if (!this.isSilent()) this.level().playSound(null, this, ModSoundEvents.SNOW_GOLEM_EQUIP_PUMPKIN, SoundSource.PLAYERS, 1.0F, 1.0F + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F);
            this.gameEvent(GameEvent.ENTITY_INTERACT, player);

            if (!this.level().isClientSide()) this.setPumpkin(true);
            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
        }
    }
}