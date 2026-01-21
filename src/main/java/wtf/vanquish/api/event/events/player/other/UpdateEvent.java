package wtf.vanquish.api.event.events.player.other;

import lombok.Getter;
import wtf.vanquish.api.event.events.Event;

public class UpdateEvent extends Event<UpdateEvent> {
    @Getter private static final UpdateEvent instance = new UpdateEvent();
}
