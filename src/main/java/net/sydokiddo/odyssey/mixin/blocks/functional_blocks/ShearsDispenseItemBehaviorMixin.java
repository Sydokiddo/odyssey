package net.sydokiddo.odyssey.mixin.blocks.functional_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import net.sydokiddo.odyssey.registry.misc.OCommonMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
import java.util.function.Predicate;

@Mixin(ShearsDispenseItemBehavior.class)
public class ShearsDispenseItemBehaviorMixin extends OptionalDispenseItemBehavior {

    @Inject(method = "execute", at = @At("RETURN"))
    private void odyssey$executeCustomShearDispenseBehavior(BlockSource blockSource, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {

        ServerLevel level = blockSource.level();
        BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));

        if (!level.isClientSide()) {
            this.setSuccess(tryShearingPrimedTNT(level, blockPos));
            if (this.isSuccess() && itemStack.hurt(1, level.getRandom(), null)) itemStack.setCount(0);
        }
    }

    // Dispensers with Shears are able to defuse Primed TNT

    @Unique private static final Predicate<PrimedTnt> CAN_DEFUSE = (primedTnt) -> primedTnt.getFuse() > OCommonMethods.TNT_CANNOT_DEFUSE_TICKS;

    @Unique
    private static boolean tryShearingPrimedTNT(ServerLevel serverLevel, BlockPos blockPos) {

        List<PrimedTnt> list = serverLevel.getEntitiesOfClass(PrimedTnt.class, new AABB(blockPos), CAN_DEFUSE);

        if (Odyssey.getConfig().entities.miscEntitiesConfig.tnt_defusing) {
            for (PrimedTnt primedTnt : list) {
                OCommonMethods.defusePrimedTNT(serverLevel, primedTnt, blockPos, ModSoundEvents.TNT_SHEAR);
                serverLevel.gameEvent(null, GameEvent.SHEAR, blockPos);
                serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), 0, 0.0, 0.0, 0.0, 0.0);
                return true;
            }
        }
        return false;
    }
}