package net.sydokiddo.odyssey.mixin.items;

import net.minecraft.world.item.*;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    // Changes the max stack size of certain items

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void odyssey_changeMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        if (this.getItem() instanceof SignItem || this.getItem() instanceof HangingSignItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.sign_stack_size);
        }
        if (this.getItem() instanceof MinecartItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.minecart_stack_size);
        }
        if (this.getItem() instanceof BoatItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.boat_stack_size);
        }
        if (this.getItem() instanceof BannerItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.banner_stack_size);
        }
        if (this.getItem() instanceof BannerPatternItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.banner_pattern_stack_size);
        }
        if (this.getItem() instanceof ArmorStandItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.armor_stand_stack_size);
        }
        if (this.getItem() instanceof BlockItem item && item.getBlock() instanceof DecoratedPotBlock) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.decorated_pot_stack_size);
        }
        if (this.getItem() instanceof SnowballItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.snowball_stack_size);
        }
        if (this.getItem() instanceof EggItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.egg_stack_size);
        }
    }
}