package it1901.gr2002.globingular.persistence;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import it1901.gr2002.globingular.core.CountryCollector;

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
                final String[] arr = collector.getVisitedCountries();
                gen.writeArray(arr, 0, arr.length);
                gen.writeEndObject();
    }
}
