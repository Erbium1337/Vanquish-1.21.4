package wtf.vanquish.api.event.events.player.other;

import lombok.Getter;
import wtf.vanquish.api.event.events.Event;

public class CloseScreenEvent extends Event<CloseScreenEvent> {
    @Getter private static final CloseScreenEvent instance = new CloseScreenEvent();
}
