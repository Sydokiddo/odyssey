package net.sydokiddo.odyssey.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class SpawnerBubbleParticle extends TextureSheetParticle {

    // region Initialization and Ticking

    private SpawnerBubbleParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(clientLevel, d, e, f);
        this.setSize(0.02F, 0.02F);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
        this.xd = g * (double)0.2F + (Math.random() * 2.0 - 1.0) * (double)0.02F;
        this.yd = h * (double)0.2F + (Math.random() * 2.0 - 1.0) * (double)0.02F;
        this.zd = i * (double)0.2F + (Math.random() * 2.0 - 1.0) * (double)0.02F;
        this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
    }

    @Override
    public void tick() {

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.lifetime-- <= 0) {
            this.remove();
            return;
        }

        this.yd += 0.002;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.85F;
        this.yd *= 0.85F;
        this.zd *= 0.85F;

        if (!this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
            this.remove();
        }
    }

    // endregion

    // region Rendering

    @Override
    public int getLightColor(float f) {
        float g = ((float)this.age + f) / (float)this.lifetime;
        g = Mth.clamp(g, 1.0F, 0.0F);
        int lightColor = super.getLightColor(f);
        int j = lightColor & 0xFF;
        int k = lightColor >> 16 & 0xFF;
        if ((j += (int)(g * 15.0f * 16.0F)) > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    // endregion

    // region Providers

    @Environment(EnvType.CLIENT)
    public static class SpawnerBubbleProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public SpawnerBubbleProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            SpawnerBubbleParticle spawnerBubbleParticle = new SpawnerBubbleParticle(clientLevel, d, e, f, g, h, i);
            spawnerBubbleParticle.pickSprite(this.sprite);
            return spawnerBubbleParticle;
        }
    }

    // endregion
}