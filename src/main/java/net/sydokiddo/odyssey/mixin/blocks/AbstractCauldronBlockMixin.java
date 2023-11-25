package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.PotionCauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void odyssey$addCauldronInteractions(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {

        // region Water Cauldrons Extinguishing in the Nether

        if (player.getItemInHand(interactionHand).getItem().equals(Items.WATER_BUCKET) && level.dimensionType().ultraWarm()) {

            level.playSound(null, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.8F);

            if (level instanceof ServerLevel serverLevel) {

                serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, blockPos.getX() + 0.5, blockPos.getY() + 0.6, blockPos.getZ() + 0.5, 8, 0.2, 0.2, 0.2, 0);

                if (!player.getAbilities().instabuild) {
                    player.getItemInHand(interactionHand).shrink(1);
                    player.setItemInHand(interactionHand, new ItemStack(Items.BUCKET.asItem()));
                }
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
        }

        // endregion

        // region Inserting Potions into Cauldrons

        CauldronInteraction.EMPTY.put(Items.POTION, (cauldronState, world, pos, user, hand, stack) -> {

            if (!world.isClientSide()) {

                Potion potion = PotionUtils.getPotion(stack);
                Item item = stack.getItem();

                if (potion != Potions.WATER) {
                    PotionCauldronBlock potionCauldronBlock = ModBlocks.POTION_CAULDRON_STATE;
                    potionCauldronBlock.setPotion(potion);
                    world.setBlockAndUpdate(pos, potionCauldronBlock.defaultBlockState());
                } else {
                    world.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
                }

                user.setItemInHand(hand, ItemUtils.createFilledResult(stack, user, new ItemStack(Items.GLASS_BOTTLE)));
                user.awardStat(Stats.USE_CAULDRON);
                user.awardStat(Stats.ITEM_USED.get(item));

                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return InteractionResult.sidedSuccess(world.isClientSide());
        });

        // endregion
    }
}