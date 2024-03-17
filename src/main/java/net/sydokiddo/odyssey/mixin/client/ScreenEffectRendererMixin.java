package net.sydokiddo.odyssey.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.effect.MobEffects;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z"), cancellable = true)
    private static void odyssey$hideFireOverlay(Minecraft minecraft, PoseStack poseStack, CallbackInfo info) {
        if (minecraft.player != null && minecraft.player.hasEffect(MobEffects.FIRE_RESISTANCE) && Odyssey.getConfig().entities.miscEntitiesConfig.hidden_fire_overlay_with_fire_resistance) {
            info.cancel();
        }
    }
}