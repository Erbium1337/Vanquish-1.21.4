package wtf.vanquish.client.ui.clickgui.module;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wtf.vanquish.api.module.setting.Setting;
import wtf.vanquish.api.utils.animation.AnimationUtil;
import wtf.vanquish.client.ui.UIComponent;

@Getter
@RequiredArgsConstructor
public abstract class SettingComponent extends UIComponent {
    private final Setting<?> setting;
    private final AnimationUtil visibleAnimation = new AnimationUtil();

    public void updateHeight(float value) {
        setHeight(scaled(value));
    }
}
