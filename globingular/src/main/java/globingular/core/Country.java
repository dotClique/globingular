package globingular.core;

import java.util.Arrays;

public class Country {
    /**
     * Two-letter representation of the Country. Following the ISO 3166-1 Alpha-2 code. I.e. 'AU'.
     */
    private final String countryCode;

    /**
     * Shortname of the Country. I.e. 'Australia'
     */
    private final String name;

    /**
     * Full longname of the Country. I.e. 'The Commonwealth of Australia'
     */
    private final String longname;

    /**
     * The sovereignty of the Country. I.e. 'UN' for a UN member state
     */
    private final String sovereignty;

    /**
     * The region the Country is part of. I.e. 'OC' for Oceania
     */
    private final String region;

    /**
     * Population of the Country. I.e. 25646823
     */
    private final long population;

    /**
     * List of Provinces part of the Country.
     * Currently placeholder for future functionality.
     */
    private final Province[] provinces;

    /**
     * Constructor for Country.
     *
     * @param countryCode Two-letter representation of the Country. Following the ISO 3166-1 Alpha-2 code. I.e. 'AU'
     * @param name        Shortname of the Country. I.e. 'Australia'
     * @param longname    Full longname of the Country. I.e. 'The Commonwealth of Australia'
     * @param sovereignty The sovereignty of the Country. I.e. 'UN' for a UN member state
     * @param region      The region the Country is part of. I.e. 'OC' for Oceania
     * @param population  Population of the Country. I.e. 25646823
     * @param provinces   List of Provinces part of the Country
     */
    public Country(final String countryCode, final String name, final String longname, final String sovereignty,
                   final String region, final long population,
                   final Province[] provinces) {
        this.countryCode = countryCode;
        this.name = name;
        this.longname = longname;
        this.sovereignty = sovereignty;
        this.region = region;
        this.population = population;
        this.provinces = provinces;
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
     * @param name        Shortname of the Country. I.e. 'Australia'
     */
    public Country(final String countryCode, final String name) {
        this(countryCode, name, name, "UN", "", 0L, new Province[0]);
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
    public String getName() {
        return name;
    }

    /**
     * Get the Country's long name.
     *
     * @return Long name for the Country
     */
    public String getLongname() {
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
                + ", name='" + name + '\''
                + ", longname='" + longname + '\''
                + ", sovereignty='" + sovereignty + '\''
                + ", region='" + region + '\''
                + ", population=" + population
                + ", provinces=" + Arrays.toString(provinces)
                + '}';
    }
}
