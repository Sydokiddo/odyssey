package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin extends DirectionalBlock {

    @Shadow @Final private boolean isSticky;
    @Shadow @Final public static BooleanProperty EXTENDED;

    private PistonBaseBlockMixin(Properties properties) {
        super(properties);
    }

    // region Piston Item Interactions

    @Unique
    private void doPistonUseEvents(Level level, BlockPos blockPos, Player player, Item item, BlockState blockState, Block block, SoundEvent soundEvent, Direction direction) {

        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, item.getDefaultInstance());
        }
        if (!level.isClientSide()) {
            player.awardStat(Stats.ITEM_USED.get(item));
        }

        ParticleUtils.spawnParticlesOnBlockFace(level, blockPos, ParticleTypes.ITEM_SLIME, UniformInt.of(3, 5), direction, () -> ParticleUtils.getRandomSpeedRanges(level.getRandom()), 0.55);
        level.setBlockAndUpdate(blockPos, block.defaultBlockState().setValue(PistonBaseBlock.FACING, blockState.getValue(PistonBaseBlock.FACING)));
        level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player));
    }

    // Pistons can now be right-clicked with a Slime Ball to turn them into Sticky Pistons, and Sticky Pistons can be right-clicked with an Axe to turn them back into normal Pistons

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull InteractionResult use(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {

        ItemStack itemInHand = player.getItemInHand(interactionHand);
        Item item = itemInHand.getItem();
        Direction direction = blockState.getValue(PistonBaseBlock.FACING);

        if (!blockState.getValue(EXTENDED) && Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.piston_interactions) {

            if (!this.isSticky && itemInHand.is(Items.SLIME_BALL)) {

                // Putting Slime Balls on Pistons

                if (!player.getAbilities().instabuild) {
                    itemInHand.shrink(1);
                }

                doPistonUseEvents(level, blockPos, player, item, blockState, Blocks.STICKY_PISTON, ModSoundEvents.PISTON_APPLY_SLIMEBALL, direction);
                return InteractionResult.sidedSuccess(level.isClientSide());

            } else if (this.isSticky && item instanceof AxeItem) {

                // Scraping Sticky Pistons

                if (!player.getAbilities().instabuild) {
                    itemInHand.hurtAndBreak(1, player, (axe) -> axe.broadcastBreakEvent(interactionHand));
                }

                popResourceFromFace(level, blockPos, direction, new ItemStack(Items.SLIME_BALL));
                doPistonUseEvents(level, blockPos, player, item, blockState, Blocks.PISTON, ModSoundEvents.PISTON_REMOVE_SLIMEBALL, direction);
                return InteractionResult.sidedSuccess(level.isClientSide());

            } else {
                return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    // endregion
}