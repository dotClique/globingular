package globingular.persistence;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import globingular.core.CountryCollector;
import globingular.core.Visit;

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
    public void serialize(final CountryCollector countryCollector, final JsonGenerator gen,
            final SerializerProvider serializers)
            throws IOException {
                gen.writeStartObject();

                gen.writeFieldName("World");
                gen.writeObject(countryCollector.getWorld());

                gen.writeFieldName("Visits");
                gen.writeStartArray();
                for (Visit visit : countryCollector.getVisits()) {
                    gen.writeObject(visit);
                }
                gen.writeEndArray();

                gen.writeEndObject();
    }
}
