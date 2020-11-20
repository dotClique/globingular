# Class diagram for the RestServer module

This page only shows this modules classes. For other diagrams, like modules, dependencies and user stories: [/diagrams](/diagrams)


## Diagram

The following diagram shows the classes in this module. As this is a failry light module only rigging the launch and connection of `globingular.restapi`, there's not that many classes or connections. `GlobingularObjectMapperProvider` uses `globingular.persistence` to provide serialization/deserialization of requests and responses. `Main` starts the server, using `GlobingularConfig` as launch-configuration.

```plantuml
package globingular.restserver {
    class GlobingularObjectMapperProvider [[java:globingular.restserver.GlobingularObjectMapperProvider]] {
        -PersistenceHandler persistenceHandler
        +ObjectMapper getContext(Class<?> aClass)
    }
    interface "ContextResolver<ObjectMapper>" as ContextResolver_ObjectMapper_ {
    }
    ContextResolver_ObjectMapper_ <|.. GlobingularObjectMapperProvider
    class GlobingularConfig [[java:globingular.restserver.GlobingularConfig]] {
        -GlobingularModule globingularModule
        -PersistenceHandler persistenceHandler
        +GlobingularConfig(GlobingularModule globingularModule, PersistenceHandler persistenceHandler)
        +GlobingularConfig(GlobingularModule globingularModule)
        +GlobingularConfig()
        +GlobingularModule getGlobingularModule()
    }
    class ResourceConfig [[java:org.glassfish.jersey.server.ResourceConfig]] {
    }
    ResourceConfig <|-- GlobingularConfig
    class Main [[java:globingular.restserver.Main]] {
        +{static}String BASE_URI
        -Main()
        +{static}HttpServer startServer()
        +{static}void main(String[] args)
    }
}
```
