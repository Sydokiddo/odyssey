package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.misc.util.dispenser.ScrapeBlockDispenserBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {

    // Custom Dispenser Methods

    @Inject(method = "getDispenseMethod", at = @At("HEAD"), cancellable = true)
    private void odyssey$addNewDispenserMethods(ItemStack itemStack, CallbackInfoReturnable<DispenseItemBehavior> cir) {
        if (itemStack.getItem() instanceof AxeItem && Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.axe_dispenser_functionality) {
            cir.setReturnValue(ScrapeBlockDispenserBehavior.INSTANCE);
        }
    }
}