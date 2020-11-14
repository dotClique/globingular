package globingular.core;

/**
 * Listener events.
 * 
 * @param <T> Type of element being passed to listeners.
 */
public interface ListenerEvent<T> {
    /**
     * Get the element for which this event fires.
     * 
     * @return the element beeing passed.
     */
    T getElement();
}
