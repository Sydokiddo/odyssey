package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
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

public class GunpowderBlock extends FallingBlock {

    public GunpowderBlock(Properties properties) {
        super(properties);
    }

    // Gunpowder Blocks will explode if set on fire

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean bl) {

        super.onPlace(state, level, pos, oldState, bl);

        for (Direction direction : Direction.values()) {
            if (level.getBlockState(pos.relative(direction)).getBlock() instanceof BaseFireBlock) {
                explode(level, pos);
            }
        }
    }

    // Gunpowder Blocks will explode if hit by a flaming projectile

    @SuppressWarnings("ALL")
    @Override
    public void onProjectileHit(Level world, BlockState state, BlockHitResult hitResult, Projectile projectile) {
        if (!world.isClientSide && projectile.isOnFire()) {
            BlockPos blockPos = hitResult.getBlockPos();
            explode(world, blockPos);
        }
    }

    // Gunpowder Blocks will explode if lit by any TNT igniters

    @SuppressWarnings("ALL")
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        ItemStack itemStack = player.getItemInHand(interactionHand);
        Item item = itemStack.getItem();

        if (Odyssey.getConfig().blocks.unstable_gunpowder_blocks && itemStack.is(ChrysalisTags.TNT_IGNITERS)) {

            explode(level, blockPos, player);
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);

            if (!player.isCreative()) {
                if (itemStack.is(Items.FLINT_AND_STEEL)) {
                    itemStack.hurtAndBreak(1, player, playerx -> player.broadcastBreakEvent(interactionHand));
                } else {
                    itemStack.shrink(1);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            explode(level, pos, explosion.getIndirectSourceEntity());
        }
    }

    @SuppressWarnings("ALL")
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean bl) {

        super.neighborChanged(state, level, pos, block, neighborPos, bl);

        if (level.getBlockState(neighborPos).getBlock() instanceof FireBlock) {
            explode(level, pos);
        }
    }

    private static void explode(Level level, BlockPos pos) {
        if (Odyssey.getConfig().blocks.unstable_gunpowder_blocks) {
            explode(level, pos, null);
        }
    }

    private static void explode(Level level, BlockPos pos, LivingEntity livingEntity) {

        level.removeBlock(pos, false);

        if (!level.isClientSide) {

            GunpowderBlockEntity gunpowderBlock = ModEntities.GUNPOWDER_BLOCK.create(level);

            assert gunpowderBlock != null;
            gunpowderBlock.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            gunpowderBlock.owner = livingEntity;

            level.addFreshEntity(gunpowderBlock);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
        }
    }

    // Gunpowder Blocks will not drop themselves when exploded

    @Override
    public boolean dropFromExplosion(Explosion explosion) {
        if (Odyssey.getConfig().blocks.unstable_gunpowder_blocks) {
            return false;
        } else {
            return super.dropFromExplosion(explosion);
        }
    }
}