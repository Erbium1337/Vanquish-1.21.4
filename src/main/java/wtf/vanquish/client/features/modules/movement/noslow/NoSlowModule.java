package wtf.vanquish.client.features.modules.movement.noslow;

import lombok.Getter;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.events.client.TickEvent;
import wtf.vanquish.api.event.events.player.other.UpdateEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.api.module.setting.ModeSetting;
import wtf.vanquish.api.system.backend.Choice;
import wtf.vanquish.client.features.modules.movement.noslow.modes.*;
import wtf.vanquish.client.features.modules.movement.noslow.modes.NoSlowCancel;
import wtf.vanquish.client.features.modules.movement.noslow.modes.NoSlowGrim;
import wtf.vanquish.client.features.modules.movement.noslow.modes.NoSlowSlotUpdate;

@ModuleRegister(name = "No Slow", category = Category.MOVEMENT)
public class NoSlowModule extends Module {
    @Getter private static final NoSlowModule instance = new NoSlowModule();

    private final NoSlowCancel noSlowCancel = new NoSlowCancel();
    private final NoSlowSlotUpdate noSlowSlotUpdate = new NoSlowSlotUpdate();
    private final NoSlowGrim noSlowGrim = new NoSlowGrim();

    private final NoSlowMode[] modes = new NoSlowMode[]{
            noSlowCancel, noSlowSlotUpdate, noSlowGrim
    };

    private NoSlowMode currentMode = noSlowCancel;

    @Getter private final ModeSetting mode = new ModeSetting("Mode").value("Cancel").values(
            Choice.getValues(modes)
    ).onAction(() -> {
        currentMode = (NoSlowMode) Choice.getChoiceByName(getMode().getValue(), modes);
    });
    @Getter private final ModeSetting grimMode = new ModeSetting("Grim mode").value("Tick").values("Tick", "Old").setVisible(() -> mode.is("Grim")).onAction(() -> {
        noSlowGrim.bypassType = switch (getGrimMode().getValue()) {
            case "Tick" -> NoSlowGrim.BypassType.TICK;

            default -> NoSlowGrim.BypassType.OLD;
        };
    });

    public NoSlowModule() {
        addSettings(mode, grimMode);
    }

    public boolean doUseNoSlow() {
        return isEnabled() && mc.player.isUsingItem() && currentMode.slowingCancel();
    }

    @Override
    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener<>(event -> {
            currentMode.onUpdate();
        }));

        EventListener tickEvent = TickEvent.getInstance().subscribe(new Listener<>(event -> {
            currentMode.onTick();
        }));

        addEvents(updateEvent, tickEvent);
    }
}
