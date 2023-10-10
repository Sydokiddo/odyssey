package net.sydokiddo.odyssey.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.entity.LivingEntity;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin <T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    private LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "isShaking", at = @At("HEAD"), cancellable = true)
    private void odyssey$slimeAndMagmaCubeShaking(T livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity.getType().is(ChrysalisTags.SLIMES) && Odyssey.getConfig().entities.hostileMobsConfig.slime_and_magma_cube_converting) {
            cir.setReturnValue(livingEntity.isFullyFrozen() || livingEntity.getEntityData().get(OdysseyRegistry.SLIME_CONVERSION) || livingEntity.getEntityData().get(OdysseyRegistry.MAGMA_CUBE_CONVERSION));
        }
    }
}