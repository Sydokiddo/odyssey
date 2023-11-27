package net.sydokiddo.odyssey.registry;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.odyssey.Odyssey;
import net.sydokiddo.odyssey.registry.blocks.ModBlocks;
import net.sydokiddo.odyssey.registry.blocks.custom_blocks.PotionCauldronInteraction;
import net.sydokiddo.odyssey.registry.entities.registry.ModEntities;
import net.sydokiddo.odyssey.registry.items.ModItems;
import net.sydokiddo.odyssey.registry.items.ModPotions;
import net.sydokiddo.odyssey.registry.misc.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class OdysseyRegistry {

    // region To remove when Chrysalis is updated

    public static boolean hasArmorTrim(ItemStack itemStack) {
        return itemStack.getTag() != null && itemStack.getTag().contains(ArmorTrim.TAG_TRIM_ID);
    }

    public static boolean hasEnchantmentOrTrim(ItemStack itemStack) {
        return (itemStack.isEnchanted() || hasArmorTrim(itemStack));
    }

    public static void addSpaceOnTooltipIfEnchantedOrTrimmed(ItemStack itemStack, List<Component> tooltip) {
        if (hasEnchantmentOrTrim(itemStack)) {
            tooltip.add(CommonComponents.EMPTY);
        }
    }

    public static BigDecimal getFoodSaturation(ItemStack itemStack) {
        float saturationAmount = Objects.requireNonNull(itemStack.getItem().getFoodProperties()).getNutrition() * Objects.requireNonNull(itemStack.getItem().getFoodProperties()).getSaturationModifier() * 2.0F;
        return new BigDecimal(saturationAmount).setScale(1, RoundingMode.DOWN);
    }

    public static void popResourceBelow(Level level, BlockPos blockPos, ItemStack itemStack, double itemDropOffset) {

        double itemHeight = (double) EntityType.ITEM.getHeight() / 2.0;
        double x = (double)blockPos.getX() + 0.5 + Mth.nextDouble(level.getRandom(), -0.25, 0.25);
        double y = (double)blockPos.getY() - itemDropOffset + Mth.nextDouble(level.getRandom(), -0.25, 0.25) - itemHeight;
        double z = (double)blockPos.getZ() + 0.5 + Mth.nextDouble(level.getRandom(), -0.25, 0.25);

        if (!level.isClientSide() && !itemStack.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            ItemEntity itemEntity = new ItemEntity(level, x, y, z, itemStack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }

    public static void playDispenserFailSound(BlockSource blockSource) {
        blockSource.level().levelEvent(1001, blockSource.pos(), 0);
    }

    // endregion

    // region Entity Data

    public static final EntityDataAccessor<Boolean>
        MAGMA_CUBE_CONVERSION = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.BOOLEAN),
        SLIME_CONVERSION = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.BOOLEAN),
        FROM_BUCKET = SynchedEntityData.defineId(Squid.class, EntityDataSerializers.BOOLEAN),
        WAXED = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.BOOLEAN)
    ;

    // endregion

    // region Debug and Common Methods

    public static void addItemDurabilityTooltip(ItemStack itemStack, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (Odyssey.getConfig().items.tooltipConfig.durability_information && itemStack.isDamaged() && !tooltipFlag.isAdvanced()) {

            tooltip.add(Component.translatable("item.durability", itemStack.getMaxDamage() - itemStack.getDamageValue(), itemStack.getMaxDamage()).withStyle(ChatFormatting.GRAY));

            if (!itemStack.is(ModTags.TOOLTIP_SPACE_BLACKLISTED)) {
                addSpaceOnTooltipIfEnchantedOrTrimmed(itemStack, tooltip);
            }
        }
    }

    public static void doSaddleRemovingEvents(LivingEntity livingEntity, Player player, InteractionHand hand) {

        livingEntity.level().playSound(null, livingEntity, ModSoundEvents.SADDLE_UNEQUIP, SoundSource.NEUTRAL, 1.0F, 1.0F);
        player.setItemInHand(hand, Items.SADDLE.getDefaultInstance());

        if (Chrysalis.IS_DEBUG && livingEntity instanceof Saddleable saddleable && saddleable.isSaddled()) {
            Odyssey.LOGGER.info("Saddle has been successfully removed from {} by {}", livingEntity.getName().getString(), player.getName().getString());
        }
    }

    public static void sendMobConversionDebugMessage(LivingEntity startingEntity, LivingEntity resultEntity) {
        if (Chrysalis.IS_DEBUG) {
            Odyssey.LOGGER.info("{} has been converted into {}", startingEntity.getName().getString(), resultEntity.getName().getString());
        }
    }

    public static void sendCauldronInteractionDebugMessage(ItemStack startingItem, ItemStack resultItem, Block block) {
        if (Chrysalis.IS_DEBUG) {
            Odyssey.LOGGER.info("{} has been converted into {} in a {}", startingItem.getItem().getName(startingItem).getString(), resultItem.getItem().getName(resultItem).getString(), block.asItem().getName(block.asItem().getDefaultInstance()));
        }
    }

    public static void shearPrimedTNT(Level level, Entity primedTnt, BlockPos blockPos) {

        if (RegistryHelpers.isBlockStateFree(level.getBlockState(blockPos)) && !level.isOutsideBuildHeight(blockPos)) {
            level.setBlock(blockPos, Blocks.TNT.defaultBlockState(), 3);
        } else if (level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            primedTnt.spawnAtLocation(Items.TNT);
        }

        level.playSound(null, blockPos, ModSoundEvents.TNT_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
        primedTnt.discard();
    }

    // endregion

    public static void registerAll() {

        // region Blocks, Items, Etc.

        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModSoundEvents.registerSounds();
        PotionCauldronInteraction.bootstrap();
        ModPotions.registerPotions();
        ModParticles.PARTICLES.register();

        // endregion

        // region Entities

        ModEntities.registerModEntities();

        // endregion

        // region Technical

        ModCreativeModeTabs.registerCreativeTabs();
        ModLootTableModifiers.modifyLootTables();
        ModCriteriaTriggers.registerCriteriaTriggers();

        // endregion

        // region Composting

        CompostingChanceRegistry.INSTANCE.add(ModBlocks.SUGAR_CANE_BLOCK, RegistryHelpers.composter85PercentChance);

        // endregion

        // region Flammable Blocks

        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.GUNPOWDER_BLOCK, 15, 100);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.SUGAR_CANE_BLOCK, 30, 60);

        // endregion

        System.out.println("Registering Content for " + Odyssey.LOGGER.getName());
    }
}