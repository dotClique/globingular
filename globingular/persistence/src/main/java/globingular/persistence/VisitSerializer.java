package globingular.persistence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import globingular.core.Visit;

import java.io.IOException;

/**
 * Serializer for {@link World} objects.
 */
public class VisitSerializer extends JsonSerializer<Visit> {

    /**
     * Serialize a given {@link World} to JSON.
     *
     * @param value The World to serialize
     * @param gen The current JSON generator
     * @param serializers The active serializers
     * @throws IOException On general generation error
     */
    @Override
    public void serialize(final Visit value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();

        gen.writeFieldName("countryCode");
        gen.writeString(value.getCountry().getCountryCode());

        gen.writeFieldName("arrival");
        gen.writeString(value.getArrival().toString());

        gen.writeFieldName("departure");
        gen.writeString(value.getDeparture().toString());

        gen.writeEndObject();
    }
}
