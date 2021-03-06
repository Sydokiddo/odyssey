package net.sydokiddo.odyssey.misc;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class OdysseyTags {
    public static final TagKey<Block> UNPHASEABLE_BLOCKS = TagKey.of(Registry.BLOCK_KEY, new Identifier("odyssey", "unphaseable_blocks"));
    public static final TagKey<Block> KINETIC_CUSHIONING_BLOCKS = TagKey.of(Registry.BLOCK_KEY, new Identifier("odyssey", "kinetic_cushioning_blocks"));
    public static final TagKey<Block> WITHER_BONE_MEAL_CONVERTIBLE = TagKey.of(Registry.BLOCK_KEY, new Identifier("odyssey", "wither_bone_meal_convertible"));
}