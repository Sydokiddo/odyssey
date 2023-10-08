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
        this.shadowRadius = 0.5F;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    // region Rendering

    @Override
    public void render(GunpowderBlockEntity gunpowderBlock, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {

        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5F, 0.0F);
        int fuse = gunpowderBlock.getFuse();

        if ((float)fuse - g + 1.0F < 10.0F) {
            float h = 1.0f - ((float)fuse - g + 1.0F) / 10.0F;
            h = Mth.clamp(h, 0.0f, 1.0F);
            h *= h;
            h *= h;
            float scale = 1.0f + h * 0.3F;
            poseStack.scale(scale, scale, scale);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5F, -0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, ModBlocks.GUNPOWDER_BLOCK.defaultBlockState(), poseStack, multiBufferSource, i, fuse / 5 % 2 == 0);
        poseStack.popPose();

        super.render(gunpowderBlock, f, g, poseStack, multiBufferSource, i);
    }

    // endregion

    @SuppressWarnings("deprecation")
    @Override
    public ResourceLocation getTextureLocation(GunpowderBlockEntity gunpowderBlock) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}