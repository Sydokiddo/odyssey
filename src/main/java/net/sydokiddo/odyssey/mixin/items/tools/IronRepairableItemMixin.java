package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.world.item.*;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ShearsItem.class, FlintAndSteelItem.class})
public class IronRepairableItemMixin extends Item implements Vanishable {

    private IronRepairableItemMixin(Properties settings) {
        super(settings);
    }

    // Shears and Flint and Steel can now be repaired using Iron Ingots

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        if (Odyssey.getConfig().items.more_repairable_items) {
            return ingredient.is(Items.IRON_INGOT);
        } else {
            return super.isValidRepairItem(stack, ingredient);
        }
    }
}