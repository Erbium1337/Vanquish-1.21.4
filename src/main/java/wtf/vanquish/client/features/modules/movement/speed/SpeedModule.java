package wtf.vanquish.client.features.modules.movement.speed;

import lombok.Generated;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.events.player.move.TravelEvent;
import wtf.vanquish.api.event.events.player.other.UpdateEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;
import wtf.vanquish.api.module.setting.ModeSetting;
import wtf.vanquish.api.module.setting.Setting;
import wtf.vanquish.api.system.backend.Choice;
import wtf.vanquish.client.features.modules.movement.speed.modes.*;

@ModuleRegister(
        name = "Speed",
        category = Category.MOVEMENT
)
public class SpeedModule extends Module {
    private static final SpeedModule instance = new SpeedModule();
    private final SpeedGrim speedGrim = new SpeedGrim(() -> {
        return this.getMode().is("Grim");
    });
    private final SpeedVanilla speedVanilla = new SpeedVanilla(() -> {
        return this.getMode().is("Vanilla");
    });
    private final SpeedMetahvh speedMetahvh = new SpeedMetahvh(() -> {
        return this.getMode().is("Metahvh");
    });
    private final SpeedAresMine speedAresMine = new SpeedAresMine(() -> {
        return this.getMode().is("AresMine");
    });
    private final SpeedHollyWorld speedHollyWorld = new SpeedHollyWorld(() -> {
        return this.getMode().is("HollyWorld");
    });
    private final SpeedMode[] modes;
    private SpeedMode currentMode;
    private final ModeSetting mode;

    public SpeedModule() {
        this.modes = new SpeedMode[]{this.speedVanilla, this.speedGrim, this.speedMetahvh, this.speedAresMine, this.speedHollyWorld};
        this.currentMode = this.speedGrim;
        this.mode = (new ModeSetting("Mode")).value(this.speedGrim.getName()).values(Choice.getValues(this.modes)).onAction(() -> {
            this.currentMode = (SpeedMode)Choice.getChoiceByName((String)this.getMode().getValue(), this.modes);
        });
        this.addSettings(new Setting[]{this.mode});
        this.addSettings(this.speedGrim.getSettings());
        this.addSettings(this.speedVanilla.getSettings());
        this.addSettings(this.speedMetahvh.getSettings());
        this.addSettings(this.speedAresMine.getSettings());
        this.addSettings(this.speedHollyWorld.getSettings());
    }

    public void toggle() {
        super.toggle();
        this.currentMode.toggle();
    }

    public void onEnable() {
        super.onEnable();
        this.currentMode.onEnable();
    }

    public void onDisable() {
        super.onDisable();
        this.currentMode.onDisable();
    }

    public void onEvent() {
        EventListener updateEvent = UpdateEvent.getInstance().subscribe(new Listener((event) -> {
            this.currentMode.onUpdate();
        }));
        EventListener travelEvent = TravelEvent.getInstance().subscribe(new Listener((event) -> {
            this.currentMode.onTravel();
        }));
        this.addEvents(new EventListener[]{updateEvent, travelEvent});
    }

    @Generated
    public static SpeedModule getInstance() {
        return instance;
    }

    @Generated
    public ModeSetting getMode() {
        return this.mode;
    }
}