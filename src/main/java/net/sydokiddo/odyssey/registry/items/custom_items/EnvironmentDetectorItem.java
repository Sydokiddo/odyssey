package net.sydokiddo.odyssey.registry.items.custom_items;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.odyssey.registry.misc.ModSoundEvents;

public class EnvironmentDetectorItem extends Item {

    public EnvironmentDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        player.playSound(ModSoundEvents.ENVIRONMENT_DETECTOR_USE, 1.0F, 1.0F + level.getRandom().nextFloat() * 0.2F);
        player.gameEvent(GameEvent.ITEM_INTERACT_START);

        if (!level.isClientSide()) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        Holder<Biome> biome = level.getBiome(player.getOnPos());

        biome.unwrapKey().ifPresent(key -> {
            Component biomeName = Component.translatable(Util.makeDescriptionId("biome", key.location()));
            Component component = Component.translatable("gui.odyssey.item.environment_detector.biome", biomeName);

            Minecraft.getInstance().gui.setOverlayMessage(component, false);
            Minecraft.getInstance().getNarrator().sayNow(component);
        });

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide());
    }
}