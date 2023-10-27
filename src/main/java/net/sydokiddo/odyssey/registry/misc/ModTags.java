package net.sydokiddo.odyssey.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.sydokiddo.odyssey.Odyssey;

@SuppressWarnings("all")
public class ModTags {

    // region Item Tags

    public static final TagKey<Item>
        REPAIRS_ANVILS = registerItemTag("repairs_anvils"),
        GIVES_ARMOR_STANDS_ARMS = registerItemTag("gives_armor_stands_arms"),
        TNT_CRAFTING_MATERIALS = registerItemTag("tnt_crafting_materials"),
        POLISHED_STONE_CRAFTING_MATERIALS = registerItemTag("polished_stone_crafting_materials"),
        POLISHED_STONE_SLAB_CRAFTING_MATERIALS = registerItemTag("polished_stone_slab_crafting_materials"),
        LODESTONE_CRAFTING_MATERIALS = registerItemTag("lodestone_crafting_materials"),
        UNDERWATER_POTTABLE_PLANTS = registerItemTag("underwater_pottable_plants"),
        TOOLTIP_SPACE_BLACKLISTED = registerItemTag("tooltip_space_blacklisted")
    ;

    // endregion

    // region Block Tags

    public static final TagKey<Block>
        PREVENTS_SNIFFER_EGG_HATCHING = registerBlockTag("prevents_sniffer_egg_hatching"),
        UNDERWATER_FLOWER_POTS = registerBlockTag("underwater_flower_pots")
    ;

    // endregion

    // region Entity Tags

    public static final TagKey<EntityType<?>>
        CAN_BE_BRUSHED = registerEntityTag("can_be_brushed")
    ;

    // endregion

    // region Registry

    private static TagKey<Item> registerItemTag(String name) {
        return TagKey.create(Registries.ITEM, Odyssey.id(name));
    }

    private static TagKey<Block> registerBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, Odyssey.id(name));
    }

    private static TagKey<EntityType<?>> registerEntityTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Odyssey.id(name));
    }

    // endregion
}