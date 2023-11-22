package net.sydokiddo.odyssey.mixin.items;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.items.ModItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();
    @Shadow public abstract ItemStack copy();
    private final Minecraft client = Minecraft.getInstance();

    // Adds unique tooltips to various items

    @Unique
    private static String clockNumberFormat(int number) {
        return (number < 10 ? "0" : "") + number;
    }

    @Inject(method = "getTooltipLines", at = @At("TAIL"))
    private void odyssey$addItemTooltipsAfterEnchantments(@Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {

        ItemStack itemStack = this.getItem().getDefaultInstance();

        if (!tooltipFlag.isAdvanced()) {

            if (itemStack.is(Items.CARROT_ON_A_STICK) && Odyssey.getConfig().items.tooltipConfig.carrots_on_a_stick) {
                ChrysalisRegistry.addHoldingTooltip(cir.getReturnValue());
                cir.getReturnValue().add(CommonComponents.space().append(Component.translatable("item.odyssey.carrot_on_a_stick.desc").withStyle(ChatFormatting.BLUE)));
            }

            if (itemStack.is(Items.WARPED_FUNGUS_ON_A_STICK) && Odyssey.getConfig().items.tooltipConfig.warped_fungus_on_a_stick) {
                ChrysalisRegistry.addHoldingTooltip(cir.getReturnValue());
                cir.getReturnValue().add(CommonComponents.space().append(Component.translatable("item.odyssey.warped_fungus_on_a_stick.desc").withStyle(ChatFormatting.BLUE)));
            }

            if (itemStack.is(Items.SHIELD) && Odyssey.getConfig().items.tooltipConfig.shields) {
                ChrysalisRegistry.addUseTooltip(cir.getReturnValue());
                cir.getReturnValue().add(CommonComponents.space().append(Component.translatable("item.odyssey.shield.desc").withStyle(ChatFormatting.BLUE)));
            }

            if (client != null && client.level != null && client.player != null) {

                // region Compass and Map Tooltips

                if (Odyssey.getConfig().items.tooltipConfig.compasses_and_maps) {

                    if (itemStack.is(Items.COMPASS) || itemStack.is(Items.FILLED_MAP)) {

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

                        ChrysalisRegistry.addCoordinatesTooltip(cir.getReturnValue(), x, y, z);
                        if (!CompassItem.isLodestoneCompass(this.copy()))
                            ChrysalisRegistry.addDirectionTooltip(cir.getReturnValue(), client);
                        if (CompassItem.isLodestoneCompass(this.copy()))
                            ChrysalisRegistry.addDimensionTooltip(cir.getReturnValue(), copy().getOrCreateTag().getString(CompassItem.TAG_LODESTONE_DIMENSION));
                    }

                    if (itemStack.is(Items.RECOVERY_COMPASS)) {

                        cir.getReturnValue().add(Component.translatable("gui.odyssey.item.recovery_compass.death_location").withStyle(ChatFormatting.GRAY));

                        if (client.player.getLastDeathLocation().isPresent()) {

                            GlobalPos deathPos = client.player.getLastDeathLocation().get();
                            int deathX = deathPos.pos().getX();
                            int deathY = deathPos.pos().getY();
                            int deathZ = deathPos.pos().getZ();

                            ChrysalisRegistry.addCoordinatesTooltip(cir.getReturnValue(), deathX, deathY, deathZ);
                            ChrysalisRegistry.addDimensionTooltip(cir.getReturnValue(), client.player.getLastDeathLocation().get().dimension().location().toString());
                        } else {
                            ChrysalisRegistry.addNullTooltip(cir.getReturnValue());
                        }
                    }
                }

                // endregion

                // region Clock Tooltips

                if (itemStack.is(Items.CLOCK) && Odyssey.getConfig().items.tooltipConfig.clocks) {

                    long time = client.level.getDayTime();

                    int hour = (int) ((time / 1000L + 6L) % 24L);
                    int minute = (int) (60L * (time % 1000L) / 1000L);

                    int hourOutput;
                    Component hourSystem;

                    if (hour <= 12) {
                        hourOutput = hour;
                    } else {
                        hourOutput = hour - 12;
                    }

                    if (hourOutput == 0) hourOutput = 12;

                    if (hour >= 12) {
                        hourSystem = Component.translatable("gui.odyssey.item.clock.hour_pm");
                    } else {
                        hourSystem = Component.translatable("gui.odyssey.item.clock.hour_am");
                    }

                    String clockTimeString = "gui.odyssey.item.clock.time";

                    if (!client.level.dimensionType().hasFixedTime()) {
                        cir.getReturnValue().add(Component.translatable(clockTimeString, hourOutput, clockNumberFormat(minute), hourSystem).withStyle(ChatFormatting.BLUE));
                    } else {
                        cir.getReturnValue().add(Component.translatable(clockTimeString, "§k00", "§k00", hourSystem.copy().withStyle(ChatFormatting.OBFUSCATED)).withStyle(ChatFormatting.BLUE));
                    }

                    cir.getReturnValue().add(Component.translatable("gui.odyssey.item.clock.day", client.level.getDayTime() / 24000L).withStyle(ChatFormatting.BLUE));
                }

                // endregion

                // region Environment Detector Tooltips

                Holder<Biome> biome = client.level.getBiome(client.player.getOnPos());

                if (itemStack.is(ModItems.ENVIRONMENT_DETECTOR)) {

                    biome.unwrapKey().ifPresent(key -> {
                        Component biomeName = Component.translatable(Util.makeDescriptionId("biome", key.location()));

                        cir.getReturnValue().add(Component.translatable("gui.odyssey.item.environment_detector.biome", biomeName).withStyle(ChatFormatting.BLUE));
                        cir.getReturnValue().add(Component.translatable("gui.odyssey.item.environment_detector.biome_temperature", biome.value().getBaseTemperature()).withStyle(ChatFormatting.BLUE));
                    });

                    BlockPos highestPos = new BlockPos(client.player.getBlockX(), client.level.getHeight(Heightmap.Types.WORLD_SURFACE, client.player.getBlockX(), client.player.getBlockZ()), client.player.getBlockZ()).above();
                    MutableComponent weatherType;

                    if (client.level.isRainingAt(highestPos)) {
                        if (client.level.isThundering()) {
                            weatherType = Component.translatable("gui.chrysalis.weather.thundering");
                        } else {
                            weatherType = Component.translatable("gui.chrysalis.weather.raining");
                        }
                    } else {
                        if (biome.value().getPrecipitationAt(highestPos) == Biome.Precipitation.SNOW && client.level.isRaining()) {
                            weatherType = Component.translatable("gui.chrysalis.weather.snowing");
                        } else {
                            weatherType = Component.translatable("gui.chrysalis.weather.clear");
                        }
                    }

                    cir.getReturnValue().add(Component.translatable("gui.odyssey.item.environment_detector.weather", weatherType.withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.BLUE));
                }

                // endregion
            }
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
            cir.setReturnValue(64);
        }
        if (this.getItem() instanceof SnowballItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.snowball_stack_size);
        }
        if (this.getItem() instanceof EggItem) {
            cir.setReturnValue(Odyssey.getConfig().items.itemStackSizeConfig.egg_stack_size);
        }
    }
}