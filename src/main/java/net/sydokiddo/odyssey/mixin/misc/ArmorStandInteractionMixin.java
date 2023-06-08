package net.sydokiddo.odyssey.mixin.misc;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStand.class)
public abstract class ArmorStandInteractionMixin extends LivingEntity {

    private ArmorStandInteractionMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract void setShowArms(boolean showArms);
    @Shadow public abstract boolean isShowArms();

    // Armor Stands can be right-clicked with a Stick to add arms to it

    @Inject(at = @At("HEAD"), method = "interactAt", cancellable = true)
    private void odyssey_addArmsToArmorStand(final Player player, final Vec3 vec3d, final InteractionHand hand, final CallbackInfoReturnable<InteractionResult> info) {

        ItemStack itemStack = player.getItemInHand(hand);

        if (!this.isShowArms() && itemStack.getItem() == Items.STICK && Odyssey.getConfig().entityChanges.armor_stand_arms) {

            this.setShowArms(true);
            player.level().playSound(null, this.getOnPos(), ModSoundEvents.ARMOR_STAND_ADD_ARMS, SoundSource.NEUTRAL, 1.0F, 1.0F);
            player.swing(hand);
            player.gameEvent(GameEvent.ENTITY_INTERACT);

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            info.setReturnValue(InteractionResult.CONSUME);
        }
    }
}