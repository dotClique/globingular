# Class diagram for the RestApi module

This page only shows this modules classes. For other diagrams, like modules, dependencies and user stories: [/diagrams](/diagrams)


## Diagram

In this diagram the resources found in this module can be seen. The main class is `GlobingularService` which acts as root-node, taking in all incoming requests and passing them along to the relevant resource-class. `CountryCollectorResource` handles requests regarding `CountryCollector`s, and passing requests regarding `Visit`s to `VisitResource`, which handles these smaller update-requests. `WorldResource` only has one endpoint, returning `World`-instances without being part of a `CountryCollector`.

As this module isn't to complex we've not included a diagram for dependencies to other modules, but these can easily be seen by looking through the fields in the diagram below.

```plantuml
package globingular.restapi {
    class CountryCollectorResource [[java:globingular.restapi.CountryCollectorResource]] {
        -GlobingularModule globingularModule
        -String username
        -CountryCollector countryCollector
        -PersistenceHandler persistenceHandler
        +CountryCollectorResource(GlobingularModule globingularModule, String username, CountryCollector countryCollector, PersistenceHandler persistenceHandler)
        +CountryCollector getCountryCollector()
        +boolean putCountryCollector(CountryCollector newCountryCollector)
        +boolean deleteCountryCollector()
        +boolean renameCountryCollector(String newName)
        +VisitResource getVisit()
        -void saveCountryCollector(String usernameToSaveAt, CountryCollector countryCollectorToSave)
    }
    class GlobingularService [[java:globingular.restapi.GlobingularService]] {
        -GlobingularModule globingularModule
        -PersistenceHandler persistenceHandler
        +GlobingularService(GlobingularModule globingularModule, PersistenceHandler persistenceHandler)
        +CountryCollectorResource getCountryCollector(String username)
        +WorldResource getWorld()
    }
    class VisitResource [[java:globingular.restapi.VisitResource]] {
        -String username
        -CountryCollector countryCollector
        -PersistenceHandler persistenceHandler
        +VisitResource(String username, CountryCollector countryCollector, PersistenceHandler persistenceHandler)
        +boolean registerVisit(Visit visit)
        +boolean removeVisit(Visit visit)
        -Visit validateAndReturnVisit(Visit visit)
        -boolean saveAppState(String user, CountryCollector collector)
    }
    class WorldResource [[java:globingular.restapi.WorldResource]] {
        -PersistenceHandler persistenceHandler
        +WorldResource(PersistenceHandler persistenceHandler)
        +World getWorld(String worldName)
    }
}
```
