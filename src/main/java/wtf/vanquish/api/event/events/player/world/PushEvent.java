package wtf.vanquish.api.event.events.player.world;

import lombok.Getter;
import wtf.vanquish.api.event.events.Event;

public class PushEvent extends Event<PushEvent.PushEventData> {
    @Getter private static final PushEvent instance = new PushEvent();

    public record PushEventData(PushingSource source) {
        public enum PushingSource {
            BLOCK, WATER, ENTITY
        }
    }
}
