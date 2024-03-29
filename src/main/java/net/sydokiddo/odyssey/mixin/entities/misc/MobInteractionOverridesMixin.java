package net.sydokiddo.odyssey.mixin.entities.misc;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.items.custom_items.OwnershipContractItem;
import net.sydokiddo.odyssey.registry.misc.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractHorse.class, AbstractChestedHorse.class, Horse.class, Wolf.class, Cat.class, Parrot.class})
public class MobInteractionOverridesMixin extends Mob {

    @Unique Mob mob = this;

    private MobInteractionOverridesMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void odyssey$cancelMobInteractions(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {

        if (!this.isAlive()) return;
        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (itemStack.is(Items.BRUSH) && this.getType().is(ModTags.CAN_BE_BRUSHED) || itemStack.is(ModItems.OWNERSHIP_CONTRACT)) {
            if (this.mob instanceof AbstractHorse && !OwnershipContractItem.isContractBound(player.getItemInHand(interactionHand))) return;
            cir.setReturnValue(super.mobInteract(player, interactionHand));
        }
    }
}