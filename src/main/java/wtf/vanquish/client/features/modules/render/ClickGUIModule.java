package wtf.vanquish.client.features.modules.render;

import lombok.Getter;
import org.lwjgl.glfw.GLFW;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.client.ui.clickgui.ScreenClickGUI;

@ModuleRegister(name = "Click GUI", category = Category.RENDER, bind = GLFW.GLFW_KEY_RIGHT_SHIFT)
public class ClickGUIModule extends Module {
    @Getter private static final ClickGUIModule instance = new ClickGUIModule();

    public ClickGUIModule() {

    }

    @Override
    public void onEnable() {
        if (mc.currentScreen != null) return;

        mc.setScreen(ScreenClickGUI.getInstance());
    }

    @Override
    public void onEvent() {
        toggle();
    }
}
