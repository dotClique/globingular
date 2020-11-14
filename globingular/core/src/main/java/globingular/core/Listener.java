package globingular.core;

/**
 * Implement to allow for adding observable/listener behavior.
 * 
 * @param <T> Type being listened for
 */
@FunctionalInterface
public interface Listener<T> {
    /**
     * Method to call in order to notify listeners.
     * Should handle behavior that the listener is waiting for.
     * 
     * @param event An event passing information from the {@link Observable} to the {@link Listener}
     */
    void notifyListener(ListenerEvent<T> event);
}
