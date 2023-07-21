package net.sydokiddo.odyssey.client.rendering.block_entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.PotionCauldronBlockEntity;

@SuppressWarnings("ALL")
@Environment(EnvType.CLIENT)
public class PotionCauldronRendering {

    public static void setRenderColors() {

        ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> {

            assert level != null;
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (pos != null && blockEntity instanceof PotionCauldronBlockEntity) {
                return ((PotionCauldronBlockEntity) blockEntity).getColor();
            }
            return 0;
        }, ModBlocks.POTION_CAULDRON);
    }
}