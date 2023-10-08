package net.sydokiddo.odyssey.mixin.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(MobBucketItem.class)
public class MobBucketItemMixin {

    @Shadow @Final private EntityType<?> type;

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void odyssey$addAxolotlBucketTooltip(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci) {

        CompoundTag compoundTag = itemStack.getTag();

        if (itemStack.is(Items.AXOLOTL_BUCKET) && this.type == EntityType.AXOLOTL && compoundTag != null && compoundTag.contains(Axolotl.VARIANT_TAG) && Odyssey.getConfig().items.mob_bucket_variant_tooltips) {

            String translationString = "entity.axolotl_type." + compoundTag.getInt(Axolotl.VARIANT_TAG);
            ChatFormatting[] chatFormattings = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};

            list.add(Component.translatable(translationString).withStyle(chatFormattings));
        }
    }
}