package net.sydokiddo.odyssey.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MagmaCubeRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.monster.MagmaCube;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(MagmaCubeRenderer.class)
public abstract class MagmaCubeRendererMixin extends MobRenderer<MagmaCube, LavaSlimeModel<MagmaCube>> {

    private MagmaCubeRendererMixin(EntityRendererProvider.Context context, LavaSlimeModel<MagmaCube> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Override
    protected boolean isShaking(MagmaCube magmaCube) {
        return super.isShaking(magmaCube) || magmaCube.getEntityData().get(OdysseyRegistry.SLIME_CONVERSION) && Odyssey.getConfig().entityChanges.slime_and_magma_cube_converting;
    }
}
