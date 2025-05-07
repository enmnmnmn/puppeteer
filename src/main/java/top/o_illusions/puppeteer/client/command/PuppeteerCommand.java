package top.o_illusions.puppeteer.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.GameModeArgumentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Uuids;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.NotNull;
import top.o_illusions.puppeteer.client.command.suggestionprovider.PlayerSuggestionProvider;

import java.util.Collection;
import java.util.List;

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
            source.sendError(Text.translatable("puppeteer.commands.puppeteer.no_player"));
            return -1;
        } else {
            if (player.interactionManager.getGameMode() != GameMode.SPECTATOR) {
                source.sendError(Text.translatable("puppeteer.commands.puppeteer.no_spectator"));
                return -1;
            } else {
                List<String> playerNames = (List<String>) source.getPlayerNames();
                if (!playerNames.contains(playerName)) {
                    source.sendError(Text.translatable("puppeteer.commands.puppeteer.no_input_player"));
                    return -1;
                }
            }
        }

        ServerWorld world = player.getServerWorld();

        ServerPlayerEntity target = (ServerPlayerEntity) world.getPlayerByUuid(Uuids.getOfflinePlayerUuid(playerName));
        if (target == null) {
            source.sendError(Text.translatable("puppeteer.commands.puppeteer.no_target_player"));
        }

        source.sendMessage(Text.literal("输入: %s".formatted(playerName)));
        source.sendMessage(Text.literal("执行单位：%s[GameMode: %s]".formatted(
                player.getName().getString(),
                player.interactionManager.getGameMode().getName()
        )));





        return 1;
    }
}
