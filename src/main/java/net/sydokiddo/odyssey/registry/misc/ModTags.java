package net.sydokiddo.odyssey.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.sydokiddo.odyssey.Odyssey;

public class ModTags {

    // Item Tags:

    public static final TagKey<Item> REPAIRS_ANVILS = TagKey.create(Registries.ITEM, new ResourceLocation(Odyssey.MOD_ID, "repairs_anvils"));
    public static final TagKey<Item> GIVES_ARMOR_STANDS_ARMS = TagKey.create(Registries.ITEM, new ResourceLocation(Odyssey.MOD_ID, "gives_armor_stands_arms"));

    // Entity Tags:

    public static final TagKey<EntityType<?>> CAN_BE_BRUSHED = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Odyssey.MOD_ID, "can_be_brushed"));
}
