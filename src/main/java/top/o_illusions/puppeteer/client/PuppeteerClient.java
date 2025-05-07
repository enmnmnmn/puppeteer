package top.o_illusions.puppeteer.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import top.o_illusions.puppeteer.client.command.PuppeteerCommand;

public class PuppeteerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PuppeteerCommand puppeteerCommand = new PuppeteerCommand();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, env) -> {
            puppeteerCommand.register(dispatcher, registryAccess);
        });
    }
}
