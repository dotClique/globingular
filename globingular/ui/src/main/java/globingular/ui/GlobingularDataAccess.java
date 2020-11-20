package globingular.ui;

import globingular.core.CountryCollector;
import globingular.core.Visit;

/**
 * Abstraction for a data access layer for the application.
 */
public interface GlobingularDataAccess {
    /**
     * Get the {@link CountryCollector} given by this instance's context.
     * If nothing is stored, {@code null} is returned.
     * 
     * @return The {@link CountryCollector} given by this instance's context.
     *         Returns {@code null} if nothing is stored using this instance's context.
     */
    CountryCollector getCountryCollector();

    /**
     * Save the {@link CountryCollector} to storage with this instance's context.
     * 
     * @param collector The {@link CountryCollector} to save.
     * @return          Whether the element exists in the storage after the operation.
     */
    boolean saveCountryCollector(CountryCollector collector);


    /**
     * Save the {@link CountryCollector} to storage with a new username and deleting the old one.
     * Using this instance's context.
     * 
     * @param newUsername The new username to save as.
     * @param collector   The {@link CountryCollector} to save.
     * @return            True if the new one exists and the old one doesn't, after the operation.
     */
    boolean renameCountryCollector(String newUsername, CountryCollector collector);

    /**
     * Delete the {@link CountryCollector} from storage with this instance's context.
     * 
     * @return          True if the element doesn't exist in the storage after the operation.
     */
    boolean deleteCountryCollector();

    /**
     * Save the {@link Visit} to storage with this instance's context.
     * 
     * @param collector The {@link CountryCollector} to save for.
     * @param visit     The {@link Visit} to save.
     * @return          Whether the element exists in the storage after the operation.
     */
    boolean saveVisit(CountryCollector collector, Visit visit);

    /**
     * Delete the {@link Visit} from storage with this instance's context.
     * 
     * @param collector The {@link CountryCollector} to delete for.
     * @param visit     The {@link Visit} to delete.
     * @return          True if the element doesn't exist in the storage after the operation.
     */
    boolean deleteVisit(CountryCollector collector, Visit visit);
}
