package net.sydokiddo.odyssey.mixin.misc;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import net.sydokiddo.odyssey.registry.misc.ModTags;
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
    @Shadow public abstract boolean isMarker();

    // Armor Stands can be right-clicked with a Stick to add arms to it

    @Inject(at = @At("HEAD"), method = "interactAt", cancellable = true)
    private void odyssey$addArmsToArmorStand(final Player player, final Vec3 vec3d, final InteractionHand hand, final CallbackInfoReturnable<InteractionResult> cir) {

        ItemStack itemInHand = player.getItemInHand(hand);

        if (!this.isShowArms() && !this.isMarker() && itemInHand.is(ModTags.GIVES_ARMOR_STANDS_ARMS) && Odyssey.getConfig().entities.miscEntitiesConfig.armor_stand_arms) {

            if (player.level().isClientSide) {
                cir.setReturnValue(InteractionResult.CONSUME);
            } else {

                this.setShowArms(true);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSoundEvents.ARMOR_STAND_ADD_ARMS, this.getSoundSource(), 1.0F, 1.0F);
                player.gameEvent(GameEvent.ENTITY_INTERACT);

                if (!player.getAbilities().instabuild) {
                    itemInHand.shrink(1);
                }

                player.awardStat(Stats.ITEM_USED.get(itemInHand.getItem()));

                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}