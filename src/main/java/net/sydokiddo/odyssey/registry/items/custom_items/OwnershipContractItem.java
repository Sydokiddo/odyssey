package net.sydokiddo.odyssey.registry.items.custom_items;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.odyssey.registry.OdysseyRegistry;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OwnershipContractItem extends Item {

    private final String ownerNameString = "OwnerName";
    public static final String mobNameString = "MobName";
    private final String mobUUIDString = "MobUUID";
    private boolean hasSuccessfullyTransferred = false;

    public OwnershipContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return isContractBound(itemStack);
    }

    @Override
    public @NotNull Rarity getRarity(ItemStack itemStack) {
        if (isContractBound(itemStack)) {
            return Rarity.RARE;
        }
        return super.getRarity(itemStack);
    }

    // region Mechanics

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);
        CompoundTag compoundTag = itemStack.getOrCreateTag();

        if (isContractBound(itemStack) && compoundTag.hasUUID(mobUUIDString)) {

            if (level instanceof ServerLevel serverLevel) {

                player.awardStat(Stats.ITEM_USED.get(this));
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);

                UUID petUUID = compoundTag.getUUID(mobUUIDString);
                LivingEntity pet = (LivingEntity) serverLevel.getEntity(petUUID);

                int oldOwnerPacketValue;

                if (this.isPetOwnedByMe(pet, player) || this.isPetMissing(pet)) {

                    if (this.isPetMissing(pet)) {
                        oldOwnerPacketValue = 0;
                    } else {
                        oldOwnerPacketValue = 1;
                        this.spawnParticlesAroundMob(serverLevel, ParticleTypes.SMOKE, pet);
                    }

                    this.playFailSound(player.level(), player);

                } else {

                    if (this.getOldOwner(pet) != null) {

                        CompoundTag newOwnerName = new CompoundTag();
                        newOwnerName.putString("NewOwner", player.getName().getString());

                        FriendlyByteBuf newOwnerPacket = new FriendlyByteBuf(Unpooled.buffer());
                        newOwnerPacket.writeInt(2);
                        newOwnerPacket.writeNbt(newOwnerName);

                        ServerPlayNetworking.send(Objects.requireNonNull(this.getOldOwner(pet)), OdysseyRegistry.OWNERSHIP_CONTRACT_PACKET_ID, newOwnerPacket);
                        Objects.requireNonNull(this.getOldOwner(pet)).playNotifySound(ModSoundEvents.OWNERSHIP_CONTRACT_TRANSFER_OWNERSHIP, SoundSource.PLAYERS, 1.0F, 1.0F + level.getRandom().nextFloat() * 0.2F);
                    }

                    this.setNewOwner(pet, player);
                    this.spawnParticlesAroundMob(serverLevel, ParticleTypes.HEART, pet);
                    if (!this.hasSuccessfullyTransferred) this.playTransferSound(serverLevel, player);

                    oldOwnerPacketValue = 3;

                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                }

                FriendlyByteBuf oldOwnerPacket = new FriendlyByteBuf(Unpooled.buffer());
                oldOwnerPacket.writeInt(oldOwnerPacketValue);
                if (player instanceof ServerPlayer serverPlayer) ServerPlayNetworking.send(serverPlayer, OdysseyRegistry.OWNERSHIP_CONTRACT_PACKET_ID, oldOwnerPacket);
            }

            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }

        return super.use(level, player, interactionHand);
    }

    private boolean canBeTamed(LivingEntity livingEntity) {
        return (livingEntity instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwnerUUID() != null || livingEntity instanceof AbstractHorse abstractHorse && abstractHorse.getOwnerUUID() != null) && (!(livingEntity instanceof Camel));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {

        if (this.canBeTamed(livingEntity) && !isContractBound(itemStack) && !this.isPetMissing(livingEntity)) {

            if (livingEntity.level() instanceof ServerLevel serverLevel) {

                player.awardStat(Stats.ITEM_USED.get(this));
                player.gameEvent(GameEvent.ENTITY_INTERACT);

                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverPlayer, itemStack, livingEntity);
                }

                Component bindFailMessage = Component.translatable("gui.odyssey.item.ownership_contract.bind_fail", livingEntity.getName().getString(), itemStack.getHoverName().getString()).withStyle(ChatFormatting.RED);
                Component bindSuccessMessage = Component.translatable("gui.odyssey.item.ownership_contract.bind_success", livingEntity.getName().getString(), itemStack.getHoverName().getString()).withStyle(ChatFormatting.WHITE);

                if (this.isPetOwnedByMe(livingEntity, player) || player.isCreative()) {

                    livingEntity.level().playSound(null, player.getOnPos().above(), ModSoundEvents.OWNERSHIP_CONTRACT_SIGN, SoundSource.PLAYERS, 1.0F, 1.0F + player.level().getRandom().nextFloat() * 0.2F);
                    this.saveMobToContract(player, livingEntity, itemStack, interactionHand);
                    this.spawnParticlesAroundMob(serverLevel, ParticleTypes.HAPPY_VILLAGER, livingEntity);

                    Minecraft.getInstance().gui.setOverlayMessage(bindSuccessMessage, false);
                    Minecraft.getInstance().getNarrator().sayNow(bindSuccessMessage);

                } else {
                    this.playFailSound(player.level(), player);
                    this.spawnParticlesAroundMob(serverLevel, ParticleTypes.SMOKE, livingEntity);

                    Minecraft.getInstance().gui.setOverlayMessage(bindFailMessage, false);
                    Minecraft.getInstance().getNarrator().sayNow(bindFailMessage);
                }
            }

            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }
        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }

    private void spawnParticlesAroundMob(ServerLevel serverLevel, ParticleOptions particleType, LivingEntity livingEntity) {
        for (int particleAmount = 0; particleAmount < 7; ++particleAmount) {
            serverLevel.sendParticles(particleType, livingEntity.getRandomX(1.0), livingEntity.getRandomY() + 0.5, livingEntity.getRandomZ(1.0), 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    private void playFailSound(Level level, Player player) {
        level.playSound(null, player.getOnPos().above(), ModSoundEvents.OWNERSHIP_CONTRACT_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F + level.getRandom().nextFloat() * 0.2F);
    }

    private void playTransferSound(ServerLevel serverLevel, Player player) {
        serverLevel.playSound(null, player.getOnPos().above(), ModSoundEvents.OWNERSHIP_CONTRACT_TRANSFER_OWNERSHIP, SoundSource.PLAYERS, 1.0F, 1.0F + serverLevel.getRandom().nextFloat() * 0.2F);
        this.hasSuccessfullyTransferred = true;
    }

    private boolean isPetOwnedByMe(LivingEntity livingEntity, Player player) {
        return (livingEntity instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwnerUUID() != null && tamableAnimal.getOwnerUUID().equals(player.getUUID()) ||
        livingEntity instanceof AbstractHorse abstractHorse && abstractHorse.getOwnerUUID() != null && abstractHorse.getOwnerUUID().equals(player.getUUID()));
    }

    private boolean isPetMissing(LivingEntity livingEntity) {
        return (livingEntity == null || !livingEntity.isAlive() || livingEntity.isDeadOrDying());
    }

    public static boolean isContractBound(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTag();
        return compoundTag != null && !compoundTag.getString(mobNameString).isEmpty();
    }

    private void saveMobToContract(Player player, LivingEntity livingEntity, ItemStack oldItem, InteractionHand interactionHand) {

        ItemStack boundContract = new ItemStack(this, 1);

        CompoundTag compoundTag = boundContract.getOrCreateTag();
        compoundTag.putUUID(mobUUIDString, livingEntity.getUUID());
        compoundTag.putString(mobNameString, livingEntity.getName().getString());

        if (oldItem.hasCustomHoverName()) {
            boundContract.setHoverName(oldItem.getHoverName());
        }

        String ownerUUIDString = "OwnerUUID";

        if (livingEntity instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwnerUUID() != null) {
            compoundTag.putUUID(ownerUUIDString, tamableAnimal.getOwnerUUID());
            if (tamableAnimal.getOwner() != null) compoundTag.putString(ownerNameString, tamableAnimal.getOwner().getName().getString());
        } else if (livingEntity instanceof AbstractHorse abstractHorse && abstractHorse.getOwnerUUID() != null) {
            compoundTag.putUUID(ownerUUIDString, abstractHorse.getOwnerUUID());
            if (abstractHorse.getOwner() != null) compoundTag.putString(ownerNameString, abstractHorse.getOwner().getName().getString());
        }

        if (oldItem.getCount() <= 1 && !player.getAbilities().instabuild) {
            player.setItemInHand(interactionHand, boundContract);
        } else {
            if (!player.getAbilities().instabuild) {
                oldItem.shrink(1);
            }
            if (!player.getInventory().add(boundContract)) {
                player.drop(boundContract, false);
            }
        }
    }

    private ServerPlayer getOldOwner(LivingEntity livingEntity) {
        if (livingEntity instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwner() instanceof ServerPlayer owner) {
            return owner;
        }
        else if (livingEntity instanceof AbstractHorse abstractHorse && abstractHorse.getOwner() instanceof ServerPlayer owner) {
            return owner;
        }
        return null;
    }

    private void setNewOwner(LivingEntity livingEntity, Player player) {
        if (livingEntity instanceof TamableAnimal tamableAnimal) {
            tamableAnimal.setOwnerUUID(player.getUUID());
            tamableAnimal.setOrderedToSit(tamableAnimal.isOrderedToSit());
        }
        else if (livingEntity instanceof AbstractHorse abstractHorse) {
            abstractHorse.setOwnerUUID(player.getUUID());
        }
    }

    // endregion

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {

        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        boolean isBound = isContractBound(itemStack);

        if (isBound) {
            tooltip.add(Component.translatable("gui.odyssey.item.ownership_contract.bound").withStyle(style -> style.withItalic(true).withColor(ChatFormatting.DARK_PURPLE)));
            tooltip.add(CommonComponents.EMPTY);
        }

        if (compoundTag.contains(mobNameString)) {
            tooltip.add(Component.translatable("gui.odyssey.item.ownership_contract.bound_pet").withStyle(ChatFormatting.BLUE).append(CommonComponents.space()).append(Component.translatable("gui.odyssey.item.ownership_contract.name", compoundTag.getString(mobNameString)).withStyle(style -> style.withItalic(true).withColor(ChatFormatting.GRAY))));
        } else {
            tooltip.add(Component.translatable("gui.odyssey.item.ownership_contract.bound_pet").withStyle(ChatFormatting.BLUE).append(CommonComponents.space()).append(Component.translatable("gui.chrysalis.none").withStyle(style -> style.withItalic(true).withColor(ChatFormatting.GRAY))));
        }

        if (compoundTag.contains(ownerNameString)) {
            tooltip.add(Component.translatable("gui.odyssey.item.ownership_contract.bound_pet_owner").withStyle(ChatFormatting.BLUE).append(CommonComponents.space()).append(Component.translatable("gui.odyssey.item.ownership_contract.name", compoundTag.getString(ownerNameString)).withStyle(style -> style.withItalic(true).withColor(ChatFormatting.GRAY))));
        } else {
            tooltip.add(Component.translatable("gui.odyssey.item.ownership_contract.bound_pet_owner").withStyle(ChatFormatting.BLUE).append(CommonComponents.space()).append(Component.translatable("gui.chrysalis.none").withStyle(style -> style.withItalic(true).withColor(ChatFormatting.GRAY))));
        }

        tooltip.add(CommonComponents.EMPTY);

        if (isBound) {
            tooltip.add(Component.translatable("gui.chrysalis.item.when_used", Minecraft.getInstance().options.keyUse.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
            tooltip.add(CommonComponents.space().append(Component.translatable("item.odyssey.ownership_contract.bound_desc").withStyle(ChatFormatting.BLUE)));
        } else {
            tooltip.add(Component.translatable("gui.odyssey.item.ownership_contract.when_used").withStyle(ChatFormatting.GRAY));
            tooltip.add(CommonComponents.space().append(Component.translatable("item.odyssey.ownership_contract.unbound_desc").withStyle(ChatFormatting.BLUE)));
        }
    }
}