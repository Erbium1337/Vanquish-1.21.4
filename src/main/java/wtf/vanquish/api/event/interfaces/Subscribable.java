package wtf.vanquish.api.event.interfaces;

import wtf.vanquish.api.event.EventListener;

public interface Subscribable<L, T> {
    EventListener subscribe(L listener);
    void unsubscribe(L listener);
}
