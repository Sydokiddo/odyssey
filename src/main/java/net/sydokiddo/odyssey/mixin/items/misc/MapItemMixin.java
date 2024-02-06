package net.sydokiddo.odyssey.mixin.items.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.misc.util.misc.MapTooltipComponent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import java.util.Optional;

@Mixin(MapItem.class)
public class MapItemMixin extends Item {

    private MapItemMixin(Properties properties) {
        super(properties);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack itemStack) {
        if (Odyssey.getConfig().items.tooltipConfig.maps) {
            return Optional.of(new MapTooltipComponent(itemStack));
        }
        return super.getTooltipImage(itemStack);
    }
}