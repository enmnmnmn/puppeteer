package top.o_illusions.puppeteer.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import top.o_illusions.puppeteer.client.command.PuppeteerCommand;
import top.o_illusions.puppeteer.client.util.Manipulation;


public class PuppeteerClient implements ClientModInitializer {
    public Manipulation manipulation = new Manipulation();
    public static PuppeteerCommand puppeteerCommand = new PuppeteerCommand();

    @Override
    public void onInitializeClient() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            puppeteerCommand.register(dispatcher, registryAccess);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            puppeteerCommand.pupp(client);
        });
    }

    public static PuppeteerCommand getPuppeteerCommand() {
        return puppeteerCommand;
    }
}
