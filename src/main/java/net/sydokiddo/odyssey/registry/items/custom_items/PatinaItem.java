package net.sydokiddo.odyssey.registry.items.custom_items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import java.util.Optional;

public class PatinaItem extends Item {

    public PatinaItem(Properties properties) {
        super(properties);
    }

    public static Optional<BlockState> getNextCopperState(BlockState blockState) {
        return WeatheringCopper.getNext(blockState.getBlock()).map(block -> block.withPropertiesOf(blockState));
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {

        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        Player player = useOnContext.getPlayer();
        ItemStack itemStack = useOnContext.getItemInHand();

        Optional<BlockState> copperState = getNextCopperState(level.getBlockState(blockPos));

        if (copperState.isPresent()) {

            level.playSound(player, blockPos, ModSoundEvents.PATINA_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3005, blockPos, 0);

            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, useOnContext.getItemInHand());
            }

            if (player != null && !player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            level.setBlock(blockPos, copperState.get(), 11);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, copperState.get()));

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}