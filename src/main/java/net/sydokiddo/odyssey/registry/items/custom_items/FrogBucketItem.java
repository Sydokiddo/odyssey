package net.sydokiddo.odyssey.registry.items.custom_items;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInContainerItem;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class FrogBucketItem extends MobInContainerItem {

    private final EntityType<?> type;

    public FrogBucketItem(EntityType<?> entityType, SoundEvent emptySound, Properties properties, Item returnItem) {
        super(entityType, emptySound, properties, returnItem);
        this.type = entityType;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {

        CompoundTag compoundTag = itemStack.getTag();
        String VARIANT_TAG = "variant";

        if (this.type == EntityType.FROG && compoundTag != null && compoundTag.contains(VARIANT_TAG)) {

            String translationString = "entity.odyssey.frog_type." + itemStack.getTag().getString(EntityDataSerializers.FROG_VARIANT.toString());
            ChatFormatting[] chatFormattings = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};

            list.add(Component.translatable(translationString).withStyle(chatFormattings));
        }
    }
}