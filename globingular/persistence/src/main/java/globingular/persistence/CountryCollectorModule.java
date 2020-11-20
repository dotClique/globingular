package globingular.persistence;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import globingular.core.Country;
import globingular.core.CountryCollector;
import globingular.core.Visit;
import globingular.core.World;

/**
 * <p>CountryCollectorModule has a Container-object for serialization and deserialization,
 * and has methods to get module name and version, and a method to add serializers and
 * deserializers to the SetupContext.
 * 
 * CountryoCollectorModule class holds:
 * <ul>
 * <li>Serializers</li>
 * <li>Deserializers</li>
 * </ul>
 * </p>
 */

class CountryCollectorModule extends Module {

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
    CountryCollectorModule() {
        serializers.addSerializer(CountryCollector.class, new CountryCollectorSerializer());
        serializers.addSerializer(World.class, new WorldSerializer());
        serializers.addSerializer(Visit.class, new VisitSerializer());
        deserializers.addDeserializer(CountryCollector.class, new CountryCollectorDeserializer());
        deserializers.addDeserializer(World.class, new WorldDeserializer());
        deserializers.addDeserializer(Country.class, new CountryDeserializer());
        deserializers.addDeserializer(Visit.class, new VisitDeserializer());
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
