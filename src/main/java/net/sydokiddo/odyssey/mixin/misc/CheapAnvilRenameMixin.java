package net.sydokiddo.odyssey.mixin.misc;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = AnvilMenu.class)
public abstract class CheapAnvilRenameMixin {

    @Shadow @Final private DataSlot cost;

    // Renaming items now only costs 1 XP always

    @Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private void odyssey_renamingItemsOnlyCost1XP(CallbackInfo ci, ItemStack itemStack, int i, int j, int k) {
        if (k > 0 && k == i && Odyssey.getConfig().itemChanges.anvil_renaming_only_costs_one_xp) {
            cost.set(1);
        }
    }
}