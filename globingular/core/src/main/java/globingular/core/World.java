package globingular.core;

import java.util.HashMap;
import java.util.Set;

/**
 * An immutable store of {@link Country} objects, representing a single world,
 * such as Earth, Middle Earth from Lord of the Rings, or the entire observable
 * universe.
 */
public class World {

    /**
     * Immutable set containing every Country that exists in this World.
     */
    private final Set<Country> countries;
    /**
     * Name of the current world.
     */
    private final String worldName;
    /**
     * Existing countries by countryCodes according to the ISO 3166-1 alpha-2
     * standard. Values must be bijection with the countries-set.
     */
    private final HashMap<String, Country> countriesByCode = new HashMap<>();
    /**
     * Existing countries by short name. Values must be bijection with the
     * countries-set.
     */
    private final HashMap<String, Country> countriesByName = new HashMap<>();

    /**
     * Get an existing Country (registered to this instance) by its countryCode.
     *
     * @param countryCode ISO two-letter country-code of the target Country
     * @return The target Country if existing, otherwise null
     */
    public Country getCountryFromCode(final String countryCode) {
        return countriesByCode.get(countryCode);
    }

    /**
     * Get an existing Country (registered to this instance) by its name.
     *
     * @param countryName Short-name of the target Country
     * @return The target Country if existing, otherwise null
     */
    public Country getCountryFromName(final String countryName) {
        return countriesByName.get(countryName);
    }

    /**
     * Get the immutable set containing existing countries.
     *
     * @return Immutable set containing existing countries
     */
    public Set<Country> getCountries() {
        return countries;
    }

    /**
     * Get name of the current world.
     * 
     * @return The name of the world
     */
    public String getWorldName() {
        return this.worldName;
    }

    /**
     * Create a World containing an immutable set of countries, where every country
     * has a unique short name and ISO 3166-1 Alpha-2 country-code. As no
     * countryName is provided, {@code null} will be used.
     *
     * @param countries The list of every country this world contains
     * @throws DuplicateIdentifierException If two countries have the same
     *                                      countryCode or short name
     */
    public World(final Country... countries) {
        this(null, countries);
    }

    /**
     * Create a World containing an immutable set of countries, where every country
     * has a unique short name and ISO 3166-1 Alpha-2 country-code.
     *
     * @param worldName The name of the world, if none is provided {@code null} will
     *                  be used
     * @param countries The list of every country this world contains
     * @throws DuplicateIdentifierException If two countries have the same
     *                                      countryCode or short name
     */
    public World(final String worldName, final Country... countries) {
        this.worldName = worldName;
        for (final Country country : countries) {
            final Country duplicateCode = getCountryFromCode(country.getCountryCode());
            final Country duplicateName = getCountryFromName(country.getShortName());

            if (duplicateCode != null) {
                throw new DuplicateIdentifierException("Country cannot be added because it duplicates existing code \""
                        + duplicateCode.getCountryCode() + "\" from Country \"" + duplicateCode.getShortName() + "\"");
            }
            if (duplicateName != null) {
                throw new DuplicateIdentifierException(
                        "Country cannot be added because it duplicates existing name \"" + duplicateName.getShortName()
                                + "\" from Country with code \"" + duplicateName.getCountryCode() + "\"");
            }
            countriesByCode.put(country.getCountryCode().toUpperCase(), country);
            countriesByName.put(country.getShortName().toLowerCase(), country);
        }
        this.countries = Set.of(countries);
    }

    /**
     * Check if the provided Country exists in this World.
     *
     * @param country The Country to check
     * @return Whether the Country exists in this World
     */
    public boolean countryExists(final Country country) {
        return countries.contains(country);
    }
}
