package net.sydokiddo.odyssey.misc.util.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.sydokiddo.odyssey.Odyssey;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class MapTooltipComponent implements ClientTooltipComponent, TooltipComponent {

    private final ResourceLocation map_background = new ResourceLocation("textures/map/map_background.png");
    private final Integer id;
    private final MapItemSavedData data;

    public MapTooltipComponent(ItemStack itemStack) {
        id = MapItem.getMapId(itemStack);
        data = MapItem.getSavedData(id, Minecraft.getInstance().level);
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {

        if (Minecraft.getInstance().level == null || id == null || data == null || !Odyssey.getConfig().items.tooltipConfig.maps) return;
        final PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        guiGraphics.blit(map_background, x, y, 0, 0, 64, 64, 64, 64);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(x + 3.2F, y + 3.2F, 401);
        poseStack.scale(0.45F, 0.45F, 1);
        Minecraft.getInstance().gameRenderer.getMapRenderer().render(poseStack, guiGraphics.bufferSource(), id, data, true, 15728880);
        poseStack.popPose();
    }

    @Override
    public int getHeight() {
        return 72;
    }

    @Override
    public int getWidth(Font font) {
        return 66;
    }
}