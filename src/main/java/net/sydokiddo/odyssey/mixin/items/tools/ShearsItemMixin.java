package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.world.item.*;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShearsItem.class)
public class ShearsItemMixin extends Item {

    private ShearsItemMixin(Properties settings) {
        super(settings);
    }

    // Shears can now be repaired using Iron Ingots

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        if (Odyssey.getConfig().itemChanges.more_repairable_items) {
            return ingredient.is(Items.IRON_INGOT);
        } else {
            return super.isValidRepairItem(stack, ingredient);
        }
    }
}