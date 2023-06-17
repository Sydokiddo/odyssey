package net.sydokiddo.odyssey.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.world.entity.monster.Slime;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(SlimeRenderer.class)
public abstract class SlimeRendererMixin extends MobRenderer<Slime, SlimeModel<Slime>> {

    private SlimeRendererMixin(EntityRendererProvider.Context context, SlimeModel<Slime> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Override
    protected boolean isShaking(Slime slime) {
        return super.isShaking(slime) || slime.getEntityData().get(OdysseyRegistry.MAGMA_CUBE_CONVERSION) && Odyssey.getConfig().entityChanges.slime_and_magma_cube_converting;
    }
}
