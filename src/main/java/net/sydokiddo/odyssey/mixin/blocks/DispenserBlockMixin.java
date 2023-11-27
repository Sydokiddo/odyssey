package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.sydokiddo.chrysalis.misc.util.dispenser.DispenseContainerMobDispenserBehavior;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.misc.util.dispenser.ApplyPatinaToCopperDispenserBehavior;
import net.sydokiddo.odyssey.misc.util.dispenser.ApplySlimeballToPistonDispenserBehavior;
import net.sydokiddo.odyssey.misc.util.dispenser.ScrapeBlockDispenserBehavior;
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
            cir.setReturnValue(DispenseContainerMobDispenserBehavior.INSTANCE);
        }
        if (item == Items.SLIME_BALL && Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.piston_interactions) {
            cir.setReturnValue(ApplySlimeballToPistonDispenserBehavior.INSTANCE);
        }
        if (item == ModItems.PATINA) {
            cir.setReturnValue(ApplyPatinaToCopperDispenserBehavior.INSTANCE);
        }
        if (item instanceof AxeItem && Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.axe_dispenser_functionality) {
            cir.setReturnValue(ScrapeBlockDispenserBehavior.INSTANCE);
        }
    }
}