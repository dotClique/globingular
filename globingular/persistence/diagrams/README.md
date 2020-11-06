```plantuml


package globingular.persistence {	
	class CountryCollectorSerializer [[java:globingular.persistence.CountryCollectorSerializer]] {
		+void serialize(CountryCollector collector, JsonGenerator gen, SerializerProvider serializers)
	}
	class "JsonSerializer<CountryCollector>" as JsonSerializer_CountryCollector_ {
	}
	JsonSerializer_CountryCollector_ <|-- CountryCollectorSerializer
	
	
	
	
	class CountryDeserializer [[java:globingular.persistence.CountryDeserializer]] {
		+Country deserialize(JsonParser p, DeserializationContext ctxt)
	}
	class "JsonDeserializer<Country>" as JsonDeserializer_Country_ {
	}
	JsonDeserializer_Country_ <|-- CountryDeserializer
	
	
	
	
	class CountryCollectorModule [[java:globingular.persistence.CountryCollectorModule]] {
		-SimpleSerializers serializers
		-SimpleDeserializers deserializers
		+CountryCollectorModule()
		+String getModuleName()
		+Version version()
		+void setupModule(SetupContext context)
	}
	class Module [[java:com.sun.tools.sjavac.Module]] {
	}
	Module <|-- CountryCollectorModule
	
	
	
	
	class CountryCollectorDeserializer [[java:globingular.persistence.CountryCollectorDeserializer]] {
		+CountryCollector deserialize(JsonParser p, DeserializationContext ctxt)
	}
	class "JsonDeserializer<CountryCollector>" as JsonDeserializer_CountryCollector_ {
	}
	JsonDeserializer_CountryCollector_ <|-- CountryCollectorDeserializer
	
	
	
	
	class PersistenceHandler [[java:globingular.persistence.PersistenceHandler]] {
		-{static}Path FILE_COLLECTOR
		-{static}Path DATA_FOLDER
		-{static}String FILE_MAP_WORLD
		-{static}String SAMPLE_COLLECTOR
		-ObjectMapper objectMapper
		-ObjectMapper getObjectMapper()
		+CountryCollector loadMapCountryCollector()
		+void setAutosave(CountryCollector countryCollector)
		-World loadMapWorld()
		-void saveState(CountryCollector countryCollector)
	}
	
	
	
	
	class VisitDeserializer [[java:globingular.persistence.VisitDeserializer]] {
		+Visit deserialize(JsonParser p, DeserializationContext ctxt)
	}
	class "JsonDeserializer<Visit>" as JsonDeserializer_Visit_ {
	}
	JsonDeserializer_Visit_ <|-- VisitDeserializer
	
	
	
	
	class VisitSerializer [[java:globingular.persistence.VisitSerializer]] {
		+void serialize(Visit value, JsonGenerator gen, SerializerProvider serializers)
	}
	class "JsonSerializer<Visit>" as JsonSerializer_Visit_ {
	}
	JsonSerializer_Visit_ <|-- VisitSerializer
	
	
	
	
	class WorldDeserializer [[java:globingular.persistence.WorldDeserializer]] {
		+World deserialize(JsonParser p, DeserializationContext ctxt)
	}
	class "JsonDeserializer<World>" as JsonDeserializer_World_ {
	}
	JsonDeserializer_World_ <|-- WorldDeserializer
	
	
	
	
	class WorldSerializer [[java:globingular.persistence.WorldSerializer]] {
		+void serialize(World value, JsonGenerator gen, SerializerProvider serializers)
	}
	class "JsonSerializer<World>" as JsonSerializer_World_ {
	}
	JsonSerializer_World_ <|-- WorldSerializer
}



```