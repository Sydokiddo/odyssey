package net.sydokiddo.odyssey.mixin.allay_tweaks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.sydokiddo.odyssey.util.MobBookHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Decreases book item stack by 1 if the player captures an Allay with one

@Mixin(BucketItem.class)
public class BookItemMixin {
    @Inject(method = "getEmptiedStack", at = @At(value = "HEAD"), cancellable = true)
    private static void stackableBook(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        if (MobBookHelper.isModified(stack) && stack.getCount() > 1) {
            MobBookHelper.insertNewItem(player, new ItemStack(Items.BOOK));
            stack.decrement(1);
            cir.setReturnValue(stack);
        }
    }
}