package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.PowderSnowCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import java.util.Map;
import java.util.function.Predicate;

@Mixin(PowderSnowCauldronBlock.class)
public class PowderSnowCauldronBlockMixin extends LayeredCauldronBlock {

    private PowderSnowCauldronBlockMixin(Properties properties, Predicate<Biome.Precipitation> predicate, Map<Item, CauldronInteraction> map) {
        super(properties, predicate, map);
    }

    // Causes entities to freeze when inside of Powder Snow Cauldrons

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (this.isEntityInsideContent(blockState, blockPos, entity)) {

            entity.setIsInPowderSnow(true);

            if (level.isClientSide) {

                RandomSource randomSource = level.getRandom();

                if (entity.xOld != entity.getX() || entity.zOld != entity.getZ() && randomSource.nextBoolean()) {
                    level.addParticle(ParticleTypes.SNOWFLAKE, entity.getX(), blockPos.getY() + 1, entity.getZ(), Mth.randomBetween(randomSource, -1.0F, 1.0F) * 0.083333336F, 0.05F, Mth.randomBetween(randomSource, -1.0F, 1.0F) * 0.083333336F);
                }
            }
        }
    }
}