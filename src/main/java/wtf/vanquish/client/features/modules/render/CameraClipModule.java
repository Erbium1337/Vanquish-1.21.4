package wtf.vanquish.client.features.modules.render;

import lombok.Getter;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.api.module.setting.SliderSetting;

@ModuleRegister(name = "Camera Clip", category = Category.RENDER)
public class CameraClipModule extends Module {
    @Getter private static final CameraClipModule instance = new CameraClipModule();

    public final SliderSetting distance = new SliderSetting("Distance").value(4f).range(1f,10f).step(1f);

    public CameraClipModule() {
        addSettings(distance);
    }

    @Override
    public void onEvent() {

    }
}
