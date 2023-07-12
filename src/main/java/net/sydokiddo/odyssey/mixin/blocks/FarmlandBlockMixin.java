package net.sydokiddo.odyssey.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.Odyssey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class FarmlandBlockMixin extends Block {

    private FarmlandBlockMixin(Properties settings) {
        super(settings);
    }

    // Farmland won't be trampled if the entity has Feather Falling

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmBlock;turnToDirt(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"), method="fallOn", cancellable = true)
    private void odyssey_preventFarmlandTramplingWithFeatherFalling(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo info) {
        if (entity != null && EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, (LivingEntity) entity) > 0 && Odyssey.getConfig().blockChanges.feather_falling_negates_farmland_trampling) {
            super.fallOn(world, state, pos, entity, fallDistance);
            info.cancel();
        }
    }
}