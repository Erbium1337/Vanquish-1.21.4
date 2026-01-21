package wtf.vanquish.client.features.modules.movement;

import lombok.Getter;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.events.player.other.UpdateEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.api.utils.player.MoveUtil;
import wtf.vanquish.api.utils.player.PlayerUtil;

@ModuleRegister(name = "No Web", category = Category.MOVEMENT)
public class NoWebModule extends Module {
    @Getter private static final NoWebModule instance = new NoWebModule();

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<>(event -> {
            if (PlayerUtil.isInWeb()) {
                mc.player.setVelocity(0, 0, 0);

                double verticalSpeed = 0.995;
                double horizantalSpeed = 0.19175;

                if (mc.options.jumpKey.isPressed()) {
                    mc.player.getVelocity().y = verticalSpeed;
                } else if (mc.options.sneakKey.isPressed()) {
                    mc.player.getVelocity().y = -verticalSpeed;
                }

                MoveUtil.setSpeed(horizantalSpeed);
            }
        }));

        addEvents(updateEvent);
    }
}
