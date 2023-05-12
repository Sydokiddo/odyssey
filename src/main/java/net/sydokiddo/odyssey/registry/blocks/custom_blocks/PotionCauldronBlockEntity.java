package net.sydokiddo.odyssey.registry.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import org.jetbrains.annotations.NotNull;
import static net.sydokiddo.odyssey.client.CauldronRendering.setRenderColors;

public class PotionCauldronBlockEntity extends BlockEntity {
    public Potion potion = ModBlocks.POTION_CAULDRON_STATE.potion;

    public PotionCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.POTION_CAULDRON_ENTITY, pos, state);
    }

    public int getColor() {
        if (this.hasPotion()) {
            return PotionUtils.getColor(this.potion);
        }
        return 0xffffff;
    }

    public Potion getPotion() {
        return this.potion;
    }

    public boolean hasPotion() {
        return this.potion != Potions.EMPTY && this.potion != null;
    }

    public boolean tryApplyPotion(Potion potion) {
        if (this.potion == Potions.EMPTY || this.potion == potion) {
            this.potion = potion;
            sync();
            return true;
        }
        return false;
    }

    public static void particleTick(Level level, BlockPos blockPos, PotionCauldronBlockEntity potionCauldronBlockEntity) {
        if (potionCauldronBlockEntity.hasPotion() && potionCauldronBlockEntity.getPotion().getEffects() != null ) {

            RandomSource randomSource = level.random;
            setRenderColors();

            int i = PotionUtils.getColor(potionCauldronBlockEntity.getPotion());

            potionCauldronBlockEntity.setChanged();
            level.sendBlockUpdated(potionCauldronBlockEntity.getBlockPos(), potionCauldronBlockEntity.getBlockState(), potionCauldronBlockEntity.getBlockState(), Block.UPDATE_ALL);

            float f1 = randomSource.nextFloat();
            if (f1 > 0.05f) return;

            double d = (double)(i >> 16 & 0xFF) / 255.0;
            double e = (double)(i >> 8 & 0xFF) / 255.0;
            double f = (double)(i & 0xFF) / 255.0;
            level.addParticle(ParticleTypes.ENTITY_EFFECT, blockPos.getX() + 0.5, blockPos.getY() + 1f, blockPos.getZ() + 0.5, d, e, f);

            potionCauldronBlockEntity.setChanged();
            level.sendBlockUpdated(potionCauldronBlockEntity.getBlockPos(), potionCauldronBlockEntity.getBlockState(), potionCauldronBlockEntity.getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        ResourceLocation identifier = BuiltInRegistries.POTION.getKey(this.potion);
        compound.putString("Potion", String.valueOf(identifier));
        super.saveAdditional(compound);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.potion = Potion.byName(compound.getString("Potion"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected void sync() {
        if (this.level != null) {
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
    }
}