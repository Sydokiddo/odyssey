package net.sydokiddo.odyssey.mixin.blocks.functional_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChiseledBookShelfBlock.class)
public class ChiseledBookshelfBlockMixin {

    // I don't like to have to cancel vanilla methods, but I currently can't find another way for this to work as of now, I'll probably fix it later

    @Redirect(method = "addBook", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private static void odyssey$cancelAddBookSound(Level level, Player player, BlockPos blockPos, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {}

    @Inject(at = @At("HEAD"), method = "addBook")
    private static void odyssey$playBoundBookInsertSound(Level level, BlockPos blockPos, Player player, ChiseledBookShelfBlockEntity chiseledBookShelfBlockEntity, ItemStack itemStack, int slot, CallbackInfo info) {

        if (!level.isClientSide) {

            SoundEvent soundEvent;

            if (itemStack.is(ModItems.ALLAY_BOUND_BOOK)) {
                soundEvent = ModSoundEvents.CHISELED_BOOKSHELF_INSERT_ALLAY;
            } else if (itemStack.is(ModItems.VEX_BOUND_BOOK)) {
                soundEvent = ModSoundEvents.CHISELED_BOOKSHELF_INSERT_VEX;
            } else {
                soundEvent = itemStack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_INSERT;
            }

            level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Redirect(method = "removeBook", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private static void odyssey$cancelRemoveBookSound(Level level, Player player, BlockPos blockPos, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {}

    @Inject(at = @At("HEAD"), method = "removeBook")
    private static void odyssey$playBoundBookRemoveSound(Level level, BlockPos blockPos, Player player, ChiseledBookShelfBlockEntity chiseledBookShelfBlockEntity, int slot, CallbackInfo info) {

        if (!level.isClientSide) {

            SoundEvent soundEvent;
            ItemStack itemStack = chiseledBookShelfBlockEntity.removeItem(slot, 1);

            if (itemStack.is(ModItems.ALLAY_BOUND_BOOK)) {
                soundEvent = ModSoundEvents.CHISELED_BOOKSHELF_PICKUP_ALLAY;
            } else if (itemStack.is(ModItems.VEX_BOUND_BOOK)) {
                soundEvent = ModSoundEvents.CHISELED_BOOKSHELF_PICKUP_VEX;
            } else {
                soundEvent = itemStack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_PICKUP;
            }

            level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (!player.getInventory().add(itemStack)) {
                player.drop(itemStack, false);
            }
        }
    }
}