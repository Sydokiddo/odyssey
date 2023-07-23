package net.sydokiddo.odyssey.client.rendering.block_entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.entities.non_living_entities.GunpowderBlockEntity;

@Environment(EnvType.CLIENT)
public class GunpowderBlockEntityRenderer extends EntityRenderer<GunpowderBlockEntity> {

    private final BlockRenderDispatcher blockRenderer;

    public GunpowderBlockEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(GunpowderBlockEntity gunpowderBlock, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {

        poseStack.pushPose();
        poseStack.translate(0.0f, 0.5f, 0.0f);
        int j = gunpowderBlock.getFuse();

        if ((float)j - g + 1.0f < 10.0f) {
            float h = 1.0f - ((float)j - g + 1.0f) / 10.0f;
            h = Mth.clamp(h, 0.0f, 1.0f);
            h *= h;
            h *= h;
            float k = 1.0f + h * 0.3f;
            poseStack.scale(k, k, k);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
        poseStack.translate(-0.5f, -0.5f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, ModBlocks.GUNPOWDER_BLOCK.defaultBlockState(), poseStack, multiBufferSource, i, j / 5 % 2 == 0);
        poseStack.popPose();

        super.render(gunpowderBlock, f, g, poseStack, multiBufferSource, i);
    }

    @SuppressWarnings("ALL")
    @Override
    public ResourceLocation getTextureLocation(GunpowderBlockEntity gunpowderBlock) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}