package wtf.vanquish.client.features.modules.other;

import lombok.Getter;
import net.minecraft.client.gui.screen.DeathScreen;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.events.client.TickEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;

@ModuleRegister(name = "Auto Respawn", category = Category.OTHER)
public class AutoRespawnModule extends Module {
    @Getter private static final AutoRespawnModule instance = new AutoRespawnModule();

    @Override
    public void onEvent() {
        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<>(event -> {
            if (mc.currentScreen instanceof DeathScreen) {
                if (mc.player.deathTime > 2) {
                    mc.player.requestRespawn();
                    mc.setScreen(null);
                }
            }
        }));

        addEvents(tickEvent);
    }
}
