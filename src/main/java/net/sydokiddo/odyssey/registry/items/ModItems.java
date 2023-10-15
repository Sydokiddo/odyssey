package net.sydokiddo.odyssey.registry.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.chrysalis.registry.items.custom_items.EnchantedGlintItem;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.items.custom_items.FrogBucketItem;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;

public class ModItems {

    // region Foods

    public static final Item IRON_POTATO = registerItem("iron_potato",
        new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat()
        .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1), 1F)
        .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0), 1F).build()).rarity(Rarity.RARE)));

    public static final Item ENCHANTED_IRON_POTATO = registerItem("enchanted_iron_potato",
        new EnchantedGlintItem(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat()
        .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 900, 1), 1F)
        .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 900, 0), 1F).build()).rarity(Rarity.EPIC)));

    // endregion

    // region Mob Buckets

    public static final Item FROG_BUCKET = registerItem("frog_bucket",
        new FrogBucketItem(EntityType.FROG, ModSoundEvents.BUCKET_EMPTY_FROG,
        new FabricItemSettings().stacksTo(1).craftRemainder(Items.BUCKET), Items.BUCKET));

    public static final Item SQUID_BUCKET = registerItem("squid_bucket",
        RegistryHelpers.registerMobInWaterBucket(EntityType.SQUID, ModSoundEvents.BUCKET_EMPTY_SQUID));

    public static final Item GLOW_SQUID_BUCKET = registerItem("glow_squid_bucket",
        RegistryHelpers.registerMobInWaterBucket(EntityType.GLOW_SQUID, ModSoundEvents.BUCKET_EMPTY_GLOW_SQUID));

    // endregion

    // region Misc Items

    public static final Item ELDER_GUARDIAN_THORN = registerItem("elder_guardian_thorn", new Item(new FabricItemSettings()));

    public static final Item WITHER_SKULL_FRAGMENT = registerItem("wither_skull_fragment", new Item(new FabricItemSettings()));

    // endregion

    // Registry

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Odyssey.id(name), item);
    }

    public static void registerModItems() {}
}