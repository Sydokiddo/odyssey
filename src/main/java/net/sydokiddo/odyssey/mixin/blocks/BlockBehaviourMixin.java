package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void odyssey$blockRightClickEvents(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {

        ItemStack mainHandItem = player.getMainHandItem();

        if (player.mayBuild() && !player.isSecondaryUseActive() && player.mayInteract(level, blockPos)) {

            // region Flower Picking

            if (Odyssey.getConfig().blocks.flower_picking && blockState.is(BlockTags.SMALL_FLOWERS) && player.getMainHandItem().isEmpty()) {

                Block.dropResources(blockState, level, blockPos);
                this.doBlockHarvestingEvents(level, blockPos, blockState, player, ModSoundEvents.SMALL_FLOWER_PICK, 0.5F, 1.0F);

                cir.setReturnValue(InteractionResult.SUCCESS);
            }

            // endregion

            // region Snow Harvesting

            if (Odyssey.getConfig().blocks.snow_layer_right_clicking && mainHandItem.is(ItemTags.SHOVELS)) {

                ItemStack itemToDrop;

                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, mainHandItem) > 0) {
                    itemToDrop = new ItemStack(Items.SNOW);
                } else {
                    itemToDrop = new ItemStack(Items.SNOWBALL);
                }

                for(int amount = 0; amount < blockState.getValue(SnowLayerBlock.LAYERS); ++amount) {
                    Block.popResource(level, blockPos, itemToDrop);
                }

                this.doBlockHarvestingEvents(level, blockPos, blockState, player, blockState.getSoundType().getBreakSound(), blockState.getSoundType().getVolume(), blockState.getSoundType().getPitch());
                level.addDestroyBlockEffect(blockPos, blockState);

                if (!player.getAbilities().instabuild) {
                    player.getMainHandItem().hurtAndBreak(1, player, (shovel) -> shovel.broadcastBreakEvent(hand));
                }

                cir.setReturnValue(InteractionResult.SUCCESS);
            }

            // endregion
        }
    }

    @Unique
    private void doBlockHarvestingEvents(Level level, BlockPos blockPos, BlockState blockState, Player player, SoundEvent soundEvent, float volume, float pitch) {
        level.removeBlock(blockPos, false);
        level.playSound(player, blockPos, soundEvent, SoundSource.BLOCKS, volume, pitch);
        level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(player, blockState));
    }
}