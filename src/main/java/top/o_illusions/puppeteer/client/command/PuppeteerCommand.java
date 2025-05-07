package top.o_illusions.puppeteer.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import top.o_illusions.puppeteer.client.command.suggestionprovider.PlayerSuggestionProvider;

public class PuppeteerCommand implements ICommand{
    public PuppeteerCommand(){
    }

    @Override
    public void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(CommandManager.literal("puppeteer")
                .requires(source -> source.hasPermissionLevel(0))
                .then(CommandManager.argument("playerName", StringArgumentType.string()).suggests(new PlayerSuggestionProvider())
                .executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "playerName")))
        ));
    }

    public int execute(ServerCommandSource source, String playerName) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null)
        {
            source.sendError(Text.literal("非玩家执行单位"));
            return -1;
        }

        source.sendMessage(Text.literal(playerName));
        source.sendMessage(Text.translatable("puppeteer.commands.puppeteer.test"));
        source.sendMessage(Text.literal("执行单位：%s".formatted(source.getPlayer().getName().getString())));

        return 1;
    }
}
