package net.sydokiddo.odyssey.registry.entities.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.registry.entities.registry.ModEntities;

public class ModBrushableBlockEntity extends BrushableBlockEntity {

    public ModBrushableBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModEntities.BRUSHABLE_BLOCK;
    }
}