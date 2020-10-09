package globingular.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.collections.transformation.SortedList;

import java.util.Comparator;

public final class CustomBindings {

    /**
     * Remove default public constructor.
     */
    private CustomBindings() {
    }

    /**
     * Create a readonly sorted-list view of the target property.
     *
     * @param <T>        The type of values in the target set and result list
     * @param targetSet  The set to synchronize with
     * @param comparator The Comparator used to sort the list
     * @return A new sorted-list view of the target property
     */
    public static <T> SortedList<T> createSortedListView(final ObservableSet<T> targetSet,
                                                         final Comparator<T> comparator) {
        ObservableList<T> backing = FXCollections.observableArrayList();
        SortedList<T> sorted = new SortedList<>(backing, comparator);
        backing.addAll(targetSet);

        targetSet.addListener((SetChangeListener<? super T>) e -> {
            if (e.wasAdded()) {
                backing.add(e.getElementAdded());
            } else {
                backing.remove(e.getElementRemoved());
            }
        });

        return sorted;
    }
}
