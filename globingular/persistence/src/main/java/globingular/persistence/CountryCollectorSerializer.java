package globingular.persistence;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import globingular.core.Country;
import globingular.core.CountryCollector;

/**
 * <p>CountryCollectorSerializer class has one method,
 * serialize(collector, gen, serializers),
 * that serializes CountryCollector objects into JSON format.</p>
 */

public class CountryCollectorSerializer extends JsonSerializer<CountryCollector> {

    /**
     * Serialize a CountryCollector object into Json format.
     */
    @Override
    public void serialize(final CountryCollector collector, final JsonGenerator gen,
            final SerializerProvider serializers)
            throws IOException {
                gen.writeStartObject();

                gen.writeFieldName("VisitedCountries");
                String[] arr =
                        collector.getVisitedCountries().stream().map(Country::getCountryCode).toArray(String[]::new);
                gen.writeArray(arr, 0, arr.length);
                gen.writeEndObject();
    }
}
