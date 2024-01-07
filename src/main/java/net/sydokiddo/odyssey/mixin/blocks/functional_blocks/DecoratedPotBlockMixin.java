package net.sydokiddo.odyssey.mixin.blocks.functional_blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.stream.Stream;

@Mixin(DecoratedPotBlock.class)
public abstract class DecoratedPotBlockMixin extends BaseEntityBlock {

    private DecoratedPotBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void odyssey$changeDecoratedPotTooltip(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo info) {

        info.cancel();
        super.appendHoverText(itemStack, blockGetter, tooltip, tooltipFlag);

        DecoratedPotBlockEntity.Decorations decorations = DecoratedPotBlockEntity.Decorations.load(BlockItem.getBlockEntityData(itemStack));

        if (!decorations.equals(DecoratedPotBlockEntity.Decorations.EMPTY)) {
            Stream.of(decorations.front(), decorations.left(), decorations.right(), decorations.back()).forEach((item) -> tooltip.add((new ItemStack(item, 1)).getHoverName().plainCopy().withStyle(ChatFormatting.GRAY)));
        }
    }
}