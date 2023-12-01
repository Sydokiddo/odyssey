package net.sydokiddo.odyssey.misc.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.Objects;

public class ShowcaseCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("showcase").executes(ShowcaseCommand::displayItemToChat));
    }

    private static int displayItemToChat(CommandContext<CommandSourceStack> context) {

        Player player =  Objects.requireNonNull(context.getSource().getPlayer());
        ItemStack itemStack = player.getMainHandItem();
        Component component = Component.translatable("gui.odyssey.showcase_command.success", player.getDisplayName(), itemStack.getDisplayName());

        if (!itemStack.isEmpty()) {
            context.getSource().getServer().getPlayerList().broadcastSystemMessage(component, false);
        } else {
            player.sendSystemMessage(Component.translatable("gui.odyssey.showcase_command.fail").withStyle(ChatFormatting.RED));
        }

        return 1;
    }
}