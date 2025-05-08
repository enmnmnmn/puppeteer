package top.o_illusions.puppeteer.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import top.o_illusions.puppeteer.client.command.suggestionprovider.PlayerSuggestionProvider;

import java.util.List;
import java.util.UUID;

public class PuppeteerCommand {
    MinecraftClient client = MinecraftClient.getInstance();
    PlayerEntity target = null;
    UUID uuid = null;
    boolean intercept = false;
    int tick = 0;

    boolean stop = true;



    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("puppeteer")
                .requires(source -> source.hasPermissionLevel(0))
                .then(ClientCommandManager.argument("playerName", StringArgumentType.string()).suggests(new PlayerSuggestionProvider())
                        .executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "playerName")))
                ));


    }

    public int execute(FabricClientCommandSource source, String playerName) {
        ClientPlayerEntity player = source.getPlayer();
        if (player == null)
        {
            source.sendError(Text.translatable("puppeteer.commands.puppeteer.no_player"));
            return -1;
        } else {
            if (!player.isSpectator()) {
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

        World world = player.getWorld();

        for (PlayerEntity playerl : world.getPlayers()) {
            if (playerl == null) {
                continue;
            } else {
                if (playerl.getName().getString().contains(playerName)) {
                    target = playerl;
                }
            }
        }
        if (target == null) {
            source.sendError(Text.translatable("puppeteer.commands.puppeteer.no_target_player"));
            return -1;
        }
        client.setCameraEntity(target);
        this.uuid = target.getUuid();

//        SpectatorMenu

        player.interact(target, Hand.MAIN_HAND);


        source.sendFeedback(Text.literal("输入: %s".formatted(playerName)));
        source.sendFeedback(Text.literal("执行单位：%s".formatted(player.getName().getString())));

        this.intercept = true;

        return 1;
    }

    public void pupp(MinecraftClient client) {



        ClientPlayerEntity player = client.player;
        if (intercept) {
            this.target = player.getWorld().getPlayerByUuid(this.uuid);
            if (target == null) {
                player.sendMessage(Text.translatable("puppeteer.commands.puppeteer.target_loss"));
                detach();
            }
            Vec3d targetPosL = target.getPos();
            player.setPos(targetPosL.x, targetPosL.y, targetPosL.z);
            Input input = player.input;
            tick++;
            tick %= 2;
            if (tick == 0) {
                if (input.pressingForward) {
                    player.networkHandler.sendCommand("player %s move forward".formatted(target.getName().getString()));
                    this.stop = false;
                }
                if (input.pressingBack) {
                    player.networkHandler.sendCommand("player %s move backward".formatted(target.getName().getString()));
                    this.stop = false;
                }
                if (input.pressingLeft) {
                    player.networkHandler.sendCommand("player %s move left".formatted(target.getName().getString()));
                    this.stop = false;
                }
                if (input.pressingRight) {
                    player.networkHandler.sendCommand("player %s move right".formatted(target.getName().getString()));
                    this.stop = false;
                }
                if (input.jumping) {
                    player.networkHandler.sendCommand("player %s jump continuous".formatted(target.getName().getString()));
                    this.stop = false;
                }
                if (input.sneaking) {
                    player.networkHandler.sendCommand("player %s sneak".formatted(target.getName().getString()));
                    this.stop = false;
                }

                if (!(input.pressingRight
                        || input.pressingLeft
                        || input.pressingBack
                        || input.pressingForward
                        || input.jumping
                        || input.sneaking
                        || this.stop)) {
                    player.networkHandler.sendCommand("player %s stop".formatted(target.getName().getString()));
                    this.stop = true;
                }

                Matrix4f targetPos = new Matrix4f().translate(1, 1, 1);
                targetPos.mul(new Matrix4f().rotation(client.gameRenderer.getCamera().getRotation()).invert());
                targetPos.translate((float) targetPosL.x, (float) targetPosL.y, (float) targetPosL.z);

                



            }
        }
    }

    public void detach() {
        client.setCameraEntity(null);
        intercept = false;
    }


}
