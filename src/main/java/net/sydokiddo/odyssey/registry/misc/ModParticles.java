package net.sydokiddo.odyssey.registry.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.sydokiddo.chrysalis.misc.util.CoreRegistry;
import net.sydokiddo.chrysalis.mixin.util.SimpleParticleTypeAccessor;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.client.particles.SpawnerBubbleParticle;

public class ModParticles {

    public static final net.sydokiddo.chrysalis.misc.util.CoreRegistry<ParticleType<?>> PARTICLES = CoreRegistry.create(Registries.PARTICLE_TYPE, Odyssey.MOD_ID);

    // region Particles

    public static final SimpleParticleType
        SPAWNER_BUBBLE = register("spawner_bubble", false)
    ;

    // endregion

    // region Registry

    @SuppressWarnings("all")
    private static SimpleParticleType register(String name, boolean alwaysSpawn) {
        return PARTICLES.register(name, SimpleParticleTypeAccessor.createSimpleParticleType(alwaysSpawn));
    }

    @Environment(EnvType.CLIENT)
    public static void registerParticles() {

        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();

        registry.register(SPAWNER_BUBBLE, SpawnerBubbleParticle.SpawnerBubbleProvider::new);
    }

    // endregion
}