package net.sydokiddo.odyssey.mixin.items.misc;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerItem.class)
public class BannerItemMixin extends Item implements Equipable {

    private BannerItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void odyssey$onBannerInitialization(Block mainBlock, Block wallBlock, Properties properties, CallbackInfo info) {
        if (Odyssey.getConfig().items.equipable_banners) {
            DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
        }
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        if (Odyssey.getConfig().items.equipable_banners) {
            return EquipmentSlot.HEAD;
        }
        return EquipmentSlot.MAINHAND;
    }

    @Override
    public @NotNull SoundEvent getEquipSound() {
        return ModSoundEvents.ARMOR_EQUIP_BANNER;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        super.use(level, player, interactionHand);

        if (Odyssey.getConfig().items.equipable_banners) {
            return this.swapWithEquipmentSlot(this, level, player, interactionHand);
        } else {
            return super.use(level, player, interactionHand);
        }
    }
}