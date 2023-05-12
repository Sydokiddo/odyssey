package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import java.util.Map;
import java.util.Objects;
import static net.minecraft.world.item.alchemy.PotionUtils.getPotion;

public class PotionCauldronInteraction {

    public static final Map<Item, CauldronInteraction> POTION_CAULDRON_BEHAVIOR = CauldronInteraction.newInteractionMap();

    public static void bootstrap() {

        POTION_CAULDRON_BEHAVIOR.put(Items.POTION, (state, level, pos, player, hand, stack) -> {

            BlockEntity blockEntity = level.getBlockEntity(pos);
            PotionCauldronBlockEntity cauldron = (PotionCauldronBlockEntity)blockEntity;
            assert cauldron != null;

            if (getPotion(stack) == cauldron.getPotion()) {
                if (state.getValue(LayeredCauldronBlock.LEVEL) != 3 && getPotion(stack) != Potions.WATER && cauldron.tryApplyPotion(getPotion(stack))) {
                    if (!level.isClientSide) {

                        player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                        player.awardStat(Stats.USE_CAULDRON);
                        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

                        level.setBlock(pos, state.cycle(LayeredCauldronBlock.LEVEL), 3);
                        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                return CauldronInteraction.fillBucket(state, level, pos, player, hand, stack, new ItemStack(Items.GLASS_BOTTLE), stateIn -> true, SoundEvents.GENERIC_EXTINGUISH_FIRE);
            }
        });

        POTION_CAULDRON_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {

            BlockEntity blockEntity = world.getBlockEntity(pos);
            PotionCauldronBlockEntity cauldron = (PotionCauldronBlockEntity)blockEntity;
            Potion potion = Objects.requireNonNull(cauldron).getPotion();
            ItemStack bottle = new ItemStack(Items.POTION);

            if (cauldron.hasPotion()) {
                if (!world.isClientSide) {

                    PotionUtils.setPotion(bottle, potion);
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, bottle));

                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

                    PotionCauldronBlock.lowerFillLevel(state, world, pos);
                    world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                }
            }
            return InteractionResult.sidedSuccess(world.isClientSide);

        });
        CauldronInteraction.addDefaultInteractions(POTION_CAULDRON_BEHAVIOR);
    }
}