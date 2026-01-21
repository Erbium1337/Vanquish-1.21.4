package wtf.vanquish.client.features.modules.movement.speed.modes;

import wtf.vanquish.api.module.setting.SliderSetting;
import wtf.vanquish.api.utils.player.MoveUtil;
import wtf.vanquish.client.features.modules.movement.speed.SpeedMode;

import java.util.function.Supplier;

public class SpeedVanilla extends SpeedMode {
    @Override
    public String getName() {
        return "Vanilla";
    }

    private final SliderSetting speed = new SliderSetting("Speed").value(1f).range(0.1f, 5f).step(0.1f);

    public SpeedVanilla(Supplier<Boolean> condition) {
        speed.setVisible(condition);
        addSettings(speed);
    }

    @Override
    public void onTravel() {
        MoveUtil.setSpeed(speed.getValue());
    }
}
