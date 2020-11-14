package globingular.core;

/**
 * Implement to allow for adding observable/listener behavior.
 * 
 * @param <T> Type being returned to listeners
 */
public interface Observable<T> {
    /**
     * Add a new {@link Listener} to be notified.
     * 
     * @param listener The listener to be notified
     * @return true if listener successfully added, otherwise false
     */
    boolean addListener(Listener<T> listener);

    /**
     * Remove the {@link Listener} to no longer be notified.
     * 
     * @param listener The listener to no longer be notified
     * @return true if listener successfully removed, otherwise false
     */
    boolean removeListener(Listener<T> listener);
}
