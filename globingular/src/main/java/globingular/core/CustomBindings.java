package globingular.core;

import javafx.beans.property.SetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.SetChangeListener;

public class CustomBindings {
    /**
     * Create a readonly sorted-list view of the target property
     *
     * @return A new sorted-list view of the target property
     */
    public static <T> ObservableList<T> createSortedListView(SetProperty<T> targetProperty) {
        ObservableList<T> backing = FXCollections.observableArrayList();
        backing.addAll(targetProperty);

        targetProperty.addListener((SetChangeListener<? super T>) e -> {
            if (e.wasAdded()) {
                backing.add(e.getElementAdded());
            } else {
                backing.remove(e.getElementRemoved());
            }
        });
        return backing.sorted();
    }
}
