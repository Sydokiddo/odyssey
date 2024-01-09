package net.sydokiddo.odyssey.registry.blocks;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockStateProperties {

    public static final BooleanProperty WAXED = BooleanProperty.create("waxed");
    public static final IntegerProperty SHEETS = IntegerProperty.create("sheets", 1, 8);
}