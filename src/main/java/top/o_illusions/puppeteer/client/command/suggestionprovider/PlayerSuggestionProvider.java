package top.o_illusions.puppeteer.client.command.suggestionprovider;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class PlayerSuggestionProvider implements SuggestionProvider<FabricClientCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext context, SuggestionsBuilder builder) throws CommandSyntaxException {
        FabricClientCommandSource source = (FabricClientCommandSource) context.getSource();

        Collection<String> playerNames = source.getPlayerNames();

        for (String playerName : playerNames) {
            builder.suggest(playerName);
        }

        return builder.buildFuture();
    }
}
