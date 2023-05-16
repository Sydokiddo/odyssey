package net.sydokiddo.odyssey.registry.misc;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.sydokiddo.odyssey.registry.items.ModItems;

public class ModLootTableModifiers {

    private static final ResourceLocation DUNGEON = new ResourceLocation("minecraft", "chests/simple_dungeon");
    private static final ResourceLocation MINESHAFT = new ResourceLocation("minecraft", "chests/abandoned_mineshaft");
    private static final ResourceLocation BASTION_TREASURE = new ResourceLocation("minecraft", "chests/bastion_treasure");
    private static final ResourceLocation BASTION_HOGLIN_STABLE = new ResourceLocation("minecraft", "chests/bastion_hoglin_stable");
    private static final ResourceLocation BASTION_OTHER = new ResourceLocation("minecraft", "chests/bastion_other");
    private static final ResourceLocation DESERT_TEMPLE = new ResourceLocation("minecraft", "chests/desert_pyramid");
    private static final ResourceLocation RUINED_PORTAL = new ResourceLocation("minecraft", "chests/ruined_portal");
    private static final ResourceLocation STRONGHOLD_CORRIDOR = new ResourceLocation("minecraft", "chests/stronghold_corridor");
    private static final ResourceLocation UNDERWATER_RUIN_BIG = new ResourceLocation("minecraft", "chests/underwater_ruin_big");
    private static final ResourceLocation WOODLAND_MANSION = new ResourceLocation("minecraft", "chests/woodland_mansion");
    private static final ResourceLocation PILLAGER_OUTPOST = new ResourceLocation("minecraft", "chests/pillager_outpost");
    private static final ResourceLocation ANCIENT_CITY = new ResourceLocation("minecraft", "chests/ancient_city");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, table, setter) -> {

            if(DUNGEON.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(4));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(12));
                table.withPool(builder);
            }

            if(MINESHAFT.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(16));
                table.withPool(builder);
            }

            if(BASTION_TREASURE.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(1));
                table.withPool(builder);
            }

            if(BASTION_HOGLIN_STABLE.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(6));
                table.withPool(builder);
            }

            if(BASTION_OTHER.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(6));
                table.withPool(builder);
            }

            if(DESERT_TEMPLE.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(2));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(8));
                table.withPool(builder);
            }

            if(RUINED_PORTAL.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(2));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(12));
                table.withPool(builder);
            }

            if(STRONGHOLD_CORRIDOR.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(3));
                table.withPool(builder);
            }

            if(UNDERWATER_RUIN_BIG.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(2));
                table.withPool(builder);
            }

            if(WOODLAND_MANSION.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(2));
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(8));
                table.withPool(builder);
            }

            if(PILLAGER_OUTPOST.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.add(LootItem.lootTableItem(ModItems.IRON_POTATO).setWeight(12));
                table.withPool(builder);
            }

            if(ANCIENT_CITY.equals(id)) {
                LootPool.Builder builder = LootPool.lootPool();
                builder.when(LootItemRandomChanceCondition.randomChance(0.20f));
                builder.add(LootItem.lootTableItem(ModItems.ENCHANTED_IRON_POTATO).setWeight(1));
                table.withPool(builder);
            }
        });
    }
}
