package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;

public class MetalButtonBlock extends ButtonBlock {

    public MetalButtonBlock(Properties properties, BlockSetType blockSetType, int ticksToStayPressed, boolean arrowsCanPress) {
        super(properties, blockSetType, ticksToStayPressed, arrowsCanPress);
    }

    @Override
    protected @NotNull SoundEvent getSound(boolean isPressed) {
        return isPressed ? ModSoundEvents.METAL_BUTTON_CLICK_ON : ModSoundEvents.METAL_BUTTON_CLICK_OFF;
    }
}