package globingular.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.util.Comparator;
import java.util.Set;

import globingular.core.Observable;

/**
 * Custom bindings between collections to extend the kinds of
 * transformations on objects that are possible whilst still being synchronized.
 */
public final class CustomBindings {

    /**
     * Remove default public constructor.
     */
    private CustomBindings() {
    }

    /**
     * Create a readonly sorted-list view of the target property.
     * Takes a separate initialSet and observable parameter, usually
     * refering to the same data. As the observable only notifies
     * about changes, the initialSet is required for setup.
     *
     * @param <T>        The type of values in the target set and result list
     * @param initialSet The set to initialize the set with
     * @param observable The observable to synchronize with
     * @param comparator The Comparator used to sort the list
     * @return A new sorted-list view of the target property
     */
    public static <T> SortedList<T> createSortedListView(final Set<T> initialSet, final Observable<T> observable,
                                                         final Comparator<T> comparator) {
        ObservableList<T> backing = FXCollections.observableArrayList();
        SortedList<T> sorted = new SortedList<>(backing, comparator);
        backing.addAll(initialSet);

        observable.addListener(event -> {
            if (event.wasAdded()) {
                backing.add(event.getElement());
            } else if (event.wasRemoved()) {
                backing.remove(event.getElement());
            }
        });

        return sorted;
    }
}
