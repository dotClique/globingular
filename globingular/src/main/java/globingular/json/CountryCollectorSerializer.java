package globingular.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import globingular.core.CountryCollector;

public class CountryCollectorSerializer extends JsonSerializer<CountryCollector> {

    @Override
    public void serialize(CountryCollector collector, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
                gen.writeStartObject();

                gen.writeFieldName("ExistingCountries");
                gen.writeStartArray();
                for (var c : collector.existingCountriesProperty().get()) {
                    gen.writeObject(c);
                }
                gen.writeEndArray();

                gen.writeFieldName("VisitedCountries");
                String[] arr =
                        collector.getVisitedCountries().stream().map(CountryCollector.Country::getCountryCode).toArray(String[]::new);
                gen.writeArray(arr, 0, arr.length);
                gen.writeEndObject();
    }
}
