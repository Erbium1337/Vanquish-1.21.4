package wtf.vanquish.client.features.modules.player;

import lombok.Getter;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.events.client.TickEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.api.module.setting.SliderSetting;
import wtf.vanquish.api.system.client.TimerManager;
import wtf.vanquish.api.utils.task.TaskPriority;

@ModuleRegister(name = "Timer", category = Category.PLAYER)
public class TimerModule extends Module {
    @Getter private static final TimerModule instance = new TimerModule();

    private final SliderSetting multiplier = new SliderSetting("Multiplier").value(2f).range(0.1f, 5f).step(0.1f);

    public TimerModule() {
        addSettings(multiplier);
    }

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<>(event -> {
            TimerManager.getInstance().addTimer(multiplier.getValue(), TaskPriority.NORMAL, this, 1);
        }));

        addEvents(tickEvent);
    }
}
