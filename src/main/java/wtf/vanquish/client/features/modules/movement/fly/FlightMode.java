package wtf.vanquish.client.features.modules.movement.fly;

import wtf.vanquish.api.event.events.player.move.MotionEvent;
import wtf.vanquish.api.system.backend.Choice;

public abstract class FlightMode extends Choice {


    // events
    public void onUpdate() {}
    public void onMotion(MotionEvent.MotionEventData event) {}

    // module methods
    public void onEnable() {}
    public void onDisable() {}
    public void toggle() {}
}
