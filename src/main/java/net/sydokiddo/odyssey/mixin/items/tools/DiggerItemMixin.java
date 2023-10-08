package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DiggerItem.class)
public class DiggerItemMixin {

    // Prevents Budding Amethyst from being mined faster with Pickaxes

    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    private void odyssey$removeBuddingAmethystFromPickaxeTag(ItemStack itemStack, BlockState blockState, CallbackInfoReturnable<Float> cir) {
        if (Odyssey.getConfig().blocks.harder_budding_amethyst && blockState.is(Blocks.BUDDING_AMETHYST)) {
            cir.setReturnValue(1.0F);
        }
    }
}