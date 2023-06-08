package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Strider.class)
public abstract class StriderMixin implements Saddleable {

    @Final @Shadow private ItemBasedSteering steering;

    // Saddles can be un-equipped from Striders

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void odyssey_removeSaddleFromStriders(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.isSaddled() && player.isDiscrete() && player.getItemInHand(hand).isEmpty() && Odyssey.getConfig().entityChanges.saddle_removing) {
            steering.setSaddle(false);
            player.setItemInHand(hand, Items.SADDLE.getDefaultInstance());
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}