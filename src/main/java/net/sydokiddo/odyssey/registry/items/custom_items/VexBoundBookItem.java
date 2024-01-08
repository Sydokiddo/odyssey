package net.sydokiddo.odyssey.registry.items.custom_items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.misc.util.entities.ContainerMob;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInContainerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VexBoundBookItem extends MobInContainerItem {

    private final EntityType<?> type;

    public VexBoundBookItem(EntityType<?> entityType, SoundEvent emptySound, Properties properties, Item returnItem) {
        super(entityType, emptySound, properties, returnItem);
        this.type = entityType;
    }

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack itemStack, @NotNull BlockPos blockPos) {
        if (level instanceof ServerLevel) {
            this.spawnVex((ServerLevel)level, itemStack, blockPos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
        }
    }

    private void spawnVex(ServerLevel serverLevel, ItemStack itemStack, BlockPos blockPos) {

        Entity entity = this.type.spawn(serverLevel, itemStack, null, blockPos, MobSpawnType.BUCKET, true, false);
        CompoundTag compoundTag = itemStack.getOrCreateTag();

        if (entity instanceof ContainerMob containerMob && containerMob instanceof Vex vex) {

            vex.setPersistenceRequired();
            vex.setBoundOrigin(blockPos);

            if (!compoundTag.contains("LifeTicks")) {
                vex.setLimitedLife(20 * (30 + serverLevel.random.nextInt(90)));
            }

            containerMob.loadFromItemTag(compoundTag);
            containerMob.setFromItem(true);
        }
    }
}