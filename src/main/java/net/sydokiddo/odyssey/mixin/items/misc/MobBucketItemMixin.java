package net.sydokiddo.odyssey.mixin.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(MobBucketItem.class)
public class MobBucketItemMixin extends BucketItem {

    @Shadow @Final private EntityType<?> type;

    private MobBucketItemMixin(Fluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void odyssey$addAxolotlBucketTooltip(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo ci) {

        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
        CompoundTag compoundTag = itemStack.getTag();

        if (itemStack.is(Items.AXOLOTL_BUCKET) && this.type == EntityType.AXOLOTL && compoundTag != null && compoundTag.contains(Axolotl.VARIANT_TAG) && Odyssey.getConfig().items.tooltipConfig.axolotl_buckets) {

            String translationString = "entity.axolotl_type." + compoundTag.getInt(Axolotl.VARIANT_TAG);
            ChatFormatting[] chatFormattings = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};

            tooltip.add(Component.translatable(translationString).withStyle(chatFormattings));
        }
    }
}