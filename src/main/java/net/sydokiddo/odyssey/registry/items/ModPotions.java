package net.sydokiddo.odyssey.registry.items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.sydokiddo.chrysalis.misc.util.helpers.RegistryHelper;
import net.sydokiddo.odyssey.Odyssey;

public class ModPotions {

    // region Potion Types

    private static Potion
        MINING_FATIGUE,
        LONG_MINING_FATIGUE,
        STRONG_MINING_FATIGUE,

        HASTE,
        LONG_HASTE,
        STRONG_HASTE
    ;

    // endregion

    // region Potion Stats

    public static Potion registerMiningFatiguePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, Odyssey.id(name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 3600, 0)));
    }

    public static Potion registerLongMiningFatiguePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, Odyssey.id(name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 9600, 0)));
    }

    public static Potion registerStrongMiningFatiguePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, Odyssey.id(name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1800, 1)));
    }

    public static Potion registerHastePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, Odyssey.id(name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SPEED, 3600, 0)));
    }

    public static Potion registerLongHastePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, Odyssey.id(name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SPEED, 9600, 0)));
    }

    public static Potion registerStrongHastePotion(String name) {
        return Registry.register(BuiltInRegistries.POTION, Odyssey.id(name),
        new Potion(new MobEffectInstance(MobEffects.DIG_SPEED, 1800, 1)));
    }

    // endregion

    // region Registry

    public static void registerPotions() {

        if (Odyssey.getConfig().items.potionConfig.allow_mining_fatigue_potions) {

            MINING_FATIGUE = registerMiningFatiguePotion("mining_fatigue");
            LONG_MINING_FATIGUE = registerLongMiningFatiguePotion("long_mining_fatigue");
            STRONG_MINING_FATIGUE = registerStrongMiningFatiguePotion("strong_mining_fatigue");

            RegistryHelper.registerBasePotionRecipe(ModItems.ELDER_GUARDIAN_THORN, ModPotions.MINING_FATIGUE);
            RegistryHelper.registerLongPotionRecipe(ModPotions.MINING_FATIGUE, ModPotions.LONG_MINING_FATIGUE);
            RegistryHelper.registerStrongPotionRecipe(ModPotions.MINING_FATIGUE, ModPotions.STRONG_MINING_FATIGUE);
        }

        if (Odyssey.getConfig().items.potionConfig.allow_haste_potions) {

            HASTE = registerHastePotion("haste");
            LONG_HASTE = registerLongHastePotion("long_haste");
            STRONG_HASTE = registerStrongHastePotion("strong_haste");

            RegistryHelper.registerInvertedPotionRecipe(ModPotions.MINING_FATIGUE, ModPotions.HASTE);

            RegistryHelper.registerLongPotionRecipe(ModPotions.HASTE, ModPotions.LONG_HASTE);
            RegistryHelper.registerInvertedPotionRecipe(ModPotions.LONG_MINING_FATIGUE, ModPotions.LONG_HASTE);

            RegistryHelper.registerStrongPotionRecipe(ModPotions.HASTE, ModPotions.STRONG_HASTE);
            RegistryHelper.registerInvertedPotionRecipe(ModPotions.STRONG_MINING_FATIGUE, ModPotions.STRONG_HASTE);
        }
    }

    // endregion
}