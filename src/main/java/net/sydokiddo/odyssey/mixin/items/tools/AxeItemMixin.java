package net.sydokiddo.odyssey.mixin.items.tools;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.items.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"))
    private void odyssey$dropPatinaFromScrapingCopper(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {

        Level level = useOnContext.getLevel();
        Optional<BlockState> weatheringCopper = WeatheringCopper.getPrevious(level.getBlockState(useOnContext.getClickedPos()));

        if (weatheringCopper.isPresent() && level.getRandom().nextFloat() < Odyssey.getConfig().items.patina_drop_chance) {
            Block.popResourceFromFace(level, useOnContext.getClickedPos(), useOnContext.getClickedFace(), new ItemStack(ModItems.PATINA));
        }
    }
}