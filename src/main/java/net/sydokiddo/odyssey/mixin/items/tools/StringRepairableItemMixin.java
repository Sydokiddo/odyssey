package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.world.item.*;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({BowItem.class, CrossbowItem.class, FishingRodItem.class})
public class StringRepairableItemMixin extends Item {

    private StringRepairableItemMixin(Properties settings) {
        super(settings);
    }

    // Bows and Crossbows can now be repaired using String

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        if (Odyssey.getConfig().items.more_repairable_items) {
            return ingredient.is(Items.STRING);
        } else {
            return super.isValidRepairItem(stack, ingredient);
        }
    }
}