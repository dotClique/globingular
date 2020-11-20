# Globingular.Persistence

The Persistance module handles all file-interaction, serialization and deserialization. Since Deliverable 2 more of the logic  has been moved into the UI-module, leaving only the actual loading and saving of files, as well as serialization and deserialization to and from Json to Persistence.


## Diagrams

This page contains class-diagrams for the Core-module. For other diagrams see [/diagrams](/diagrams).


## The classes

In addition to the classes shown in the diagram below, we're using Jackson for serialization and deserialization. Subsequently we have a `xSerializer` and `xDeserializer` for each class `x` that require it. And `GlobingularModule` which collects these into a *module* for Jackson. `PersistenceHandler` register `GlobingularModule` to requested `ObjectMapper`s before returning them, enabling these serializers and deserializers for Jackson.

```plantuml
package globingular.persistence {

    class PersistenceHandler [[java:globingular.persistence.PersistenceHandler]] {
        +{static}String INJECTED_MAP
        +{static}String INJECTED_MAP_WORLD
        +{static}String INJECTED_MAP_PERSISTENCE
        -Map<String,World> defaultWorlds
        -String predominantDefaultWorldName
        +PersistenceHandler()
        +{static}ObjectMapper getUninjectedObjectMapper()
        +ObjectMapper getObjectMapper()
        +World getPredominantDefaultWorld()
        +World getDefaultWorld(String worldName)
        +World getDefaultWorldOr(String worldName, World or)
        +String serialize(Object object)
        +T parse(String serialized, Class<T> type)
    }
    class FileHandler [[java:globingular.persistence.FileHandler]] {
        +{static}Path DATA_FOLDER
        -{static}String DEFAULT_USERNAME
        -{static}String DEFAULT_WORLD_FILENAME
        -FileHandler()
        +{static}CountryCollector loadCountryCollector(PersistenceHandler persistenceHandler, String username)
        ~{static}World loadPredominantDefaultWorld()
        +{static}boolean saveCountryCollector(PersistenceHandler persistenceHandler, String username, CountryCollector countryCollector)
        -{static}boolean deleteCountryCollector(String username)
        -{static}Path pathFromUsername(String username, String defaultFilename)
    }
}

class "core.World" as World {
}

PersistenceHandler --> "defaultWorlds: *" World
```
