package it1901.gr2002.globingular.core;

public class Province {

    /**
     * The short-code of the Province, following the ISO 3166-2 standard for states
     * and provinces.
     */
    private String provinceCode;

    /**
     * The capital of the Province.
     */
    private String capital;

    /**
     * The full name of the Province.
     */
    private String name;

    /**
     * The population of the Province.
     */
    private long population;

    /**
     * Create a new Province instance with the given arguments.
     * 
     * @param provinceCode ISO 3166-2 code for the Province
     * @param capital Name of the Province's capital
     * @param name Full name of the Province
     * @param population Population of the Province
     */
    public Province(final String provinceCode, final String capital, final String name, final long population) {
        this.provinceCode = provinceCode;
        this.capital = capital;
        this.name = name;
        this.population = population;
    }

    /**
     * Get the Province's short-code.
     * 
     * @return The province code, following the ISO 3166-2 standard
     */
    public String getProvinceCode() {
        return provinceCode;
    }

    /**
     * Get the Province's full name.
     * 
     * @return The full name of the Province
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Province's capital.
     * 
     * @return The capital of the Province
     */
    public String getCapital() {
        return capital;
    }

    /**
     * Get the Province's population.
     * 
     * @return The population of the Province
     */
    public long getPopulation() {
        return population;
    }
}
