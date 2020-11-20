package globingular.persistence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import globingular.core.Visit;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Serializer for {@link Visit} objects.
 */
class VisitSerializer extends JsonSerializer<Visit> {

    /**
     * Serialize a given {@link Visit} to JSON.
     *
     * @param value The Visit to serialize
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
        LocalDate arrival = value.getArrival();
        gen.writeString(arrival == null ? null : arrival.toString());

        gen.writeFieldName("departure");
        LocalDate departure = value.getDeparture();
        gen.writeString(departure == null ? null : departure.toString());

        gen.writeEndObject();
    }
}
