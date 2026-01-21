package wtf.vanquish.client.features.modules.movement;

import lombok.Getter;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.api.module.setting.BooleanSetting;
import wtf.vanquish.api.module.setting.ModeSetting;
import wtf.vanquish.api.utils.rotation.manager.RotationManager;
import wtf.vanquish.api.utils.rotation.manager.RotationPlan;
import wtf.vanquish.client.features.modules.combat.AuraModule;

@ModuleRegister(name = "Move Fix", category = Category.MOVEMENT)
public class MoveFixModule extends Module {
    @Getter private static final MoveFixModule instance = new MoveFixModule();

    private final ModeSetting mode = new ModeSetting("Mode").value("Focus").values("Focus", "Free");
    public final BooleanSetting targeting = new BooleanSetting("Targeting").value(true);

    public MoveFixModule() {
        addSettings(mode, targeting);
    }

    @Override
    public void onEvent() {

    }

    public static boolean isTargeting() {
        RotationManager rotationManager = RotationManager.getInstance();
        RotationPlan plan = rotationManager.getCurrentRotationPlan();
        return plan != null && plan.provider() instanceof AuraModule && instance.targeting.getValue();
    }

    public static boolean enabled() {
        return instance.isEnabled();
    }

    public static boolean isFree() {
        return instance.mode.is("Free");
    }
}
