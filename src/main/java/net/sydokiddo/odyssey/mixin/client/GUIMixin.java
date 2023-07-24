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

    // Shifts the vehicle mob's health downwards

    @ModifyVariable(method = "renderVehicleHealth", at = @At(value = "STORE"), ordinal = 2)
    private int odyssey_moveVehicleHealthDownwards(int y) {
        assert this.minecraft.gameMode != null;
        if (this.minecraft.gameMode.canHurtPlayer() && Odyssey.getConfig().entityChanges.improved_mount_hud) {
            y -= 10;
        }
        return y;
    }

    // Allows for the player's hunger bar to always render when on a mount

    @Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int odyssey_alwaysRenderFoodBar(Gui instance, LivingEntity livingEntity) {
        if (Odyssey.getConfig().entityChanges.improved_mount_hud) {
            return 0;
        } else {
            return this.getVehicleMaxHearts(livingEntity);
        }
    }

    // Shifts the player's air bubble meter upwards

    @ModifyVariable(method = "renderPlayerHealth", at = @At(value = "STORE", ordinal = 1), ordinal = 10)
    private int odyssey_moveAirBubblesUpwards(int y) {

        LivingEntity vehicle = getPlayerVehicleWithHealth();

        if (vehicle != null && Odyssey.getConfig().entityChanges.improved_mount_hud) {
            int rows = getVisibleVehicleHeartRows(getVehicleMaxHearts(vehicle));
            y -= rows * 10;
        }
        return y;
    }
}
