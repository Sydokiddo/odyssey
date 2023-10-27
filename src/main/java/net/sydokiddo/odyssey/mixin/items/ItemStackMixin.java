package net.sydokiddo.odyssey.mixin.items;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.items.ModItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();
    @Shadow public abstract ItemStack copy();
    private final Minecraft client = Minecraft.getInstance();

    // To Move to Chrysalis

    private void addCoordinatesTooltip(List<Component> tooltip, int x, int y, int z) {
        tooltip.add(Component.translatable("gui.chrysalis.coordinates", x, y, z).withStyle(ChatFormatting.BLUE));
    }

    private void addDimensionTooltip(List<Component> tooltip, String dimension) {
        tooltip.add(Component.translatable("gui.chrysalis.dimension", Component.translatable("dimension." + dimension).withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.BLUE));
    }

    private void addNullTooltip(List<Component> tooltip) {
        tooltip.add(Component.translatable("gui.chrysalis.none").withStyle(ChatFormatting.BLUE));
    }

    @Inject(method = "getTooltipLines", at = @At("RETURN"))
    private void odyssey$addCompassAndClockTooltips(@Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {

        if (client != null && client.level != null && client.player != null) {

            if (Odyssey.getConfig().items.more_tooltips) {

                // region Compass and Map

                if (getItem() == Items.COMPASS || getItem() == Items.FILLED_MAP) {

                    int x;
                    int y;
                    int z;

                    if (CompassItem.isLodestoneCompass(this.copy())) {

                        cir.getReturnValue().add(Component.translatable("gui.odyssey.item.compass.lodestone_location").withStyle(ChatFormatting.GRAY));

                        x = copy().getOrCreateTag().getCompound(CompassItem.TAG_LODESTONE_POS).getInt("X");
                        y = copy().getOrCreateTag().getCompound(CompassItem.TAG_LODESTONE_POS).getInt("Y");
                        z = copy().getOrCreateTag().getCompound(CompassItem.TAG_LODESTONE_POS).getInt("Z");

                    } else {

                        cir.getReturnValue().add(Component.translatable("gui.odyssey.item.compass.current_location").withStyle(ChatFormatting.GRAY));

                        x = client.player.getBlockX();
                        y = client.player.getBlockY();
                        z = client.player.getBlockZ();
                    }

                    this.addCoordinatesTooltip(cir.getReturnValue(), x, y, z);
                    if (!CompassItem.isLodestoneCompass(this.copy()))
                        cir.getReturnValue().add(Component.translatable("gui.odyssey.direction." + client.player.getDirection().getName()).withStyle(ChatFormatting.BLUE));
                    if (CompassItem.isLodestoneCompass(this.copy()))
                        this.addDimensionTooltip(cir.getReturnValue(), copy().getOrCreateTag().getString(CompassItem.TAG_LODESTONE_DIMENSION));
                }

                // endregion

                // region Recovery Compass

                if (getItem() == Items.RECOVERY_COMPASS) {

                    cir.getReturnValue().add(Component.translatable("gui.odyssey.item.compass.death_location").withStyle(ChatFormatting.GRAY));

                    if (client.player.getLastDeathLocation().isPresent()) {

                        GlobalPos deathPos = client.player.getLastDeathLocation().get();
                        int deathX = deathPos.pos().getX();
                        int deathY = deathPos.pos().getY();
                        int deathZ = deathPos.pos().getZ();

                        this.addCoordinatesTooltip(cir.getReturnValue(), deathX, deathY, deathZ);
                        this.addDimensionTooltip(cir.getReturnValue(), client.player.getLastDeathLocation().get().dimension().location().toString());

                    } else {
                        this.addNullTooltip(cir.getReturnValue());
                    }
                }

                // endregion
            }

            // region Environment Detector

            Holder<Biome> biome = client.level.getBiome(client.player.getOnPos());

            if (getItem() == ModItems.ENVIRONMENT_DETECTOR) {

                biome.unwrapKey().ifPresent(key -> {
                    Component biomeName = Component.translatable(Util.makeDescriptionId("biome", key.location()));

                    cir.getReturnValue().add(Component.translatable("gui.odyssey.item.environment_detector.biome", biomeName).withStyle(ChatFormatting.BLUE));
                    cir.getReturnValue().add(Component.translatable("gui.odyssey.item.environment_detector.biome_temperature", biome.value().getBaseTemperature()).withStyle(ChatFormatting.BLUE));
                });

                BlockPos highestPos = new BlockPos(client.player.getBlockX(), client.level.getHeight(Heightmap.Types.WORLD_SURFACE, client.player.getBlockX(), client.player.getBlockZ()), client.player.getBlockZ()).above();

                if (client.level.isRainingAt(highestPos)) {
                    if (client.level.isThundering()) {
                        cir.getReturnValue().add(Component.translatable("gui.odyssey.item.environment_detector.weather_thundering").withStyle(ChatFormatting.BLUE));
                    } else {
                        cir.getReturnValue().add(Component.translatable("gui.odyssey.item.environment_detector.weather_raining").withStyle(ChatFormatting.BLUE));
                    }
                } else {
                    if (biome.value().getPrecipitationAt(highestPos) == Biome.Precipitation.SNOW && client.level.isRaining()) {
                        cir.getReturnValue().add(Component.translatable("gui.odyssey.item.environment_detector.weather_snowing").withStyle(ChatFormatting.BLUE));
                    } else {
                        cir.getReturnValue().add(Component.translatable("gui.odyssey.item.environment_detector.weather_clear").withStyle(ChatFormatting.BLUE));
                    }
                }
            }

            // endregion
        }
    }

    // Changes the max stack size of certain items

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void odyssey$changeMaxStackSizes(CallbackInfoReturnable<Integer> cir) {
        if (this.getItem() instanceof SignItem || this.getItem() instanceof HangingSignItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.sign_stack_size);
        }
        if (this.getItem() instanceof MinecartItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.minecart_stack_size);
        }
        if (this.getItem() instanceof BoatItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.boat_stack_size);
        }
        if (this.getItem() instanceof BannerItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.banner_stack_size);
        }
        if (this.getItem() instanceof BannerPatternItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.banner_pattern_stack_size);
        }
        if (this.getItem() instanceof ArmorStandItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.armor_stand_stack_size);
        }
        if (this.getItem() instanceof BlockItem item && item.getBlock() instanceof DecoratedPotBlock) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.decorated_pot_stack_size);
        }
        if (this.getItem() instanceof SnowballItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.snowball_stack_size);
        }
        if (this.getItem() instanceof EggItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.egg_stack_size);
        }
    }
}