package wtf.vanquish.api.event.interfaces;

import wtf.vanquish.api.event.Listener;

public interface Cacheable<T> {
    Listener<T>[] getCache();
}