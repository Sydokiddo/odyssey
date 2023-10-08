package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.sydokiddo.chrysalis.misc.util.dispenser.DispenseBucketMobDispenserBehavior;
import net.sydokiddo.odyssey.registry.items.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {

    // Custom Dispenser Methods

    @Inject(method = "getDispenseMethod", at = @At("HEAD"), cancellable = true)
    private void odyssey$addNewDispenserMethods(ItemStack itemStack, CallbackInfoReturnable<DispenseItemBehavior> cir) {

        Item item = itemStack.getItem();

        if (item == ModItems.FROG_BUCKET || item == ModItems.SQUID_BUCKET || item == ModItems.GLOW_SQUID_BUCKET) {
            cir.setReturnValue(DispenseBucketMobDispenserBehavior.INSTANCE);
        }
    }
}