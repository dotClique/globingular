package globingular.core;

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
