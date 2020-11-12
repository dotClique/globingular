package globingular.core;

/**
 * <p>Country class that contains information about a single country. The Country class is
 * used as a template for creating objects out of the countries listed in the data file
 * of the project.</p>
 * 
 * <p>An instance of the Country class contains a:
 * <ul>
 * <li>Country code</li>
 * <li>Name</li>
 * <li>Long name</li>
 * <li>Sovereignty</li>
 * <li>Region</li>
 * <li>Population</li>
 * </ul>
 * </p>
 */

public class Country {
    /**
     * Unique two-letter representation of the Country. Following the ISO 3166-1
     * Alpha-2 code. E.g. 'AU'
     */
    private final String countryCode;

    /**
     * Shortname of the Country. E.g. 'Australia'
     */
    private final String shortName;

    /**
     * Full longname of the Country. E.g. 'The Commonwealth of Australia'
     */
    private final String longname;

    /**
     * The sovereignty of the Country. E.g. 'UN' for a UN member state
     */
    private final String sovereignty;

    /**
     * The region the Country is part of. E.g. 'OC' for Oceania
     */
    private final String region;

    /**
     * Population of the Country. E.g. 25646823
     */
    private final long population;

    /**
     * Constructor for Country, an immutable store of data representing one country.
     *
     * @param countryCode Two-letter representation of the Country. Following the ISO 3166-1 Alpha-2 code. I.e. 'AU'
     * @param shortName   Shortname of the Country. I.e. 'Australia'
     * @param longname    Full longname of the Country. I.e. 'The Commonwealth of Australia'
     * @param sovereignty The sovereignty of the Country. I.e. 'UN' for a UN member state
     * @param region      The region the Country is part of. I.e. 'OC' for Oceania
     * @param population  Population of the Country. I.e. 25646823
     */
    public Country(final String countryCode, final String shortName, final String longname, final String sovereignty,
                   final String region, final long population) {
        this.countryCode = countryCode;
        this.shortName = shortName;
        this.longname = longname;
        this.sovereignty = sovereignty;
        this.region = region;
        this.population = population;
    }

    /**
     * Shorthand constructor for Country. Defaults are the following:
     * longname: name
     * sovereignty: 'UN'
     * region: ''
     * population: 0L
     * provinces: new Province[0]
     *
     * @param countryCode Two-letter representation of the Country. Following the ISO 3166-1 Alpha-2 code. I.e. 'AU'
     * @param shortName   Shortname of the Country. I.e. 'Australia'
     */
    public Country(final String countryCode, final String shortName) {
        this(countryCode, shortName, shortName, "UN", "", 0L);
    }

    /**
     * Get the Country's ISO 3166-1 Alpha-2 code.
     *
     * @return A two-letter representation, following the ISO 3166-standard
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Get the Country's short name.
     *
     * @return Short name for the Country
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Get the Country's long name.
     *
     * @return Long name for the Country
     */
    public String getLongName() {
        return longname;
    }

    /**
     * Get the Country's sovereignty.
     *
     * @return Sovereignty of the Country
     */
    public String getSovereignty() {
        return sovereignty;
    }

    /**
     * Get the Country's region.
     *
     * @return Region of the Country
     */
    public String getRegion() {
        return region;
    }

    /**
     * Get the Country's population.
     *
     * @return Population of the Country
     */
    public long getPopulation() {
        return population;
    }

    /**
     * Return a plain text representation of the object. Only used as a
     * human-readable representation. Not a stable interface.
     */
    @Override
    public String toString() {
        return "Country{"
                + "countryCode='" + countryCode + '\''
                + ", shortName='" + shortName + '\''
                + ", longName='" + longname + '\''
                + ", sovereignty='" + sovereignty + '\''
                + ", region='" + region + '\''
                + ", population=" + population
                + '}';
    }
}
