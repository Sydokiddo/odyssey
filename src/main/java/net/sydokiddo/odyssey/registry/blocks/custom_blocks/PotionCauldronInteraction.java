package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.entities.block_entities.PotionCauldronBlockEntity;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import java.util.Map;
import java.util.Objects;
import static net.minecraft.world.item.alchemy.PotionUtils.getPotion;

public interface PotionCauldronInteraction {

    CauldronInteraction.InteractionMap POTION_CAULDRON_BEHAVIOR = CauldronInteraction.newInteractionMap("potion");

    static void bootstrap() {

        Map<Item, CauldronInteraction> potionCauldron = POTION_CAULDRON_BEHAVIOR.map();

        // region Inserting Potions into Cauldrons

        potionCauldron.put(Items.POTION, (blockState, level, blockPos, player, hand, itemStack) -> {

            PotionCauldronBlockEntity cauldron = (PotionCauldronBlockEntity) level.getBlockEntity(blockPos);

            if (cauldron != null && getPotion(itemStack) == cauldron.getPotion()) {

                if (blockState.getValue(LayeredCauldronBlock.LEVEL) != 3 && getPotion(itemStack) != Potions.WATER && cauldron.tryApplyPotion(getPotion(itemStack))) {

                    if (!level.isClientSide()) {

                        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                        player.awardStat(Stats.USE_CAULDRON);
                        player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));

                        level.setBlock(blockPos, blockState.cycle(LayeredCauldronBlock.LEVEL), 3);
                        level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide());

                } else {
                    return InteractionResult.PASS;
                }

            } else {

                // Dissipating Particles and Sound

                if (level instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 10; ++i) {
                        serverLevel.sendParticles(ParticleTypes.POOF, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 1.0D, (double) blockPos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                }
                return CauldronInteraction.fillBucket(blockState, level, blockPos, player, hand, itemStack, new ItemStack(Items.GLASS_BOTTLE), stateIn -> true, ModSoundEvents.CAULDRON_POTION_DISSIPATE);
            }
        });

        // endregion

        // region Removing Potions from Potion Cauldrons

        potionCauldron.put(Items.GLASS_BOTTLE, (blockState, level, blockPos, player, hand, itemStack) -> {

            PotionCauldronBlockEntity cauldron = (PotionCauldronBlockEntity) level.getBlockEntity(blockPos);
            Potion potion = Objects.requireNonNull(cauldron).getPotion();
            ItemStack bottle = new ItemStack(Items.POTION);

            if (cauldron.hasPotion() && !level.isClientSide()) {

                PotionUtils.setPotion(bottle, potion);
                player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, bottle));

                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));

                PotionCauldronBlock.lowerFillLevel(blockState, level, blockPos);
                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        });

        // endregion

        // region Tipping Arrows with Potion Cauldrons

        potionCauldron.put(Items.ARROW, (blockState, level, blockPos, player, hand, itemStack) -> {

            PotionCauldronBlockEntity cauldron = (PotionCauldronBlockEntity) level.getBlockEntity(blockPos);
            Potion potion = Objects.requireNonNull(cauldron).getPotion();
            ItemStack tippedArrow = new ItemStack(Items.TIPPED_ARROW, player.getItemInHand(hand).getCount());

            if (cauldron.hasPotion() && !level.isClientSide() && getPotion(itemStack) != Potions.WATER) {
                PotionUtils.setPotion(tippedArrow, potion);
                doCauldronConsumeInteraction(blockState, level, blockPos, player, hand, Items.ARROW.getDefaultInstance(), tippedArrow);
                level.playSound(null, blockPos, ModSoundEvents.CAULDRON_TIP_ARROW, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        });

        // endregion

        // region Making Potatoes into Poisonous Potatoes

        potionCauldron.put(Items.POTATO, (blockState, level, blockPos, player, hand, itemStack) -> {

            PotionCauldronBlockEntity cauldron = (PotionCauldronBlockEntity) level.getBlockEntity(blockPos);
            ItemStack poisonousPotato = new ItemStack(Items.POISONOUS_POTATO, player.getItemInHand(hand).getCount());

            if (cauldron != null && (cauldron.getPotion() == Potions.POISON || cauldron.getPotion() == Potions.LONG_POISON || cauldron.getPotion() == Potions.STRONG_POISON)) {

                if (cauldron.hasPotion() && !level.isClientSide()) {
                    doCauldronConsumeInteraction(blockState, level, blockPos, player, hand, Items.POTATO.getDefaultInstance(), poisonousPotato);
                    level.playSound(null, blockPos, ModSoundEvents.CAULDRON_POISON_POTATO, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());

            } else {
                return InteractionResult.PASS;
            }
        });

        // endregion

        CauldronInteraction.addDefaultInteractions(potionCauldron);
    }

    private static void doCauldronConsumeInteraction(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack startingItem, ItemStack resultItem) {

        player.awardStat(Stats.ITEM_USED.get(startingItem.getItem()));
        player.awardStat(Stats.USE_CAULDRON);

        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, startingItem);
        }

        player.setItemInHand(interactionHand, resultItem);
        PotionCauldronBlock.lowerFillLevel(blockState, level, blockPos);
        level.gameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);

        if (Chrysalis.IS_DEBUG) {
            Odyssey.LOGGER.info("{} has been converted into {} in a {}", startingItem.getItem().getName(startingItem).getString(), resultItem.getItem().getName(resultItem).getString(), blockState.getBlock().asItem().getName(blockState.getBlock().asItem().getDefaultInstance()));
        }
    }
}