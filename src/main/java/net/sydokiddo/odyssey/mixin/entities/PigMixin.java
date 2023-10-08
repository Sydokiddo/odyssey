package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
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

@Mixin(Pig.class)
public abstract class PigMixin extends Animal implements Saddleable {

    @Shadow @Final private ItemBasedSteering steering;

    private PigMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    // Saddles can be un-equipped from Pigs

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void odyssey$removeSaddleFromPig(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.isSaddled() && player.isDiscrete() && player.getItemInHand(hand).isEmpty() && Odyssey.getConfig().entities.saddle_removing) {
            this.steering.setSaddle(false);
            OdysseyRegistry.doSaddleRemovingEvents(this, player, hand);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}