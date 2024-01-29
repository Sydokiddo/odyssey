package net.sydokiddo.odyssey.mixin.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.odyssey.Odyssey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;

@Mixin(HorseArmorItem.class)
public class HorseArmorItemMixin extends Item {

    @Shadow @Final private int protection;

    private HorseArmorItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (Odyssey.getConfig().items.tooltipConfig.horse_armor) {
            tooltip.add(CommonComponents.EMPTY);
            tooltip.add(Component.translatable("gui.odyssey.item.when_on_horse").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("item.odyssey.horse_armor.desc", this.protection).withStyle(ChatFormatting.BLUE));
            ItemHelper.addSpaceOnTooltipIfEnchantedOrTrimmed(itemStack, tooltip);
        }
    }
}