package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Strider.class)
public abstract class StriderMixin extends Animal implements Saddleable {

    @Shadow @Final private ItemBasedSteering steering;

    private StriderMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    // Saddles can be un-equipped from Striders

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void odyssey$removeSaddlesFromStriders(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.isSaddled() && player.isDiscrete() && player.getItemInHand(interactionHand).isEmpty() && Odyssey.getConfig().entities.passiveMobsConfig.saddle_removing) {
            this.steering.setSaddle(false);
            OdysseyRegistry.doSaddleRemovingEvents(this, player, interactionHand);
            cir.setReturnValue(InteractionResult.sidedSuccess(player.level().isClientSide()));
        }
    }
}