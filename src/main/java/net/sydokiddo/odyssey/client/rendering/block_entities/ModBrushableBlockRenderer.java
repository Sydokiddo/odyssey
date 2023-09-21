package net.sydokiddo.odyssey.client.rendering.block_entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.sydokiddo.odyssey.registry.entities.block_entities.ModBrushableBlockEntity;

@Environment(EnvType.CLIENT)
public class ModBrushableBlockRenderer implements BlockEntityRenderer<ModBrushableBlockEntity> {

    // Quite literally just a copy of the vanilla Brushable Block Entity renderer.
    // This is pretty hacky, but sadly I don't think there's a better way around this.

    private final ItemRenderer itemRenderer;

    public ModBrushableBlockRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(ModBrushableBlockEntity brushableBlockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {

        if (brushableBlockEntity.getLevel() != null) {

            int dustedState = brushableBlockEntity.getBlockState().getValue(BlockStateProperties.DUSTED);

            if (dustedState > 0) {

                Direction direction = brushableBlockEntity.getHitDirection();

                if (direction != null) {

                    ItemStack itemStack = brushableBlockEntity.getItem();
                    if (!itemStack.isEmpty()) {
                        poseStack.pushPose();
                        poseStack.translate(0.0F, 0.5F, 0.0F);
                        float[] fs = this.translations(direction, dustedState);
                        poseStack.translate(fs[0], fs[1], fs[2]);
                        poseStack.mulPose(Axis.YP.rotationDegrees(75.0F));
                        boolean bl = direction == Direction.EAST || direction == Direction.WEST;
                        poseStack.mulPose(Axis.YP.rotationDegrees((float)((bl ? 90 : 0) + 11)));
                        poseStack.scale(0.5F, 0.5F, 0.5F);
                        int l = LevelRenderer.getLightColor(brushableBlockEntity.getLevel(), brushableBlockEntity.getBlockState(), brushableBlockEntity.getBlockPos().relative(direction));
                        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, l, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, brushableBlockEntity.getLevel(), 0);
                        poseStack.popPose();
                    }
                }
            }
        }
    }

    private float[] translations(Direction direction, int dustedLevel) {
        float[] fs = new float[]{0.5F, 0.0F, 0.5F};
        float f = (float)dustedLevel / 10.0F * 0.75F;
        switch (direction) {
            case EAST -> fs[0] = 0.73F + f;
            case WEST -> fs[0] = 0.25F - f;
            case UP -> fs[1] = 0.25F + f;
            case DOWN -> fs[1] = -0.23F - f;
            case NORTH -> fs[2] = 0.25F - f;
            case SOUTH -> fs[2] = 0.73F + f;
        }
        return fs;
    }
}