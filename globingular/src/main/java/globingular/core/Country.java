package globingular.core;

import java.util.*;

public class Country {
    private static final HashMap<String, Country> registeredCountriesByCode = new HashMap<>();
    private static final HashMap<String, Country> registeredCountriesByName = new HashMap<>();

    private final String countryCode;
    private final String name;
    private final String longname;
    private final String sovereignty;
    private final String region;
    private final long population;
    private final Province[] provinces;

    public Country(String countryCode, String name, String longname, String sovereignty, String region, long population,
                   Province[] provinces) {
        if (registeredCountriesByCode.containsKey(countryCode)) {
            throw new UnsupportedOperationException("NUHUHH! #code");
        }
        if (registeredCountriesByName.containsKey(name)) {
            throw new UnsupportedOperationException("NUHUHH! #name");
        }
        registeredCountriesByCode.put(countryCode, this);
        registeredCountriesByName.put(name, this);
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

    public static Country getCountryFromCode(String countryCode) {
        return registeredCountriesByCode.get(countryCode);
    }

    public static Country getCountryFromName(String countryName) {
        return registeredCountriesByName.get(countryName);
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
