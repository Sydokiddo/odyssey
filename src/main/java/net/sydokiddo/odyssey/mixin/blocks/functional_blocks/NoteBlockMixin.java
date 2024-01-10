package net.sydokiddo.odyssey.mixin.blocks.functional_blocks;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import net.sydokiddo.odyssey.registry.blocks.ModBlockStateProperties;
import net.sydokiddo.odyssey.registry.misc.OCommonMethods;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin extends Block {

    @Unique private static final BooleanProperty WAXED = ModBlockStateProperties.WAXED;

    @Shadow @Final public static EnumProperty<NoteBlockInstrument> INSTRUMENT;
    @Shadow @Nullable protected abstract ResourceLocation getCustomSoundId(Level level, BlockPos blockPos);
    @Shadow @Final public static IntegerProperty NOTE;
    @Shadow public abstract void playNote(@Nullable Entity entity, BlockState blockState, Level level, BlockPos blockPos);

    private NoteBlockMixin(Properties properties) {
        super(properties);
    }

    // region Block State Initialization

    @Inject(method = "<init>", at = @At("RETURN"))
    private void odyssey$registerNoteBlockDefaultBlockStates(Properties properties, CallbackInfo info) {
        this.registerDefaultState(this.stateDefinition.any().setValue(WAXED, false));
    }

    @Inject(method = "createBlockStateDefinition", at = @At("RETURN"))
    private void odyssey$createNoteBlockBlockStates(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo info) {
        builder.add(WAXED);
    }

    // endregion

    // region Note Block Interacting

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void odyssey$noteBlockWaxing(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!blockState.getValue(WAXED) && itemStack.is(Items.HONEYCOMB) && Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.noteBlockConfig.note_block_waxing) {

            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));

            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);
            }
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            OCommonMethods.sendWaxingDebugMessage(level, this.getName().getString(), player, blockPos);

            level.setBlock(blockPos, blockState.setValue(WAXED, true), 3);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, blockState));
            level.levelEvent(player, 3003, blockPos, 0);

            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), cancellable = true)
    private void odyssey$noteBlockInteracting(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (blockState.getValue(WAXED)) {

            cir.cancel();

            if (Chrysalis.IS_DEBUG && !level.isClientSide()) {
                Odyssey.LOGGER.info("{} is waxed, preventing its note from being cycled", this.getName().getString());
            }

            if (player instanceof ServerPlayer serverPlayer) this.sendNoteBlockPacket(serverPlayer);

            this.playNote(player, blockState, level, blockPos);
            player.awardStat(Stats.PLAY_NOTEBLOCK);

            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
    }

    // endregion

    // region Note HUD Message

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/NoteBlock;playNote(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void odyssey$sendNoteHUDMessageOnAttack(BlockState blockState, Level level, BlockPos blockPos, Player player, CallbackInfo info) {
        if (player instanceof ServerPlayer serverPlayer) {
            this.sendNoteBlockPacket(serverPlayer);
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/NoteBlock;playNote(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void odyssey$sendNoteHUDMessageOnUse(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (player instanceof ServerPlayer serverPlayer) {
            this.sendNoteBlockPacket(serverPlayer);
        }
    }

    @Unique
    private void sendNoteBlockPacket(ServerPlayer serverPlayer) {

        if (!Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.noteBlockConfig.note_block_gui_rendering) return;

        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeInt(0);
        ServerPlayNetworking.send(serverPlayer, OdysseyRegistry.NOTE_BLOCK_PACKET_ID, packet);
    }

    // endregion

    // region Note Block Muffling

    @Inject(method = "playNote", at = @At("HEAD"), cancellable = true)
    private void odyssey$playMuffledNote(Entity entity, BlockState blockState, Level level, BlockPos blockPos, CallbackInfo info) {

        TagKey<Block> woolCarpetsTag = BlockTags.WOOL_CARPETS;

        if (level.getBlockState(blockPos.above()).is(woolCarpetsTag) || blockState.getValue(INSTRUMENT).worksAboveNoteBlock() && level.getBlockState(blockPos.above()).is(woolCarpetsTag) && Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.noteBlockConfig.note_block_muffling) {

            info.cancel();
            level.blockEvent(blockPos, this, 1, 0);
            level.gameEvent(entity, GameEvent.NOTE_BLOCK_PLAY, blockPos);

            if (Chrysalis.IS_DEBUG && !level.isClientSide()) {
                Odyssey.LOGGER.info("Detected a Carpet above {}, playing muffled sound", this.getName().getString());
            }
        }
    }

    @Inject(method = "triggerEvent", at = @At("HEAD"), cancellable = true)
    private void odyssey$triggerMuffledNoteBlockEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j, CallbackInfoReturnable<Boolean> cir) {

        if (i == 1 && Odyssey.getConfig().blocks.qualityOfLifeBlockConfig.noteBlockConfig.note_block_muffling) {

            cir.cancel();

            NoteBlockInstrument noteBlockInstrument = blockState.getValue(INSTRUMENT);
            Holder<SoundEvent> soundEventHolder;
            float pitch;

            if (noteBlockInstrument.isTunable()) {
                int note = blockState.getValue(NOTE);
                pitch = (float) Math.pow(2.0, (note - 12) / 12.0);
                level.addParticle(ParticleTypes.NOTE, (double) blockPos.getX() + 0.5, (double) blockPos.getY() + 1.3, (double) blockPos.getZ() + 0.5, (double) note / 24.0, 0.0, 0.0);
            } else {
                pitch = 1.0F;
            }

            if (noteBlockInstrument.hasCustomSound()) {
                ResourceLocation resourceLocation = this.getCustomSoundId(level, blockPos);
                if (resourceLocation == null) return;
                soundEventHolder = Holder.direct(SoundEvent.createVariableRangeEvent(resourceLocation));
            } else {
                soundEventHolder = noteBlockInstrument.getSoundEvent();
            }

            level.playSeededSound(null, (double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.5, (double) blockPos.getZ() + 0.5, soundEventHolder, SoundSource.RECORDS, 0.3F, pitch, level.random.nextLong());
            cir.setReturnValue(true);
        }
    }

    // endregion
}