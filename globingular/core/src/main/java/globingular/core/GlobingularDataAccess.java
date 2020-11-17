package globingular.core;

/**
 * Abstraction for a data access layer for the application.
 */
public interface GlobingularDataAccess {
    /**
     * Get the {@link CountryCollector} given by this instance's context.
     * @return The {@link CountryCollector} given by this instance's context.
     */
    CountryCollector getCountryCollector();

    /**
     * Put the {@link CountryCollector} to storage with this instance's context.
     * @param collector The {@link CountryCollector} to put.
     * @return Whether the element exists in the storage after the operation.
     */
    boolean putCountryCollector(CountryCollector collector);
}
