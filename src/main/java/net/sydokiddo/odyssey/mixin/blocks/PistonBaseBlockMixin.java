package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
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
public class PistonBaseBlockMixin extends DirectionalBlock {

    @Shadow @Final private boolean isSticky;
    @Shadow @Final public static BooleanProperty EXTENDED;

    private PistonBaseBlockMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private void doPistonUseEvents(Level level, BlockPos blockPos, Player player, Item item, BlockState blockState, Block block, SoundEvent soundEvent) {

        if (!level.isClientSide()) {
            player.awardStat(Stats.ITEM_USED.get(item));
        }

        level.setBlockAndUpdate(blockPos, block.defaultBlockState().setValue(PistonBaseBlock.FACING, blockState.getValue(PistonBaseBlock.FACING)));
        level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0f, 1.0f);
        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player));
    }

    @SuppressWarnings("ALL")
    @Override
    public InteractionResult use(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {

        ItemStack itemStack = player.getItemInHand(interactionHand);
        Item item = itemStack.getItem();
        Direction direction = blockState.getValue(PistonBaseBlock.FACING);

        if (!blockState.getValue(EXTENDED) && Odyssey.getConfig().blocks.piston_interactions) {

            if (!this.isSticky && itemStack.is(Items.SLIME_BALL)) {

                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                doPistonUseEvents(level, blockPos, player, item, blockState, Blocks.STICKY_PISTON, ModSoundEvents.PISTON_APPLY_SLIMEBALL);
                return InteractionResult.sidedSuccess(level.isClientSide);

            } else if (this.isSticky && item instanceof AxeItem) {

                if (!player.getAbilities().instabuild) {
                    itemStack.hurtAndBreak(1, player, playerx -> player.broadcastBreakEvent(interactionHand));
                }

                popResourceFromFace(level, blockPos, direction, new ItemStack(Items.SLIME_BALL));
                doPistonUseEvents(level, blockPos, player, item, blockState, Blocks.PISTON, ModSoundEvents.PISTON_REMOVE_SLIMEBALL);
                return InteractionResult.sidedSuccess(level.isClientSide);

            } else {
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;
    }
}
