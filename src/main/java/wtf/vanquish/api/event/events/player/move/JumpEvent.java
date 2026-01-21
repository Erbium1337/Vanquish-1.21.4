package wtf.vanquish.api.event.events.player.move;

import lombok.Getter;
import wtf.vanquish.api.event.events.Event;

public class JumpEvent extends Event<JumpEvent> {
    @Getter private static final JumpEvent instance = new JumpEvent();
}
