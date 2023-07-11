package net.sydokiddo.odyssey.registry.items.custom_items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInEmptyBucketItem;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;

public class FrogBucketItem extends MobInEmptyBucketItem {

    public FrogBucketItem(EntityType<?> entityType, SoundEvent soundEvent, Properties properties) {
        super(entityType, soundEvent, properties);
    }

    @Override
    public SoundEvent getEmptySound() {
        return ModSoundEvents.BUCKET_EMPTY_FROG;
    }
}