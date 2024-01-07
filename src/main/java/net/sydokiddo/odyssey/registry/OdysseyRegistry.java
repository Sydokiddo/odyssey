package net.sydokiddo.odyssey.registry;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.misc.util.commands.HatCommand;
import net.sydokiddo.odyssey.misc.util.commands.ShowcaseCommand;
import net.sydokiddo.odyssey.misc.util.dispenser.ApplyPatinaToCopperDispenserBehavior;
import net.sydokiddo.odyssey.misc.util.dispenser.ApplySlimeballToPistonDispenserBehavior;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.PotionCauldronInteraction;
import net.sydokiddo.odyssey.registry.entities.registry.ModEntities;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.items.ModPotions;
import net.sydokiddo.odyssey.registry.misc.*;

public class OdysseyRegistry {

    // region Entity Data

    public static final EntityDataAccessor<Boolean>
        MAGMA_CUBE_CONVERSION = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.BOOLEAN),
        SLIME_CONVERSION = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.BOOLEAN),
        FROM_BUCKET = SynchedEntityData.defineId(Squid.class, EntityDataSerializers.BOOLEAN),
        WAXED = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.BOOLEAN),
        ALLAY_FROM_BOOK = SynchedEntityData.defineId(Allay.class, EntityDataSerializers.BOOLEAN),
        VEX_FROM_BOOK = SynchedEntityData.defineId(Vex.class, EntityDataSerializers.BOOLEAN)
    ;

    // endregion

    // region Packets

    public static final ResourceLocation ENVIRONMENT_DETECTOR_PACKET_ID = Odyssey.id("environment_detector_packet");

    // endregion

    public static void registerAll() {

        // region Blocks, Items, Etc.

        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModSoundEvents.registerSounds();
        PotionCauldronInteraction.bootstrap();
        ModPotions.registerPotions();
        ModParticles.PARTICLES.register();

        // endregion

        // region Entities

        ModEntities.registerModEntities();

        // endregion

        // region Technical

        ModCreativeModeTabs.registerCreativeTabs();
        ModLootTableModifiers.modifyLootTables();
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> ShowcaseCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> HatCommand.register(commandDispatcher));
        HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> OCommonMethods.renderCompassAndMapOverlay(guiGraphics));

        // endregion

        // region Composting

        CompostingChanceRegistry.INSTANCE.add(ModBlocks.SUGAR_CANE_BLOCK, 0.85F);

        // endregion

        // region Flammable Blocks

        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.GUNPOWDER_BLOCK, 15, 100);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.SUGAR_CANE_BLOCK, 30, 60);

        // endregion

        // region Dispenser Methods

        DispenserBlock.registerBehavior(ModItems.PATINA, new ApplyPatinaToCopperDispenserBehavior());

        if (Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.piston_interactions) {
            DispenserBlock.registerBehavior(Items.SLIME_BALL, new ApplySlimeballToPistonDispenserBehavior());
        }

        // endregion

        System.out.println("Registering Content for " + Odyssey.LOGGER.getName());
    }
}