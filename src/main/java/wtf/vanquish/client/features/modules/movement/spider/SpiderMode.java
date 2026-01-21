package wtf.vanquish.client.features.modules.movement.spider;

import wtf.vanquish.api.event.events.player.move.MotionEvent;
import wtf.vanquish.api.system.backend.Choice;

public abstract class SpiderMode extends Choice {
    public void onUpdate() {}
    public void onMotion(MotionEvent.MotionEventData event) {}

    public boolean hozColl() {
        return mc.player.horizontalCollision;
    }
}
