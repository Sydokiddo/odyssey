package net.sydokiddo.odyssey.registry.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.sydokiddo.chrysalis.registry.items.custom_items.EnchantmentGlintItem;
import net.sydokiddo.odyssey.Odyssey;

@SuppressWarnings("ALL")
public class ModItems {

    // List of Items:

    public static final Item ELDER_GUARDIAN_THORN = registerItem("elder_guardian_thorn",
        new Item(new FabricItemSettings()));

    public static final Item WITHER_SKULL_FRAGMENT = registerItem("wither_skull_fragment",
        new Item(new FabricItemSettings()));

    public static final Item IRON_POTATO = registerItem("iron_potato",
        new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(6).saturationMod(10).alwaysEat()
        .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1), 1f)
        .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0), 1f).build()).rarity(Rarity.RARE)));

    public static final Item ENCHANTED_IRON_POTATO = registerItem("enchanted_iron_potato",
        new EnchantmentGlintItem(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(6).saturationMod(10).alwaysEat()
        .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 900, 1), 1f)
        .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 900, 0), 1f).build()).rarity(Rarity.EPIC)));

    // Registry for Items:

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Odyssey.MOD_ID, name), item);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Odyssey.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Odyssey.MOD_ID, name),
        new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModItems() {}
}
