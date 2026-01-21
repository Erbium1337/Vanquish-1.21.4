package wtf.vanquish.client.features.modules.combat.elytratarget;

import lombok.Getter;
import wtf.vanquish.api.event.EventListener;
import wtf.vanquish.api.event.Listener;
import wtf.vanquish.api.event.events.other.RotationUpdateEvent;
import wtf.vanquish.api.module.Category;
import wtf.vanquish.api.module.Module;
import wtf.vanquish.api.module.ModuleRegister;

@ModuleRegister(name = "Elytra Target", category = Category.COMBAT)
public class ElytraTargetModule extends Module {
    @Getter private static final ElytraTargetModule instance = new ElytraTargetModule();
    public final ElytraRotationProcessor elytraRotationProcessor = new ElytraRotationProcessor(this);

    public ElytraTargetModule() {
        addSettings(elytraRotationProcessor.getSettings());
    }

    @Override
    public void onEvent() {
        EventListener rotationUpdateEvent = RotationUpdateEvent.getInstance().subscribe(new Listener<>(event -> {
            elytraRotationProcessor.processRotation();
        }));

        addEvents(rotationUpdateEvent);
    }
}
