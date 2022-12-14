package globingular.core;

import java.util.Collection;

/**
 * Implement to allow for adding observable/listener behavior.
 * 
 * @param <T> Type being returned to listeners
 */
public interface Observable<T> {
    /**
     * Get a collection of the registered listeners.
     *
     * @return The registered listeners.
     */
    Collection<Listener<T>> getListeners();

    /**
     * Add a new {@link Listener} to be notified.
     * 
     * @param listener The listener to be notified
     */
    void addListener(Listener<T> listener);

    /**
     * Remove the {@link Listener} to no longer be notified.
     * 
     * @param listener The listener to no longer be notified
     */
    void removeListener(Listener<T> listener);
}
