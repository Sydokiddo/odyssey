package net.sydokiddo.odyssey.registry;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.misc.util.commands.HatCommand;
import net.sydokiddo.odyssey.misc.util.commands.ShowcaseCommand;
import net.sydokiddo.odyssey.misc.util.dispenser.AddPaperToPaperBlockDispenserBehavior;
import net.sydokiddo.odyssey.misc.util.dispenser.ApplyPatinaToCopperDispenserBehavior;
import net.sydokiddo.odyssey.misc.util.dispenser.ApplySlimeballToPistonDispenserBehavior;
import net.sydokiddo.odyssey.misc.util.dispenser.FillCracksOnCrackedBlocksDispenserBehavior;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.PotionCauldronInteraction;
import net.sydokiddo.odyssey.registry.entities.registry.ModEntities;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.items.ModPotions;
import net.sydokiddo.odyssey.registry.misc.*;
import java.util.Optional;

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
    public static final ResourceLocation OWNERSHIP_CONTRACT_PACKET_ID = Odyssey.id("ownership_contract_packet");
    public static final ResourceLocation NOTE_BLOCK_PACKET_ID = Odyssey.id("note_block_packet");

    // endregion

    // region Misc

    private static final ImmutableMap<Block, Block> CRACKED_BLOCK_FILLING = ImmutableMap.<Block, Block>builder()
        .put(Blocks.CRACKED_STONE_BRICKS, Blocks.STONE_BRICKS)
        .put(Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICKS)
        .put(Blocks.CRACKED_DEEPSLATE_TILES, Blocks.DEEPSLATE_TILES)
        .put(Blocks.CRACKED_NETHER_BRICKS, Blocks.NETHER_BRICKS)
        .put(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS)
        .put(Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS)
        .build();

    public static Optional<BlockState> getFillableCrackedBlocks(BlockState blockState) {
        return Optional.ofNullable(CRACKED_BLOCK_FILLING.get(blockState.getBlock())).map((block) -> block.withPropertiesOf(blockState));
    }

    private static final ImmutableMap<Block, Block> MOSSY_BLOCK_SCRAPING = ImmutableMap.<Block, Block>builder()
        .put(Blocks.MOSSY_COBBLESTONE, Blocks.COBBLESTONE)
        .put(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.COBBLESTONE_STAIRS)
        .put(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.COBBLESTONE_SLAB)
        .put(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.COBBLESTONE_WALL)
        .put(Blocks.MOSSY_STONE_BRICKS, Blocks.STONE_BRICKS)
        .put(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.STONE_BRICK_STAIRS)
        .put(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.STONE_BRICK_SLAB)
        .put(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.STONE_BRICK_WALL)
        .put(Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS)
        .build();

    public static Optional<BlockState> getScrapableMossyBlocks(BlockState blockState) {
        return Optional.ofNullable(MOSSY_BLOCK_SCRAPING.get(blockState.getBlock())).map((block) -> block.withPropertiesOf(blockState));
    }

    private static void executeBlockConversionEvents(Level level, BlockPos blockPos, BlockState newBlockState, ItemStack itemStack, SoundEvent soundEvent, Player player) {

        level.setBlock(blockPos, newBlockState, 11);
        level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, newBlockState));

        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
        if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);
    }

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

        // endregion

        // region Composting

        CompostingChanceRegistry.INSTANCE.add(ModBlocks.SUGAR_CANE_BLOCK, 0.85F);

        // endregion

        // region Flammable Blocks

        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.GUNPOWDER_BLOCK, 15, 100);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.SUGAR_CANE_BLOCK, 30, 60);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.PAPER_BLOCK, 30, 60);

        // endregion

        // region Dispenser Methods

        DispenserBlock.registerBehavior(ModItems.PATINA, new ApplyPatinaToCopperDispenserBehavior());
        DispenserBlock.registerBehavior(Items.PAPER, new AddPaperToPaperBlockDispenserBehavior());
        if (Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.piston_interactions) DispenserBlock.registerBehavior(Items.SLIME_BALL, new ApplySlimeballToPistonDispenserBehavior());
        if (Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.cracked_and_mossy_block_interactions) DispenserBlock.registerBehavior(Items.CLAY_BALL, new FillCracksOnCrackedBlocksDispenserBehavior());

        // endregion

        // region Interaction Events

        UseBlockCallback.EVENT.register((player, level, interactionHand, hitResult) -> {

            if (!Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.cracked_and_mossy_block_interactions) return InteractionResult.PASS;

            BlockPos blockPos = hitResult.getBlockPos();
            ItemStack itemInHand = player.getItemInHand(interactionHand);

            Optional<BlockState> fillableCrackedBlocks = getFillableCrackedBlocks(level.getBlockState(blockPos));
            Optional<BlockState> scrapableMossyBlocks = getScrapableMossyBlocks(level.getBlockState(blockPos));

            // region Cracked Block Filling

            if (itemInHand.is(Items.CLAY_BALL) && fillableCrackedBlocks.isPresent()) {

                ParticleUtils.spawnParticlesOnBlockFace(level, blockPos, new BlockParticleOption(ParticleTypes.BLOCK, Blocks.CLAY.defaultBlockState()), UniformInt.of(3, 5), hitResult.getDirection(), () -> ParticleUtils.getRandomSpeedRanges(level.getRandom()), 0.55);
                executeBlockConversionEvents(level, blockPos, fillableCrackedBlocks.get(), itemInHand, ModSoundEvents.CLAY_BALL_FILL_CRACKS, player);
                if (!player.getAbilities().instabuild) itemInHand.shrink(1);

                return InteractionResult.sidedSuccess(level.isClientSide());
            }

            // endregion

            // region Mossy Block Scraping

            if (itemInHand.is(ItemTags.AXES) && scrapableMossyBlocks.isPresent()) {

                executeBlockConversionEvents(level, blockPos, scrapableMossyBlocks.get(), itemInHand, ModSoundEvents.AXE_SCRAPE_MOSS, player);
                Block.popResourceFromFace(level, hitResult.getBlockPos(), hitResult.getDirection(), new ItemStack(Items.MOSS_BLOCK));
                if (!player.getAbilities().instabuild) itemInHand.hurtAndBreak(1, player, (axe) -> axe.broadcastBreakEvent(interactionHand));

                return InteractionResult.sidedSuccess(level.isClientSide());
            }

            // endregion

            return InteractionResult.PASS;
        });

        // endregion

        System.out.println("Registering Content for " + Odyssey.LOGGER.getName());
    }
}