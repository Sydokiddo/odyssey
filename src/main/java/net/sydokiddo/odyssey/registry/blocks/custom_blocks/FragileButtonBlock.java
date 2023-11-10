package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.Nullable;

public class FragileButtonBlock extends ButtonBlock {

    private static final BooleanProperty CRACKED = BlockStateProperties.CRACKED;

    public FragileButtonBlock(Properties properties, BlockSetType blockSetType, int ticksToStayPressed, boolean arrowsCanPress) {
        super(properties, blockSetType, ticksToStayPressed, arrowsCanPress);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(FACE, AttachFace.WALL).setValue(CRACKED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CRACKED);
    }

    @Override
    public SoundType getSoundType(BlockState blockState) {
        if (blockState.getValue(CRACKED)) {
            return ModSoundEvents.FRAGILE_BUTTON_CRACKED;
        }
        return ModSoundEvents.FRAGILE_BUTTON;
    }

    @Override
    protected void playSound(@Nullable Player player, LevelAccessor levelAccessor, BlockPos blockPos, boolean isPressed) {

        super.playSound(player, levelAccessor, blockPos, isPressed);

        if (!isPressed) {
            levelAccessor.setBlock(blockPos, levelAccessor.getBlockState(blockPos).setValue(CRACKED, true), 0);
            levelAccessor.destroyBlock(blockPos, true);
        }
    }
}