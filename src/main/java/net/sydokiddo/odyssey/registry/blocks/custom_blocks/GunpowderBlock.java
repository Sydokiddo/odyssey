package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.entities.non_living_entities.GunpowderBlockEntity;
import net.sydokiddo.odyssey.registry.entities.registry.ModEntities;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class GunpowderBlock extends FallingBlock {

    public GunpowderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends FallingBlock> codec() {
        return simpleCodec(GunpowderBlock::new);
    }

    // region Block Interactions

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState adjacentBlockState, boolean moved) {

        super.onPlace(blockState, level, blockPos, adjacentBlockState, moved);

        for (Direction direction : Direction.values()) {
            if (level.getBlockState(blockPos.relative(direction)).getBlock() instanceof BaseFireBlock) explode(level, blockPos);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos blockPos, Block block, BlockPos neighborPos, boolean moved) {
        super.neighborChanged(state, level, blockPos, block, neighborPos, moved);
        if (level.getBlockState(neighborPos).getBlock() instanceof FireBlock) explode(level, blockPos);
    }

    @Override
    public void onProjectileHit(Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile) {
        if (!level.isClientSide() && projectile.isOnFire()) explode(level, blockHitResult.getBlockPos());
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        ItemStack itemInHand = player.getItemInHand(interactionHand);

        if (Odyssey.getConfig().blocks.miscBlocksConfig.unstable_gunpowder_blocks && itemInHand.is(ChrysalisTags.TNT_IGNITERS)) {

            explode(level, blockPos, player);
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
            player.awardStat(Stats.ITEM_USED.get(itemInHand.getItem()));

            if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemInHand);

            if (!player.getAbilities().instabuild) {
                if (itemInHand.is(Items.FLINT_AND_STEEL)) {
                    itemInHand.hurtAndBreak(1, player, (flintAndSteel) -> flintAndSteel.broadcastBreakEvent(interactionHand));
                } else {
                    itemInHand.shrink(1);
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    // endregion

    // region Gunpowder Block Exploding

    @Override
    public void wasExploded(Level level, BlockPos blockPos, Explosion explosion) {
        if (!level.isClientSide()) explode(level, blockPos, explosion.getIndirectSourceEntity());
    }

    public static void explode(Level level, BlockPos blockPos) {
        if (Odyssey.getConfig().blocks.miscBlocksConfig.unstable_gunpowder_blocks) explode(level, blockPos, null);
    }

    private static void explode(Level level, BlockPos blockPos, LivingEntity livingEntity) {

        level.removeBlock(blockPos, false);

        if (!level.isClientSide()) {

            GunpowderBlockEntity gunpowderBlock = ModEntities.GUNPOWDER_BLOCK.create(level);

            assert gunpowderBlock != null;
            gunpowderBlock.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
            gunpowderBlock.owner = livingEntity;

            level.addFreshEntity(gunpowderBlock);
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
        }
    }

    @Override
    public boolean dropFromExplosion(Explosion explosion) {
        if (Odyssey.getConfig().blocks.miscBlocksConfig.unstable_gunpowder_blocks) {
            return false;
        } else {
            return super.dropFromExplosion(explosion);
        }
    }

    // endregion
}