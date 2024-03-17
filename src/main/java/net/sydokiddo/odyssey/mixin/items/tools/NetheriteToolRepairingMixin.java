package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Tiers.class)
public class NetheriteToolRepairingMixin {

    @SuppressWarnings("deprecation")
    @Shadow @Final private LazyLoadedValue<Ingredient> repairIngredient;

    @Inject(method = "getRepairIngredient", at = @At("HEAD"), cancellable = true)
    private void odyssey$makeNetheriteToolsRepairableWithScrap(CallbackInfoReturnable<Ingredient> cir) {
        if (this.repairIngredient.get().test(new ItemStack(Items.NETHERITE_INGOT))) {
            cir.setReturnValue(Ingredient.of(Items.NETHERITE_SCRAP));
        }
    }
}