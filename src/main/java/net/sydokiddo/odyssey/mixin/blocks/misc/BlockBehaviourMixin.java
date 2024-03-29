package net.sydokiddo.odyssey.mixin.blocks.misc;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void odyssey$blockRightClickEvents(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {

        ItemStack mainHandItem = player.getMainHandItem();

        if (player.mayBuild() && !player.isSecondaryUseActive() && player.mayInteract(level, blockPos)) {

            // region Flower Picking

            if (Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.flower_picking && blockState.is(BlockTags.SMALL_FLOWERS) && player.getMainHandItem().isEmpty()) {

                Block.dropResources(blockState, level, blockPos);
                this.doBlockHarvestingEvents(level, blockPos, blockState, player, ModSoundEvents.SMALL_FLOWER_PICK, 0.5F);

                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
            }

            // endregion

            // region Snow Harvesting

            if (Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.snow_layer_right_clicking && blockState.is(Blocks.SNOW) && mainHandItem.is(ItemTags.SHOVELS)) {

                Block.dropResources(blockState, level, blockPos, null, player, mainHandItem);
                this.doBlockHarvestingEvents(level, blockPos, blockState, player, ModSoundEvents.SHOVEL_DIG_SNOW, 1.0F);
                level.addDestroyBlockEffect(blockPos, blockState);
                player.awardStat(Stats.ITEM_USED.get(mainHandItem.getItem()));

                if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, mainHandItem);
                if (!player.getAbilities().instabuild) player.getMainHandItem().hurtAndBreak(1, player, (shovel) -> shovel.broadcastBreakEvent(interactionHand));

                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
            }

            // endregion
        }
    }

    @Unique
    private void doBlockHarvestingEvents(Level level, BlockPos blockPos, BlockState blockState, Player player, SoundEvent soundEvent, float volume) {
        level.removeBlock(blockPos, false);
        level.playSound(player, blockPos, soundEvent, SoundSource.BLOCKS, volume, 1.0F);
        level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(player, blockState));
    }

    @Inject(at = @At("HEAD"), method = "onProjectileHit")
    private void odyssey$onProjectileHitBlock(Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile, CallbackInfo info) {
        if (!level.isClientSide() && blockState.getBlock() instanceof NoteBlock noteBlock && Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.noteBlockConfig.note_block_sensitivity) {

            Entity projectileOwner;

            if (projectile.getOwner() != null) {
                projectileOwner = projectile.getOwner();
                if (projectileOwner instanceof Player player) player.awardStat(Stats.PLAY_NOTEBLOCK);
            } else {
                projectileOwner = projectile;
            }

            noteBlock.playNote(projectileOwner, blockState, level, blockHitResult.getBlockPos());

            if (Chrysalis.IS_DEBUG) Odyssey.LOGGER.info("{} has been hit by a projectile at {}", blockState.getBlock().getName().getString(), blockHitResult.getBlockPos());
        }
    }
}