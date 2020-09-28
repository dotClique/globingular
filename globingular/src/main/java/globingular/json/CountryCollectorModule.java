package globingular.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import globingular.core.CountryCollector;

public class CountryCollectorModule extends Module {

    /**
     * Container-object holding Serializers.
     */
    private final SimpleSerializers serializers = new SimpleSerializers();

    /**
     * Container-object holding JsonDeserializers.
     */
    private final SimpleDeserializers deserializers = new SimpleDeserializers();

    /**
     * Container-object used for Serialization and Deserialization.
     */
    public CountryCollectorModule() {
        serializers.addSerializer(CountryCollector.class, new CountryCollectorSerializer());
        deserializers.addDeserializer(CountryCollector.class, new CountryCollectorDeserializer());
    }

    /**
     * Get the module's name.
     */
    @Override
    public String getModuleName() {
        return this.getClass().getName();
    }

    /**
     * Get the current module's version.
     */
    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    /**
     * Add Serializers and Deserializers to context.
     */
    @Override
    public void setupModule(final SetupContext context) {
        context.addSerializers(serializers);
        context.addDeserializers(deserializers);
    }
}
