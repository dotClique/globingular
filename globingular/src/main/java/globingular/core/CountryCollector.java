package globingular.core;

import javafx.beans.property.ReadOnlySetWrapper;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.*;

import java.util.*;

import static java.util.Arrays.binarySearch;

public class CountryCollector {

    /**
     * Set of visited countries. Using SetProperty for observability and binding.
     */
    private final SetProperty<Country> visitedCountries = new SimpleSetProperty<>(FXCollections.observableSet());
    private final SetProperty<Country> existingCountries = new SimpleSetProperty<>(FXCollections.observableSet());
    /**
     * Existing countries by countryCodes according to the ISO 3166-1 alpha-2 standard.
     */
    private final HashMap<String, Country> existingCountriesByCode = new HashMap<>();
    private final HashMap<String, Country> existingCountriesByName = new HashMap<>();

    private final SetProperty<Country> existingCountriesReadOnly = new ReadOnlySetWrapper<>(existingCountries);
    private final ObservableList<Country> visitedCountriesSorted =
            CustomBindings.createSortedListView(this.visitedCountries);

    /**
     * Set visitation status of the given country
     *
     * @param country The Country to log
     */
    public void setVisited(final Country country) {
        if (!existingCountries.contains(country)) throw new IllegalArgumentException("Unknown country");
        this.visitedCountries.add(country);
    }

    /**
     * Remove visitation status of the given country
     * @param country The Country to mark as not visited
     */
    public void removeVisited(final Country country) {
        if (!existingCountries.contains(country)) throw new IllegalArgumentException("Unknown country");
        this.visitedCountries.remove(country);
    }

    /**
     * Check if the given country has been visited
     *
     * @param country The Country to check
     * @return Returns true if the country has been visited. Returns false for all
     * non-logged countries
     */
    public boolean hasVisited(final Country country) {
        if (!existingCountries.contains(country)) throw new IllegalArgumentException("Unknown country");
        return this.visitedCountries.contains(country);
    }

    /**
     * Get an observable set of all visits.
     * Cannot be iterated safely while removing items.
     *
     * @return Observable set of all visits
     */
    public ObservableSet<Country> getVisitedCountries() {
        return visitedCountries.get();
    }

    /**
     * Get the property responsible for keeping track of visited countries
     *
     * @return Property responsible for keeping track of visited countries
     */
    public SetProperty<Country> visitedCountriesProperty() {
        return visitedCountries;
    }

    /**
     * Get a sorted-list view of the visitedCountries-property
     *
     * @return Sorted-list view of the visitedCountries-property
     */
    public final ObservableList<Country> getVisitedCountriesSorted() {
        return visitedCountriesSorted;
    }

    /**
     * Get the amount of countries visited
     *
     * @return Returns number of countries visited
     */
    public int numberVisited() {
        return this.visitedCountries.size();
    }

    /**
     * Get a readonly-wrapper around the property responsible for keeping track of existing countries
     *
     * @return Readonly-wrapper around property responsible for keeping track of existing countries
     */
    public SetProperty<Country> existingCountriesProperty() {
        return existingCountriesReadOnly;
    }

    /**
     * Return a plain text representation of the object. Only used as a
     * human-readable representation. Not a stable interface.
     */
    @Override
    public String toString() {
        return this.visitedCountries.getValue().toString();
    }

    /**
     * Get an existing Country (registered to this instance) by its countryCode
     *
     * @param countryCode ISO two-letter country-code of the target Country
     * @return The target Country if existing, otherwise null
     */
    public Country getCountryFromCode(String countryCode) {
        return existingCountriesByCode.get(countryCode);
    }

    /**
     * Get an existing Country (registered to this instance) by its name
     *
     * @param countryName Short-name of the target Country
     * @return The target Country if existing, otherwise null
     */
    public Country getCountryFromName(String countryName) {
        return existingCountriesByName.get(countryName);
    }

    public class Country {
        private final String countryCode;
        private final String name;
        private final String longname;
        private final String sovereignty;
        private final String region;
        private final long population;
        private final Province[] provinces;

        public Country(String countryCode, String name, String longname, String sovereignty, String region,
                       long population,
                       Province[] provinces) {
            if (existingCountriesByCode.containsKey(countryCode)) {
                throw new UnsupportedOperationException("NUHUHH! #code");
            }
            if (existingCountriesByName.containsKey(name)) {
                throw new UnsupportedOperationException("NUHUHH! #name");
            }

            existingCountriesByCode.put(countryCode, this);
            existingCountriesByName.put(name, this);
            existingCountries.add(this);

            this.countryCode = countryCode;
            this.name = name;
            this.longname = longname;
            this.sovereignty = sovereignty;
            this.region = region;
            this.population = population;
            this.provinces = provinces;
        }

        public Country(String countryCode, String name, String longname, String region, long population) {
            this(countryCode, name, longname, "UN", region, population, new Province[]{});
        }

        public String getCountryCode() {
            return countryCode;
        }

        public String getName() {
            return name;
        }

        public String getLongname() {
            return longname;
        }

        public String getSovereignty() {
            return sovereignty;
        }

        public String getRegion() {
            return region;
        }

        public long getPopulation() {
            return population;
        }

        @Override
        public String toString() {
            return "Country{" +
                    "countryCode='" + countryCode + '\'' +
                    ", name='" + name + '\'' +
                    ", longname='" + longname + '\'' +
                    ", sovereignty='" + sovereignty + '\'' +
                    ", region='" + region + '\'' +
                    ", population=" + population +
                    ", provinces=" + Arrays.toString(provinces) +
                    '}';
        }
    }
}
