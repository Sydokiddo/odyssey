package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.registry.entities.block_entities.ModBrushableBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ModBrushableBlock extends BrushableBlock {

    public ModBrushableBlock(Block block, SoundEvent brushingSound, SoundEvent brushingCompleteSound, Properties properties) {
        super(block, brushingSound, brushingCompleteSound, properties);
    }

    @Override @Nullable
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ModBrushableBlockEntity(blockPos, blockState);
    }
}