package net.sydokiddo.odyssey.mixin.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PrimedTnt.class)
public abstract class PrimedTNTMixin extends Entity {

    @Shadow public abstract int getFuse();

    private PrimedTNTMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);
        boolean canDefuse = !itemStack.isEmpty() && itemStack.getItem() instanceof ShearsItem && this.getFuse() > 10;
        BlockPos blockPos = this.getOnPos().above();

        if (Odyssey.getConfig().entities.miscEntitiesConfig.tnt_defusing) {

            if (!this.level().isClientSide) {

                if (canDefuse) {
                    OdysseyRegistry.shearPrimedTNT(this.level(), this, blockPos);
                    player.gameEvent(GameEvent.SHEAR);
                    itemStack.hurtAndBreak(1, player, (shears) -> shears.broadcastBreakEvent(interactionHand));
                }
                return InteractionResult.CONSUME;

            } else {
                if (canDefuse) this.level().addParticle(ParticleTypes.LARGE_SMOKE, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), 0.0D, 0.0D, 0.0D);
                return canDefuse ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }
        }
        return super.interact(player, interactionHand);
    }
}