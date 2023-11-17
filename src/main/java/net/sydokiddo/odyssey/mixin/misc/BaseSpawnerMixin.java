package net.sydokiddo.odyssey.mixin.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.sydokiddo.odyssey.registry.misc.ModParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {

    @Redirect(method = "clientTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void odyssey$removeDefaultSpawnerClientParticles(Level level, ParticleOptions particleOptions, double d, double e, double f, double g, double h, double i) {}

    @Inject(method = "clientTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void odyssey$replaceSpawnerClientParticles(Level level, BlockPos blockPos, CallbackInfo ci) {

        RandomSource randomSource = level.getRandom();

        double x = blockPos.getX() + randomSource.nextDouble();
        double y = blockPos.getY() + randomSource.nextDouble();
        double z = blockPos.getZ() + randomSource.nextDouble();

        ParticleOptions particleType;

        if (level.getBlockState(blockPos).getValue(BlockStateProperties.WATERLOGGED)) {
            particleType = ModParticles.SPAWNER_BUBBLE;
        } else {
            particleType = ParticleTypes.FLAME;
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
        }

        level.addParticle(particleType, x, y, z, 0.0, 0.0, 0.0);
    }

    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;levelEvent(ILnet/minecraft/core/BlockPos;I)V"))
    private void odyssey$replaceSpawnerServerParticles(ServerLevel serverLevel, int levelEvent, BlockPos blockPos, int i) {
        if (serverLevel.getBlockState(blockPos).getValue(BlockStateProperties.WATERLOGGED)) {

            RandomSource randomSource = serverLevel.getRandom();

            for (int amount = 0; amount < 20; ++amount) {

                double x = (double)blockPos.getX() + 0.5 + (randomSource.nextDouble() - 0.5) * 2.0;
                double y = (double)blockPos.getY() + 0.5 + (randomSource.nextDouble() - 0.5) * 2.0;
                double z = (double)blockPos.getZ() + 0.5 + (randomSource.nextDouble() - 0.5) * 2.0;

                serverLevel.sendParticles(ModParticles.SPAWNER_BUBBLE, x, y, z, 0, 0.0, 0.0, 0.0, 0.0);
            }
        } else {
            serverLevel.levelEvent(2004, blockPos, 0);
        }
    }
}