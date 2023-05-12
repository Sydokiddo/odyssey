package net.sydokiddo.odyssey.registry.items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.sydokiddo.chrysalis.mixin.util.BrewingRecipeRegistryMixin;
import net.sydokiddo.odyssey.Odyssey;

public class ModPotions {

    private static Potion MINING_FATIGUE;
    private static Potion LONG_MINING_FATIGUE;
    private static Potion STRONG_MINING_FATIGUE;

    private static Potion HASTE;
    private static Potion LONG_HASTE;
    private static Potion STRONG_HASTE;

    // Register Potion Stats:

    public static Potion registerMiningFatiguePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Odyssey.MOD_ID, name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 3600, 0)));
    }
    public static Potion registerLongMiningFatiguePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Odyssey.MOD_ID, name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 9600, 0)));
    }
    public static Potion registerStrongMiningFatiguePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Odyssey.MOD_ID, name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1800, 1)));
    }
    public static Potion registerHastePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Odyssey.MOD_ID, name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SPEED, 3600, 0)));
    }
    public static Potion registerLongHastePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Odyssey.MOD_ID, name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SPEED, 9600, 0)));
    }
    public static Potion registerStrongHastePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Odyssey.MOD_ID, name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SPEED, 1800, 1)));
    }

    // Register Potions:

    public static void registerMiningFatiguePotion() {
        MINING_FATIGUE = registerMiningFatiguePotion("mining_fatigue");
        registerMiningFatiguePotionRecipe();
    }

    public static void registerLongMiningFatiguePotion() {
        LONG_MINING_FATIGUE = registerLongMiningFatiguePotion("long_mining_fatigue");
        registerLongMiningFatiguePotionRecipe();
    }

    public static void registerStrongMiningFatiguePotion() {
        STRONG_MINING_FATIGUE = registerStrongMiningFatiguePotion("strong_mining_fatigue");
        registerStrongMiningFatiguePotionRecipe();
    }

    public static void registerHastePotion() {
        HASTE = registerHastePotion("haste");
        registerHastePotionRecipe();
    }

    public static void registerLongHastePotion() {
        LONG_HASTE = registerLongHastePotion("long_haste");
        registerLongHastePotionRecipe();
    }

    public static void registerStrongHastePotion() {
        STRONG_HASTE = registerStrongHastePotion("strong_haste");
        registerStrongHastePotionRecipe();
    }

    // Register Potion Recipes:

    private static void registerMiningFatiguePotionRecipe() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.ELDER_GUARDIAN_THORN, ModPotions.MINING_FATIGUE);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.HASTE, Items.FERMENTED_SPIDER_EYE, ModPotions.MINING_FATIGUE);
    }

    private static void registerLongMiningFatiguePotionRecipe() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.MINING_FATIGUE, Items.REDSTONE, ModPotions.LONG_MINING_FATIGUE);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.LONG_HASTE, Items.FERMENTED_SPIDER_EYE, ModPotions.LONG_MINING_FATIGUE);
    }

    private static void registerStrongMiningFatiguePotionRecipe() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.MINING_FATIGUE, Items.GLOWSTONE_DUST, ModPotions.STRONG_MINING_FATIGUE);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.STRONG_HASTE, Items.FERMENTED_SPIDER_EYE, ModPotions.STRONG_MINING_FATIGUE);
    }

    private static void registerHastePotionRecipe() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.MINING_FATIGUE, Items.FERMENTED_SPIDER_EYE, ModPotions.HASTE);
    }

    private static void registerLongHastePotionRecipe() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.HASTE, Items.REDSTONE, ModPotions.LONG_HASTE);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.LONG_MINING_FATIGUE, Items.FERMENTED_SPIDER_EYE, ModPotions.LONG_HASTE);
    }

    private static void registerStrongHastePotionRecipe() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.HASTE, Items.GLOWSTONE_DUST, ModPotions.STRONG_HASTE);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(ModPotions.STRONG_MINING_FATIGUE, Items.FERMENTED_SPIDER_EYE, ModPotions.STRONG_HASTE);
    }
}
