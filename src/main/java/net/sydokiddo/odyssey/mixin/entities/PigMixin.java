package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Pig.class)
public abstract class PigMixin extends Animal implements Saddleable {

    @Final @Shadow private ItemBasedSteering steering;

    private PigMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    // Saddles can be un-equipped from Pigs

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void odyssey_removeSaddleFromPigs(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.isSaddled() && player.isDiscrete() && player.getItemInHand(hand).isEmpty() && Odyssey.getConfig().entityChanges.saddle_removing) {
            this.steering.setSaddle(false);
            this.level().playSound(null, this, ModSoundEvents.SADDLE_UNEQUIP, SoundSource.NEUTRAL, 1.0f, 1.0f);
            player.setItemInHand(hand, Items.SADDLE.getDefaultInstance());
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}