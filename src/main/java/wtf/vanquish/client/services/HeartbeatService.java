package wtf.vanquish.client.services;

import lombok.Getter;
import net.minecraft.client.gui.screen.ChatScreen;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.events.client.KeyEvent;
import wtf.vanquish.api.event.events.other.ScreenEvent;
import wtf.vanquish.api.event.events.client.TickEvent;
import wtf.vanquish.api.event.events.render.Render2DEvent;
import wtf.vanquish.api.module.ModuleManager;
import wtf.vanquish.api.system.client.GpsManager;
import wtf.vanquish.api.system.configs.ConfigSkin;
import wtf.vanquish.api.system.configs.MacroManager;
import wtf.vanquish.api.system.draggable.DraggableManager;
import wtf.vanquish.api.system.interfaces.QuickImports;
import wtf.vanquish.api.utils.other.ScreenUtil;
import wtf.vanquish.api.utils.other.SlownessManager;

public class HeartbeatService implements QuickImports {
    @Getter private static final HeartbeatService instance = new HeartbeatService();

    public void load() {
        keyEvent();
        render2dEvent();
        tickEvent();
        screenEvent();
    }

    private void screenEvent() {
        ScreenEvent.getInstance().subscribe(new Listener<>(event -> {
            ScreenUtil.drawButton(event);
        }));
    }

    private void tickEvent() {
        TickEvent.getInstance().subscribe(new Listener<>(event -> {
            SlownessManager.tick();

            ConfigSkin.getInstance().fetchSkin();
        }));
    }

    private void render2dEvent() {
        Render2DEvent.getInstance().subscribe(new Listener<>(event -> {
            if (mc.currentScreen instanceof ChatScreen) {
                DraggableManager.getInstance().getDraggables().forEach((s, draggable) -> {
                    if (draggable.getModule().isEnabled()) {
                        draggable.onDraw();
                    }
                });
            }

            GpsManager.getInstance().update(event.context());
        }));
    }

    private void keyEvent() {
        KeyEvent.getInstance().subscribe(new Listener<>(event -> {
            if (event.action() != 1 || event.key() == -999 || event.key() == -1) return;

            int action = event.action();
            int key = event.key() + (event.mouse() ? -100 : 0);

            if (mc.currentScreen == null) {
                ModuleManager.getInstance().getModules().forEach(module -> {
                    int bind = module.getBind();
                    if (bind == key && module.hasBind()) {
                        module.toggle();
                    }
                });

                MacroManager.getInstance().onKeyPressed(key);
            }
        }));
    }
}
