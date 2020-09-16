package globingular.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import globingular.CountryCollector;

public class CountryCollectorModule extends Module {

    private final SimpleSerializers serializers = new SimpleSerializers();
    private final SimpleDeserializers deserializers = new SimpleDeserializers();

    public CountryCollectorModule() {
        serializers.addSerializer(CountryCollector.class, new CountryCollectorSerializer());
        deserializers.addDeserializer(CountryCollector.class, new CountryCollectorDeserializer());
    }

    @Override
    public String getModuleName() {
        System.out.println(this.getClass().getName());
        return this.getClass().getName();
        // return "CountryCollectorModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addSerializers(serializers);
        context.addDeserializers(deserializers);
    }
}
