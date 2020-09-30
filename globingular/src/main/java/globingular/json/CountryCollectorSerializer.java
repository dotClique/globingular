package globingular.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import globingular.core.Country;
import globingular.core.CountryCollector;

public class CountryCollectorSerializer extends JsonSerializer<CountryCollector> {

    /**
     * Serialize a CountryCollector object into Json format.
     */
    @Override
    public void serialize(final CountryCollector collector, final JsonGenerator gen,
            final SerializerProvider serializers)
            throws IOException {
                gen.writeStartObject();

                gen.writeFieldName("World");
                gen.writeNumber(collector.getWorld().hashCode());
                gen.writeFieldName("VisitedCountries");
                String[] arr =
                        collector.getVisitedCountries().stream().map(Country::getCountryCode).toArray(String[]::new);
                gen.writeArray(arr, 0, arr.length);
                gen.writeEndObject();
    }
}
