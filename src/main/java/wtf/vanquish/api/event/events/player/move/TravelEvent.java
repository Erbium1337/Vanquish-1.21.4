package wtf.vanquish.api.event.events.player.move;

import lombok.Getter;
import wtf.vanquish.api.event.events.Event;

public class TravelEvent extends Event<TravelEvent> {
    @Getter private static final TravelEvent instance = new TravelEvent();
}
