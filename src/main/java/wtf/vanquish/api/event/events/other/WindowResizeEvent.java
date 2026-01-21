package wtf.vanquish.api.event.events.other;

import lombok.Getter;
import wtf.vanquish.api.event.events.Event;

public class WindowResizeEvent extends Event<WindowResizeEvent> {
    @Getter private static final WindowResizeEvent instance = new WindowResizeEvent();
}
