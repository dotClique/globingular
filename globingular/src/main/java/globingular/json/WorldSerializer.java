package globingular.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import globingular.core.Country;
import globingular.core.World;

import java.io.IOException;

public class WorldSerializer extends JsonSerializer<World> {

    @Override
    public void serialize(final World value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("Countries");
        gen.writeStartArray();
        for (Country country : value.getCountries()) {
            gen.writeObject(country);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
