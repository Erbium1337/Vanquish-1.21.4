package wtf.vanquish.api.event.interfaces;

public interface Notifiable<E> {
    void notify(E event);
}
