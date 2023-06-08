package net.sydokiddo.odyssey.registry.misc;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.registry.items.ModItems;

public class ModLootTableModifiers {

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, table, setter) -> {

            if(RegistryHelpers.DUNGEON.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(4));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(12));
                table.withPool(builder);
            }

            if(RegistryHelpers.MINESHAFT.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(16));
                table.withPool(builder);
            }

            if(RegistryHelpers.BASTION_TREASURE.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(1));
                table.withPool(builder);
            }

            if(RegistryHelpers.BASTION_HOGLIN_STABLE.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(6));
                table.withPool(builder);
            }

            if(RegistryHelpers.BASTION_OTHER.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(6));
                table.withPool(builder);
            }

            if(RegistryHelpers.DESERT_TEMPLE.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(2));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(8));
                table.withPool(builder);
            }

            if(RegistryHelpers.RUINED_PORTAL.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(2));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(12));
                table.withPool(builder);
            }

            if(RegistryHelpers.STRONGHOLD_CORRIDOR.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(1));
                table.withPool(builder);
            }

            if(RegistryHelpers.UNDERWATER_RUIN_BIG.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(2));
                table.withPool(builder);
            }

            if(RegistryHelpers.WOODLAND_MANSION.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(2));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(8));
                table.withPool(builder);
            }

            if(RegistryHelpers.PILLAGER_OUTPOST.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(12));
                table.withPool(builder);
            }

            if(RegistryHelpers.ANCIENT_CITY.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.when(LootItemRandomChanceCondition.randomChance(0.20f));
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(1));
                table.withPool(builder);
            }
        });
    }
}
