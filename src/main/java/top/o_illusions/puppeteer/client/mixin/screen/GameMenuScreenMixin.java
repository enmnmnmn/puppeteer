package top.o_illusions.puppeteer.client.mixin.screen;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.o_illusions.puppeteer.client.PuppeteerClient;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        ButtonWidget detach = this.addDrawableChild(
                new ButtonWidget.Builder(Text.translatable("puppeteer.mixin.screen.game_menu_scrren.detach_butten"),
                        (button) -> {
            PuppeteerClient.getPuppeteerCommand().detach();
            client.setScreen(null);
        }).build());

        detach.setWidth(40);
    }
}
