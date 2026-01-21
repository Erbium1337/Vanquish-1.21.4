package wtf.vanquish.client.features.modules.other;

import lombok.Getter;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.events.player.other.UpdateEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;

@ModuleRegister(name = "Fast Break", category = Category.OTHER)
public class FastBreakModule extends Module {
    @Getter private static final FastBreakModule instance = new FastBreakModule();

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<>(event -> {
            mc.interactionManager.blockBreakingCooldown = 0;
            mc.interactionManager.cancelBlockBreaking();
        }));

        addEvents(updateEvent);
    }
}
