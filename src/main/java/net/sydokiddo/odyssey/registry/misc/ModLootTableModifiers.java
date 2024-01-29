package net.sydokiddo.odyssey.registry.misc;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.sydokiddo.chrysalis.misc.util.helpers.RegistryHelper;
import net.sydokiddo.odyssey.registry.items.ModItems;

public class ModLootTableModifiers {

    public static void modifyLootTables() {

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, table, setter) -> {

            // region Dungeon Loot Table

            if (RegistryHelper.DUNGEON.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(4));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(12));
                table.withPool(builder);
            }

            // endregion

            // region Mineshaft Loot Table

            if (RegistryHelper.MINESHAFT.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(16));
                table.withPool(builder);
            }

            // endregion

            // region Bastion Loot Tables

            if (RegistryHelper.BASTION_TREASURE.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(1));
                table.withPool(builder);
            }

            if (RegistryHelper.BASTION_HOGLIN_STABLE.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(6));
                table.withPool(builder);
            }

            if (RegistryHelper.BASTION_OTHER.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(6));
                table.withPool(builder);
            }

            // endregion

            // region Desert Temple Loot Table

            if (RegistryHelper.DESERT_TEMPLE.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(2));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(8));
                table.withPool(builder);
            }

            // endregion

            // region Ruined Portal Loot Table

            if (RegistryHelper.RUINED_PORTAL.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(2));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(12));
                table.withPool(builder);
            }

            // endregion

            // region Stronghold Loot Table

            if (RegistryHelper.STRONGHOLD_CORRIDOR.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(2));
                table.withPool(builder);
            }

            // endregion

            // region Underwater Ruin Loot Table

            if (RegistryHelper.UNDERWATER_RUIN_BIG.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(2));
                table.withPool(builder);
            }

            // endregion

            // region Woodland Mansion Loot Table

            if (RegistryHelper.WOODLAND_MANSION.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(2));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(8));
                table.withPool(builder);
            }

            // endregion

            // region Pillager Outpost Loot Table

            if (RegistryHelper.PILLAGER_OUTPOST.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(12));
                table.withPool(builder);
            }

            // endregion

            // region Ancient City Loot Table

            if (RegistryHelper.ANCIENT_CITY.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.when(LootItemRandomChanceCondition.randomChance(0.20f));
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(1));
                table.withPool(builder);
            }

            // endregion
        });
    }
}