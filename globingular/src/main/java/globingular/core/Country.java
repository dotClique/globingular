package globingular.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import globingular.json.CountriesSerializer;

import java.util.Arrays;

public class Country {
    private String countryCode;
    private String name;
    private String longname;
    private String sovereignty;
    private String region;
    private long population;
    private Province[] provinces;

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
