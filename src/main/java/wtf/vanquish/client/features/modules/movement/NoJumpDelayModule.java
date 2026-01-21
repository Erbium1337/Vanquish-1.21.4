package wtf.vanquish.client.features.modules.movement;

import lombok.Getter;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.events.player.other.UpdateEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;

@ModuleRegister(name = "No Jump Delay", category = Category.MOVEMENT)
public class NoJumpDelayModule extends Module {
    @Getter private static final NoJumpDelayModule instance = new NoJumpDelayModule();

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<>(event -> {
            mc.player.jumpingCooldown = 0;
        }));

        addEvents(updateEvent);
    }
}
