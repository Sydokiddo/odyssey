package net.sydokiddo.odyssey.registry;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.Slime;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.PotionCauldronInteraction;
import net.sydokiddo.odyssey.registry.entities.registry.ModEntities;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.items.ModPotions;
import net.sydokiddo.odyssey.registry.misc.ModCreativeModeTabs;
import net.sydokiddo.odyssey.registry.misc.ModLootTableModifiers;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;

public class OdysseyRegistry {

    // Entity Data

    public static final EntityDataAccessor<Boolean> MAGMA_CUBE_CONVERSION = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SLIME_CONVERSION = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> WAXED = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Squid.class, EntityDataSerializers.BOOLEAN);

    public static void registerAll() {

        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModSoundEvents.registerSounds();
        ModEntities.registerModEntities();

        PotionCauldronInteraction.bootstrap();
        ModPotions.registerPotions();

        ModCreativeModeTabs.registerCreativeTabs();
        ModLootTableModifiers.modifyLootTables();

        System.out.println("Registering Content for Odyssey");
    }
}