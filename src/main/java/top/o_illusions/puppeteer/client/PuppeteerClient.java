package top.o_illusions.puppeteer.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import top.o_illusions.puppeteer.client.command.PuppeteerCommand;


public class PuppeteerClient implements ClientModInitializer {
    public static PuppeteerCommand puppeteerCommand = new PuppeteerCommand();

    @Override
    public void onInitializeClient() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            puppeteerCommand.register(dispatcher, registryAccess);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            puppeteerCommand.pupp(client);
            ClientPlayerEntity player = client.player;
            if (player == null) {
                return;
            }
            Vec3d targetPosL = player.getPos();
            Matrix4f targetPos = new Matrix4f().translate(1, 1, 1);
            targetPos.mul(new Matrix4f().rotation(client.gameRenderer.getCamera().getRotation()).invert());
//            targetPos.translate((float) targetPosL.x, (float) targetPosL.y, (float) targetPosL.z);

//            player.sendMessage(Text.literal("X:%f, Y:%f, Z:%f".formatted(targetPos.m30(), targetPos.m31(), targetPos.m32())));

            player.sendMessage(Text.literal("X:%f, Y:%f, Z:%f, W:%f".formatted(targetPos.m00(), targetPos.m01(), targetPos.m02(), targetPos.m03())));
            player.sendMessage(Text.literal("X:%f, Y:%f, Z:%f, W:%f".formatted(targetPos.m10(), targetPos.m11(), targetPos.m12(), targetPos.m13())));
            player.sendMessage(Text.literal("X:%f, Y:%f, Z:%f, W:%f".formatted(targetPos.m20(), targetPos.m21(), targetPos.m22(), targetPos.m23())));
            player.sendMessage(Text.literal("X:%f, Y:%f, Z:%f, W:%f".formatted(targetPos.m30(), targetPos.m31(), targetPos.m32(), targetPos.m33())));

        });
    }

    public static PuppeteerCommand getPuppeteerCommand() {
        return puppeteerCommand;
    }
}
