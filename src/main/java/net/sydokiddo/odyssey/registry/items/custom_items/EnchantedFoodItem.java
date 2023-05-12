package net.sydokiddo.odyssey.registry.items.custom_items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EnchantedFoodItem extends Item {

    public EnchantedFoodItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}
