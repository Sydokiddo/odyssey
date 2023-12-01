package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BrushItem.class)
public class BrushItemMixin extends Item {

    private BrushItemMixin(Properties settings) {
        super(settings);
    }

    // Brushes can now be repaired using Feathers

    @Override
    public boolean isValidRepairItem(ItemStack itemStack, ItemStack ingredient) {
        if (Odyssey.getConfig().items.more_repairable_items) {
            return ingredient.is(Items.FEATHER);
        } else {
            return super.isValidRepairItem(itemStack, ingredient);
        }
    }
}