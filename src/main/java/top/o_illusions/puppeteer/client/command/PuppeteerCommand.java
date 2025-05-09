package top.o_illusions.puppeteer.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import top.o_illusions.puppeteer.client.command.suggestionprovider.PlayerSuggestionProvider;
import top.o_illusions.puppeteer.client.util.Manipulation;

import java.util.List;
import java.util.UUID;

public class PuppeteerCommand {
    MinecraftClient client = MinecraftClient.getInstance();
    Manipulation manipulation = Manipulation.getInstance();
    PlayerEntity target = null;
    UUID uuid = null;
    boolean intercept = false;
    int tick = 0;
    int l = 2;

    boolean stop = true;



    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("puppeteer")
                .requires(source -> source.hasPermissionLevel(0))
                .then(ClientCommandManager.argument("playerName", StringArgumentType.string()).suggests(new PlayerSuggestionProvider())
                        .executes((context) -> execute(context.getSource(), StringArgumentType.getString(context, "playerName")))
                ));

        dispatcher.register(ClientCommandManager.literal("puppeteer_cfg")
                .requires(source -> source.hasPermissionLevel(0))
                .then(ClientCommandManager.argument("freq", IntegerArgumentType.integer(1, 20))
                        .executes((context) -> {
                            this.l = IntegerArgumentType.getInteger(context, "freq");

                            return 1;
                        })));
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
            Input input = player.input;
            tick++;
            tick %= l;
            if (tick == 0) {
                if (input.pressingForward) {
                    sendCommand(player, manipulation.getCommand(Manipulation.DUMMY_FORWARD).formatted(target.getName().getString()));
                    this.stop = false;
                }
                if (input.pressingBack) {
                    sendCommand(player, manipulation.getCommand(Manipulation.DUMMY_BACKWARD).formatted(target.getName().getString()));
                    this.stop = false;
                }
                if (input.pressingLeft) {
                    sendCommand(player, manipulation.getCommand(Manipulation.DUMMY_LEFT).formatted(target.getName().getString()));
                    this.stop = false;
                }
                if (input.pressingRight) {
                    sendCommand(player, manipulation.getCommand(Manipulation.DUMMY_RIGHT).formatted(target.getName().getString()));
                    this.stop = false;
                }
                if (input.jumping) {
                    sendCommand(player, manipulation.getCommand(Manipulation.DUMMY_JUMP).formatted(target.getName().getString()));
                    this.stop = false;
                }
                if (input.sneaking) {
                    sendCommand(player, manipulation.getCommand(Manipulation.DUMMY_SNEAK).formatted(target.getName().getString()));
                    this.stop = false;
                }

                if (!(input.pressingRight
                        || input.pressingLeft
                        || input.pressingBack
                        || input.pressingForward
                        || input.jumping
                        || input.sneaking
                        || this.stop)) {
                    sendCommand(player, manipulation.getCommand(Manipulation.DUMMY_STOP).formatted(target.getName().getString()));
                    this.stop = true;
                }

                Vec3d targetPos = target.getEyePos();
                Vector4f targetLookPos = new Vector4f(0, 0, -1, 1);
                Matrix4f rotation = new Matrix4f().rotation(client.gameRenderer.getCamera().getRotation());
                Matrix4f translate = new Matrix4f().translate((float) targetPos.x, (float) targetPos.y, (float) targetPos.z);
                targetLookPos.mul(rotation);
                targetLookPos.mul(10000, 10000, 10000, 1);
                targetLookPos.mul(translate);

                sendCommand(player, manipulation.getCommand(Manipulation.DUMMY_LOOK).formatted(target.getName().getString(), targetLookPos.x, targetLookPos.y, targetLookPos.z));
            }
        }
    }

    public void sendCommand(ClientPlayerEntity player, String command) {
        player.networkHandler.sendCommand(command);
    }

    public void detach() {
        client.setCameraEntity(null);
        intercept = false;
        target = null;
    }


}
