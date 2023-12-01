package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.world.item.*;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TridentItem.class)
public class TridentItemMixin extends Item {

    private TridentItemMixin(Properties settings) {
        super(settings);
    }

    // Tridents can now be repaired using Prismarine Shards

    @Override
    public boolean isValidRepairItem(ItemStack itemStack, ItemStack ingredient) {
        if (Odyssey.getConfig().items.more_repairable_items) {
            return ingredient.is(Items.PRISMARINE_SHARD);
        } else {
            return super.isValidRepairItem(itemStack, ingredient);
        }
    }
}