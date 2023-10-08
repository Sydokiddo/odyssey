package net.sydokiddo.odyssey.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.LivingEntity;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public abstract class GUIMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow protected abstract LivingEntity getPlayerVehicleWithHealth();
    @Shadow protected abstract int getVisibleVehicleHeartRows(int i);
    @Shadow protected abstract int getVehicleMaxHearts(LivingEntity livingEntity);

    // region Improved Mount HUD

    // Shifts the vehicle mob's health downwards

    @ModifyVariable(method = "renderVehicleHealth", at = @At(value = "STORE"), ordinal = 2)
    private int odyssey$moveVehicleHealthDownwards(int offset) {
        if (this.minecraft.gameMode != null && this.minecraft.gameMode.canHurtPlayer() && Odyssey.getConfig().entities.improved_mount_hud) {
            offset -= 10;
        }
        return offset;
    }

    // Allows for the player's hunger bar to always render when on a mount

    @Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int odyssey$alwaysRenderFoodBar(Gui gui, LivingEntity livingEntity) {
        if (Odyssey.getConfig().entities.improved_mount_hud) {
            return 0;
        } else {
            return this.getVehicleMaxHearts(livingEntity);
        }
    }

    // Shifts the player's air bubble meter upwards

    @ModifyVariable(method = "renderPlayerHealth", at = @At(value = "STORE", ordinal = 1), ordinal = 10)
    private int odyssey$moveAirBubblesUpwards(int offset) {

        LivingEntity vehicle = getPlayerVehicleWithHealth();

        if (vehicle != null && Odyssey.getConfig().entities.improved_mount_hud) {
            offset -= getVisibleVehicleHeartRows(getVehicleMaxHearts(vehicle)) * 10;
        }
        return offset;
    }

    // endregion
}