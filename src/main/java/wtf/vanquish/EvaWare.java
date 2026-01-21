package wtf.vanquish;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import wtf.vanquish.api.command.CommandManager;
import wtf.vanquish.api.module.ModuleManager;
import wtf.vanquish.api.system.DiscordHook;
import wtf.vanquish.api.system.configs.ConfigManager;
import wtf.vanquish.api.system.configs.ConfigSkin;
import wtf.vanquish.api.system.configs.FriendManager;
import wtf.vanquish.api.system.configs.MacroManager;
import wtf.vanquish.api.system.draggable.DraggableManager;
import wtf.vanquish.api.system.files.FileManager;
import wtf.vanquish.api.utils.other.SoundUtil;
import wtf.vanquish.api.utils.render.KawaseBlurProgram;
import wtf.vanquish.api.utils.render.fonts.Fonts;
import wtf.vanquish.api.utils.rotation.manager.RotationManager;
import wtf.vanquish.client.services.HeartbeatService;
import wtf.vanquish.client.services.RenderService;
import wtf.vanquish.client.ui.theme.ThemeEditor;
import wtf.vanquish.client.ui.widget.WidgetManager;

public class EvaWare implements ClientModInitializer {
	@Getter private static EvaWare instance = new EvaWare();

    @Override
	public void onInitializeClient() {
        instance = this;

        SoundUtil.load();

        loadManagers();
        loadServices();
        loadFiles();
    }

    public void postLoad() {
        ModuleManager.getInstance().getModules().sort((a, b) -> Float.compare(
                Fonts.PS_MEDIUM.getWidth(b.getName(), 7f),
                Fonts.PS_MEDIUM.getWidth(a.getName(), 7f)
        ));

        KawaseBlurProgram.load();
    }

    private void loadFiles() {
        ConfigManager.getInstance().load("autoConfig");
        DraggableManager.getInstance().load();
        FriendManager.getInstance().load();
        MacroManager.getInstance().load();
    }

    private void loadManagers() {
        WidgetManager.getInstance().load();
        RotationManager.getInstance().load();

        ModuleManager.getInstance().load();
        CommandManager.getInstance().load();

        ThemeEditor.getInstance().load();
    }

    private void loadServices() {
        HeartbeatService.getInstance().load();
        RenderService.getInstance().load();
        ConfigSkin.getInstance().load();

        DiscordHook.startRPC();
    }

    public void onClose() {
        ConfigManager.getInstance().save("autoConfig");
        FileManager.getInstance().save();
        ThemeEditor.getInstance().save(true);
        DraggableManager.getInstance().save();
        MacroManager.getInstance().save();

        DiscordHook.stopRPC();
    }
}