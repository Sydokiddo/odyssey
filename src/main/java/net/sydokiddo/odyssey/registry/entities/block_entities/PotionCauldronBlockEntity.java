package net.sydokiddo.odyssey.registry.entities.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.client.rendering.ModEntityRenderer;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.entities.registry.ModEntities;
import org.jetbrains.annotations.NotNull;

public class PotionCauldronBlockEntity extends BlockEntity {

    public Potion potion = ModBlocks.POTION_CAULDRON_STATE.potion;
    private final String potionString = "potion";

    public PotionCauldronBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModEntities.POTION_CAULDRON, blockPos, blockState);
    }

    // region NBT

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putString(this.potionString, String.valueOf(BuiltInRegistries.POTION.getKey(this.getPotion())));
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        this.potion = Potion.byName(compoundTag.getString(this.potionString));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    public int getColor() {
        if (this.hasPotion()) {
            return PotionUtils.getColor(this.getPotion());
        }
        return 0xffffff;
    }

    public Potion getPotion() {
        return this.potion;
    }

    public boolean hasPotion() {
        return this.getPotion() != Potions.EMPTY && this.getPotion() != null;
    }

    // endregion

    // region Particles

    public static void particleTick(Level level, BlockPos blockPos, PotionCauldronBlockEntity potionCauldronBlockEntity) {

        if (potionCauldronBlockEntity.hasPotion()) {
            potionCauldronBlockEntity.getPotion().getEffects();
            ModEntityRenderer.setPotionCauldronRenderColors();
            int color = PotionUtils.getColor(potionCauldronBlockEntity.getPotion());

            potionCauldronBlockEntity.setChanged();
            level.sendBlockUpdated(potionCauldronBlockEntity.getBlockPos(), potionCauldronBlockEntity.getBlockState(), potionCauldronBlockEntity.getBlockState(), Block.UPDATE_ALL);

            if (level.getRandom().nextFloat() > 0.05F) return;

            double r = (double) (color >> 16 & 0xFF) / 255.0;
            double g = (double) (color >> 8 & 0xFF) / 255.0;
            double b = (double) (color & 0xFF) / 255.0;

            level.addParticle(ParticleTypes.ENTITY_EFFECT, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, r, g, b);

            potionCauldronBlockEntity.setChanged();
            level.sendBlockUpdated(potionCauldronBlockEntity.getBlockPos(), potionCauldronBlockEntity.getBlockState(), potionCauldronBlockEntity.getBlockState(), Block.UPDATE_ALL);
        }
    }

    // endregion

    // region Misc Methods

    public boolean tryApplyPotion(Potion potion) {
        if (this.getPotion() == Potions.EMPTY || this.getPotion() == potion) {

            this.potion = potion;

            if (this.getLevel() != null) {
                this.setChanged();
                this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
            }
            return true;
        }
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // endregion
}