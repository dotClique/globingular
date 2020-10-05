package it1901.gr2002.globingular.core;

import java.util.Arrays;

public class Country {
    /**
     * Unique two-letter representation of the Country. Following the ISO 3166-1
     * Alpha-2 code. I.e. 'AU'
     */
    private String countryCode;

    /**
     * Shortname of the Country. I.e. 'Australia'
     */
    private String name;

    /**
     * Full longname of the Country. I.e. 'The Commonwealth of Australia'
     */
    private String longname;

    /**
     * The sovereignty of the Country. I.e. 'UN' for a UN member state
     */
    private String sovereignty;

    /**
     * The region the Country is part of. I.e. 'OC' for Oceania
     */
    private String region;

    /**
     * Population of the Country. I.e. 25646823
     */
    private long population;

    /**
     * List of Provinces part of the Country.
     */
    private Province[] provinces;

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
