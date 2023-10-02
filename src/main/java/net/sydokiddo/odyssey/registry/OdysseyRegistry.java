package net.sydokiddo.odyssey.registry;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.PotionCauldronInteraction;
import net.sydokiddo.odyssey.registry.entities.registry.ModEntities;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.items.ModPotions;
import net.sydokiddo.odyssey.registry.misc.ModCreativeModeTabs;
import net.sydokiddo.odyssey.registry.misc.ModCriteriaTriggers;
import net.sydokiddo.odyssey.registry.misc.ModLootTableModifiers;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;

public class OdysseyRegistry {

    // Entity Data

    public static final EntityDataAccessor<Boolean> MAGMA_CUBE_CONVERSION = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SLIME_CONVERSION = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> WAXED = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Squid.class, EntityDataSerializers.BOOLEAN);

    // Debug and Common Methods

    public static void doSaddleRemovingEvents(LivingEntity livingEntity, Player player, InteractionHand hand) {

        livingEntity.level().playSound(null, livingEntity, ModSoundEvents.SADDLE_UNEQUIP, SoundSource.NEUTRAL, 1.0f, 1.0f);
        player.setItemInHand(hand, Items.SADDLE.getDefaultInstance());

        if (Chrysalis.IS_DEBUG) {
            Odyssey.LOGGER.info("Saddle has been successfully removed from {} by {}", livingEntity.getName().getString(), player.getName().getString());
        }
    }

    public static void sendMobBucketingDebugMessage(LivingEntity livingEntity, Player player) {
        if (Chrysalis.IS_DEBUG) {
            Odyssey.LOGGER.info("{} has been picked up in a bucket by {}", livingEntity.getName().getString(), player.getName().getString());
        }
    }

    public static void sendMobConversionDebugMessage(LivingEntity startingEntity, LivingEntity resultEntity) {
        if (Chrysalis.IS_DEBUG) {
            Odyssey.LOGGER.info("{} has been converted into {}", startingEntity.getName().getString(), resultEntity.getName().getString());
        }
    }

    public static void sendCauldronInteractionDebugMessage(ItemStack startingItem, ItemStack resultItem, Block block) {
        if (Chrysalis.IS_DEBUG) {
            Odyssey.LOGGER.info("{} has been converted into {} in a {}", startingItem.getItem().getName(startingItem).getString(), resultItem.getItem().getName(resultItem).getString(), block.asItem().getName(block.asItem().getDefaultInstance()));
        }
    }

    public static void registerAll() {

        // - Blocks, Items, Etc.

        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModSoundEvents.registerSounds();
        PotionCauldronInteraction.bootstrap();
        ModPotions.registerPotions();

        // - Entities

        ModEntities.registerModEntities();

        // - Technical

        ModCreativeModeTabs.registerCreativeTabs();
        ModLootTableModifiers.modifyLootTables();
        ModCriteriaTriggers.registerCriteriaTriggers();

        // - Composting

        CompostingChanceRegistry.INSTANCE.add(ModBlocks.SUGAR_CANE_BLOCK, RegistryHelpers.composter85PercentChance);

        // - Flammable Blocks

        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.GUNPOWDER_BLOCK, 15, 100);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.SUGAR_CANE_BLOCK, 30, 60);

        System.out.println("Registering Content for Odyssey");
    }
}